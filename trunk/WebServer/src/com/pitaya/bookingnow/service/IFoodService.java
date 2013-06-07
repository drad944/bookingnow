package com.pitaya.bookingnow.service;

import com.pitaya.bookingnow.model.Food;

public interface IFoodService {
    int deleteByPrimaryKey(Integer fid);

    int insert(Food record);

    int insertSelective(Food record);

    Food selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(Food record);

    int updateByPrimaryKeyWithBLOBs(Food record);

    int updateByPrimaryKey(Food record);
}