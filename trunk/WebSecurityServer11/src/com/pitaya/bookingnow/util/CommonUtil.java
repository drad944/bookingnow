package com.pitaya.bookingnow.util;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	public static String getRemoteUrl(HttpServletRequest request){
		   if (request.getHeader("x-forwarded-for") == null || request.getHeader("x-forwarded-for").length() == 0) {
               return request.getRemoteAddr();
           }
           return request.getHeader("x-forwarded-for");
	}
	
}
