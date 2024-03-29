package com.pitaya.bookingnow.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateFood implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1891609178790863633L;
	private String key;
	private boolean isFree;
	private Long order_food_refid;
	private Long version;
	private int count;
	private String preference;
	
	public UpdateFood(String key, Long refid, Long version, boolean isfree, int count){
		this.key = key;
		this.order_food_refid = refid;
		this.version = version;
		this.isFree = isfree;
		this.count = count;
		this.preference = "";
	}
	
	public String getFoodKey(){
		return this.key;
	}
	
	public boolean isFree(){
		return this.isFree;
	}
	
	public Long getRefId(){
		return this.order_food_refid;
	}
	
	public Long getVersion(){
		return this.version;
	}
	
	public int getQuantity(){
		return this.count;
	}
	
	public String getPreference(){
		return this.preference;
	}
	
	public void setPreference(String pref){
		this.preference = pref;
	}
	
}
