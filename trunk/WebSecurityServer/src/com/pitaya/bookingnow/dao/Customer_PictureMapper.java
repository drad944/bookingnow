package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Customer_Picture;

public interface Customer_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer_Picture picture);

    int insertSelective(Customer_Picture picture);

    Customer_Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer_Picture picture);

    int updateByPrimaryKeyWithBLOBs(Customer_Picture picture);

    int updateByPrimaryKey(Customer_Picture picture);
}