package com.pitaya.bookingnow.service.impl.security;
import java.util.List;


import com.pitaya.bookingnow.dao.security.AuthorityMapper;
import com.pitaya.bookingnow.entity.security.Authority;
import com.pitaya.bookingnow.service.security.IAuthorityService;

public class AuthorityService implements IAuthorityService {

	private AuthorityMapper authorityDao;
	
	public AuthorityMapper getAuthorityDao() {
		return authorityDao;
	}

	public void setAuthorityDao(AuthorityMapper authorityDao) {
		this.authorityDao = authorityDao;
	}

	@Override
	public boolean add(Authority authority) {
		if(authorityDao.insertSelective(authority) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAuthorityById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Authority authority) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Authority authority) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Authority> searchAuthoritys(Authority authority) {
		// TODO Auto-generated method stub
		return null;
	}



}