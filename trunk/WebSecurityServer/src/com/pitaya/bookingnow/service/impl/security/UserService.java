package com.pitaya.bookingnow.service.impl.security;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.ImageUtil;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public class UserService implements IUserService {

	private UserMapper userDao;
	
	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	@Override
	public MyResult add(User user) {
		MyResult result = new MyResult();
		if (user != null && user.getAccount() != null) {
			if (user.getModifyTime() == null) {
				user.setModifyTime(new Date().getTime());
			}
			if (userDao.insert(user) == 1) {
				result.setExecuteResult(true);
				result.setUser(userDao.selectByPrimaryKey(user.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to insert user to DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user info in client data.");
		}
		return result;
	}

	@Override
	public MyResult removeUserById(Long id) {
		MyResult result = new MyResult();
		if (id != null) {
			if (userDao.deleteByPrimaryKey(id) == 1) {
				
				result.setExecuteResult(true);
				return result;
			}else {
				throw new RuntimeException("fail to delete user in DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user id in client data.");
		}
		return result;
	}

	@Override
	public MyResult modify(User user) {
		MyResult result = new MyResult();
		if (user != null && user.getId() != null) {
			if (user.getModifyTime() == null) {
				user.setModifyTime(new Date().getTime());
			}
			if (userDao.updateByPrimaryKeySelective(user) == 1) {
				
				result.setExecuteResult(true);
				result.setUser(userDao.selectByPrimaryKey(user.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to update user info in DB.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user id in client data.");
		}
		return result;
	}

	@Override
	public List<User> searchUsers(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			return userDao.searchUsers(user);
		}else {
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
		}
		return null;
	}

	@Override
	public MyResult login(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			User loginUser = userDao.login(user);
			if (loginUser != null && loginUser.getId() != null) {
				result.setExecuteResult(true);
				result.setUser(loginUser);
				return result;
			}else {
				result.getErrorDetails().put("user_exist", "can not find user in DB data.");
			}
		}else {
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
		}
		
		return result;
	}

	@Override
	public User searchUserById(Long id) {
		if (id != null) {
			return userDao.selectByPrimaryKey(id);
		}
		return null;
	}

	@Override
	public List<User> searchUsersWithRole(User user) {
		MyResult result = new MyResult();
		if (user != null) {
			return userDao.searchUsersWithRole(user);
		}else {
			result.getErrorDetails().put("user_exist", "can not find user in client data.");
		}
		return null;
	}

	@Override
	public List<User> searchAllUsers() {
		return userDao.searchAllUsers();
	}

	@Override
	public User getUserRole(Long id) {
		if(id != null){
			return userDao.getUserRole(id);
		}
		return null;
	}

	@Override
	public MyResult cropPicture(SearchParams params) {
		MyResult result = new MyResult();
		if (params != null) {
			if (params.getUser_id() != null 
					&& params.getImagePath() != null
					&& params.getX() != null
					&& params.getY() != null
					&& params.getW() != null
					&& params.getH() != null) {
				User realUser = userDao.selectByPrimaryKey(params.getUser_id());
				if (realUser != null 
						&& realUser.getImage_relative_path() != null 
						&& realUser.getImage_relative_path().equals(params.getImagePath())) {
					String imageType = ImageUtil.parseImageType(params.getImagePath());
					String targetImagePath = ImageUtil.generateImagePath("images/user/u", imageType);
					String pathprefix = ServletActionContext.getServletContext().getRealPath("/");
					if (ImageUtil.cut(pathprefix + params.getImagePath(), imageType, pathprefix + targetImagePath, params.getX(), params.getY(), params.getW(), params.getH())) {
						
						
						User newUser = new User();
						newUser.setId(realUser.getId());
						newUser.setImage_relative_path(targetImagePath);
						newUser.parseImageSize();
						
						if (userDao.updateByPrimaryKeySelective(newUser) == 1) {
							result.setExecuteResult(true);
							result.setUser(userDao.selectByPrimaryKey(newUser.getId()));
							
							//delete old user picture
							
							return result;
						}else {
							throw new RuntimeException("can not update user info in DB");
						}
						
					}else {
						result.getErrorDetails().put("crop_error", "can not crop image in server side");
					}
				}else {
					result.getErrorDetails().put("image_exist", "can not find image path in DB data.");
				}
				
			}else {
				result.getErrorDetails().put("params_exist", "params is not enough in client data.");
			}
		}else {
			result.getErrorDetails().put("params_exist", "can not find params in client data.");
		}
		
		return result;
	}
	
	


}