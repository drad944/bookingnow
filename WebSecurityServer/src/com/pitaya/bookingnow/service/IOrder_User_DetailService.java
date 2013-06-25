package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_User_Detail;


public interface IOrder_User_DetailService {
	
	boolean add(Order_User_Detail order_user_detail);
	
	boolean removeOrder_User_DetailById(Long id);
	
	boolean remove(Order_User_Detail order_user_detail);

	boolean modify(Order_User_Detail order_user_detail);
	
	List<Order_User_Detail> searchOrder_User_Details(Order_User_Detail order_user_detail);
	
	
}