package com.pitaya.bookingnow.dao.security;

import com.pitaya.bookingnow.entity.security.Authority_Resource_Detail;

public interface Authority_Resource_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Authority_Resource_Detail record);

    int insertSelective(Authority_Resource_Detail record);

    Authority_Resource_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Authority_Resource_Detail record);

    int updateByPrimaryKey(Authority_Resource_Detail record);
}