package com.pitaya.bookingnow.service.security;




import java.util.List;

import com.pitaya.bookingnow.entity.security.Role;




public interface IRoleService{
	boolean add(Role role);
	
	boolean removeRoleById(Long id);
	
	boolean remove(Role role);

	boolean modify(Role role);
	
	List<Role> searchRoles(Role role);

}
