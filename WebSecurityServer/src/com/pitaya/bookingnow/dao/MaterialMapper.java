package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Material;

public interface MaterialMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Material record);

    int insertSelective(Material record);

    Material selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Material record);

    int updateByPrimaryKey(Material record);
}