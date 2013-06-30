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

	@Test
	public void testSelectFullOrderByPrimaryKey() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			Order newOrder = null;

			newOrder = orderMapper.selectFullOrderByPrimaryKey((long) 1);

			if (newOrder != null) {

				System.out.println("order id: " + newOrder.getId());
				System.out.println("status: " + newOrder.getStatus());
				System.out.println("allowance: " + newOrder.getAllowance());
				System.out.println("PrePay: " + newOrder.getPrePay());
				System.out.println("Total_price: " + newOrder.getTotal_price());
				System.out.println("Submit_time: " + newOrder.getSubmit_time());
				System.out.println("Enabled: " + newOrder.getEnabled());
				System.out.println();

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

			List<Order> orders = orderMapper.selectFullOrders(newOrder);
			
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
						System.out.println("user picture id: " + user.getPicture_id());
					}
					
					Customer customer = order.getCustomer();
					if(customer != null) {
						System.out.println("customer id: " + customer.getId());
						System.out.println("customer name: " + customer.getName());
						System.out.println("customer sex: " + customer.getSex());
						System.out.println("customer picture id: " + customer.getPicture_id());
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
								System.out.println("    " + "table seat count: " + table.getSeatCount());
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
								System.out.println("    " + "food picture id: " + food.getPicture_id());
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