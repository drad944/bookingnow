package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.Role_Authority_Detail;




public interface IRole_Authority_DetailService{
	boolean add(Role_Authority_Detail role_authority_detail);
	
	boolean removeRole_Authority_DetailById(Long id);
	
	boolean remove(Role_Authority_Detail role_authority_detail);

	boolean modify(Role_Authority_Detail role_authority_detail);
	
	List<Role_Authority_Detail> searchRole_Authority_Details(Role_Authority_Detail role_authority_detail);

}
