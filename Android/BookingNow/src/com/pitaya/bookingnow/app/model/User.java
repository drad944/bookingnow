package com.pitaya.bookingnow.app.model;

public class User {
	
	private Integer role;
	private String username;
	private Long userid;
	
	public User(Long uid, String name, Integer role){
		this.userid = uid;
		this.username = name;
		this.role = role;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public Integer getRole(){
		return this.role;
	}
	
	public Long getUserId(){
		return this.userid;
	}
}
