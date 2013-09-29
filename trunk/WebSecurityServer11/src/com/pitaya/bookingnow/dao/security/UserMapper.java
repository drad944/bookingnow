package com.pitaya.bookingnow.dao.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User user);

    int insertSelective(User user);

    User selectByPrimaryKey(Long id);

    User getUserRole(Long id);
    
    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User user);
    
    List<User> searchUsers(User user);
    
    List<User> searchUsersWithRole(User user);
    
    List<User> searchAllUsers();
    
    User login(User user);
    
    User existUser(User user);
    
}