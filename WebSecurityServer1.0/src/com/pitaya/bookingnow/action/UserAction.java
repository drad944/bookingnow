package com.pitaya.bookingnow.action;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.ImageUtil;
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
	
	private File uploadImage; //上传的文件
    private String uploadImageFileName; //文件名称
    private String uploadImageContentType; //文件类型
	
	public File getUploadImage() {
		return uploadImage;
	}
	public void setUploadImage(File uploadImage) {
		this.uploadImage = uploadImage;
	}
	public String getUploadImageFileName() {
		return uploadImageFileName;
	}
	public void setUploadImageFileName(String uploadImageFileName) {
		this.uploadImageFileName = uploadImageFileName;
	}
	public String getUploadImageContentType() {
		return uploadImageContentType;
	}
	public void setUploadImageContentType(String uploadImageContentType) {
		this.uploadImageContentType = uploadImageContentType;
	}
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
	public String switchPageByUser() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		loginUser = (User) session.get("loginUser");
		
		if (loginUser != null && loginUser.getId() != null) {
			if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_ADMIN) {
				return "switchToAdminPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_MANAGER) {
				return "switchToManagerPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_CASHIER) {
				return "switchToCashierPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_CHEF) {
				return "switchToChefPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_WAITER) {
				return "switchToWaiterPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_WELCOMER) {
				return "switchToWelcomerPage";
			}else if(loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_ANONYMOUS
					|| loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_CUSTOMER
					|| loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_CUSTOMER_VIP1
					|| loginUser.getRole_Details().get(0).getRole().getType() == Constants.ROLE_CUSTOMER_VIP2)
			{
				return "switchToCustomerPage";
			}
			
		}
		return "switchToOtherPage";
	}
	
	public String findLoginUser() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		loginUser = (User) session.get("loginUser");
		
		if (loginUser != null && loginUser.getId() != null) {
			return "findLoginUserSuccess";
		}
        
		if (result == null) {
			result = new MyResult();
		}
		result.setErrorType(Constants.FAIL);
		result.setExecuteResult(false);
		result.getErrorDetails().put("user_exist", "can not find user in client data.");
		return "Fail";
	}
	
	public String loginUser() {
		
		if(user != null) {
        	result = userService.login(user);
        }
        
        if(result.isExecuteResult()){ 
        	Map<String,Object> session = ActionContext.getContext().getSession();
        	loginUser = result.getUser();
        	user = result.getUser();
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
	
	public String findWithIDUser() {
		if(user != null && user.getId() != null) {
        	user = userService.searchUserById(user.getId());
        	if (user != null) {
        		if (matchedUsers == null) {
					matchedUsers = new HashMap<String, List<User>>();
				}
        		List<User> users = new ArrayList<User>();
				users.add(user);
				matchedUsers.put("result", users);
        		return "findWithIDUserSuccess";
			}
        	return "Fail";
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
	
	public String findRoleWithUser() {
		if(user != null) {
        	List<User> users = userService.searchUsersWithRole(user);
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
	
	public String existUser() {
		if (result == null) {
			result = new MyResult();
		}
		
		if(user != null && user.getAccount() != null && (user.getAccount().equals("") == false)) {
			if(userService.existUser(user)) {
				result.setExecuteResult(true);
			}else {
				result.getErrorDetails().put("user_exist", "can not find user account in DB data.");
				result.setErrorType(Constants.SUCCESS);
				result.setExecuteResult(false);
			}
			return "existUserSuccess";
        }else {
			result.setErrorType(Constants.FAIL);
			result.setExecuteResult(false);
			result.getErrorDetails().put("user_exist", "can not find user account in client data.");
			return "Fail";
		}
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
	
	public String registerRoleWithUser() {
		if(user != null) {
			result = userService.addUserWithRole(user);
			
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
	
	public String updateRoleWithUser() {
		if(user != null) {
			result = userService.modifyRoleWithUser(user);
			
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
	
	public String cropPictureForUser() {
		if(params != null) {
			result = userService.cropPicture(params);
			
			if(result.isExecuteResult()){ 
				loginUser = result.getUser(); 
	            return "cropPictureForUserSuccess";  
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
	
	
	public String uploadImageForUser() {
		Map<String,Object> session = ActionContext.getContext().getSession();
		loginUser = (User) session.get("loginUser");
		if(loginUser != null && loginUser.getId() != null) {
			String realpath = ServletActionContext.getServletContext().getRealPath("/images/temp");
	        if (uploadImage != null) {
	        	String imageTypeString = ImageUtil.parseImageType(uploadImageFileName);
	        	String targetPath = ImageUtil.generateImagePath(realpath, imageTypeString);
	        	
	            File savefile = new File(targetPath);
	            if (!savefile.getParentFile().exists()){
	            	savefile.getParentFile().mkdirs();
	            }
	            
	                
	            try {
					FileUtils.copyFile(uploadImage, savefile);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            //crop user picture
	            params.setUser_id(loginUser.getId());
	            params.setImagePath(savefile.getAbsolutePath());
	            result = userService.cropPicture(params);
	            
	            if(savefile.exists()) {
    				savefile.delete();
    			}
	    		if(result.isExecuteResult()){ 
	    			
					loginUser = result.getUser();
					
	    			return "uploadImageForUserSuccess";
		        }else{  
		            return "Fail";  
		        }  
	        }else {
	        	if (result == null) {
					result = new MyResult();
					result.setErrorType(Constants.FAIL);
					result.getErrorDetails().put("file_exist", "can not find file to upload.");
				}
	        }
	        
			
		}else {
			if (result == null) {
				result = new MyResult();
				result.setErrorType(Constants.FAIL);
				result.getErrorDetails().put("user_exist", "can not find login user.");
			}
		}
		
        return "Fail";
	}
}
