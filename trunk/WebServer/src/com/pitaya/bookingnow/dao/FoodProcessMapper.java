package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.model.FoodProcess;

public interface FoodProcessMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(FoodProcess record);

    int insertSelective(FoodProcess record);

    FoodProcess selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(FoodProcess record);

    int updateByPrimaryKey(FoodProcess record);
}