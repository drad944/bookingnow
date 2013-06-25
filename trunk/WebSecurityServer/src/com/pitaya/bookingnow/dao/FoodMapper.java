package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Food;

public interface FoodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food record);

    int insertSelective(Food record);

    Food selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food record);

    int updateByPrimaryKey(Food record);
}