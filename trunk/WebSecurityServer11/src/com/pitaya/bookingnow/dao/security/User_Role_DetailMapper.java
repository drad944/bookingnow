package com.pitaya.bookingnow.dao.security;

import com.pitaya.bookingnow.entity.security.User_Role_Detail;

public interface User_Role_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User_Role_Detail record);

    int insertSelective(User_Role_Detail record);

    User_Role_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User_Role_Detail record);

    int updateByPrimaryKey(User_Role_Detail record);
}