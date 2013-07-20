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
import com.pitaya.bookingnow.util.SearchParams;

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
	
	public void showFullOrderInfo(Order realOrder) {
		String space = "    ";
		if (realOrder != null) {
			System.out.println("Id : " + realOrder.getId());
			System.out.println("Allowance : " + realOrder.getAllowance());
			System.out.println("Customer_count : " + realOrder.getCustomer_count());
			System.out.println("Customer_id : " + realOrder.getCustomer_id());
			System.out.println("ModifyTime : " + realOrder.getModifyTime());
			System.out.println("PrePay : " + realOrder.getPrePay());
			System.out.println("Status : " + realOrder.getStatus());
			System.out.println("Submit_time : " + realOrder.getSubmit_time());
			System.out.println("Total_price : " + realOrder.getTotal_price());
			System.out.println("User_id : " + realOrder.getUser_id());
			System.out.println("Enabled : " + realOrder.getEnabled());
			
			User realUser = realOrder.getUser();
			if (realUser != null) {
				System.out.println(space + "Id : " + realUser.getId());
				System.out.println(space + "Account : " + realUser.getAccount());
				System.out.println(space + "Address : " + realUser.getAddress());
				System.out.println(space + "Description : " + realUser.getDescription());
				System.out.println(space + "Email : " + realUser.getEmail());
				System.out.println(space + "Name : " + realUser.getName());
				System.out.println(space + "Password : " + realUser.getPassword());
				System.out.println(space + "Phone : " + realUser.getPhone());
				System.out.println(space + "Birthday : " + realUser.getBirthday());
				System.out.println(space + "Department : " + realUser.getDepartment());
				System.out.println(space + "Picture_id : " + realUser.getPicture_id());
				System.out.println(space + "Sex : " + realUser.getSex());
				System.out.println(space + "Sub_system : " + realUser.getSub_system());
				System.out.println(space + "Enabled : " + realUser.getEnabled());
			}
			
			Customer realCustomer = realOrder.getCustomer();
			if (realCustomer != null) {
				System.out.println(space + "Id : " + realCustomer.getId());
				System.out.println(space + "Account : " + realCustomer.getAccount());
				System.out.println(space + "Address : " + realCustomer.getAddress());
				System.out.println(space + "Email : " + realCustomer.getEmail());
				System.out.println(space + "Name : " + realCustomer.getName());
				System.out.println(space + "Password : " + realCustomer.getPassword());
				System.out.println(space + "Phone : " + realCustomer.getPhone());
				System.out.println(space + "Birthday : " + realCustomer.getBirthday());
				System.out.println(space + "Picture_id : " + realCustomer.getPicture_id());
				System.out.println(space + "Sex : " + realCustomer.getSex());
				System.out.println(space + "Enabled : " + realCustomer.getEnabled());
			}
			
			
			List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
			 for (int j = 0; j < realFood_Details.size(); j++) {
				 Order_Food_Detail realFood_Detail = realFood_Details.get(j);
				 if (realFood_Detail != null) {
					System.out.println(space + "Id : " + realFood_Detail.getId());
					System.out.println(space + "Count : " + realFood_Detail.getCount());
					System.out.println(space + "Food_id : " + realFood_Detail.getFood_id());
					System.out.println(space + "Last_modify_time : " + realFood_Detail.getLast_modify_time());
					System.out.println(space + "Order_id : " + realFood_Detail.getOrder_id());
					System.out.println(space + "Preference : " + realFood_Detail.getPreference());
					System.out.println(space + "Status : " + realFood_Detail.getStatus());
					System.out.println(space + "Enabled : " + realFood_Detail.getEnabled());
					System.out.println(space + "IsFree : " + realFood_Detail.getIsFree());
					if (realFood_Detail.getFood() != null) {
						Food realFood = realFood_Detail.getFood();
						System.out.println(space + space + "id : " + realFood.getId());
						System.out.println(space + space + "Category : " + realFood.getCategory());
						System.out.println(space + space + "Description : " + realFood.getDescription());
						System.out.println(space + space + "Name : " + realFood.getName());
						System.out.println(space + space + "Period : " + realFood.getPeriod());
						System.out.println(space + space + "Picture_id : " + realFood.getPicture_id());
						System.out.println(space + space + "Price : " + realFood.getPrice());
						System.out.println(space + space + "Recommendation : " + realFood.getRecommendation());
						System.out.println(space + space + "Status : " + realFood.getStatus());
						System.out.println(space + space + "Version : " + realFood.getVersion());
					}
				}
			}
			 
			 List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
			 for (int j = 0; j < realTable_Details.size(); j++) {
				 Order_Table_Detail realTable_Detail = realTable_Details.get(j);
				 if (realTable_Detail != null) {
					System.out.println(space + "Id : " + realTable_Detail.getId());
					System.out.println(space + "Order_id : " + realTable_Detail.getOrder_id());
					System.out.println(space + "RealCustomerCount : " + realTable_Detail.getRealCustomerCount());
					System.out.println(space + "Table_id : " + realTable_Detail.getTable_id());
					System.out.println(space + "Enabled : " + realTable_Detail.getEnabled());
					if (realTable_Detail.getTable() != null) {
						Table realTable = realTable_Detail.getTable();
						System.out.println(space + space + "id : " + realTable.getId());
						System.out.println(space + space + "Address : " + realTable.getAddress());
						System.out.println(space + space + "IndoorPrice : " + realTable.getIndoorPrice());
						System.out.println(space + space + "MaxCustomerCount : " + realTable.getMaxCustomerCount());
						System.out.println(space + space + "MinCustomerCount : " + realTable.getMinCustomerCount());
						System.out.println(space + space + "Status : " + realTable.getStatus());
					}
					System.out.println("-------------------------------------------------------");
				}
			}
		}
		

	}
	
	@Test
	public void testSearchFullOrdersByFullOrder() {
		Order order = new Order();
		User user = new User();
		user.setId((long) 2);
		order.setUser(user);
		
		List<Order> realOrders = orderService.searchFullOrdersByFullOrder(order);
		
		if (realOrders != null) {
			
			for (int i = 0; i < realOrders.size(); i++) {
				Order realOrder = realOrders.get(i);
				showFullOrderInfo(realOrder);
			}
		}
		
	}
	

	@Test
	public void testSearchFullOrders() {
		List<Integer> orderStatusList = new ArrayList<Integer>();
		orderStatusList.add(Constants.ORDER_COMMITED);
		orderStatusList.add(Constants.ORDER_COOKING);
		orderStatusList.add(Constants.ORDER_NEW);
		SearchParams params = new SearchParams();
		params.setUser_id((long) 3);
		params.setOrderStatusList(orderStatusList);
		
		List<Order> realOrders = orderService.searchFullOrders(params);
		
		if (realOrders != null) {
			
			for (int i = 0; i < realOrders.size(); i++) {
				Order realOrder = realOrders.get(i);
				showFullOrderInfo(realOrder);
			}
		}
		
	}
	
	@Test
	public void testAddNewOrder() {
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
		

		MyResult result = orderService.addNewOrder(order);
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
		}
	}
	
	@Test
	public void testUpdateNewOrderToConfirmed() {
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
			food.setVersion((long) 1351232321);
			
			food.setPrice(i + 11.0);
			food_Detail.setFood(food);
			food_Detail.setCount(2);
			food_Details.add(food_Detail);
		}
		order.setFood_details(food_Details);
		
		
		MyResult result = orderService.updateNewOrderToConfirmed(order);
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
		
	}
	
	@Test
	public void testAddWaitingOrder() {
		Order order = new Order();
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		
		order.setCustomer(customer);
		order.setCustomer_count(6);
		
		
		MyResult result = orderService.addWaitingOrder(order);
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
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
		
		Order tempOrder = new Order();
		tempOrder.setCustomer(customer);
		
		List<Order> orders = orderService.searchFullOrdersByFullOrder(tempOrder);
		if (orders != null && orders.size() > 0) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				MyResult result = orderService.cancelOrder(order);
				if (result.isExecuteResult()) {
					
					System.out.println("add new order successfully!");
				} else {
					System.out.println("add new order failed!");
					Map<String, String> falseResults = result.getErrorDetails();
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
		
		Order tempOrder = new Order();
		tempOrder.setCustomer(customer);
		
		List<Order> orders = orderService.searchFullOrdersByFullOrder(tempOrder);
		if (orders != null && orders.size() > 0) {
			for (int i = 0; i < orders.size(); i++) {
				Order order = orders.get(i);
				MyResult result = orderService.deleteOrder(order);
				if (result.isExecuteResult()) {
					
					System.out.println("add new order successfully!");
				} else {
					System.out.println("add new order failed!");
					Map<String, String> falseResults = result.getErrorDetails();
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
	public void testUpdateWaitingOrderToWaiting() {
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
		
		Order tempOrder = new Order();
		tempOrder.setCustomer(customer);
		
		Order order = orderService.searchFullOrdersByFullOrder(tempOrder).get(0);
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
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
	@Test
	public void testUpdateWaitingOrderToConfirmed() {
		
		Customer customer = new Customer();
		customer.setName("zhan");
		customer.setPhone("13546768131");
		customer.setSex(Constants.CUSTOMER_MALE);
		
		Order tempOrder = new Order();
		tempOrder.setCustomer(customer);
		
		Order order = orderService.searchFullOrdersByFullOrder(tempOrder).get(0);
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
		
		MyResult result = orderService.updateWaitingOrderToConfirmed(order);
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
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
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
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
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
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
		if (result.isExecuteResult()) {
			
			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry entry = (Map.Entry) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
	}
	
}
