package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Material;

public interface MaterialMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Material material);

    int insertSelective(Material material);

    Material selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Material material);

    int updateByPrimaryKey(Material material);
}