package com.pitaya.bookingnow.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;

import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.service.FoodMenuContentProvider;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.views.*;
import com.pitaya.bookingnow.message.*;
import com.pitaya.bookinnow.app.util.ToastUtil;

import android.app.ActionBar;
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
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HomeActivity extends FragmentActivity {
	
	private static String TAG = "HomeActivity";
	private static String messageKey = UUID.randomUUID().toString();
	
	private SlideContent homecontent;
	private String role;
	
	private AlertDialog loginDialog;
	private ProgressDialog progressingDialog;
	
	private MessageService messageService;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(bundle != null){
			this.role = bundle.getString("role");
		}
		this.setHomeContent();
		messageService = MessageService.initService("192.168.0.102", 19191);
		messageService.registerHandler(messageKey,  new MessageHandler());
		//testInsertDB();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "Role is " + this.role);
		MessageService.initService("192.168.0.102", 19191);
		if(messageService.isConnecting()){ 
			showConnectResultToast("正在连接服务器...");
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
	
	private void setHomeContent(){
		if(homecontent == null){
			ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
			homecontent = new SlideContent(this, contentViews);
			View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
			LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
			if(this.role == null){
				for(int i=0; i < 3; i++){
					final int index = i;
					TextView menuitem = new TextView(this);
					menuitem.setTextColor(android.graphics.Color.WHITE);
					menuitem.setTextSize(25);
					menuitem.setGravity(Gravity.CENTER);
					FoodMenuContentView foodmenucontentview = new FoodMenuContentView("menu" + index, this, homecontent, new Ticket("A1", "rmzhang"));
					contentViews.add(foodmenucontentview);
					switch(index){
						case 0:
							menuitem.setText("菜单");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									homecontent.selectItem("menu" + index);
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
							menuitem.setText("设置");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									homecontent.selectItem("menu" + index);
								}
								
							});
							break;
					}
					menuitems.addView(menuitem);
				}
			} 
			homecontent.setMenu(leftmenu);
		}
		setContentView(homecontent);
	}
	
//	private void updateHomeContent(String config){
//		ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
//		homecontent.updateContentViews(contentViews);
//		View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
//		LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
//		for(int i=0; i < 4; i++){
//			final int index = i;
//			TextView menuitem = new TextView(this);
//			menuitem.setTextColor(android.graphics.Color.WHITE);
//			menuitem.setTextSize(25);
//			menuitem.setGravity(Gravity.CENTER);
//			contentViews.add(new FoodMenuContentView("menu" + index, this, homecontent));
//			switch(index){
//				case 0:
//					menuitem.setText("订单");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem("menu" + index);
//						}
//					});
//					break;
//				case 1:
//					menuitem.setText("菜单");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem("menu" + index);
//						}
//					});
//					break;
//				case 2:
//					menuitem.setText("设置");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem("menu" + index);
//						}
//						
//					});
//					break;
//				case 3:
//					menuitem.setText("退出");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem("menu" + index);
//						}
//						
//					});
//					break;
//			}
//			menuitems.addView(menuitem);
//		}
//		homecontent.setMenu(leftmenu);
//		setContentView(homecontent);
//	}
	
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
	
	private void handleLoginResultMsg(BaseResultMessage message){
		this.progressingDialog.dismiss();
		if(message.getResult().equals("success")){
			Log.i("HomeActivity", "Success to login");
			this.role = message.getDetail();
			this.loginDialog.dismiss();
			//updateHomeContent(null);
		} else if(message.getResult().equals("fail")){
			this.showLoginDialog(message.getDetail());
		}
	}
	
	private void handleConnectResultMsg(BaseResultMessage message){
		showConnectResultToast(message.getDetail());
	}
	
	
	private class MessageHandler extends Handler{
        @Override  
        public void handleMessage(Message msg) {
            super.handleMessage(msg);  
            Bundle bundle = msg.getData();  
            Object obj =bundle.getSerializable("message");
            if(obj instanceof BaseResultMessage){
            	BaseResultMessage resultmsg = (BaseResultMessage)obj;
            	if(resultmsg.getRequestType().equals("login")){
            		handleLoginResultMsg(resultmsg);
            	} else if(resultmsg.getRequestType().equals("connection")){
            		handleConnectResultMsg(resultmsg);
            	}
            }
        }
    }
	
	//Temp function for test
	private byte[] getIconData(Bitmap bitmap){
	    int size = bitmap.getWidth()*bitmap.getHeight()*4;
	    ByteArrayOutputStream out = new ByteArrayOutputStream(size);
	    try {
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	        out.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return out.toByteArray();
	}
	
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
				
				byte[] simage = this.getIconData(BitmapFactory.decodeResource(this.getResources(), index));
				byte[] limage = this.getIconData(BitmapFactory.decodeResource(this.getResources(), index2));
				String revision = String.valueOf(System.currentTimeMillis());
				ContentValues values = new ContentValues();
				values.put(FoodMenuTable.COLUMN_FOOD_KEY, food_key);
				values.put(FoodMenuTable.COLUMN_NAME, name);
				values.put(FoodMenuTable.COLUMN_CATEGORY, category);
				values.put(FoodMenuTable.COLUMN_DESCRIPTION, desc);
				values.put(FoodMenuTable.COLUMN_PRICE, price);
				values.put(FoodMenuTable.COLUMN_STATUS, status);
				values.put(FoodMenuTable.COLUMN_ORDERINDEX, orderidx);
				values.put(FoodMenuTable.COLUMN_IMAGE_S, simage);
				values.put(FoodMenuTable.COLUMN_IMAGE_L, limage);
				values.put(FoodMenuTable.COLUMN_REVISION, revision);
				getContentResolver().insert(FoodMenuContentProvider.CONTENT_URI, values);
            }
        }  
	}
	
}
