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
}
