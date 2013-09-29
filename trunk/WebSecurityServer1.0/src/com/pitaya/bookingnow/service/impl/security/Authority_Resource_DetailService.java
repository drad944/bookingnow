package com.pitaya.bookingnow.service.impl.security;
import java.util.List;

import com.pitaya.bookingnow.dao.security.Authority_Resource_DetailMapper;
import com.pitaya.bookingnow.entity.security.Authority_Resource_Detail;
import com.pitaya.bookingnow.service.security.IAuthority_Resource_DetailService;

public class Authority_Resource_DetailService implements IAuthority_Resource_DetailService {

	private Authority_Resource_DetailMapper authority_resource_detailDao;
	
	public Authority_Resource_DetailMapper getAuthority_Resource_DetailDao() {
		return authority_resource_detailDao;
	}

	public void setAuthority_Resource_DetailDao(Authority_Resource_DetailMapper authority_resource_detailDao) {
		this.authority_resource_detailDao = authority_resource_detailDao;
	}

	@Override
	public boolean add(Authority_Resource_Detail authority_resource_detail) {
		if(authority_resource_detailDao.insertSelective(authority_resource_detail) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAuthority_Resource_DetailById(Long id) {
		
		return false;
	}

	@Override
	public boolean remove(Authority_Resource_Detail authority_resource_detail) {
		
		return false;
	}

	@Override
	public boolean modify(Authority_Resource_Detail authority_resource_detail) {
		
		return false;
	}

	@Override
	public List<Authority_Resource_Detail> searchAuthority_Resource_Details(Authority_Resource_Detail authority_resource_detail) {
		
		return null;
	}


}