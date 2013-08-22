package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.util.Constants;

import android.util.Log;

public class RegisterMessage extends Message{

	private static final long serialVersionUID = 6185409247446469881L;
	
	private Long userId;
	private String username;
	private String password;
	private String action;
	
	public RegisterMessage(){}
	
	public RegisterMessage(Long id, String action) {
		super(Constants.REGISTER_MESSAGE);
		this.userId = id;
		this.action = action;
	}
	
	public RegisterMessage(String username, String password, String action) {
		super(Constants.REGISTER_MESSAGE);
		this.username = username;
		this.password = password;
		this.action = action;
	}

	public void setUserId(Long id){
		this.userId = id;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setAction(String act){
		this.action = act;
	}
	
	public String getAction(){
		return this.action;
	}
	
	public void setUsername(String uname){
		this.username = uname;
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
			jsonObj.put("action", this.getAction());
			if(this.getUserId() != null){
				jsonObj.put("userId", this.getUserId());
			}
			if(this.getUsername() != null && this.getPassword() != null){
				jsonObj.put("username", this.getUsername());
				jsonObj.put("password", this.getPassword());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			super.fromJSONObject(jsonObj);
			this.setUserId(jsonObj.getLong("userId"));
			this.setType(jsonObj.getString("action"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
