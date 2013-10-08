package com.pitaya.bookingnow.app.model;

public class User {
	
	private Integer role;
	private String username;
	private String password;
	private Long userid;
	
	public User(Long uid, String name, String pwd, Integer role){
		this.userid = uid;
		this.username = name;
		this.password = pwd;
		this.role = role;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public Integer getRole(){
		return this.role;
	}
	
	public Long getUserId(){
		return this.userid;
	}
}
