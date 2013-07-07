package com.pitaya.bookingnow.service;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.MyResult;

public interface IOrderService {
	boolean add(Order order);
	
	boolean removeOrderById(Long id);
	
	boolean remove(Order order);

	boolean modify(Order order);
	
	List<Order> searchOrders(Order order);
	
	MyResult addWaitingOrder(Order order);
	
	MyResult addNewOrder(Order order);
	
	MyResult updateWaitingOrderToConfirmed(Order order);
}