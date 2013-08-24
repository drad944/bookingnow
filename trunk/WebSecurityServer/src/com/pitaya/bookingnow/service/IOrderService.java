package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public interface IOrderService {
	boolean add(Order order);
	
	boolean removeOrderById(Long id);
	
	boolean remove(Order order);

	boolean modify(Order order);
	
	List<Order> searchOrders(Order order);
	
	List<Order> searchFullOrders(SearchParams params);
	
	List<Order> searchFullOrdersWithoutFoods(SearchParams params);
	
	List<Order> searchFullOrdersByFullOrder(Order order);
	
	List<Order> searchOrdersByFullOrder(Order order);
	
	Order searchMinFullOrderByPrimaryKey(Long id);
	
	Order searchOrderById(Long id);
	
	Order getTablesOfOrder(Long id);
	
	MyResult addNewOrder(Order order);
	
	MyResult updateNewOrderToConfirmed(Order order);
	
	
	MyResult addWaitingOrder(Order order);
	
	MyResult updateFoodsOfWaitingOrder(Order order);
	
	MyResult updateTablesOfWaitingOrder(Order order);
	
	MyResult updateWaitingOrderToConfirmed(Order order);
	
	MyResult updateFoodsInConfirmedOrder(Order order);
	
	
	MyResult cancelFoods(Order order);
	
	MyResult cancelOrder(Order order);
	
	MyResult updateOrderToPaying(Order order);
	
	MyResult updateOrderToFinished(Order order);
	
	MyResult updateOrder(Order order);
	
	MyResult calculateOrder(Order order);
	
	MyResult deleteOrder(Order order);
	
	MyResult switchStatusForOrder(Order order);
	
    Order getFoodStatusOfOrder(Long id);
	
}