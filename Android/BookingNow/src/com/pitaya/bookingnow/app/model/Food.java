package com.pitaya.bookingnow.app.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.pitaya.bookingnow.message.IJSONTransition;

public class Food implements Serializable, IJSONTransition{
	
	private static final long serialVersionUID = -7297469175751088249L;
	private static final String TAG = "Food";
	
	private String key;
	private String name;
	private String description;
	private String category;
	private Long version;
	private Long image_version;
	private double price;
	private byte[] imageBytesSmall;
	private byte[] imageBytesLarge;
	private String image_s_name;
	private String image_l_name;
	private boolean isRecommended;
	private int status;
	private int orderindex;
	
	public Food(){
	}
	
	//This is for checking update
	public Food(String key, Long version, Long iversion){
		this.key = key;
		this.version = version;
		this.image_version = iversion;
	}
	
	//This is for menu display
	public Food(String key, String name, double price, String desc, String category, 
			boolean recommend){
		this.key = key;
		this.name = name;
		this.price = price;
		this.description = desc;
		this.category = category;
		this.image_s_name = this.key + "_s";
		this.image_l_name = this.key + "_l";
//		this.image_s_name = "tests.jpg";
//		this.image_l_name = "testl.jpg";
	}
	
	//This is for save new food to local
	public Food(String key, String name, double price, String desc, String category, 
			boolean recommend, Long version, Long iversion){
		this(key, name, price, desc, category, recommend);
		this.version = version;
		this.image_version = iversion;
	}
	
	public Food(String key, String name, double price, String desc, String category, 
			boolean recommend, byte[] bytes, byte[] bytes2){
		this(key, name, price, desc, category, recommend);
		this.imageBytesSmall = bytes;
		this.imageBytesLarge = bytes2;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public byte[] getSmallImage(){
		return this.imageBytesSmall;
	}
	
	public byte[] getLargeImage(){
		return this.imageBytesLarge;
	}
	
	public String getSmallImageName(){
		return this.image_s_name;
	}
	
	public String getLargeImageName(){
		return this.image_l_name;
	}
	
	public String getCategory(){
		return this.category;
	}
	
	public Double getPrice(){
		return this.price;
	}
	
	public Long getVersion(){
		return this.version;
	}
	
	public Long getImageVersion(){
		return this.image_version;
	}
	
	public boolean isRecommended(){
		return this.isRecommended;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public void setOrderIndex(int idx){
		this.orderindex = idx;
	}
	
	public int getOrderIndex(){
		return this.orderindex;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Food)){
			return false;
		}
		return this.key.equals(((Food)obj).getKey());
	}
	
	@Override
	public int hashCode(){
		return this.key.hashCode();
	}

	@Override
	public void toJSONObject(JSONObject jfood) {
		JSONObject jpic = new JSONObject();
		try {
			jfood.put("id", Long.parseLong(getKey()));
			jfood.put("version", getVersion());
			jpic.put("version", getImageVersion());
			jfood.put("picture", jpic);
		} catch (NumberFormatException e) {
			Log.w(TAG, "Fail to parse food to json object");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.w(TAG, "Fail to parse food to json object");
			e.printStackTrace();
		}
	}

	@Override
	public void fromJSONObject(JSONObject jfood) {
		try {
			this.key = String.valueOf(jfood.getLong("id"));
			this.name = jfood.getString("name");
			this.category = jfood.getString("category");
			this.description = jfood.getString("description");
			this.price = jfood.getDouble("price");
			this.isRecommended = jfood.getInt("recommendation") > 0;
			this.status = jfood.getInt("status");
			this.version = jfood.getLong("version");
			if(jfood.getJSONObject("picture") != null){
				this.image_version = jfood.getJSONObject("picture").getLong("version");
			}
		} catch (JSONException e) {
			Log.w(TAG, "Fail to parse food from json object: " + jfood.toString());
			e.printStackTrace();
		}
	}
	
}
