package com.pitaya.bookingnow.service.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.Authority;


public interface IAuthorityService {
boolean add(Authority authority);
	
	boolean removeAuthorityById(Long id);
	
	boolean remove(Authority authority);

	boolean modify(Authority authority);
	
	List<Authority> searchAuthoritys(Authority authority);
}
