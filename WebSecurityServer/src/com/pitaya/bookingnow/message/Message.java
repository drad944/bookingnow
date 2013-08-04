package com.pitaya.bookingnow.message;

import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6356895567978585140L;
	
	private String category;
	private String type; 
	
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
	
}
