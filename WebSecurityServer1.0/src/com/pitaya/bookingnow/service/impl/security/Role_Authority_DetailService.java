package com.pitaya.bookingnow.service.impl.security;
import java.util.List;

import com.pitaya.bookingnow.dao.security.Role_Authority_DetailMapper;
import com.pitaya.bookingnow.entity.security.Role_Authority_Detail;
import com.pitaya.bookingnow.service.security.IRole_Authority_DetailService;

public class Role_Authority_DetailService implements IRole_Authority_DetailService {

	private Role_Authority_DetailMapper role_authority_detailDao;
	
	public Role_Authority_DetailMapper getRole_Authority_DetailDao() {
		return role_authority_detailDao;
	}

	public void setRole_Authority_DetailDao(Role_Authority_DetailMapper role_authority_detailDao) {
		this.role_authority_detailDao = role_authority_detailDao;
	}

	@Override
	public boolean add(Role_Authority_Detail role_authority_detail) {
		if(role_authority_detailDao.insertSelective(role_authority_detail) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeRole_Authority_DetailById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Role_Authority_Detail role_authority_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Role_Authority_Detail role_authority_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Role_Authority_Detail> searchRole_Authority_Details(Role_Authority_Detail role_authority_detail) {
		// TODO Auto-generated method stub
		return null;
	}

}