package com.pitaya.bookingnow.action;


import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;


public class UserAction extends BaseAction{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private IUserService userService;
	
	private String j_username;
	
	private String j_password;
	
	private User user;
	private User loginUser;
	
	public String getJ_username() {
		return j_username;
	}
	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}
	public String getJ_password() {
		return j_password;
	}
	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}
	public User getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String spring_security_loginUser() {
		if (j_username != null && j_password != null) {
			user = new User();
			user.setAccount(j_username);
			user.setPassword(j_password);
			return loginUser();
		}
		return "loginFail";
		
	}
	
	public String loginUser() {
		
		if(user != null) {
        	loginUser = userService.login(user);
        }
        
        if(loginUser != null){ 
        	Map<String,Object> session = ActionContext.getContext().getSession();
    		session.put("loginUser", loginUser);
    		
            return "loginSuccess";  
        }else{  
            return "loginFail";  
        }  
	}
	
	public String logoutUser() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		
		session.remove("loginUser");
		return "logoutSuccess";
	}
	
	public String findUser() {
		List<User> loginUsers = null;
		if(user != null) {
        	loginUsers = userService.searchUsers(user);
        }
        
        if(loginUsers != null){  
            return "findSuccess";  
        }else{  
            return "findFail";  
        }  
	}
	
	public String registerUser() {
		if(user != null) {
			if(userService.add(user)){  
	            return "registerSuccess";  
	        }else{  
	            return "registerFail";  
	        }  
        }
		return "registerFail";  
	}
	
	public String updateUser() {
		if(user != null) {
			if(userService.modify(user)){  
	            return "updateSuccess";  
	        }else{  
	            return "updateFail";  
	        }  
        }
            return "updateFail";  
	}
	
	
	
	public String removeUser() {
		if(user != null) {
			if(userService.remove(user)){  
	            return "removeSuccess";  
	        }else{  
	            return "removeFail";  
	        }  
        }
            return "removeFail";  
	}
	
}
