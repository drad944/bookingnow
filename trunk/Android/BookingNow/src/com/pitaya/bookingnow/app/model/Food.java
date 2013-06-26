package com.pitaya.bookingnow.app.model;

import java.io.Serializable;

public class Food implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7297469175751088249L;
	private String key;
	private String name;
	private String description;
	private String category;
	private float price;
	private byte[] imageBytesSmall;
	private byte[] imageBytesLarge;
	
	public Food(){
	}
	
	public Food(String key, String name, float price, String desc, String category, byte[] bytes){
		this.key = key;
		this.name = name;
		this.price = price;
		this.description = desc;
		this.category = category;
		this.imageBytesSmall = bytes;
	}
	
	public Food(String key, String name, float price, String desc, String category, byte[] bytes, byte[] bytes2){
		this(key, name, price, desc, category, bytes);
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
	
	public String getCategory(){
		return this.category;
	}
	
	public float getPrice(){
		return this.price;
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
	
}
