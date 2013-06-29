package com.pitaya.bookingnow.app.data;

import com.pitaya.bookinnow.app.util.Constants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpHandler extends Handler{
	
	private final static String TAG= "HttpHandler";

	
	public void onSuccess(String response){
	}
	
	public void onFail(int statuscode){
	}
	
	
	@Override  
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        int result =bundle.getInt("result");
        if(result == Constants.SUCCESS){
        	this.onSuccess(bundle.getString("detail"));
        } else if(result == Constants.FAIL){
        	Log.e(TAG, "Fail to execute http request, code is " + bundle.getInt("statusCode"));
        	this.onFail(bundle.getInt("statusCode"));
        }
    }
	
}
