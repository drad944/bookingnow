package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Table_Detail;

public class TestOrderService {
	private OrderService orderService;

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		OrderService orderService = (OrderService) aCtx.getBean("orderService");
		assertNotNull(orderService);
		this.orderService = orderService;
	}

	@Test
	public void testAddWaitingOrder() {
		Order order = new Order();
		order.setUser_id((long) 3);
		Order_Table_Detail table_Detail = new Order_Table_Detail();
		table_Detail.setTable_id((long) 4);

		boolean result = orderService.addWaitingOrder(order);
		if (result) {
			System.out.println("add waiting order successfully!");
		} else {
			System.out.println("add waiting order failed!");
		}
	}
}
