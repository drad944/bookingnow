package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.entity.Order_Table_Detail;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.IOrderService;

public class TestOrderService {
	private IOrderService orderService;

	public IOrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		IOrderService orderService = aCtx.getBean(IOrderService.class);
		assertNotNull(orderService);
		this.orderService = orderService;
	}

	@Test
	public void testAddWaitingOrder() {
		Order order = new Order();
		
		User user = new User();
		user.setId((long) 3);
		
		Order_Table_Detail table_Detail = new Order_Table_Detail();
		
		Table table = new Table();
		table.setId((long) 4);
		
		table_Detail.setTable(table);
		
		List<Order_Table_Detail> table_Details = new ArrayList<Order_Table_Detail>();
		table_Details.add(table_Detail);
		order.setTable_details(table_Details);
		
		order.setUser(user);
		

		Map<String, String> result = orderService.addWaitingOrder(order);
		if (result != null && result.size() > 0) {
			
			System.out.println("add waiting order successfully!");
		} else {
			
			System.out.println("add waiting order failed!");
		}
	}
	
	@Test
	public void testUpdateWaitingOrderToConfirmed() {
		Order order = new Order();
		order.setId((long) 8);
		order.setTotal_price(105.0);
		order.setCustomer_count(3);
		
		User user = new User();
		user.setId((long) 3);
		order.setUser(user);
		
		List<Order_Food_Detail> food_Details = new ArrayList<Order_Food_Detail>();
		
		for (int i = 0; i < 5; i++) {
			Order_Food_Detail food_Detail = new Order_Food_Detail();
			
			Food food = new Food();
			food.setId((long) i+1);
			food.setPrice(i + 11.0);
			food_Detail.setFood(food);
			food_Detail.setCount(2);
			food_Details.add(food_Detail);
		}
		order.setFood_details(food_Details);
		
		
		Map<String, String> result = orderService.updateWaitingOrderToConfirmed(order);
		if (result != null && result.size() > 0) {
			
			System.out.println("add waiting order successfully!");
		} else {
			
			System.out.println("add waiting order failed!");
		}
		
	}
}
