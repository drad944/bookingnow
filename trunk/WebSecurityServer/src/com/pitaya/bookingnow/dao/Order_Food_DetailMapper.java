package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Order_Food_Detail;

public interface Order_Food_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_Food_Detail record);

    int insertSelective(Order_Food_Detail record);

    Order_Food_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_Food_Detail record);

    int updateByPrimaryKey(Order_Food_Detail record);
}