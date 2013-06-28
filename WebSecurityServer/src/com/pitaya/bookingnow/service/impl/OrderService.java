package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.OrderMapper;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;

public class OrderService implements IOrderService{

	private OrderMapper orderDao;
	
	public OrderMapper getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderMapper orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public boolean add(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeOrderById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Order> searchOrders(Order order) {
		
		return orderDao.searchOrders(order);
	}

}
