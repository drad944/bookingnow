package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;

import com.pitaya.bookingnow.app.net.MessageService;
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
import android.content.DialogInterface;  

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
		messageService = MessageService.initService("192.168.0.102", 19191);
		messageService.registerHandler(messageKey,  new MessageHandler());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "Role is " + this.role);
		this.setHomeContent();
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
	   
	
	@Override  
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		messageService.unregisterHandler(messageKey);
    }
	
	private void setHomeContent(){
		if(homecontent == null){
			if(this.role == null){
				ArrayList<BaseContentView> contentViews = new ArrayList<BaseContentView>();
				homecontent = new SlideContent(this, contentViews);
				View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
				LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
				
				for(int i=0; i < 3; i++){
					final int index = i;
					TextView menuitem = new TextView(this);
					menuitem.setTextColor(android.graphics.Color.WHITE);
					menuitem.setTextSize(25);
					menuitem.setGravity(Gravity.CENTER);
					contentViews.add(new GoodMenuContentView(this));
					switch(index){
						case 0:
							menuitem.setText("菜单");
							menuitem.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View view) {
									homecontent.selectItem(index);
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
									homecontent.selectItem(index);
								}
								
							});
							break;
					}
					menuitems.addView(menuitem);
				}
				homecontent.setMenu(leftmenu);
				setContentView(homecontent);
			} else if(this.role.equals("waiter")){
				this.updateHomeContent(null);
			}
		} else {
			setContentView(homecontent);
		}
		
	}
	
	private void updateHomeContent(String config){
//		ArrayList<View> contentViews = new ArrayList<View>();
//		homecontent = new SlideContent(this, contentViews);
//		View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
//		LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
//		for(int i=0; i < 4; i++){
//			final int index = i;
//			TextView menuitem = new TextView(this);
//			menuitem.setTextColor(android.graphics.Color.WHITE);
//			menuitem.setTextSize(25);
//			menuitem.setGravity(Gravity.CENTER);
//			switch(index){
//				case 0:
//					menuitem.setText("订单");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem(index);
//						}
//					});
//					contentViews.add(new GalleryView(this));
//					break;
//				case 1:
//					menuitem.setText("菜单");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem(index);
//						}
//					});
//					contentViews.add(new GalleryView(this));
//					break;
//				case 2:
//					menuitem.setText("设置");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem(index);
//						}
//						
//					});
//					contentViews.add(new GalleryView(this));
//					break;
//				case 3:
//					menuitem.setText("退出");
//					menuitem.setOnClickListener(new OnClickListener(){
//						@Override
//						public void onClick(View view) {
//							homecontent.selectItem(index);
//						}
//						
//					});
//					contentViews.add(new GalleryView(this));
//					break;
//			}
//			menuitems.addView(menuitem);
//		}
//		homecontent.setMenu(leftmenu);
//		setContentView(homecontent);
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
	
	private void handleLoginResultMsg(BaseResultMessage message){
		this.progressingDialog.dismiss();
		if(message.getResult().equals("success")){
			Log.i("HomeActivity", "Success to login");
			this.role = message.getDetail();
			this.loginDialog.dismiss();
			updateHomeContent(null);
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
	
}
