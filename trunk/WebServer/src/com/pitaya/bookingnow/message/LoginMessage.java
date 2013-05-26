package com.pitaya.bookingnow.message;

public class LoginMessage extends Message{

	private static final long serialVersionUID = 6185409247446469881L;
	
	private String username;
	private String password;
	
	public LoginMessage(){}
	
	public LoginMessage(String key, String username, String password) {
		super(key);
		this.username = username;
		this.password = password;
		this.type = LoginMessage.class.getName();
	}

	public void setUsername(String username){
		this.username = username;
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
