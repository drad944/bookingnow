package com.pitaya.bookingnow.action;


import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;


public class UserAction extends ActionSupport{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private IUserService userService;
	private User user;
	
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
	
	public String loginUser() {
		User loginUser = null;
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