package com.pitaya.bookingnow.message;

import com.pitaya.bookingnow.util.Constants;

public class RegisterMessage extends Message{

	private static final long serialVersionUID = 6185409247446469881L;
	
	private Long userId;
	private String action;
	private String username;
	private String password;
	
	public RegisterMessage(){
		super(Constants.REGISTER_MESSAGE);
	}
	
	public RegisterMessage(Long id) {
		super(Constants.REGISTER_MESSAGE);
		this.userId = id;
	}

	public void setAction(String act){
		this.action = act;
	}
	
	public String getAction(){
		return this.action;
	}
	
	public void setUserId(Long id){
		this.userId = id;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setUsername(String uname){
		this.username = uname;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setPassword(String pwd){
		this.password = pwd;
	}
	
	public String getPassword(){
		return this.password;
	}

}
