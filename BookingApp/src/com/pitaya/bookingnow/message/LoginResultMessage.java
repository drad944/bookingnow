package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class LoginResultMessage extends Message{
	
	private static String LOGTAG = "LoginResultMessage";
	private static final long serialVersionUID = 6351604296315217738L;
	
	private String result;
	private String detail;
	
	public LoginResultMessage(){}
	
	public LoginResultMessage(String key, String result, String detail) {
		super(key);
		this.result = result;
		this.detail = detail;
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
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			super.toJSONObject(jsonObj);
			jsonObj.put("result", this.getResult());
			jsonObj.put("detail", this.getDetail());
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
		} catch (JSONException e) {
			Log.e(LOGTAG, "Fail to parse login result message from json string");
			e.printStackTrace();
		}
	}
	
}
