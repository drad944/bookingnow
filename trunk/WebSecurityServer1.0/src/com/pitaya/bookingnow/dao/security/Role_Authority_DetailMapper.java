package com.pitaya.bookingnow.dao.security;

import com.pitaya.bookingnow.entity.security.Role_Authority_Detail;

public interface Role_Authority_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role_Authority_Detail record);

    int insertSelective(Role_Authority_Detail record);

    Role_Authority_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role_Authority_Detail record);

    int updateByPrimaryKey(Role_Authority_Detail record);
}