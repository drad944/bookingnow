package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_Food_Detail;


public interface IOrder_Food_DetailService {
	
	boolean add(Order_Food_Detail order_food_detail);
	
	boolean removeOrder_Food_DetailById(Long id);
	
	boolean remove(Order_Food_Detail order_food_detail);

	boolean modify(Order_Food_Detail order_food_detail);
	
	List<Order_Food_Detail> searchOrder_Food_Details(Order_Food_Detail order_food_detail);
	
	
}