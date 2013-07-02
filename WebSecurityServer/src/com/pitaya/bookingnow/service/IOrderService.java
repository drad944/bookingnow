package com.pitaya.bookingnow.service;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order;

public interface IOrderService {
	boolean add(Order order);
	
	boolean removeOrderById(Long id);
	
	boolean remove(Order order);

	boolean modify(Order order);
	
	List<Order> searchOrders(Order order);
	
	Map<String, String> addWaitingOrder(Order order);
	
	boolean addNewOrder(Order order);
	
	Map<String, String> updateWaitingOrderToConfirmed(Order order);
}