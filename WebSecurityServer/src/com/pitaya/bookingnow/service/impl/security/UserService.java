package com.pitaya.bookingnow.service.impl.security;
import java.util.List;


import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.MyResult;

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
		if (user != null) {
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


}