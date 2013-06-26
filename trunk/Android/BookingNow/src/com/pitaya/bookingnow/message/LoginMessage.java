package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class LoginMessage extends Message{

	private static String LOGTAG = "LoginMessage";
	private static final long serialVersionUID = 6185409247446469881L;
	
	private String username;
	private String password;
	
	public LoginMessage(){}
	
	public LoginMessage(String key, String username, String password) {
		super(key);
		this.username = username;
		this.password = password;
	}

	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setPassword(String pwd){
		this.password = pwd;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			super.toJSONObject(jsonObj);
			jsonObj.put("username", this.getUsername());
			jsonObj.put("password", this.getPassword());
		} catch (JSONException e) {
			Log.e(LOGTAG, "Fail to parse json string from login message object");
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			super.fromJSONObject(jsonObj);
			this.setUsername(jsonObj.getString("username"));
			this.setPassword(jsonObj.getString("password"));
		} catch (JSONException e) {
			Log.e(LOGTAG, "Fail to parse login message object from json string");
			e.printStackTrace();
		}
	}
}
