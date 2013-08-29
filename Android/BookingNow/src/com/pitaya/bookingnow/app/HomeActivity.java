package com.pitaya.bookingnow.app;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.data.ProgressHandler;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.User;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuContentProvider;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.FoodService;
import com.pitaya.bookingnow.app.service.EnhancedMessageService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;
import com.pitaya.bookingnow.app.views.*;
import com.pitaya.bookingnow.message.*;
import com.pitaya.bookingnow.app.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
//import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;  
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HomeActivity extends FragmentActivity {
	
	public static final int MENU_CHECKING_LOADER = 0;
	public static final int MENU_LOADER = MENU_CHECKING_LOADER + 1;
	public static final int ORDER_LIST_LOADER = MENU_LOADER + 1;
	
	private static String TAG = "HomeActivity";
	
	private SlideContent homecontent;
	private View mLeftMenu;
	private View mInfoView;
	private TextView mInfoText;
	private View mOrderBtn;
	private View mLoginBtn;
	private View mLogoutBtn;
	
	private AlertDialog loginDialog;
	private AlertDialog menuUpdateDialog;
	private ProgressDialog mProgressingDialog;
	
	private MessageHandler mMessageHandler;
	private EnhancedMessageService mMessageService;
	private boolean mIsBound = false;
	private boolean isUpdating = false;
	private boolean isRemeberMeAfterLogin = false;
	private boolean isLogining = false;
	private String username;
	private String password;
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "Service connected");
			mMessageService = ((EnhancedMessageService.MessageBinder)service).getService();
		    mIsBound = true;
			mMessageService.registerHandler(Constants.RESULT_MESSAGE, mMessageHandler);
			mMessageService.registerHandler(Constants.FOOD_MESSAGE, mMessageHandler);
			if(mMessageService.isReady()){
				/* 
				 * Maybe we register the result message too late, so
				 * do the things here
				 */
				refreshMenuByRole();
				doAutoLogin();
				checkMenuUpdate();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "Service disconnected");
		    mIsBound = false;
			mMessageService = null;
			UserManager.setLoginUser(HomeActivity.this, null);
			refreshMenuByRole();
		}

	};
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ContentUtil.init(this);
		mMessageHandler = new MessageHandler();
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener() {
			
			@Override
			public void onMessage(Message message) {
	            if(message instanceof ResultMessage){
	            	ResultMessage resultmsg = (ResultMessage)message;
	            	if(resultmsg.getRequestType() == Constants.REGISTER_REQUEST){
	            		handleRegisterResultMsg(resultmsg);
	            	} else if(resultmsg.getRequestType() == Constants.SOCKET_CONNECTION){
	            		handleConnectResultMsg(resultmsg);
	            	}
	            } else if(message instanceof FoodMessage){
	            	HomeActivity.this.checkMenuUpdate();
	            }
			}
		});
		this.doStartService();
		this.setHomeContent();
		setOrder();
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		setOrder();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!this.mIsBound){
			this.doBindService();
		} else {
			boolean isServiceReady = true;
			if(!this.mMessageService.isReady() && !this.mMessageService.isConnecting()){
				isServiceReady = false;
				this.doStartService();
			}
			this.refreshMenuByRole();
			//If service is not ready, these will be done until handleconnectresult
			if(isServiceReady){
				this.doAutoLogin();
				this.checkMenuUpdate();
			}
		}
	}
	
	@Override  
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		this.doUnbindService();
    }
	
	private void doStartService(){
		this.startService(new Intent(this, EnhancedMessageService.class));
	}
	
	private void doBindService() {
		bindService(new Intent(this, EnhancedMessageService.class), mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void doUnbindService() {
	    if (mIsBound) {
	    	mMessageService.unregisterHandler(mMessageHandler);
	        unbindService(mConnection);
			mIsBound = false;
	    }
	}
	
	private void setOrder(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null && bundle.getSerializable("order") != null){
			((FoodMenuContentView)this.homecontent.getContentView("menu")).setOrderAndRefresh((Order)bundle.getSerializable("order"));
		}
	}
	
	private synchronized void refreshMenuByRole(){
		Integer role = UserManager.getUserRole(this);
		if(role != null){
			this.mOrderBtn.setVisibility(View.VISIBLE);
			this.mLogoutBtn.setVisibility(View.VISIBLE);
			this.mInfoView.setVisibility(View.VISIBLE);
			this.mLoginBtn.setVisibility(View.GONE);
			this.mInfoText.setText(UserManager.getUsername(this) + " (" + UserManager.getRoleName() +")");
		} else {
			this.mOrderBtn.setVisibility(View.GONE);
			this.mLogoutBtn.setVisibility(View.GONE);
			this.mInfoView.setVisibility(View.GONE);
			if(this.mIsBound && this.mMessageService.isReady()
					&& UserManager.getUsernameAndPassword(this) == null){
				this.mLoginBtn.setVisibility(View.VISIBLE);
			} else {
				this.mLoginBtn.setVisibility(View.GONE);
			}
		}
	}
	
	private void setHomeContent(){
		if(homecontent == null){
			ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
			//Set menu with to 200dp
			homecontent = new SlideContent(this, 200, contentViews);
			this.mLeftMenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
			LinearLayout menuitems = (LinearLayout)(mLeftMenu.findViewById(R.id.leftmenu));
			
			this.mInfoView = menuitems.findViewById(R.id.infoview);
			this.mInfoText = (TextView)menuitems.findViewById(R.id.infotext);
			this.mInfoView.setVisibility(View.GONE);
			
			View menuitem = menuitems.findViewById(R.id.menu_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if(!homecontent.getCurrentContentViewKey().equals("menu")){
						((FoodMenuContentView)homecontent.getContentView("menu")).setOrder(null);
					}
					homecontent.selectItem("menu");
				}
			});
			
			this.mOrderBtn = menuitems.findViewById(R.id.order_btn);
			mOrderBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					homecontent.selectItem("order");
				}
				
			});
			mOrderBtn.setVisibility(View.GONE);
			
			this.mLoginBtn = menuitems.findViewById(R.id.login_btn);
			mLoginBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					showLoginDialog();
				}
				
			});
			mLoginBtn.setVisibility(View.GONE);
			
			this.mLogoutBtn = menuitems.findViewById(R.id.logout_btn);
			mLogoutBtn.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
	           	 	if(HomeActivity.this.mIsBound){
	           	 		mMessageService.sendMessage(new RegisterMessage(UserManager.getUserId(HomeActivity.this), "unregister"));
	           	 	}
	           	 	UserManager.cleanRemeberMe(HomeActivity.this);
	           	 	UserManager.setLoginUser(HomeActivity.this, null);
					HomeActivity.this.refreshMenuByRole();
					((FoodMenuContentView)HomeActivity.this.homecontent.getContentView("menu")).setOrder(null);
					HomeActivity.this.homecontent.refreshItem("menu");
				}
				
			});
			mLogoutBtn.setVisibility(View.GONE);
			
			menuitem = menuitems.findViewById(R.id.setting_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					HomeActivity.this.homecontent.selectItem("setting");
				}
				
			});
			
			OrderContentView orderview = new OrderContentView("order", this, homecontent);
			contentViews.add(orderview);
			FoodMenuContentView menucontentview = new FoodMenuContentView("menu", this, homecontent);
			contentViews.add(menucontentview);
			SettingsContentView settingscontentview = new SettingsContentView("setting", this, homecontent);
			contentViews.add(settingscontentview);
			homecontent.setMenu(this.mLeftMenu);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setContentView(homecontent);
		homecontent.selectItem("menu");
	}
	
	private void showConfirmDialog(final String key){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle(R.string.confirm);
		 builder.setIcon(android.R.drawable.ic_dialog_info);
		 builder.setPositiveButton(R.string.ok,
		       new DialogInterface.OnClickListener()
		       {
		           @Override
		           public void onClick(DialogInterface dialog, int which)
		           {
		        	   homecontent.selectItem(key);
		           }
		       });
		 
		 builder.setNegativeButton(R.string.cancel,
		       new DialogInterface.OnClickListener()
		       {
		           @Override
		           public void onClick(DialogInterface dialog, int which)
		           {
		        	   return;
		           }
		       });
		 
		 builder.create().show();
	}

	private void showLoginDialog(){
         if(loginDialog == null){
        	 LayoutInflater factory = LayoutInflater.from(HomeActivity.this);  
             final View dialogView = factory.inflate(R.layout.logindialog, null);
             loginDialog = new AlertDialog.Builder(HomeActivity.this)
             .setTitle(getResources().getString(R.string.login_title))
             .setView(dialogView)
             .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){

            	  @Override  
                  public void onClick(DialogInterface dialog, int which) {
	            		 if(isLogining == true){
	            			return;
	            		 } else {
	            			 isLogining = true;
	            		 }
	                 	 HomeActivity.this.username = ((EditText)dialogView.findViewById(R.id.username)).getText().toString();
	                 	 HomeActivity.this.password = ((EditText)dialogView.findViewById(R.id.password)).getText().toString();
	                 	 HomeActivity.this.isRemeberMeAfterLogin = ((CheckBox)dialogView.findViewById(R.id.autologin)).isChecked();
	                 	 RegisterMessage message = new RegisterMessage(username, password, "register");
	                 	 if(HomeActivity.this.mIsBound && HomeActivity.this.mMessageService.isReady()){
	                 		HomeActivity.this.mMessageService.sendMessage(message);
	                 		 mProgressingDialog  = ProgressDialog.show(HomeActivity.this, 
	                 				getResources().getString(R.string.waiting), getResources().getString(R.string.logining), true);
	                 	 } else {
	                 		 isLogining = false;
	                 		 ToastUtil.showToast(HomeActivity.this, "登录失败, 请检查网络", Toast.LENGTH_LONG);
	                 	 }
            	  }
             }).create();
//                 	 UserManager.login(username, password, new HttpHandler(){
//	                    		@Override
//	                 			public void onSuccess(String action, String response){
//	                    			progressingDialog.dismiss();
//	                 				if(response == null || response.trim().equals("")){
//	                 					handleLoginError("服务器错误");
//	                 				} else{
//	                 					try {
//											JSONObject juser = new JSONObject(response);
//											afterLoginSuccess(juser, false);
//										} catch (JSONException e) {
//											e.printStackTrace();
//											handleLoginError("服务器错误");
//										}
//	                 				}
//	                 			}
//	                 			
//	                 			@Override
//	                 			public void onFail(String action, int errorcode){
//	                 				progressingDialog.dismiss();
//	                 				Log.e(TAG, "Fail to login with error code: " + errorcode);
//	                 				handleLoginError("请检查网络连接");
//	                 			}
//                 	 });
         }
         loginDialog.show();
    }
	
	private void handleRegisterResultMsg(ResultMessage message){
		if(message.getResult() == Constants.SUCCESS){
			Log.i(TAG, "Success to register client to server: " + message.getDetail());
			String detail = message.getDetail();
			if(detail != null && !detail.equals("")){
				String [] infos = detail.split("###");
				try{
					User user = new User(Long.parseLong(infos[0]), infos[1], Integer.parseInt(infos[2]));
					UserManager.setLoginUser(this, user);
					if(this.isRemeberMeAfterLogin){
						UserManager.rememberMe(this, this.username, this.password);
					}
					ToastUtil.showToast(this, "登录成功", Toast.LENGTH_SHORT);
					this.refreshMenuByRole();
				} catch (Exception e){
					Log.e(TAG, "Fail to parse register result message: " + detail);
				}
			}
		} else if(message.getResult() == Constants.FAIL){
			Log.e(TAG, "Fail to register to server: " + message.getDetail());
			ToastUtil.showToast(this, message.getDetail(), Toast.LENGTH_SHORT);
		}
		if(mProgressingDialog != null){
			mProgressingDialog.dismiss();
		}
		this.isLogining = false;
	}
	
	private void handleConnectResultMsg(ResultMessage message){
		ToastUtil.showToast(this, message.getDetail() , Toast.LENGTH_SHORT);
		this.refreshMenuByRole();
		if(message.getResult() == Constants.SUCCESS){
			this.doAutoLogin();
			this.checkMenuUpdate();
		} else {
			((FoodMenuContentView)this.homecontent.getContentView("menu")).setOrder(null);
			this.homecontent.refreshItem("menu");
		}
	}
	
	private void doAutoLogin(){
		if(isLogining == true){
			return;
		} else {
			isLogining = true;
		}
		if(UserManager.getUserRole(HomeActivity.this) == null){
			String [] userinfo = UserManager.getUsernameAndPassword(this);
			if(userinfo != null){
				//auto login
				this.isRemeberMeAfterLogin = false;
				String username = userinfo[0];
				String password = userinfo[1];
				RegisterMessage message = new RegisterMessage(username, password, "register");
				if(this.mIsBound && this.mMessageService.isReady()){
					this.mMessageService.sendMessage(message);
					ToastUtil.showToast(this, "自动登录中...", Toast.LENGTH_SHORT);
				} else {
					isLogining = false;
					ToastUtil.showToast(this, "自动登录失败, 请检查网络", Toast.LENGTH_LONG);
				}
			} else {
				isLogining = false;
			}
		} else {
			isLogining = false;
		}
	}
	
	public void checkMenuUpdate(){
		synchronized(this) {
			if(this.isUpdating){
				Log.i(TAG, "Menu is in updating");
				return;
			} else {
				this.isUpdating = true;
			}
		}
		final HttpHandler handler = new HttpHandler(){
			
			@Override
			public void onSuccess(String action, String response){
				if(response == null || response.trim().equals("")){
					Log.i(TAG, "No need to update menu, response is blank");
				} else{
					try {
						notifyUpdateMenu(FoodService.getUpdatedFoodsList(response));
					} catch (JSONException e) {
						Log.e(TAG, "Fail to parse menu update response: " + response);
						e.printStackTrace();
						synchronized(HomeActivity.this) {
							HomeActivity.this.isUpdating = false;
						}
					}
				}
			}
			
			@Override
			public void onFail(String action, int errorcode){
				Log.e(TAG, "Fail to check menu update with error code: " + errorcode);
				synchronized(HomeActivity.this) {
					HomeActivity.this.isUpdating = false;
				}
			}
			
		};
		new Thread(){
			
			@Override
			public void run(){
				ArrayList<Food> check_foods = DataService.getFoodsForUpdate(HomeActivity.this);
				if(check_foods != null){
					FoodService.checkUpdate(check_foods, handler);
				}
			}
			
		}.start();
	}
	
	private void onUpdateMenuFinish(){
		
	};
	
	private void notifyUpdateMenu(final Map<String, ArrayList<Food>> foodsToUpdate){
		if(foodsToUpdate == null){
			return;
		}
		int total = 0;
		for(Entry<String, ArrayList<Food>> entry : foodsToUpdate.entrySet()){
			total += entry.getValue().size();
		}
		if(total > 0){
			final int t = total;
			menuUpdateDialog = new AlertDialog.Builder(HomeActivity.this)
			.setTitle(getResources().getString(R.string.updatemenutitle))
			.setMessage(getResources().getString(R.string.updatemenumessage))
			.setCancelable(false)
			.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					executeUpdateMenu(t, foodsToUpdate);
				}
				
			}).create();
			menuUpdateDialog.show();
		} else {
			Log.i(TAG, "The menu is latest.");
			synchronized(HomeActivity.this) {
				HomeActivity.this.isUpdating = false;
			}
		}
	}
	
	private void executeUpdateMenu(int total, Map<String, ArrayList<Food>> foodsToUpdate){
		final ProgressDialog updateProgressDialog=new ProgressDialog(this);
		updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		updateProgressDialog.setTitle("菜单更新中");
		updateProgressDialog.setMax(total);
		updateProgressDialog.setProgress(0);
		updateProgressDialog.setCancelable(true);
		updateProgressDialog.setIndeterminate(false);
		updateProgressDialog.setCanceledOnTouchOutside(false);
		updateProgressDialog.show();
		final int max = total;
		FoodService.updateMenuFoods(this, foodsToUpdate, new ProgressHandler(total){

			@Override
			public void onSuccess(String action, String response){
				Log.i(TAG, "Success to update food " + this.index);
				updateProgressDialog.setProgress(this.index);
				if(this.index == max){
					updateProgressDialog.dismiss();
					onUpdateMenuFinish();
					synchronized(HomeActivity.this) {
						HomeActivity.this.isUpdating = false;
					}
				}
			}
			
			@Override
			public void onFail(String action, int errorcode){
				Log.w(TAG, "Fail to update food " + (this.index) +" with error code:" + errorcode);
				updateProgressDialog.setProgress(this.index);
				if(this.index == max){
					updateProgressDialog.dismiss();
					onUpdateMenuFinish();
					synchronized(HomeActivity.this) {
						HomeActivity.this.isUpdating = false;
					}
				}
			}
			
		});
	}
	
}
