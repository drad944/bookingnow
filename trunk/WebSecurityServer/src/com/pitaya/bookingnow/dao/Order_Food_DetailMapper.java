package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_Food_Detail;

public interface Order_Food_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_Food_Detail food_detail);

    int insertSelective(Order_Food_Detail food_detail);

    Order_Food_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_Food_Detail food_detail);

    int updateByPrimaryKey(Order_Food_Detail food_detail);
    
    Order_Food_Detail selectByFoodId(Long id);
    
    List<Order_Food_Detail> selectFullByFoodId(Long id);
    
    Order_Food_Detail selectFullByPrimaryKey(Long id);
    
    List<Order_Food_Detail> selectFullBySelective(Order_Food_Detail food_detail);
    
}