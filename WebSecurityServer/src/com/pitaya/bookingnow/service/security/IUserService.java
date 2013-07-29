package com.pitaya.bookingnow.service.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.util.MyResult;

public interface IUserService{
	MyResult add(User user);
	
	MyResult removeUserById(Long id);
	
	MyResult modify(User user);
	
	List<User> searchUsers(User user);
	
	List<User> searchUsersWithRole(User user);
	
	List<User> searchAllUsers();
	
	MyResult login(User user);
	
	User searchUserById(Long id);

}
