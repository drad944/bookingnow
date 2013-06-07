package com.pitaya.bookingnow.service;

import com.pitaya.bookingnow.model.Order;

public interface IOrderService {
    int deleteByPrimaryKey(Integer oid);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}