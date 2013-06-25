package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.User;





public interface IUserService{
	boolean add(User user);
	
	boolean removeUserById(Long id);
	
	boolean remove(User user);

	boolean modify(User user);
	
	List<User> searchUsers(User user);
	
	User login(User user);

}
