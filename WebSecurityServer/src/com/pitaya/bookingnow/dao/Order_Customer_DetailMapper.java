package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Order_Customer_Detail;

public interface Order_Customer_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_Customer_Detail record);

    int insertSelective(Order_Customer_Detail record);

    Order_Customer_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_Customer_Detail record);

    int updateByPrimaryKey(Order_Customer_Detail record);
}