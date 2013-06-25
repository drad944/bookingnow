package com.pitaya.bookingnow.service.impl.security;
import java.util.List;

import com.pitaya.bookingnow.dao.security.User_PictureMapper;
import com.pitaya.bookingnow.entity.security.User_Picture;
import com.pitaya.bookingnow.service.security.IUser_PictureService;

public class User_PictureService implements IUser_PictureService {

	private User_PictureMapper user_pictureDao;
	
	public User_PictureMapper getUser_PictureDao() {
		return user_pictureDao;
	}

	public void setUser_PictureDao(User_PictureMapper user_pictureDao) {
		this.user_pictureDao = user_pictureDao;
	}

	@Override
	public boolean add(User_Picture user_picture) {
		if(user_pictureDao.insertSelective(user_picture) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeUser_PictureById(Long id) {
		
		return false;
	}

	@Override
	public boolean remove(User_Picture user_picture) {
		
		return false;
	}

	@Override
	public boolean modify(User_Picture user_picture) {
		
		return false;
	}

	@Override
	public List<User_Picture> searchUser_Pictures(User_Picture user_picture) {
		
		return null;
	}


}