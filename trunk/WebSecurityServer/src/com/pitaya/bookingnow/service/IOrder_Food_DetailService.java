package com.pitaya.bookingnow.service;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;


public interface IOrder_Food_DetailService {
	
	boolean add(Order_Food_Detail food_detail);
	
	boolean removeOrder_Food_DetailById(Long id);
	
	boolean remove(Order_Food_Detail food_detail);

	boolean modify(Order_Food_Detail food_detail);
	
	List<Order_Food_Detail> searchOrder_Food_Details(Order_Food_Detail food_detail);
	
	MyResult updateFoodStatus(Order_Food_Detail food_detail);
	
	MyResult updateFoods(Map<String, List<Order_Food_Detail>> changeFoods,Long orderId);
	
	Order_Food_Detail searchFullByPrimaryKeyAndOrderId(SearchParams params);
}