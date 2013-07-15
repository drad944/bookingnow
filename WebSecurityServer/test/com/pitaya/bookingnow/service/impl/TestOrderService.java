package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.entity.Order_Table_Detail;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

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
		

		MyResult result = orderService.addWaitingOrder(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
		}
	}
	
	@Test
	public void testUpdateWaitingOrderToConfirmed() {
		Order order = new Order();
		order.setId((long) 8);
		order.setTotal_price(105.0);
		order.setCustomer_count(3);
		order.setAllowance(1.0);
		
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
		
		
		MyResult result = orderService.updateWaitingOrderToConfirmed(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
		
	}
	
	@Test
	public void testAddNewOrder() {
		Order order = new Order();
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		
		order.setCustomer(customer);
		order.setCustomer_count(6);
		
		
		MyResult result = orderService.addNewOrder(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	
	@Test
	public void testCancelOrder() {
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		customer.setEnabled(true);
		
		List<Order> orders = orderService.searchOrdersByCustomer(customer);
		if (orders != null && orders.size() > 0) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				MyResult result = orderService.cancelOrder(order);
				if (result.isResult()) {
					
					System.out.println("add new order successfully!");
				} else {
					System.out.println("add new order failed!");
					Map<String, String> falseResults = result.getResultDetails();
					Iterator iter = falseResults.entrySet().iterator(); 
					while (iter.hasNext()) { 
					    Map.Entry entry = (Map.Entry) iter.next();
					    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
					} 
					
				}
			}
			
		}else {
			System.out.println("orders is empty!.");
		}
	}
	
	@Test
	public void testDeleteOrder() {
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		customer.setEnabled(false);
		
		List<Order> orders = orderService.searchOrdersByCustomer(customer);
		if (orders != null && orders.size() > 0) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				MyResult result = orderService.deleteOrder(order);
				if (result.isResult()) {
					
					System.out.println("add new order successfully!");
				} else {
					System.out.println("add new order failed!");
					Map<String, String> falseResults = result.getResultDetails();
					Iterator iter = falseResults.entrySet().iterator(); 
					while (iter.hasNext()) { 
					    Map.Entry entry = (Map.Entry) iter.next();
					    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
					} 
					
				}
			}
			
		}else {
			System.out.println("orders is empty!.");
		}
		
	}

	@Test
	public void testUpdateNewOrderToWaiting() {
		/*
		 * welcomer update order with food list when customer is waiting table and choose food
		 * in:order_id,customer_id,food list(food id,count,isFree),order status:waiting,food status:new
		 * 
		 */
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		customer.setEnabled(true);
		User user = new User();
		user.setId((long) 3);
		
		Order order = orderService.searchOrdersByCustomer(customer).get(0);
		List<Order_Food_Detail> food_Details = new ArrayList<Order_Food_Detail>();
		for (int i = 0; i < 5; i++) {
			Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
			Food tempFood = new Food();
			tempFood.setId((long)(i + 1));
			tempFood.setPrice(i + 11.0);
			tempFood_Detail.setFood(tempFood);
			tempFood_Detail.setCount(1);
			tempFood_Detail.setIsFree(false);
			food_Details.add(tempFood_Detail);
		}
		order.setUser(user);
		order.setFood_details(food_Details);
		order.setAllowance(1.0);

		MyResult result = orderService.updateWaitingOrderToWaiting(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	@Test
	public void testUpdateNewOrderToConfirmed() {
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		
		Order order = orderService.searchOrdersByCustomer(customer).get(0);
		User user = new User();
		user.setId((long) 3);
		order.setUser(user);
		
		List<Order_Table_Detail> table_Details = new ArrayList<Order_Table_Detail>();
		for (int i = 0; i < 2; i++) {
			Order_Table_Detail table_Detail = new Order_Table_Detail();
			Table table = new Table();
			table.setId((long)(i + 2));
			table_Detail.setTable(table);
			table_Details.add(table_Detail);
		}
		order.setTable_details(table_Details);
		
		MyResult result = orderService.updateNewOrderToConfirmed(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	@Test
	public void testUpdateFoodsInConfirmedOrder() {
		
		Order order = new Order();
		order.setId((long) 8);
		User user = new User();
		user.setId((long) 3);
		order.setUser(user);
		
		List<Order_Food_Detail> food_Details = new ArrayList<Order_Food_Detail>();
		for (int i = 0; i < 2; i++) {
			Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
			tempFood_Detail.setStatus(Constants.FOOD_NEW);
			tempFood_Detail.setIsFree(false);
			tempFood_Detail.setCount(1);
			
			Food tempFood = new Food();
			tempFood.setId((long) (i + 1));
			tempFood.setPrice(i + 11.0);
			tempFood_Detail.setFood(tempFood);
			food_Details.add(tempFood_Detail);
		}
		
		Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
		tempFood_Detail.setStatus(Constants.FOOD_UNAVAILABLE);
		tempFood_Detail.setCount(1);
		
		Food tempFood = new Food();
		tempFood.setId((long)3);
		tempFood_Detail.setFood(tempFood);
		food_Details.add(tempFood_Detail);
		order.setFood_details(food_Details);

		MyResult result = orderService.updateFoodsInConfirmedOrder(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	
	@Test
	public void testCalculateOrder() {
		Order order = new Order();
		order.setId((long) 8);

		MyResult result = orderService.calculateOrder(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	@Test
	public void testUpdateOrderToFinished() {
		
		Order order = orderService.searchOrderById((long) 2);

		MyResult result = orderService.updateOrderToFinished(order);
		if (result.isResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
}
