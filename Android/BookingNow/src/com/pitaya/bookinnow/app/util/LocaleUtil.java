package com.pitaya.bookinnow.app.util;

public class LocaleUtil {

	private final static String[] zh_category= new String[]{
		"中餐",
		"西餐",
		"酒水",
		"点心",
	};
	
	public static String getString(String key, int code){
		if(key.equals("category")){
			return zh_category[code];
		} else {
			return "undefined";
		}
	}
	
}
