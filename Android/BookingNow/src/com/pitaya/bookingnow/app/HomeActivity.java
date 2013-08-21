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
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuContentProvider;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.FoodService;
import com.pitaya.bookingnow.app.service.MessageService;
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
	private TextView mUserInfoView;
	private TextView mOnlineInfoView;
	
	private AlertDialog loginDialog;
	private AlertDialog menuUpdateDialog;
	
	private MessageHandler mMessageHandler;
	private MessageService mBoundService;
	private boolean mIsBound = false;
	private boolean isUpdating = false;
	private boolean isRemeberMe = false;
	private String username;
	private String password;
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "Service connected");
			mBoundService = ((MessageService.MessageBinder)service).getService();
			mBoundService.registerHandler(Constants.RESULT_MESSAGE, mMessageHandler);
			mBoundService.registerHandler(Constants.FOOD_MESSAGE, mMessageHandler);
			if(mBoundService.isReady()){
				doAutoLogin();
				checkMenuUpdate();
				mOnlineInfoView.setText("状态: 在线" );
			} else if(mBoundService.isConnecting()){
				mOnlineInfoView.setText("状态: 连接中..." );
			} else {
				mOnlineInfoView.setText("状态: 离线" );
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "Service disconnected");
			mBoundService = null;
		}

	};
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ContentUtil.init(this);
		this.setHomeContent();
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
		setOrder();
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		setOrder();
	}
	
	void doStartService(){
		this.startService(new Intent(this, MessageService.class));
	}
	
	void doBindService() {
		bindService(new Intent(this, MessageService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}
	
	void doUnbindService() {
	    if (mIsBound) {
	        // Detach our existing connection.
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
	

	@Override
	public void onResume() {
		super.onResume();
		this.doStartService();
		if(!this.mIsBound){
			new Thread(){
				@Override
				public void run(){
					HomeActivity.this.doBindService();
				}
				
			}.start();
		}
	}
	
	@Override  
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		mBoundService.unregisterHandler(mMessageHandler);
		UserManager.setUserRole(null);
		mBoundService.sendMessage(new RegisterMessage(UserManager.getUserId(), "unregister"));
		this.doUnbindService();
    }
	
	private synchronized void refreshMenuByRole(){
		Integer role = UserManager.getUserRole();
		if(role != null){
			this.mLeftMenu.findViewById(R.id.order_btn).setVisibility(View.VISIBLE);
			this.mLeftMenu.findViewById(R.id.login_btn).setVisibility(View.GONE);
			this.mLeftMenu.findViewById(R.id.logout_btn).setVisibility(View.VISIBLE);
			this.mUserInfoView.setVisibility(View.VISIBLE);
			this.mUserInfoView.setText("用户: " + this.username);
		} else {
			this.mLeftMenu.findViewById(R.id.order_btn).setVisibility(View.GONE);
			this.mLeftMenu.findViewById(R.id.login_btn).setVisibility(View.VISIBLE);
			this.mLeftMenu.findViewById(R.id.logout_btn).setVisibility(View.GONE);
			this.mUserInfoView.setVisibility(View.GONE);
			this.mUserInfoView.setText("");
		}
	}
	
	private void setHomeContent(){
		if(homecontent == null){
			ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
			//Set menu with to 200dp
			homecontent = new SlideContent(this, 200, contentViews);
			this.mLeftMenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
			
			LinearLayout menuitems = (LinearLayout)(mLeftMenu.findViewById(R.id.leftmenu));
			
			this.mUserInfoView = (TextView)menuitems.findViewById(R.id.userinfo);
			this.mOnlineInfoView = (TextView)menuitems.findViewById(R.id.onlineinfo);
			this.mOnlineInfoView.setText("状态: 离线");
			View menuitem = menuitems.findViewById(R.id.menu_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					((FoodMenuContentView)homecontent.getContentView("menu")).setOrder(null);
					homecontent.selectItem("menu");
				}
			});
			
			menuitem = menuitems.findViewById(R.id.order_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					if(homecontent.getCurrentContentViewKey().equals("menu")){
						Order currentOrder =  ((FoodMenuContentView)homecontent.getContentView("menu")).getOrder();
						if(currentOrder != null && currentOrder.isDirty() && currentOrder.getStatus() != Constants.ORDER_NEW){
							showConfirmDialog("order");
						} else {
							homecontent.selectItem("order");
						}
					} else {
						homecontent.selectItem("order");
					}
				}
				
			});
			
			menuitem = menuitems.findViewById(R.id.login_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					showLoginDialog();
				}
				
			});
			
			menuitem = menuitems.findViewById(R.id.logout_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
	           	 	if(HomeActivity.this.mIsBound 
	           	 			&& HomeActivity.this.mBoundService.sendMessage(new RegisterMessage(UserManager.getUserId(), "unregister"))){
	           	 		Log.e(TAG, "Fail to send unregister message");
	           	 	}
					UserManager.setUserRole(null);
					SharedPreferences settings = getSharedPreferences(UserManager.SETTING_INFOS, 0);
					settings.edit().remove(UserManager.NAME).remove(UserManager.PASSWORD).commit();
					HomeActivity.this.refreshMenuByRole();
					HomeActivity.this.homecontent.selectItem("menu");
				}
				
			});
			
			menuitem = menuitems.findViewById(R.id.setting_btn);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					//TODO:
				}
				
			});
			OrderContentView orderview = new OrderContentView("order", this, homecontent);
			contentViews.add(orderview);
			FoodMenuContentView menucontentview = new FoodMenuContentView("menu", this, homecontent);
			contentViews.add(menucontentview);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			homecontent.setMenu(this.mLeftMenu);
		}
		setContentView(homecontent);
		this.refreshMenuByRole();
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
	
	private void showConnectResultToast(String result){
		 ToastUtil.showToast(this, result , Toast.LENGTH_SHORT);
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
                 	 final ProgressDialog progressingDialog  = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.waiting), getResources().getString(R.string.logining), true);  
                 	 HomeActivity.this.username = ((EditText)dialogView.findViewById(R.id.username)).getText().toString();
                 	 HomeActivity.this.password = ((EditText)dialogView.findViewById(R.id.password)).getText().toString();
                 	 HomeActivity.this.isRemeberMe = ((CheckBox)dialogView.findViewById(R.id.autologin)).isChecked();
                 	 UserManager.login(username, password, new HttpHandler(){
	                    		@Override
	                 			public void onSuccess(String action, String response){
	                    			progressingDialog.dismiss();
	                 				if(response == null || response.trim().equals("")){
	                 					handleLoginError("服务器错误");
	                 				} else{
	                 					try {
											JSONObject juser = new JSONObject(response);
											afterLoginSuccess(juser, false);
										} catch (JSONException e) {
											e.printStackTrace();
											handleLoginError("服务器错误");
										}
	                 				}
	                 			}
	                 			
	                 			@Override
	                 			public void onFail(String action, int errorcode){
	                 				progressingDialog.dismiss();
	                 				Log.e(TAG, "Fail to login with error code: " + errorcode);
	                 				handleLoginError("请检查网络连接");
	                 			}
                 	 });
                  }  
              })
              .create();
         }
         loginDialog.show();
    }
	
	private void handleLoginError(String detail){
		ToastUtil.showToast(this, detail, Toast.LENGTH_SHORT);
		this.showLoginDialog();
	}
	
	private void handleRegisterResultMsg(ResultMessage message){
		if(message.getResult() == Constants.SUCCESS){
			Log.i(TAG, "Success to register client to message service: " + message.getDetail());
		} else if(message.getResult() == Constants.FAIL){
			Log.e(TAG, "Fail to register client to message service: " + message.getDetail());
		}
	}
	
	private void handleConnectResultMsg(ResultMessage message){
		showConnectResultToast(message.getDetail());
		if(message.getResult() == Constants.SUCCESS){
			this.doAutoLogin();
			this.checkMenuUpdate();
			this.mOnlineInfoView.setText("状态: 在线");
		} else {
			UserManager.setUserRole(null);
			this.refreshMenuByRole();
			this.mOnlineInfoView.setText("状态: 离线");
		}
	}
	
	private void afterLoginSuccess(JSONObject jresp, boolean isAuto){
		String error = null;
		try {
			if(jresp.has("result") && jresp.getInt("result") == Constants.FAIL){
				error = jresp.getString("detail");
			} else {
				Long id = jresp.getLong("id");
				JSONArray roles = jresp.getJSONArray("role_Details");
				JSONObject firstrole = roles.getJSONObject(0);
				JSONObject jrole = firstrole.getJSONObject("role");
				int role = jrole.getInt("type");
				Log.i(TAG, "Login sucessfully: id is " + id + " role is " + role);
				UserManager.setUserId(id);
				UserManager.setUserRole(role);
				if(this.isRemeberMe){
					SharedPreferences settings = getSharedPreferences(UserManager.SETTING_INFOS, 0);
					settings.edit()
							  .putString(UserManager.NAME, this.username)
							  .putString(UserManager.PASSWORD, this.password)
							  .commit();
				}
				ToastUtil.showToast(this, "登录成功", Toast.LENGTH_SHORT);
				this.refreshMenuByRole();
				this.homecontent.selectItem("menu");
				if(this.mIsBound){
					if(this.mBoundService.sendMessage(new RegisterMessage(id, "register"))){
	           	 		Log.e(TAG, "Fail to send register message");
	           	 	}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			error = "服务器返回异常:" + jresp.toString();
		}
		if(error != null){
			Log.e(TAG, "Auto login fail: " + error);
			if(!isAuto){
				this.handleLoginError(error);
			} else {
				ToastUtil.showToast(this, error, Toast.LENGTH_SHORT);
			}
		}
	}
	
	private void doAutoLogin(){
		if(UserManager.getUserRole() == null){
			String [] userinfo = UserManager.getUsernameAndPassword(this);
			if(userinfo != null){
				//auto login
				this.username = userinfo[0];
				UserManager.login(userinfo[0], userinfo[1], new HttpHandler(){
				
					@Override
					public void onSuccess(String action, String response){
						if(response != null && !response.equals("")){
							try {
								JSONObject jresp =  new JSONObject(response);
								afterLoginSuccess(jresp, true);
							} catch (JSONException e) {
								e.printStackTrace();
								ToastUtil.showToast(HomeActivity.this, "登录失败，服务器错误", Toast.LENGTH_SHORT);
							}
						}
					}
					
					@Override
					public void onFail(String action, int statuscode){
						Log.e(TAG, "Login fail with error code:" + statuscode);
					}
				});
			}
		}
	}
	
	private void checkMenuUpdate(){
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
