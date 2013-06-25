package com.pitaya.bookingnow.service.impl.security;
import java.util.List;


import com.pitaya.bookingnow.dao.security.RoleMapper;
import com.pitaya.bookingnow.entity.security.Role;
import com.pitaya.bookingnow.service.security.IRoleService;

public class RoleService implements IRoleService {

	private RoleMapper roleDao;
	
	public RoleMapper getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleMapper roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	public boolean add(Role role) {
		if(roleDao.insertSelective(role) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeRoleById(Long id) {
		
		return false;
	}

	@Override
	public boolean remove(Role role) {
		
		return false;
	}

	@Override
	public boolean modify(Role role) {
		
		return false;
	}

	@Override
	public List<Role> searchRoles(Role role) {
		
		return null;
	}


}