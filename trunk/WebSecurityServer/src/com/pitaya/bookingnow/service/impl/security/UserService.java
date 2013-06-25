package com.pitaya.bookingnow.service.impl.security;
import java.util.List;


import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.security.IUserService;

public class UserService implements IUserService {

	private UserMapper userDao;
	
	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean add(User user) {
		if(userDao.insertSelective(user) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeUserById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> searchUsers(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(User user) {
		User loginUser = userDao.login(user);
		
		return loginUser;
	}


}