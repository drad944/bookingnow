package com.pitaya.bookingnow.message;

import java.io.Serializable;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6356895567978585140L;
	
	private String key;
	private String type; 
	
	public Message(){}
	
	public Message(String key){
		this.key = key;
		this.type = this.getClass().getName();
	}
	
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return this.type;
	}
	
}
