package com.pitaya.bookingnow.app.model;

import java.util.ArrayList;

public class UpdateFood {
	
	private String key;
	private boolean isFree;
	private Long order_food_refid;
	private Long version;
	private int count;
	
	public UpdateFood(String key, Long refid, Long version, boolean isfree, int count){
		this.key = key;
		this.order_food_refid = refid;
		this.version = version;
		this.isFree = isfree;
		this.count = count;
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
	
}
