package com.pitaya.bookingnow.message;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Serializable, IJSONTransition{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6356895567978585140L;
	
	private String key;
	protected String type;
	
	public Message(){}
	
	public Message(String key){
		this.key = key;
		this.type = this.getClass().getName();
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}

	protected void putCommonThings(JSONObject jsonObj) throws JSONException{
		
	}

	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			jsonObj.put("key", this.getKey());
			jsonObj.put("type", this.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			this.setKey(jsonObj.getString("key"));
			this.setType(jsonObj.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
