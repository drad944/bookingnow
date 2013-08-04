package com.pitaya.bookingnow.app.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MessageHandler extends Handler{
	
	private OnMessageListener mListener;
	
    @Override  
    public void handleMessage(Message msg) {
        super.handleMessage(msg);  
        Bundle bundle = msg.getData();  
        Object obj =bundle.getSerializable("message");
        if(obj instanceof com.pitaya.bookingnow.message.Message && this.mListener != null){
        	mListener.onMessage((com.pitaya.bookingnow.message.Message)obj);
        }
    }
    
    public void setOnMessageListener(OnMessageListener lr){
    	this.mListener = lr;
    }
    
    public interface OnMessageListener{
    	public void onMessage(com.pitaya.bookingnow.message.Message message);
    }
	
}
