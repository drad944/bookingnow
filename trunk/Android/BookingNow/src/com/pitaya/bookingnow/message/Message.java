package com.pitaya.bookingnow.message;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Serializable, IJSONTransition{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6356895567978585140L;
	
	private String category;
	protected String type;
	
	public Message(){}
	
	public Message(String category){
		this.category = category;
		this.type = this.getClass().getName();
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public String getCategory(){
		return this.category;
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
			jsonObj.put("category", this.getCategory());
			jsonObj.put("type", this.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			this.setCategory(jsonObj.getString("category"));
			this.setType(jsonObj.getString("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
