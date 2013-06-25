package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;

public interface IOrderService {
	boolean add(Order order);
	
	boolean removeOrderById(Long id);
	
	boolean remove(Order order);

	boolean modify(Order order);
	
	List<Order> searchOrders(Order order);
}