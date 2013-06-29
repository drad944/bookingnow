package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Order_Table_Detail;

public interface Order_Table_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order_Table_Detail table_detail);

    int insertSelective(Order_Table_Detail table_detail);

    Order_Table_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order_Table_Detail table_detail);

    int updateByPrimaryKey(Order_Table_Detail table_detail);
}