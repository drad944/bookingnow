package com.pitaya.bookingnow.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.entity.Order_Table_Detail;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.util.MyBatisUtil;

public class TestOrderMapper {
	static SqlSessionFactory sqlSessionFactory = null;
	static {
		sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
	}
	
	public void showFullOrderInfo(Order realOrder) {
		String space = "    ";
		String orderString = " order ";
		String userString = " user ";
		String customerString = " customer ";
		String food_detailString = " food_detail ";
		String foodString = " food ";
		String table_detailString = " table_detail ";
		String tableString = " table ";
		
		if (realOrder != null) {
			System.out.println(orderString + "Id : " + realOrder.getId());
			System.out.println(orderString + "Allowance : " + realOrder.getAllowance());
			System.out.println(orderString + "Customer_count : " + realOrder.getCustomer_count());
			System.out.println(orderString + "Customer_id : " + realOrder.getCustomer_id());
			System.out.println(orderString + "ModifyTime : " + realOrder.getModifyTime());
			System.out.println(orderString + "PrePay : " + realOrder.getPrePay());
			System.out.println(orderString + "Status : " + realOrder.getStatus());
			System.out.println(orderString + "Submit_time : " + realOrder.getSubmit_time());
			System.out.println(orderString + "Total_price : " + realOrder.getTotal_price());
			System.out.println(orderString + "User_id : " + realOrder.getUser_id());
			System.out.println(orderString + "Enabled : " + realOrder.getEnabled());
			
			User realUser = realOrder.getUser();
			if (realUser != null) {
				System.out.println(space + userString  + "Id : " + realUser.getId());
				System.out.println(space + userString  + "Image_absolute_path : " + realUser.getImage_absolute_path());
				System.out.println(space + userString  + "Image_relative_path : " + realUser.getImage_relative_path());
				System.out.println(space + userString  + "Image_size : " + realUser.getImage_size());
				System.out.println(space + userString  + "ModifyTime : " + realUser.getModifyTime());
				System.out.println(space + userString  + "Account : " + realUser.getAccount());
				System.out.println(space + userString  + "Address : " + realUser.getAddress());
				System.out.println(space + userString  + "Description : " + realUser.getDescription());
				System.out.println(space + userString  + "Email : " + realUser.getEmail());
				System.out.println(space + userString  + "Name : " + realUser.getName());
				System.out.println(space + userString  + "Password : " + realUser.getPassword());
				System.out.println(space + userString  + "Phone : " + realUser.getPhone());
				System.out.println(space + userString  + "Birthday : " + realUser.getBirthday());
				System.out.println(space + userString  + "Department : " + realUser.getDepartment());
				System.out.println(space + userString  + "Sex : " + realUser.getSex());
				System.out.println(space + userString  + "Sub_system : " + realUser.getSub_system());
				System.out.println(space + userString  + "Enabled : " + realUser.getEnabled());
			}
			
			Customer realCustomer = realOrder.getCustomer();
			if (realCustomer != null) {
				System.out.println(space + customerString  + "Id : " + realCustomer.getId());
				System.out.println(space + customerString  + "Image_absolute_path : " + realCustomer.getImage_absolute_path());
				System.out.println(space + customerString  + "Image_relative_path : " + realCustomer.getImage_relative_path());
				System.out.println(space + customerString  + "Image_size : " + realCustomer.getImage_size());
				System.out.println(space + customerString  + "ModifyTime : " + realCustomer.getModifyTime());
				System.out.println(space + customerString  + "Account : " + realCustomer.getAccount());
				System.out.println(space + customerString  + "Address : " + realCustomer.getAddress());
				System.out.println(space + customerString  + "Email : " + realCustomer.getEmail());
				System.out.println(space + customerString  + "Name : " + realCustomer.getName());
				System.out.println(space + customerString  + "Password : " + realCustomer.getPassword());
				System.out.println(space + customerString  + "Phone : " + realCustomer.getPhone());
				System.out.println(space + customerString  + "Birthday : " + realCustomer.getBirthday());
				System.out.println(space + customerString  + "Sex : " + realCustomer.getSex());
				System.out.println(space + customerString  + "Enabled : " + realCustomer.getEnabled());
			}
			
			
			List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
			 for (int j = 0; j < realFood_Details.size(); j++) {
				 Order_Food_Detail realFood_Detail = realFood_Details.get(j);
				 if (realFood_Detail != null) {
					System.out.println(space + food_detailString  + "Id : " + realFood_Detail.getId());
					System.out.println(space + food_detailString  + "Count : " + realFood_Detail.getCount());
					System.out.println(space + food_detailString  + "Food_id : " + realFood_Detail.getFood_id());
					System.out.println(space + food_detailString  + "Last_modify_time : " + realFood_Detail.getLast_modify_time());
					System.out.println(space + food_detailString  + "Order_id : " + realFood_Detail.getOrder_id());
					System.out.println(space + food_detailString  + "Preference : " + realFood_Detail.getPreference());
					System.out.println(space + food_detailString  + "Status : " + realFood_Detail.getStatus());
					System.out.println(space + food_detailString  + "Enabled : " + realFood_Detail.getEnabled());
					System.out.println(space + food_detailString  + "IsFree : " + realFood_Detail.getIsFree());
					if (realFood_Detail.getFood() != null) {
						Food realFood = realFood_Detail.getFood();
						System.out.println(space + space + foodString + "Id : " + realFood.getId());
						System.out.println(space + space + foodString + "Category : " + realFood.getCategory());
						System.out.println(space + space + foodString + "Description : " + realFood.getDescription());
						System.out.println(space + space + foodString + "Large_image_absolute_path : " + realFood.getLarge_image_absolute_path());
						System.out.println(space + space + foodString + "Large_image_relative_path : " + realFood.getLarge_image_relative_path());
						System.out.println(space + space + foodString + "Name : " + realFood.getName());
						System.out.println(space + space + foodString + "Small_image_absolute_path : " + realFood.getSmall_image_absolute_path());
						System.out.println(space + space + foodString + "Small_image_relative_path : " + realFood.getSmall_image_relative_path());
						System.out.println(space + space + foodString + "Large_image_size : " + realFood.getLarge_image_size());
						System.out.println(space + space + foodString + "Period : " + realFood.getPeriod());
						System.out.println(space + space + foodString + "Price : " + realFood.getPrice());
						System.out.println(space + space + foodString + "Recommendation : " + realFood.getRecommendation());
						System.out.println(space + space + foodString + "Small_image_size : " + realFood.getSmall_image_size());
						System.out.println(space + space + foodString + "Status : " + realFood.getStatus());
						System.out.println(space + space + foodString + "Version : " + realFood.getVersion());
						System.out.println(space + space + foodString + "Enabled : " + realFood.getEnabled());
						System.out.println(space + space + foodString + "Large_image : " + realFood.getLarge_image());
						System.out.println(space + space + foodString + "Small_image : " + realFood.getSmall_image());
						
					}
				}
			}
			 
			 List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
			 for (int j = 0; j < realTable_Details.size(); j++) {
				 Order_Table_Detail realTable_Detail = realTable_Details.get(j);
				 if (realTable_Detail != null) {
					System.out.println(space + table_detailString + "Id : " + realTable_Detail.getId());
					System.out.println(space + table_detailString + "Order_id : " + realTable_Detail.getOrder_id());
					System.out.println(space + table_detailString + "RealCustomerCount : " + realTable_Detail.getRealCustomerCount());
					System.out.println(space + table_detailString + "Table_id : " + realTable_Detail.getTable_id());
					System.out.println(space + table_detailString + "Enabled : " + realTable_Detail.getEnabled());
					if (realTable_Detail.getTable() != null) {
						Table realTable = realTable_Detail.getTable();
						System.out.println(space + space + tableString + "id : " + realTable.getId());
						System.out.println(space + space + tableString + "Address : " + realTable.getAddress());
						System.out.println(space + space + tableString + "IndoorPrice : " + realTable.getIndoorPrice());
						System.out.println(space + space + tableString + "MaxCustomerCount : " + realTable.getMaxCustomerCount());
						System.out.println(space + space + tableString + "MinCustomerCount : " + realTable.getMinCustomerCount());
						System.out.println(space + space + tableString + "Status : " + realTable.getStatus());
					}
					System.out.println("-------------------------------------------------------");
				}
			}
		}
	}
	
	@Test
	public void testSelectMinFullOrderByPrimaryKey() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			Order order = null;

			order = orderMapper.selectFullOrderByPrimaryKey((long) 1);

			showFullOrderInfo(order);
		} finally {
			sqlSession.close();
		}
	}


	@Test
	public void testSelectFullOrderByPrimaryKey() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			Order order = null;

			order = orderMapper.selectFullOrderByPrimaryKey((long) 2);

			if (order != null) {

				System.out.println("order id: " + order.getId());
				System.out.println("order status: " + order.getStatus());
				System.out.println("order allowance: " + order.getAllowance());
				System.out.println("order PrePay: " + order.getPrePay());
				System.out.println("order Total_price: " + order.getTotal_price());
				System.out.println("order Submit_time: " + order.getSubmit_time());
				System.out.println("order Enabled: " + order.getEnabled());
				
				User user = order.getUser();
				if(user != null) {
					System.out.println("user id: " + user.getId());
					System.out.println("user name: " + user.getName());
					System.out.println("user sex: " + user.getSex());
					System.out.println("user department: " + user.getDepartment());
				}
				
				Customer customer = order.getCustomer();
				if(customer != null) {
					System.out.println("customer id: " + customer.getId());
					System.out.println("customer name: " + customer.getName());
					System.out.println("customer sex: " + customer.getSex());
				}
				
				List<Order_Table_Detail> table_Details = order.getTable_details();
				if (table_Details != null && table_Details.size() > 0) {
					for (int i = 0; i < table_Details.size(); i++) {
						Order_Table_Detail table_Detail = table_Details.get(i);
						System.out.println("table detail id: " + table_Detail.getId());
						System.out.println("table detail realCustomerCount: " + table_Detail.getRealCustomerCount());
						System.out.println("table detail table id: " + table_Detail.getTable_id());
						System.out.println("table detail order id: " + table_Detail.getOrder_id());
						
						Table table = table_Detail.getTable();
						if (table != null) {
							System.out.println("    " + "table id: " + table.getId());
							System.out.println("    " + "table status: " + table.getStatus());
							System.out.println("    " + "table min customer count: " + table.getMinCustomerCount());
							System.out.println("    " + "table address: " + table.getAddress());
							
						}
					}
				}
				
				List<Order_Food_Detail> food_Details = order.getFood_details();
				if (food_Details != null && food_Details.size() > 0) {
					for (int i = 0; i < food_Details.size(); i++) {
						Order_Food_Detail food_Detail = food_Details.get(i);
						System.out.println("food detail id: " + food_Detail.getId());
						System.out.println("food detail status: " + food_Detail.getStatus());
						System.out.println("food detail count: " + food_Detail.getCount());
						System.out.println("food detail last modify time: " + food_Detail.getLast_modify_time());
						System.out.println("food detail food id: " + food_Detail.getFood_id());
						System.out.println("food detail order id: " + food_Detail.getOrder_id());
						
						Food food = food_Detail.getFood();
						if (food != null) {
							System.out.println("    " + "food id: " + food.getId());
							System.out.println("    " + "food name: " + food.getName());
							System.out.println("    " + "food status: " + food.getStatus());
							System.out.println("    " + "food price: " + food.getPrice());
							System.out.println("    " + "food category: " + food.getCategory());
						}
					}
				}

			} else {
				System.out.println("can not find order!");
			}
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void testSelectFullOrders() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			Order newOrder = new Order();
			newOrder.setAllowance(0.95);

			List<Order> orders = orderMapper.searchFullOrdersByFullOrderInfo(newOrder);
			
			if (orders != null && orders.size() > 0) {
				for (int j = 0; j < orders.size(); j++) {
					Order order = orders.get(j);
					System.out.println("order id: " + order.getId());
					System.out.println("order status: " + order.getStatus());
					System.out.println("order allowance: " + order.getAllowance());
					System.out.println("order PrePay: " + order.getPrePay());
					System.out.println("order Total_price: " + order.getTotal_price());
					System.out.println("order Submit_time: " + order.getSubmit_time());
					System.out.println("order Enabled: " + order.getEnabled());
					
					User user = order.getUser();
					if(user != null) {
						System.out.println("user id: " + user.getId());
						System.out.println("user name: " + user.getName());
						System.out.println("user sex: " + user.getSex());
						System.out.println("user department: " + user.getDepartment());
					}
					
					Customer customer = order.getCustomer();
					if(customer != null) {
						System.out.println("customer id: " + customer.getId());
						System.out.println("customer name: " + customer.getName());
						System.out.println("customer sex: " + customer.getSex());
					}
					
					List<Order_Table_Detail> table_Details = order.getTable_details();
					if (table_Details != null && table_Details.size() > 0) {
						for (int i = 0; i < table_Details.size(); i++) {
							Order_Table_Detail table_Detail = table_Details.get(i);
							System.out.println("table detail id: " + table_Detail.getId());
							System.out.println("table detail realCustomerCount: " + table_Detail.getRealCustomerCount());
							System.out.println("table detail table id: " + table_Detail.getTable_id());
							System.out.println("table detail order id: " + table_Detail.getOrder_id());
							
							Table table = table_Detail.getTable();
							if (table != null) {
								System.out.println("    " + "table id: " + table.getId());
								System.out.println("    " + "table status: " + table.getStatus());
								System.out.println("    " + "table min customer count: " + table.getMinCustomerCount());
								System.out.println("    " + "table address: " + table.getAddress());
								
							}
						}
					}
					
					List<Order_Food_Detail> food_Details = order.getFood_details();
					if (food_Details != null && food_Details.size() > 0) {
						for (int i = 0; i < food_Details.size(); i++) {
							Order_Food_Detail food_Detail = food_Details.get(i);
							System.out.println("food detail id: " + food_Detail.getId());
							System.out.println("food detail status: " + food_Detail.getStatus());
							System.out.println("food detail count: " + food_Detail.getCount());
							System.out.println("food detail last modify time: " + food_Detail.getLast_modify_time());
							System.out.println("food detail food id: " + food_Detail.getFood_id());
							System.out.println("food detail order id: " + food_Detail.getOrder_id());
							
							Food food = food_Detail.getFood();
							if (food != null) {
								System.out.println("    " + "food id: " + food.getId());
								System.out.println("    " + "food name: " + food.getName());
								System.out.println("    " + "food status: " + food.getStatus());
								System.out.println("    " + "food price: " + food.getPrice());
								System.out.println("    " + "food category: " + food.getCategory());
							}
						}
					}
				}
				
				
			}else {
				System.out.println("can not find orders!");
			}
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void testAdd() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
			Order newOrder = new Order();

			newOrder.setSubmit_time(new Date().getTime());
			newOrder.setEnabled(true);
			newOrder.setAllowance(0.98);
			newOrder.setStatus(1);
			newOrder.setModifyTime(new Date().getTime());

			orderMapper.insert(newOrder);

			sqlSession.commit();
		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void testGetOrder() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			Order newOrder = null;

			newOrder = orderMapper.selectByPrimaryKey((long) 1);
			System.out.println("id: " + newOrder.getId());
			System.out.println("status: " + newOrder.getStatus());
			System.out.println("allowance: " + newOrder.getAllowance());
			System.out.println("PrePay: " + newOrder.getPrePay());
			System.out.println("Total_price: " + newOrder.getTotal_price());
			System.out.println("Submit_time: " + newOrder.getSubmit_time());
			System.out.println("Enabled: " + newOrder.getEnabled());

		} finally {
			sqlSession.close();
		}
	}

	@Test
	public void testGetOrders() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			List<Order> resultOrders = null;
			Order templateOrder = new Order();
			templateOrder.setAllowance(0.95);

			resultOrders = orderMapper.searchOrders(templateOrder);

			if (resultOrders != null) {
				for (int i = 0; i < resultOrders.size(); i++) {
					Order tempOrder = resultOrders.get(i);
					System.out.println("id: " + tempOrder.getId());
					System.out.println("status: " + tempOrder.getStatus());
					System.out
							.println("allowance: " + tempOrder.getAllowance());
					System.out.println("modify time: "
							+ tempOrder.getModifyTime());
					System.out.println("submit time: "
							+ tempOrder.getSubmit_time());
					System.out.println("enabled: " + tempOrder.getEnabled());
				}
			}

		} finally {
			sqlSession.close();
		}
	}
	
	
}