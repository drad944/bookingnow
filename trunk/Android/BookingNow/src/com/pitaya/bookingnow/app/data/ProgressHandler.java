package com.pitaya.bookingnow.app.data;

import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookinnow.app.util.Constants;

import android.os.Bundle;

public class ProgressHandler extends HttpHandler {
	
	protected int index;
	protected int total;
	private Map<String, Boolean> handleResults;
	
	public ProgressHandler(int total){
		super();
		this.total = total;
		this.index = 0;
		this.handleResults = new HashMap<String, Boolean>();
	}
	
	public Map<String, Boolean> getResults(){
		return this.handleResults;
	}
	
	protected void afterHandlerMessage(Bundle bundle){
		int result = bundle.getInt(RESULT);
        String itemkey = bundle.getString("key");
        if(result == Constants.SUCCESS){
        	if(handleResults.get(itemkey) == null){
        		handleResults.put(itemkey, true);
        		this.index ++;
        	}
        } else if(result == Constants.FAIL){
        	if(handleResults.get(itemkey) == null){
        		this.index ++;
        	}
        	handleResults.put(itemkey, false);
        }
        super.afterHandlerMessage(bundle);
	}
	
}