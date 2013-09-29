package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.User_Role_Detail;




public interface IUser_Role_DetailService{
	boolean add(User_Role_Detail user_role_detail);
	
	boolean removeUser_Role_DetailById(Long id);
	
	boolean remove(User_Role_Detail user_role_detail);

	boolean modify(User_Role_Detail user_role_detail);
	
	List<User_Role_Detail> searchUser_Role_Details(User_Role_Detail user_role_detail);

}
