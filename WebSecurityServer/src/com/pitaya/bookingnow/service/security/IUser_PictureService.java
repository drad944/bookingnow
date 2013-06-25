package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.User_Picture;




public interface IUser_PictureService{
	boolean add(User_Picture user_picture);
	
	boolean removeUser_PictureById(Long id);
	
	boolean remove(User_Picture user_picture);

	boolean modify(User_Picture user_picture);
	
	List<User_Picture> searchUser_Pictures(User_Picture user_picture);

}
