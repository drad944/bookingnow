package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.SearchParams;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order order);

    int insertSelective(Order order);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order order);

    int updateByPrimaryKey(Order order);
    
    List<Order> searchOrders(Order order);
    
    Order selectFullOrderByPrimaryKey(Long id);
    
    List<Order> searchFullOrdersByFullOrderInfo(Order order);
    
    List<Order> searchOrdersByFullOrderInfo(Order order);
    
    List<Order> searchFullOrders(SearchParams params);
    
    
    
    Order selectMinFullOrderByPrimaryKey(Long id);
    
    List<Order> searchMinFullOrdersByFullOrderInfo(Order order);
    
    List<Order> searchMinFullOrders(SearchParams params);
    
    List<Order> searchFullOrdersWithoutFoods(SearchParams params);
    
}