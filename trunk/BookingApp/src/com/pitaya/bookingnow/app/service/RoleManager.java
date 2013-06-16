package com.pitaya.bookingnow.app.service;

public class RoleManager {
	
	public static final int WAITER = 1;
	public static final int KITCHEN = WAITER + 1;
	public static final int WELCOMER = KITCHEN + 1;
	public static final int CASHER = WELCOMER + 1;
	public static int ROLE = WELCOMER;
	
	public static int getRole(){ 
		return ROLE;
	}
	
	public static void setRole(int role){
		ROLE = role;
	}
	
}
