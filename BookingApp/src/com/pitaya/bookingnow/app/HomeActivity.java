package com.pitaya.bookingnow.app;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pitaya.bookingnow.app.R;

import com.pitaya.bookingnow.app.net.MessageService;
import com.pitaya.bookingnow.app.views.ContentView;
import com.pitaya.bookingnow.app.views.GalleryView;
import com.pitaya.bookingnow.app.views.SlideContent;
import com.pitaya.bookingnow.message.LoginMessage;
import com.pitaya.bookingnow.message.LoginResultMessage;
import com.pitaya.bookinnow.app.util.ToastUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.content.DialogInterface;  

public class HomeActivity extends FragmentActivity {
	
	private SlideContent homecontent;
	private String role;
	private AlertDialog loginDialog;
	private ProgressDialog progressingDialog;
	private MessageService messageService;
	private String messageKey = UUID.randomUUID().toString();
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//setContentView(R.layout.home_layout);
		messageService = MessageService.initService("192.168.0.102", 19191);
		messageService.registerHandler(messageKey,  new MessageHandler());
//		boolean showonce = false;
//		while(!messageService.isReady()){
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			if(showonce == false){
//			    showConnectFailToast();
//			    showonce = true;
//			}
//		}
//      this.showLoginDialog(getResources().getString(R.string.login_title));
		this.showHomeContent();
	}
	
	private void showHomeContent(){
		Map<Integer, ContentView> contentViews = new HashMap<Integer, ContentView>();
		homecontent = new SlideContent(this, contentViews);
		View leftmenu = getLayoutInflater().inflate(R.layout.leftmenu, null);
		LinearLayout menuitems = (LinearLayout)(leftmenu.findViewById(R.id.leftmenu));
		for(int i=0; i < 5; i++){
			final int index = i;
			TextView menuitem = new TextView(this);
			menuitem.setTextColor(android.graphics.Color.WHITE);
			menuitem.setTextSize(20);
			menuitem.setText("menu" + i);
			menuitem.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					homecontent.selectItem(index);
				}
				
			});
			menuitems.addView(menuitem);
			contentViews.put(i, new GalleryView(this));
		}
		homecontent.setMenu(leftmenu);
		setContentView(homecontent);
	}
	
	private void showConnectFailToast(){
		 ToastUtil.showToast(this, getResources().getString(R.string.connect_fail), Toast.LENGTH_LONG);
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
	
	public void handleLoginResultMsg(LoginResultMessage message){
		this.progressingDialog.dismiss();
		if(message.getResult().equals("success")){
			this.role = message.getDetail();
			this.loginDialog.dismiss();
		} else if(message.getResult().equals("fail")){
			this.showLoginDialog(message.getDetail());
		}
	}
	
	private class MessageHandler extends Handler{
        @Override  
        public void handleMessage(Message msg) {
            super.handleMessage(msg);  
            Bundle bundle = msg.getData();  
            Object obj =bundle.getSerializable("message");
            if(obj instanceof LoginResultMessage){
            	handleLoginResultMsg((LoginResultMessage)obj);
            }
        }
    }
	
}
