package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order order);

    int insertSelective(Order order);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order order);

    int updateByPrimaryKey(Order order);
    
    List<Order> searchOrders(Order order);
    
    Order selectFullOrderByPrimaryKey(Long id);
    
    List<Order> selectFullOrders(Order order);
    
    
}