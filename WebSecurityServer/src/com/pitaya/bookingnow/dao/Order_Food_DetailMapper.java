package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Order_Food_Detail;

public interface Order_Food_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_Food_Detail food_detail);

    int insertSelective(Order_Food_Detail food_detail);

    Order_Food_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_Food_Detail food_detail);

    int updateByPrimaryKey(Order_Food_Detail food_detail);
    
    Order_Food_Detail selectByFoodId(Long id);
    
    Order_Food_Detail selectFullByFoodId(Long id);
    
}