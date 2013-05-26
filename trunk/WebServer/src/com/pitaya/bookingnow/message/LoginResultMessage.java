package com.pitaya.bookingnow.message;

public class LoginResultMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6351604296315217738L;
	
	private String result;
	private String detail;
	
	public LoginResultMessage(){}
	
	public LoginResultMessage(String key, String result, String detail) {
		super(key);
		this.result = result;
		this.detail = detail;
		this.type = LoginResultMessage.class.getName();
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
	
}
