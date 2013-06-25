package com.pitaya.bookingnow.service.impl.security;
import java.util.List;

import com.pitaya.bookingnow.dao.security.User_Role_DetailMapper;
import com.pitaya.bookingnow.entity.security.User_Role_Detail;
import com.pitaya.bookingnow.service.security.IUser_Role_DetailService;

public class User_Role_DetailService implements IUser_Role_DetailService {

	private User_Role_DetailMapper user_role_detailDao;
	
	public User_Role_DetailMapper getUser_Role_DetailDao() {
		return user_role_detailDao;
	}

	public void setUser_Role_DetailDao(User_Role_DetailMapper user_role_detailDao) {
		this.user_role_detailDao = user_role_detailDao;
	}

	@Override
	public boolean add(User_Role_Detail user_role_detail) {
		if(user_role_detailDao.insertSelective(user_role_detail) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeUser_Role_DetailById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(User_Role_Detail user_role_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(User_Role_Detail user_role_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User_Role_Detail> searchUser_Role_Details(User_Role_Detail user_role_detail) {
		// TODO Auto-generated method stub
		return null;
	}



}