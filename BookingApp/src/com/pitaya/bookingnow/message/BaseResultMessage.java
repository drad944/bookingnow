package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class BaseResultMessage extends Message{
	
	private static String LOGTAG = "BaseResultMessage";
	private static final long serialVersionUID = 6351604296315217738L;
	
	private String requestType;
	private String result;
	private String detail;
	
	public BaseResultMessage(){}
	
	public BaseResultMessage(String key, String reqtype, String result, String detail) {
		super(key);
		this.result = result;
		this.detail = detail;
		this.requestType = reqtype;
	}

	public void setResult(String rs){
		this.result = rs;
	}
	
	public String getResult(){
		return this.result;
	}
	
	public void setDetail(String detail){
		this.detail = detail;
	}
	
	public String getDetail(){
		return this.detail;
	}
	
	public void setRequestType(String type){
		this.requestType = type;
	}
	
	public String getRequestType(){
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
			Log.e(LOGTAG, "Fail to parse json string from login result message");
			e.printStackTrace();
		}
	}
	
	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			super.fromJSONObject(jsonObj);
			this.setResult(jsonObj.getString("result"));
			this.setDetail(jsonObj.getString("detail"));
			this.setRequestType(jsonObj.getString("requestType"));
		} catch (JSONException e) {
			Log.e(LOGTAG, "Fail to parse login result message from json string");
			e.printStackTrace();
		}
	}
	
}
