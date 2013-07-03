package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookinnow.app.util.Constants;

public class UserManager {
	
	public static final String SETTING_INFOS = "SETTINGInfos";
	public static final String NAME = "NAME";  
	public static final String PASSWORD = "PASSWORD";
	public static Integer ROLE = null;
	public static Long userId = null;

	private static final String TAG = "UserManager";
	
	public static Integer getUserRole(){ 
		return ROLE;
	}
	
	public static void setUserRole(Integer role){
		ROLE = role;
	}
	
	public static Long getUserId(){ 
		return userId;
	}
	
	public static void setUserId(Long id){
		userId = id;
	}
	
	public static String[] getUsernameAndPassword(Context context){
		String[] params = null;
		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		String name = settings.getString(NAME, "");
        String password = settings.getString(PASSWORD, "");
        if(!name.equals("")&&!password.equals("")){
        	params = new String[2];
        	params[0] = name;
        	params[1] = password;
        }
        return params;
	}
	
	public static void login(String username, String password, HttpHandler handler){
		if(username != null && password != null){
			JSONObject jparam = new JSONObject();
			JSONObject juser = new JSONObject();
			try {
				juser.put("account", username);
				juser.put("password", password);
				jparam.put("user", juser);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				HttpService.post("loginUser.action", new StringEntity(jparam.toString()), handler);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
