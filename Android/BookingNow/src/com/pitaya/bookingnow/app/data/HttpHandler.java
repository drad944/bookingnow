package com.pitaya.bookingnow.app.data;

import com.pitaya.bookingnow.app.util.Constants;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpHandler extends Handler implements AsyncCallBack{
	
	public final static String RESULT = "result";
	public final static String ACTION_TYPE = "action_type";
	public final static String ERROR_CODE = "code";
	public final static String RESPONSE = "response";
	
	private final static String TAG= "HttpHandler";

	protected void afterHandlerMessage(Bundle bundle){
        int result =bundle.getInt(RESULT);
        if(result == Constants.SUCCESS){
        	this.onSuccess(bundle.getString(ACTION_TYPE), bundle.getString(RESPONSE));
        } else if(result == Constants.FAIL){
        	Log.e(TAG, "Error in http service, code is " + bundle.getInt(ERROR_CODE));
        	this.onFail(bundle.getString(ACTION_TYPE), bundle.getInt(ERROR_CODE));
        }
	}
	
	@Override  
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        afterHandlerMessage(msg.getData());
    }

	@Override
	public void onSuccess(String action, String response) {
		
	}

	@Override
	public void onFail(String action, int statuscode) {
		
	}
	
}
