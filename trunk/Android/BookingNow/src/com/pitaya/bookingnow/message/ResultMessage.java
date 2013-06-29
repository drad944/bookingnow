package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ResultMessage extends Message{
	
	private static String LOGTAG = "BaseResultMessage";
	private static final long serialVersionUID = 6351604296315217738L;
	
	private int requestType;
	private int result;
	private String detail;
	
	public ResultMessage(){}
	
	public ResultMessage(String key, int reqtype, int result, String detail) {
		super(key);
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
			Log.e(LOGTAG, "Fail to parse json string from login result message");
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
			Log.e(LOGTAG, "Fail to parse login result message from json string");
			e.printStackTrace();
		}
	}
	
}
