package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_Table_Detail;


public interface IOrder_Table_DetailService {
	
	boolean add(Order_Table_Detail order_table_detail);
	
	boolean removeOrder_Table_DetailById(Long id);
	
	boolean remove(Order_Table_Detail order_table_detail);

	boolean modify(Order_Table_Detail order_table_detail);
	
	List<Order_Table_Detail> searchOrder_Table_Details(Order_Table_Detail order_table_detail);
	
	
}