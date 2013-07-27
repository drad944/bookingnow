package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Customer;

public interface CustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Customer customer);

    int insertSelective(Customer customer);

    Customer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Customer customer);

    int updateByPrimaryKey(Customer customer);
    
    List<Customer> selectCustomers(Customer customer);
    
    Customer login(Customer customer);
}