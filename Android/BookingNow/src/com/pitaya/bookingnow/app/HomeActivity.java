package com.pitaya.bookingnow.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
import com.pitaya.bookingnow.app.data.ProgressHandler;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuContentProvider;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.FoodService;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.views.*;
import com.pitaya.bookingnow.message.*;
import com.pitaya.bookinnow.app.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.ContentValues;
import android.content.DialogInterface;  
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HomeActivity extends FragmentActivity {
	
	public static final int MENU_CHECKING_LOADER = 0;
	public static final int MENU_LOADER = MENU_CHECKING_LOADER + 1;
	public static final int ORDER_LIST_LOADER = MENU_LOADER + 1;
	
	private static String TAG = "HomeActivity";
	private static String messageKey = UUID.randomUUID().toString();
	
	private SlideContent homecontent;
	private String role;
	
	private AlertDialog loginDialog;
	private ProgressDialog progressingDialog;
	
	private MessageService messageService;
	private  boolean isUpdating = false;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(bundle != null){
			this.role = bundle.getString("role");
		}
		SharedPreferences settings = getSharedPreferences(UserManager.SETTING_INFOS, 0);
		settings.edit()
				.putString(UserManager.NAME, "lili")
				.putString(UserManager.PASSWORD, "123456")
				.commit();
		this.setHomeContent();
		messageService = MessageService.initService("192.168.0.102", 19191);
		messageService.registerHandler(messageKey,  new MessageHandler());
		//testInsertDB();
		setOrder();
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		setIntent(intent);
		setOrder();
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
		if(!messageService.isReady()){
			if(messageService.isConnecting()){ 
				showConnectResultToast("正在连接服务器...");
			} else {
				MessageService.initService("192.168.0.102", 19191);
			}
		} else {
			Log.i(TAG, "The service is ready");
			this.doLoginIfNeed();
			this.checkMenuUpdate();
		}
	}
	
	
   @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString("role", this.role);
		super.onSaveInstanceState(savedInstanceState);
    }
	   
//    @Override
//    public void onConfigurationChanged(Configuration newConfig){
//    	super.onConfigurationChanged(newConfig);
//    	setContentView(homecontent);
//    }
   
	
	@Override  
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		messageService.unregisterHandler(messageKey);
    }
	
	private synchronized void refreshMenuByRole(){
		Integer role = UserManager.getUserRole();
		if(role != null){
			
		} else {
			
		}
	}
	
	private void setHomeContent(){
		if(homecontent == null){
			ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
			homecontent = new SlideContent(this, contentViews);
			View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
			LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
			if(this.role == null){
				for(int i=0; i < 3; i++){
					TextView menuitem = new TextView(this);
					menuitem.setTextColor(android.graphics.Color.WHITE);
					menuitem.setTextSize(25);
					menuitem.setGravity(Gravity.CENTER);
					
					switch(i){
						case 0:
							menuitem.setText("菜单");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									((FoodMenuContentView)homecontent.getContentView("menu")).setOrder(null);
									homecontent.selectItem("menu");
								}
							});
							break;
						case 1:
							menuitem.setText("登录");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									showLoginDialog(getResources().getString(R.string.login_title));
								}
								
							});
							break;
						case 2:
							menuitem.setText("订单");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									if(homecontent.getCurrentContentViewKey().equals("menu")){
										Order currentOrder =  ((FoodMenuContentView)homecontent.getContentView("menu")).getOrder();
										if(currentOrder != null && currentOrder.isDirty() && currentOrder.getStatus() != Order.NEW){
											showConfirmDialog("order");
										} else {
											homecontent.selectItem("order");
										}
									} else {
										homecontent.selectItem("order");
									}
								}
								
							});
							break;
					}
					
					menuitems.addView(menuitem);
				}//end for
				OrderContentView orderview = new OrderContentView("order", this, homecontent);
				contentViews.add(orderview);
				FoodMenuContentView menucontentview = new FoodMenuContentView("menu", this, homecontent);
				contentViews.add(menucontentview);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			homecontent.setMenu(leftmenu);
		}
		setContentView(homecontent);
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
	
	private void showLoginDialog(String title){
         LayoutInflater factory = LayoutInflater.from(HomeActivity.this);  
         final View dialogView = factory.inflate(R.layout.logindialog, null);
         loginDialog = new AlertDialog.Builder(HomeActivity.this)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){
                     @Override  
                     public void onClick(DialogInterface dialog, int which) {
                    	 progressingDialog  = ProgressDialog.show(HomeActivity.this, getResources().getString(R.string.waiting), getResources().getString(R.string.logining), true);  
                    	 String username = ((EditText)dialogView.findViewById(R.id.username)).getText().toString();
                    	 String password = ((EditText)dialogView.findViewById(R.id.password)).getText().toString();
                    	 if(!messageService.sendMessage(new LoginMessage(messageKey, username, password))){
                    		 handleLoginError("请检查连接");
                    	 }
                     }  
                 })
                 .create();  
         loginDialog.show();
    }
	
	private void handleLoginError(String error){
		this.progressingDialog.dismiss();
		this.showLoginDialog(error);
	}
	
	private void handleLoginResultMsg(ResultMessage message){
		this.progressingDialog.dismiss();
		if(message.getResult() == Constants.SUCCESS){
			Log.i("HomeActivity", "Success to login");
			this.role = message.getDetail();
			this.loginDialog.dismiss();
		} else if(message.getResult() == Constants.FAIL){
			this.showLoginDialog(message.getDetail());
		}
	}
	
	private void handleConnectResultMsg(ResultMessage message){
		showConnectResultToast(message.getDetail());
		if(message.getResult() == Constants.SUCCESS){
			this.doLoginIfNeed();
			this.checkMenuUpdate();
		}
	}
	
	private void doLoginIfNeed(){
		if(UserManager.getUserRole() == null){
			String [] userinfo = UserManager.getUsernameAndPassword(this);
			if(userinfo != null){
				//auto login
				UserManager.login(userinfo[0], userinfo[1], new HttpHandler(){
				
					@Override
					public void onSuccess(String action, String response){
						if(response != null && !response.equals("")){
							try {
								JSONObject jresp =  new JSONObject(response);
								if(jresp.has("result") && jresp.getInt("result") == Constants.FAIL){
									Log.w(TAG, "Login fail reason:" + jresp.getString("detail"));
								} else {
									Long id = jresp.getLong("id");
									JSONArray roles = jresp.getJSONArray("role_Details");
									JSONObject firstrole = roles.getJSONObject(0);
									JSONObject jrole = firstrole.getJSONObject("role");
									String role_name = jrole.getString("name");
									Log.i(TAG, "Login sucessfully: id is " + id + " role is " + role_name);
									UserManager.setUserId(id);
									UserManager.setUserRole(Constants.WAITER_ROLE);
									HomeActivity.this.refreshMenuByRole();
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
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
	
	private class MessageHandler extends Handler{
		
        @Override  
        public void handleMessage(Message msg) {
            super.handleMessage(msg);  
            Bundle bundle = msg.getData();  
            Object obj =bundle.getSerializable("message");
            if(obj instanceof ResultMessage){
            	ResultMessage resultmsg = (ResultMessage)obj;
            	if(resultmsg.getRequestType() == Constants.LOGIN_REQUEST){
            		handleLoginResultMsg(resultmsg);
            	} else if(resultmsg.getRequestType() == Constants.SOCKET_CONNECTION){
            		handleConnectResultMsg(resultmsg);
            	}
            }
        }
    }
	
	private void checkMenuUpdate(){
		synchronized(this) {
			if(this.isUpdating){
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
						executeUpdateMenu(FoodService.getUpdatedFoodsList(response));
					} catch (JSONException e) {
						Log.e(TAG, "Fail to parse menu update response: " + response);
						e.printStackTrace();
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
	
	private void executeUpdateMenu(Map<String, ArrayList<Food>> foodsToUpdate){
		if(foodsToUpdate == null){
			return;
		}
		int total = 0;
		for(Entry<String, ArrayList<Food>> entry : foodsToUpdate.entrySet()){
			total += entry.getValue().size();
		}
		if(total > 0){
			final ProgressDialog updateProgressDialog=new ProgressDialog(this);
			updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			updateProgressDialog.setTitle("菜单更新中");
			updateProgressDialog.setMax(total);
			updateProgressDialog.setProgress(0);
			updateProgressDialog.setCancelable(true);
			updateProgressDialog.setIndeterminate(false);
			updateProgressDialog.show();
			final int max = total;
			FoodService.updateMenuFoods(this, foodsToUpdate, new ProgressHandler(total){

				@Override
				public void onSuccess(String action, String response){
					Log.i(TAG, "Success to update food " + this.index);
					updateProgressDialog.setProgress(this.index);
					if(this.index == max){
						synchronized(HomeActivity.this) {
							HomeActivity.this.isUpdating = false;
						}
						updateProgressDialog.dismiss();
						onUpdateMenuFinish();
					}
				}
				
				@Override
				public void onFail(String action, int errorcode){
					Log.w(TAG, "Fail to update food " + (this.index) +" with error code:" + errorcode);
					updateProgressDialog.setProgress(this.index);
					if(this.index == max){
						synchronized(HomeActivity.this) {
							HomeActivity.this.isUpdating = false;
						}
						updateProgressDialog.dismiss();
						onUpdateMenuFinish();
					}
				}
				
			});
		} else {
			Log.i(TAG, "The menu is latest.");
		}
	}
	
	//temp for test
	private void testInsertDB(){
        Field[] fields = R.drawable.class.getDeclaredFields();
        String [] categories = new String[]{"中餐","西餐","点心","酒水"};
        for (int i=0; i < fields.length; i++){
        	Field field = fields[i];
        	
            if (field.getName().endsWith("s")){
            	String food_key = UUID.randomUUID().toString();
            	String name = field.getName();
            	String category = categories[i%4];
            	String desc = "这是第"+i+"道菜的描述";
            	float price = i*10f;
            	String status = "ready";
            	int orderidx = i%4+1;

                int index = 0;
				try {
					index = field.getInt(R.drawable.class);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				int index2 = 0;
				try {
					Field field2 = R.drawable.class.getField(field.getName().substring(0,1)+"l");
					index2 = field2.getInt(R.drawable.class);
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				byte[] simage = FileUtil.getImageBytes(BitmapFactory.decodeResource(this.getResources(), index));
				byte[] limage =FileUtil.getImageBytes(BitmapFactory.decodeResource(this.getResources(), index2));
				String image_s_name = food_key + "_s";
				String image_l_name =  food_key + "_l";
				FileUtil.writeFile(this, image_s_name, simage);
				FileUtil.writeFile(this, image_l_name, limage);
				String revision = String.valueOf(System.currentTimeMillis());
				ContentValues values = new ContentValues();
				values.put(FoodMenuTable.COLUMN_FOOD_KEY, food_key);
				values.put(FoodMenuTable.COLUMN_NAME, name);
				values.put(FoodMenuTable.COLUMN_CATEGORY, category);
				values.put(FoodMenuTable.COLUMN_DESCRIPTION, desc);
				values.put(FoodMenuTable.COLUMN_PRICE, price);
				values.put(FoodMenuTable.COLUMN_STATUS, status);
				values.put(FoodMenuTable.COLUMN_RECOMMENDATION, "false");
				values.put(FoodMenuTable.COLUMN_ORDERINDEX, orderidx);
				values.put(FoodMenuTable.COLUMN_REVISION, revision);
				values.put(FoodMenuTable.COLUMN_IAMGE_REVISION, revision);
				getContentResolver().insert(FoodMenuContentProvider.CONTENT_URI, values);
            }
        }  
	}
	
}
