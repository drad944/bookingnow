package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.MyResult;

public interface IOrderService {
	boolean add(Order order);
	
	boolean removeOrderById(Long id);
	
	boolean remove(Order order);

	boolean modify(Order order);
	
	List<Order> searchOrders(Order order);
	
	MyResult addWaitingOrder(Order order);
	
	MyResult updateWaitingOrderToConfirmed(Order order);
	
	
	MyResult addNewOrder(Order order);
	
	MyResult updateNewOrderToWaiting(Order order);
	
	MyResult updateNewOrderToConfirmed(Order order);
	
	MyResult updateFoodInConfirmedOrder(Order order);
	
	
	MyResult cancelFoods(Order order);
	
	MyResult cancelOrder(Order order);
	
	MyResult updateOrderToFinished(Order order);
	
	MyResult updateOrder(Order order);
}