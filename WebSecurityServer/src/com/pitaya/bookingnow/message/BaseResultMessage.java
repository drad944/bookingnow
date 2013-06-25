package com.pitaya.bookingnow.message;

public class BaseResultMessage extends Message{
	
	private static final long serialVersionUID = 6351604296315217738L;
	
	private String requestType;
	private String result;
	private String detail;
	
	public BaseResultMessage(){}
	
	public BaseResultMessage(String key, String reqtype, String result, String detail) {
		super(key);
		this.result = result;
		this.detail = detail;
		this.requestType = reqtype;
	}

	public void setResult(String rs){
		this.result = rs;
	}
	
	public String getResult(){
		return this.result;
	}
	
	public void setDetail(String detail){
		this.detail = detail;
	}
	
	public String getDetail(){
		return this.detail;
	}
	
	public void setRequestType(String type){
		this.requestType = type;
	}
	
	public String getRequestType(){
		return this.requestType;
	}
	
}
