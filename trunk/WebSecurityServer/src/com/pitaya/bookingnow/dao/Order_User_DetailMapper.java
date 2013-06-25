package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Order_User_Detail;

public interface Order_User_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_User_Detail record);

    int insertSelective(Order_User_Detail record);

    Order_User_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_User_Detail record);

    int updateByPrimaryKey(Order_User_Detail record);
}