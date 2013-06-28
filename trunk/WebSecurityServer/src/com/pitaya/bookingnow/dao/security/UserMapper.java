package com.pitaya.bookingnow.dao.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    List<User> searchUsers(User record);
    User login(User record);
}