package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.util.Constants;

import android.util.Log;

public class RegisterMessage extends Message{

	private static final long serialVersionUID = 6185409247446469881L;
	
	private Long userId;
	private String action;
	
	public RegisterMessage(){}
	
	public RegisterMessage(Long id, String action) {
		super(Constants.REGISTER_MESSAGE);
		this.userId = id;
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
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			super.toJSONObject(jsonObj);
			jsonObj.put("userId", this.getUserId());
			jsonObj.put("action", this.getAction());
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
