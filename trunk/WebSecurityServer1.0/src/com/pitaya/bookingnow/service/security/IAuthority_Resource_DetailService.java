package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.Authority_Resource_Detail;




public interface IAuthority_Resource_DetailService{
	boolean add(Authority_Resource_Detail authority_resource_detail);
	
	boolean removeAuthority_Resource_DetailById(Long id);
	
	boolean remove(Authority_Resource_Detail authority_resource_detail);

	boolean modify(Authority_Resource_Detail authority_resource_detail);
	
	List<Authority_Resource_Detail> searchAuthority_Resource_Details(Authority_Resource_Detail authority_resource_detail);

}
