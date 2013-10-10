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
	public static final String PASSWORD = "password";
	public static final String USER_ID = "userid";  
	public static final String ROLE_ID = "role";
	
	private static Integer role = null;
	private static Long userId = null;
	private static String username = null;
	private static String password = null;

	private static final String TAG = "UserManager";
	
	public static String getRoleName(){
		switch(role){
			case Constants.ROLE_WAITER:
				return "服务员";
			case Constants.ROLE_WELCOME:
				return "迎宾";
			case Constants.ROLE_CHEF:
				return "厨房";
		}
		return "未知";
	}
	
	public static Integer getUserRole(Context context){
		if(role == null || role == -1){
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
		if(userId == null || userId == -1){
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
		if(username == null || username.equals("")){
			SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
			username = settings.getString(USERNAME, "");
		}
		if(!username.equals("")){
			return username;
		} else {
			return null;
		}
	}
	
	public static String getPassword(Context context){
		if(password == null || password.equals("")){
			SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
			password = settings.getString(PASSWORD, "");
		}
		if(!password.equals("")){
			return password;
		} else {
			return null;
		}
	}
	
	public static void setLoginUser(Context context, User user){
		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		if(user != null){
			settings.edit()
			  .putString(UserManager.USERNAME, user.getUsername())
			  .putString(UserManager.PASSWORD, user.getPassword())
			  .putLong(UserManager.USER_ID, user.getUserId())
			  .putInt(UserManager.ROLE_ID, user.getRole())
			  .commit();
			  role = user.getRole();
			  userId = user.getUserId();
			  username = user.getUsername();
			  password = user.getPassword();
		} else {
			settings.edit()
			  .remove(UserManager.USERNAME)
			  .remove(UserManager.PASSWORD)
			  .remove(UserManager.USER_ID)
			  .remove(UserManager.ROLE_ID)
			  .commit();
			role = null;
			userId = null;
			username = null;
			password = null;
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
	
	public static void rememberMe(Context context, String username, String password){
		if(username != null && password != null){
			SharedPreferences settings = context.getSharedPreferences(UserManager.SETTING_INFOS, 0);
			settings.edit()
					  .putString(UserManager.AUTO_LOGIN_NAME, username)
					  .putString(UserManager.AUTO_LOGIN_PASSWORD, password)
					  .commit();
		}
	}
	
	public static void cleanRemeberMe(Context context){
		SharedPreferences settings = context.getSharedPreferences(UserManager.SETTING_INFOS, 0);
		settings.edit()
				  .remove(UserManager.AUTO_LOGIN_NAME)
				  .remove(UserManager.AUTO_LOGIN_PASSWORD)
				  .commit();
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
