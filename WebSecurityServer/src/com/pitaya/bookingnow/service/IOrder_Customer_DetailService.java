package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_Customer_Detail;


public interface IOrder_Customer_DetailService {
	
	boolean add(Order_Customer_Detail order_customer_detail);
	
	boolean removeOrder_Customer_DetailById(Long id);
	
	boolean remove(Order_Customer_Detail order_customer_detail);

	boolean modify(Order_Customer_Detail order_customer_detail);
	
	List<Order_Customer_Detail> searchOrder_Customer_Details(Order_Customer_Detail order_customer_detail);
	
	
}