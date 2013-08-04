package com.pitaya.bookingnow.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.util.Constants;

public class OrderDetailMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791996146552713312L;
	
	protected boolean hasNew;
	protected List<CookingItem> updateItems;
	protected List<CookingItem> removeItems;
	
	public OrderDetailMessage(){
		super(Constants.ORDER_MESSAGE);
	}
		
	public void setUpdateItems(List<CookingItem> items){
		this.updateItems = items;
	}

	public List<CookingItem> getUpdateItems(){
		return this.updateItems;
	}
	
	public void setRemoveItems(List<CookingItem> items){
		this.removeItems = items;
	}

	public List<CookingItem> getRemoveItems(){
		return this.removeItems;
	}
	
	public void setHasNew(boolean flag){
		this.hasNew = flag;
	}
	
	public boolean getHasNew(){
		return this.hasNew;
	}
	
	@Override
	public void toJSONObject(JSONObject jsonObj) {
		try {
			jsonObj.put("key", this.getCategory());
			jsonObj.put("type", this.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		try {
			super.fromJSONObject(jsonObj);
			if(jsonObj.has("updateItems")){
				JSONArray jupdateitems = jsonObj.getJSONArray("updateItems");
				if(jupdateitems.length() > 0){
					updateItems = new ArrayList<CookingItem>();
					for(int i = 0; i < jupdateitems.length(); i++){
						CookingItem updateitem = new CookingItem();
						updateitem.fromJSONObject(jupdateitems.getJSONObject(i));
						updateItems.add(updateitem);
					}
				}
			}
			if(jsonObj.has("removeItems")){
				JSONArray jremoveitems = jsonObj.getJSONArray("removeItems");
				if(jremoveitems.length() > 0){
					removeItems = new ArrayList<CookingItem>();
					for(int i = 0; i < jremoveitems.length(); i++){
						CookingItem removeitem = new CookingItem();
						removeitem.fromJSONObject(jremoveitems.getJSONObject(i));
						removeItems.add(removeitem);
					}
				}
			}
			this.hasNew = jsonObj.getBoolean("hasNew");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
