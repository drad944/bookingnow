package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.util.Constants;

import android.util.Log;

public class ResultMessage extends Message{
	
	private static String TAG = "ResultMessage";
	private static final long serialVersionUID = 6351604296315217738L;
	
	private int requestType;
	private int result;
	private String detail;
	
	public ResultMessage(){}
	
	public ResultMessage(int reqtype, int result, String detail) {
		super(Constants.RESULT_MESSAGE);
		this.result = result;
		this.detail = detail;
		this.requestType = reqtype;
	}

	public void setResult(int rs){
		this.result = rs;
	}
	
	public int getResult(){
		return this.result;
	}
	
	public void setDetail(String detail){
		this.detail = detail;
	}
	
	public String getDetail(){
		return this.detail;
	}
	
	public void setRequestType(int type){
		this.requestType = type;
	}
	
	public int getRequestType(){
		return this.requestType;
	}
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			super.toJSONObject(jsonObj);
			jsonObj.put("result", this.getResult());
			jsonObj.put("detail", this.getDetail());
			jsonObj.put("requestType", this.getRequestType());
		} catch (JSONException e) {
			Log.e(TAG, "Fail to parse json string from result message");
			e.printStackTrace();
		}
	}
	
	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			super.fromJSONObject(jsonObj);
			this.setResult(jsonObj.getInt("result"));
			this.setDetail(jsonObj.getString("detail"));
			this.setRequestType(jsonObj.getInt("requestType"));
		} catch (JSONException e) {
			Log.e(TAG, "Fail to parse result message from json string");
			e.printStackTrace();
		}
	}
	
}
