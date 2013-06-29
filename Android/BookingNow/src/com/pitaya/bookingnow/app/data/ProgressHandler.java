package com.pitaya.bookingnow.app.data;

import com.pitaya.bookinnow.app.util.Constants;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class ProgressHandler extends HttpHandler {
	
	private final static String TAG = "ProgressHandler";
	
	protected int index;
	protected int total;
	
	public ProgressHandler(int total){
		super();
		this.total = total;
		this.index = 0;
	}
	
	public void setIndex(int idx){
		this.index = idx;
	}
	
	public void onOtherFail(String detail){
	}
	
	@Override  
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle = msg.getData();
        int result = bundle.getInt("result");
        if(result == Constants.SUCCESS){
        	this.onSuccess(bundle.getString("detail"));
        } else if(result == Constants.FAIL){
        	String type = bundle.getString("detail");
        	if(type.equals("http")){
            	Log.e(TAG, "Fail to execute http request, code is " + bundle.getInt("statusCode"));
            	this.onFail(bundle.getInt("statusCode"));
        	} else {
        		this.onOtherFail(type);
        	}
        }
    }
		
}
