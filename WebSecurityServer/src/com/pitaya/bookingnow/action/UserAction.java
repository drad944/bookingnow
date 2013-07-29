package com.pitaya.bookingnow.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;


public class UserAction extends BaseAction{
	private static final long serialVersionUID = 1921350238570284375L;
	
	private IUserService userService;
	
	private SearchParams params;
	
	private Map<String, List<User>> matchedUsers;
	
	private String j_username;
	
	private String j_password;
	
	private User user;
	private User loginUser;
	
	public Map<String, List<User>> getMatchedUsers() {
		return matchedUsers;
	}
	public void setMatchedUsers(Map<String, List<User>> matchedUsers) {
		this.matchedUsers = matchedUsers;
	}
	public SearchParams getParams() {
		return params;
	}
	public void setParams(SearchParams params) {
		this.params = params;
	}
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
		return "Fail";
		
	}
	
	public String loginUser() {
		
		if(user != null) {
        	result = userService.login(user);
        }
        
        if(result.isExecuteResult()){ 
        	Map<String,Object> session = ActionContext.getContext().getSession();
        	loginUser = result.getUser();
    		session.put("loginUser", loginUser);
    		
            return "loginUserSuccess";  
        }else{  
            return "Fail";  
        }  
	}
	
	public String logoutUser() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		
		session.remove("loginUser");
		return "logoutUserSuccess";
	}
	
	public String findUser() {
		if(user != null) {
        	List<User> users = userService.searchUsers(user);
        	if (matchedUsers == null) {
				matchedUsers = new HashMap<String,List<User>>();
			}
        	matchedUsers.put("result", users);
        	return "findUserSuccess";
        }else {
			if (result == null) {
				result = new MyResult();
			}
			result.setErrorType(Constants.FAIL);
			result.setExecuteResult(false);
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
			return "Fail";
		}
	}
	
	public String registerUser() {
		if(user != null) {
			result = userService.add(user);
			
			if(result.isExecuteResult()){ 
				user = result.getUser();
	            return "registerUserSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail"; 
	}
	
	public String updateUser() {
		if(user != null) {
			result = userService.modify(user);
			
			if(result.isExecuteResult()){ 
				user = result.getUser();
	            return "updateUserSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail";
	}
	
	
	
	public String removeUser() {
		if(user != null) {
			result = userService.removeUserById(user.getId());
			
			if(result.isExecuteResult()){ 
				user = result.getUser();
	            return "removeUserSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "removeFail";
	}
	
	
	public String findAllUser() {
		
		List<User> users = userService.searchAllUsers();
    	if (matchedUsers == null) {
			matchedUsers = new HashMap<String,List<User>>();
		}
    	matchedUsers.put("result", users);
    	return "findAllUserSuccess";
	}
	
}
