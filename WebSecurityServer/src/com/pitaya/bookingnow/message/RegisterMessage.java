package com.pitaya.bookingnow.message;

import com.pitaya.bookingnow.util.Constants;

public class RegisterMessage extends Message{

	private static final long serialVersionUID = 6185409247446469881L;
	
	private Long userId;
	private String action;
	
	public RegisterMessage(){}
	
	public RegisterMessage(Long id) {
		super(Constants.REGISTER_MESSAGE);
		this.userId = id;
	}

	public void setUserId(Long id){
		this.userId = id;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setAction(String act){
		this.action = act;
	}
	
	public String getAction(){
		return this.action;
	}

}
