package com.pitaya.bookingnow.entity;

public class ClientInfo {
	
	private Long userid;
	private Integer roleType;
	private String name;
	private Boolean isAuth;
	
	public void setUserid(Long id){
		this.userid = id;
	}
	
	public Long getUserid(){
		return this.userid;
	}
	
	public void setRoleType(int t){
		this.roleType = t;
	}
	
	public Integer getRoleType(){
		return this.roleType;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	public void setIsAuth(boolean flag){
		this.isAuth = flag;
	}
	
	public Boolean getIsAuth(){
		return this.isAuth;
	}
	
}
