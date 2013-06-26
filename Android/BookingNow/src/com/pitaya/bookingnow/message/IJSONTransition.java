package com.pitaya.bookingnow.message;

import org.json.JSONObject;

public interface IJSONTransition {

	public void toJSONObject(JSONObject jsonObj);
	public void fromJSONObject(JSONObject jsonObj);
	
}
