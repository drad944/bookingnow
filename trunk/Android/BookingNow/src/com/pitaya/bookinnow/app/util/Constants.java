package com.pitaya.bookinnow.app.util;

public class Constants {
	
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
	
	public static final int ROLE_TYPE = 1300;
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
	
	//error code, pay attention to avoid conflict with the one used by http status code
	public static final int WRONG_ACTION_ERROR = 0;
	public static final int IO_EXCEPTION_ERROR = WRONG_ACTION_ERROR + 1;
	public static final int PROTOCOL_EXCEPTION_ERROR = IO_EXCEPTION_ERROR + 1;
	public static final int WRITE_FILE_ERROR = PROTOCOL_EXCEPTION_ERROR + 1;
	public static final int GET_FOOD_IMAGE_ERROR = WRITE_FILE_ERROR + 1;
}
