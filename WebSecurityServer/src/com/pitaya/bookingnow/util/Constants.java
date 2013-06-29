package com.pitaya.bookingnow.util;

public class Constants {
	
	//order and food status
	public static final int ALL = -1;
	public static final int NEW = 0;
	public static final int COMMITED = NEW + 1;
	public static final int PAYING = COMMITED + 1;
	public static final int FINISHED = PAYING + 1;
	public static final int WAITING = FINISHED + 1;
	public static final int COOKING = WAITING + 1;
	public static final int UNAVAILABLE = COOKING + 1;
	public static final int AVAILABLE = UNAVAILABLE + 1;
	
	public static final int SUCCESS = 50;
	public static final int FAIL = SUCCESS + 1;
	
	public static final int SOCKET_CONNECTION = 100;
	public static final int LOGIN_REQUEST = SOCKET_CONNECTION + 1;
	
	//table status
	public static final int TABLE_STATUS = 200;
	public static final int TABLE_EMPTY = TABLE_STATUS + 1;
	public static final int TABLE_BOOKING = TABLE_EMPTY + 1;
	public static final int TABLE_USING = TABLE_BOOKING + 1;
	
	//food version
	public static final int FOOD_VERSION = 300;
	public static final int FOOD_VERSION_1_0 = FOOD_VERSION + 1;
	
	//food preference
	public static final int FOOD_PREFERENCE = 400;
	public static final int FOOD_PREFERENCE_SWEET = FOOD_PREFERENCE + 1;
	public static final int FOOD_PREFERENCE_SALTY = FOOD_PREFERENCE_SWEET + 1;
	public static final int FOOD_PREFERENCE_NORMAL = FOOD_PREFERENCE_SALTY + 1;
	
	//food category
	public static final int FOOD_CATEGORY = 500;
	
	
	//customer sex
	public static final int CUSTOMER_SEX = 600;
	public static final int CUSTOMER_MALE = CUSTOMER_SEX + 1;
	public static final int CUSTOMER_FAMALE = CUSTOMER_MALE + 1;
	public static final int CUSTOMER_OTHER = CUSTOMER_FAMALE + 1;
	
	//user sex
	public static final int USER_SEX = 700;
	public static final int USER_MALE = USER_SEX + 1;
	public static final int USER_FAMALE = USER_MALE + 1;
	public static final int USER_OTHER = USER_FAMALE + 1;
	
	//user department
	
	public static final int USER_DEPARTMENT = 800;
	public static final int USER_DEPARTMENT_BUSSINESS = USER_DEPARTMENT + 1;
	public static final int USER_DEPARTMENT_PRODUCTION = USER_DEPARTMENT_BUSSINESS + 1;
	public static final int USER_DEPARTMENT_FINANCE = USER_DEPARTMENT_PRODUCTION + 1;
	public static final int USER_DEPARTMENT_PERSONNEL = USER_DEPARTMENT_FINANCE + 1;
	public static final int USER_DEPARTMENT_DEVERLOPE = USER_DEPARTMENT_PERSONNEL + 1;
	public static final int USER_DEPARTMENT_MANAGEMENT = USER_DEPARTMENT_DEVERLOPE + 1;
	
	//metarial category
	public static final int METARIAL_CATEGORY = 900;
	public static final int METARIAL_CATEGORY_CONDIMENT = METARIAL_CATEGORY + 1;
	
	//sub system
	public static final int SUB_SYSTEM = 1000;
	
	//module
	public static final int MODULE = 1100;
	public static final int MODULE_BOOKINGNOW = MODULE + 1;
	
	//resource type
	public static final int RESOURCE_TYPE = 1200;
	public static final int RESOURCE_TYPE_JS = RESOURCE_TYPE + 1;
	public static final int RESOURCE_TYPE_CSS = RESOURCE_TYPE_JS + 1;
	public static final int RESOURCE_TYPE_JSP = RESOURCE_TYPE_CSS + 1;
	public static final int RESOURCE_TYPE_HTML = RESOURCE_TYPE_JSP + 1;
	public static final int RESOURCE_TYPE_CLASS = RESOURCE_TYPE_HTML + 1;
	public static final int RESOURCE_TYPE_CLASS_METHOD = RESOURCE_TYPE_CLASS + 1;
	
	public static final int KITCHEN_ROLE = 1300;
	public static final int WAITER_ROLE = KITCHEN_ROLE + 1;
	public static final int WELCOME_ROLE = WAITER_ROLE + 1;
	public static final int CASH_ROLE = WELCOME_ROLE + 1;
}
