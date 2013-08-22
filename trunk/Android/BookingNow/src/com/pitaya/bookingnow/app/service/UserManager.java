package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.User;
import com.pitaya.bookingnow.app.util.Constants;

public class UserManager {
	
	public static final String SETTING_INFOS = "userinfos";
	//Save auto login info
	public static final String AUTO_LOGIN_NAME = "auto_username";  
	public static final String AUTO_LOGIN_PASSWORD = "auto_password";
	//Save current login user info
	public static final String USERNAME = "username";
	public static final String USER_ID = "userid";  
	public static final String ROLE_ID = "role";
	
	public static Integer role = null;
	public static Long userId = null;
	public static String username = null;

	private static final String TAG = "UserManager";
	
	public static Integer getUserRole(Context context){
		if(role == null){
			SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
			role = settings.getInt(ROLE_ID, -1);
		}
		if(role != -1){
			return role;
		} else {
			return null;
		}
	}
	
	public static Long getUserId(Context context){
		if(userId == null){
			SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
			userId = settings.getLong(USER_ID, -1L);
		}
		if(userId != -1L){
			return userId;
		} else {
			return null;
		}
	}
	
	public static String getUsername(Context context){
		if(username == null){
			SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
			username = settings.getString(USERNAME, "");
		}
		if(!username.equals("")){
			return username;
		} else {
			return null;
		}
	}
	
	public static void setLoginUser(Context context, User user){
		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		if(user != null){
			settings.edit()
			  .putString(UserManager.USERNAME, user.getUsername())
			  .putLong(UserManager.USER_ID, user.getUserId())
			  .putInt(UserManager.ROLE_ID, user.getRole())
			  .commit();
		} else {
			settings.edit()
			  .remove(UserManager.USERNAME)
			  .remove(UserManager.USER_ID)
			  .remove(UserManager.ROLE_ID)
			  .commit();
		}
	}
	
	public static String[] getUsernameAndPassword(Context context){
		String[] params = null;
		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		String name = settings.getString(UserManager.AUTO_LOGIN_NAME, "");
        String password = settings.getString(UserManager.AUTO_LOGIN_PASSWORD, "");
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
