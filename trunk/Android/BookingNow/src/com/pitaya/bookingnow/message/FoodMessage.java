package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.util.Constants;

public class FoodMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5081500912835364921L;
	private boolean hasNew;
	
	public FoodMessage(){
		super(Constants.FOOD_MESSAGE);
	}
	
	public void setHasNew(boolean  b){
		this.hasNew = b;
	}
	
	public boolean getHasNew(){
		return this.hasNew;
	}
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		super.toJSONObject(jsonObj);
		try {
			jsonObj.put("hasNew", this.getHasNew());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		super.fromJSONObject(jsonObj);
		try {
			this.hasNew = jsonObj.getBoolean("hasNew");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
