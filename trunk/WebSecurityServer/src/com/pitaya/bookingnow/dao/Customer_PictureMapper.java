package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Customer_Picture;

public interface Customer_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer_Picture record);

    int insertSelective(Customer_Picture record);

    Customer_Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer_Picture record);

    int updateByPrimaryKeyWithBLOBs(Customer_Picture record);

    int updateByPrimaryKey(Customer_Picture record);
}