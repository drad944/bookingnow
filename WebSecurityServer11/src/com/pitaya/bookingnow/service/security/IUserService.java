package com.pitaya.bookingnow.service.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public interface IUserService{
	MyResult add(User user);
	
	MyResult addUserWithRole(User user);
	
	MyResult removeUserById(Long id);
	
	MyResult modify(User user);
	
	MyResult modifyRoleWithUser(User user);
	
	List<User> searchUsers(User user);
	
	List<User> searchUsersWithRole(User user);
	
	List<User> searchAllUsers();
	
	MyResult login(User user);
	
	User searchUserById(Long id);
	
	User getUserRole(Long id);
	
	MyResult cropPicture(SearchParams params);
	
	boolean existUser(User user);
}
