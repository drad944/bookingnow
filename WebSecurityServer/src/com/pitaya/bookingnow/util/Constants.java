package com.pitaya.bookingnow.util;

public class Constants {
	//order status
	public static final int ORDER_STATUS = 0;
	public static final int ORDER_NEW = ORDER_STATUS + 1;
	public static final int ORDER_WAITING = ORDER_STATUS + 2;
	public static final int ORDER_AVAILABLE = ORDER_STATUS + 3;
	public static final int ORDER_UNAVAILABLE = ORDER_STATUS + 4;
	public static final int ORDER_PAYING = ORDER_STATUS + 5;
	public static final int ORDER_FINISHED = ORDER_STATUS + 6;
	
	//table status
	public static final int TABLE_STATUS = 100;
	public static final int TABLE_EMPTY = TABLE_STATUS + 1;
	public static final int TABLE_BOOKING = TABLE_STATUS + 2;
	public static final int TABLE_USING = TABLE_STATUS + 3;
	
	//food version
	public static final int FOOD_VERSION = 200;
	public static final int FOOD_VERSION_1_0 = FOOD_VERSION + 1;
	
	//food status
	public static final int FOOD_STATUS = 300;
	public static final int FOOD_NEW = FOOD_STATUS + 1;
	public static final int FOOD_COOKING = FOOD_STATUS + 2;
	public static final int FOOD_READY = FOOD_STATUS + 3;
	public static final int FOOD_FINISH = FOOD_STATUS + 4;
	
	//food preference
	public static final int FOOD_PREFERENCE = 400;
	public static final int FOOD_PREFERENCE_SWEET = FOOD_PREFERENCE + 1;
	public static final int FOOD_PREFERENCE_SALTY = FOOD_PREFERENCE + 2;
	public static final int FOOD_PREFERENCE_NORMAL = FOOD_PREFERENCE + 3;
	
	//food category
	public static final int FOOD_CATEGORY = 500;
	
	
	//customer sex
	public static final int CUSTOMER_SEX = 600;
	public static final int CUSTOMER_MAN = CUSTOMER_SEX + 1;
	public static final int CUSTOMER_WOMAN = CUSTOMER_SEX + 2;
	public static final int CUSTOMER_OTHER = CUSTOMER_SEX + 3;
	
	//user sex
	public static final int USER_SEX = 700;
	public static final int USER_MAN = USER_SEX + 1;
	public static final int USER_WOMAN = USER_SEX + 2;
	public static final int USER_OTHER = USER_SEX + 3;
	
	//user department
	
	public static final int USER_DEPARTMENT = 800;
	public static final int USER_DEPARTMENT_BUSSINESS = USER_DEPARTMENT + 1;
	public static final int USER_DEPARTMENT_PRODUCTION = USER_DEPARTMENT + 2;
	public static final int USER_DEPARTMENT_FINANCE = USER_DEPARTMENT + 3;
	public static final int USER_DEPARTMENT_PERSONNEL = USER_DEPARTMENT + 4;
	public static final int USER_DEPARTMENT_DEVERLOPE = USER_DEPARTMENT + 5;
	public static final int USER_DEPARTMENT_MANAGEMENT = USER_DEPARTMENT + 6;
	
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
	public static final int RESOURCE_TYPE_CSS = RESOURCE_TYPE + 2;
	public static final int RESOURCE_TYPE_JSP = RESOURCE_TYPE + 3;
	public static final int RESOURCE_TYPE_HTML = RESOURCE_TYPE + 4;
	public static final int RESOURCE_TYPE_CLASS = RESOURCE_TYPE + 5;
	public static final int RESOURCE_TYPE_CLASS_METHOD = RESOURCE_TYPE + 6;
}
