package com.pitaya.bookingnow.dao.security;

import java.util.List;

import com.pitaya.bookingnow.entity.security.Role;

public interface RoleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Role role);

    int insertSelective(Role role);

    Role selectByPrimaryKey(Long id);
    
    List<Role> searchRoles(Role role);

    int updateByPrimaryKeySelective(Role role);

    int updateByPrimaryKey(Role role);
}