package com.pitaya.bookingnow.security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pitaya.bookingnow.dao.security.UserMapper;


public class MyUserDetailService implements UserDetailsService {
	
    @Resource
    private UserMapper userDao;
    

    public UserMapper getUserDao() {
		return userDao;
	}


	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		com.pitaya.bookingnow.entity.security.User tempUser = new com.pitaya.bookingnow.entity.security.User();
		tempUser.setAccount(username);
		
		List<com.pitaya.bookingnow.entity.security.User> listUsers = userDao.searchUsers(tempUser);
		
		
		if (listUsers != null && listUsers.size() > 0) {
			tempUser = listUsers.get(0);
		}
    	
        
        if (null == tempUser) {
            throw new UsernameNotFoundException("can not find user : " + username + "in database.");
        }
        
        
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        //List<String> list = userDao.getAuthoritiesByUsername(username);
        List<String> list = new ArrayList<>();
        list.add("ROLE_ADMIN");
        list.add("ROLE_USER");
        
        for (int i = 0; i < list.size(); i++) {
            auths.add(new GrantedAuthorityImpl(list.get(i)));
            System.out.println("loadUserByUsername : " + list.get(i));
        }
        return new User(username, tempUser.getPassword(), true, true, true, true, auths);
		
	}
	
}