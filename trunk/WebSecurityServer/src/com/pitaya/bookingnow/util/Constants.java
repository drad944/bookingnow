package com.pitaya.bookingnow.util;

public class Constants {
	
	//order status
	public static final int ALL = -1;
	
	public static final int ORDER_NEW = 1;
	public static final int ORDER_WELCOMER_NEW = ORDER_NEW + 1;
	public static final int ORDER_WAITING = ORDER_WELCOMER_NEW + 1;
	public static final int ORDER_COMMITED = ORDER_WAITING + 1;
	public static final int ORDER_PAYING = ORDER_COMMITED + 1;
	public static final int ORDER_FINISHED = ORDER_PAYING + 1;
	public static final int ORDER_UNAVAILABLE = ORDER_FINISHED + 1;
	public static final int ORDER_AVAILABLE = ORDER_UNAVAILABLE + 1;
	
	//food status
	public static final int FOOD_NEW = 1;
	public static final int FOOD_CONFIRMED = FOOD_NEW + 1;
	public static final int FOOD_COOKING = FOOD_CONFIRMED + 1;
	public static final int FOOD_FINISHED = FOOD_COOKING + 1;
	public static final int FOOD_UNAVAILABLE = FOOD_FINISHED + 1;
	public static final int FOOD_AVAILABLE = FOOD_UNAVAILABLE + 1;
	
	//table status
	public static final int TABLE_STATUS = 1;
	public static final int TABLE_EMPTY = TABLE_STATUS + 1;
	public static final int TABLE_BOOKING = TABLE_EMPTY + 1;
	public static final int TABLE_USING = TABLE_BOOKING + 1;
	
	//food version
	public static final int FOOD_VERSION = 1;
	public static final int FOOD_VERSION_1_0 = FOOD_VERSION + 1;
	
	//food preference
	public static final int FOOD_PREFERENCE = 1;
	public static final int FOOD_PREFERENCE_SWEET = FOOD_PREFERENCE + 1;
	public static final int FOOD_PREFERENCE_SALTY = FOOD_PREFERENCE_SWEET + 1;
	public static final int FOOD_PREFERENCE_NORMAL = FOOD_PREFERENCE_SALTY + 1;
	
	//food category
	public static final int FOOD_CATEGORY = 1;
	
	
	//customer sex
	public static final int CUSTOMER_SEX = 1;
	public static final int CUSTOMER_MALE = CUSTOMER_SEX + 1;
	public static final int CUSTOMER_FAMALE = CUSTOMER_MALE + 1;
	public static final int CUSTOMER_OTHER = CUSTOMER_FAMALE + 1;
	
	//user sex
	public static final int USER_SEX = 1;
	public static final int USER_MALE = USER_SEX + 1;
	public static final int USER_FAMALE = USER_MALE + 1;
	public static final int USER_OTHER = USER_FAMALE + 1;
	
	//user department
	
	public static final int USER_DEPARTMENT = 1;
	public static final int USER_DEPARTMENT_BUSSINESS = USER_DEPARTMENT + 1;
	public static final int USER_DEPARTMENT_PRODUCTION = USER_DEPARTMENT_BUSSINESS + 1;
	public static final int USER_DEPARTMENT_FINANCE = USER_DEPARTMENT_PRODUCTION + 1;
	public static final int USER_DEPARTMENT_PERSONNEL = USER_DEPARTMENT_FINANCE + 1;
	public static final int USER_DEPARTMENT_DEVERLOPE = USER_DEPARTMENT_PERSONNEL + 1;
	public static final int USER_DEPARTMENT_MANAGEMENT = USER_DEPARTMENT_DEVERLOPE + 1;
	
	//metarial category
	public static final int METARIAL_CATEGORY = 1;
	public static final int METARIAL_CATEGORY_CONDIMENT = METARIAL_CATEGORY + 1;
	
	//sub system
	public static final int SUB_SYSTEM = 1;
	
	//module
	public static final int MODULE = 1;
	public static final int MODULE_BOOKINGNOW = MODULE + 1;
	
	//resource type
	public static final int RESOURCE_TYPE = 1;
	public static final int RESOURCE_TYPE_JS = RESOURCE_TYPE + 1;
	public static final int RESOURCE_TYPE_CSS = RESOURCE_TYPE_JS + 1;
	public static final int RESOURCE_TYPE_JSP = RESOURCE_TYPE_CSS + 1;
	public static final int RESOURCE_TYPE_HTML = RESOURCE_TYPE_JSP + 1;
	public static final int RESOURCE_TYPE_CLASS = RESOURCE_TYPE_HTML + 1;
	public static final int RESOURCE_TYPE_CLASS_METHOD = RESOURCE_TYPE_CLASS + 1;
	
	public static final int ROLE_TYPE = 1;
	public static final int ROLE_ANONYMOUS = ROLE_TYPE + 1;
	public static final int ROLE_CUSTOMER = ROLE_ANONYMOUS + 1;
	public static final int ROLE_CUSTOMER_VIP1 = ROLE_CUSTOMER + 1;
	public static final int ROLE_CUSTOMER_VIP2 = ROLE_CUSTOMER_VIP1 + 1;
	public static final int ROLE_WELCOME = ROLE_CUSTOMER_VIP2 + 1;
	public static final int ROLE_CHEF = ROLE_WELCOME + 1;
	public static final int ROLE_WAITER = ROLE_CHEF + 1;
	public static final int ROLE_CASHIER = ROLE_WAITER + 1;
	public static final int ROLE_MANAGER = ROLE_CASHIER + 1;
	public static final int ROLE_ADMIN = ROLE_MANAGER + 1;
	
	public static final int AUTHORITY_TYPE = 1;
	public static final int AUTHORITY_ANONYMOUS = AUTHORITY_TYPE + 1;
	public static final int AUTHORITY_CUSTOMER = AUTHORITY_ANONYMOUS + 1;
	public static final int AUTHORITY_CUSTOMER_VIP1 = AUTHORITY_CUSTOMER + 1;
	public static final int AUTHORITY_CUSTOMER_VIP2 = AUTHORITY_CUSTOMER_VIP1 + 1;
	public static final int AUTHORITY_WELCOME = AUTHORITY_CUSTOMER_VIP2 + 1;
	public static final int AUTHORITY_CHEF = AUTHORITY_WELCOME + 1;
	public static final int AUTHORITY_WAITER = AUTHORITY_CHEF + 1;
	public static final int AUTHORITY_CASHIER = AUTHORITY_WAITER + 1;
	public static final int AUTHORITY_MANAGER = AUTHORITY_CASHIER + 1;
	public static final int AUTHORITY_ADMIN = AUTHORITY_MANAGER + 1;

	public static final String ACTION_ADD = "new";
	public static final String ACTION_REMOVE = "delete";
	public static final String ACTION_UPDATE = "update";
	
	public static final int SUCCESS = 1;
	public static final int FAIL = SUCCESS + 1;
	
	public static final int SOCKET_CONNECTION = 1;
	public static final int LOGIN_REQUEST = SOCKET_CONNECTION + 1;
	public static final int REGISTER_REQUEST = LOGIN_REQUEST + 1;
	
	public static final String ORDER_MESSAGE = "order_message";
	public static final String REGISTER_MESSAGE = "register_message";
	public static final String RESULT_MESSAGE = "result_message";
	public static final String SYSTEM_MESSAGE = "system_message";
	
}
