package com.pitaya.bookingnow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.OrderMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.dao.Order_Table_DetailMapper;
import com.pitaya.bookingnow.dao.TableMapper;
import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.entity.Order_Table_Detail;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;

public class OrderService implements IOrderService{

	private OrderMapper orderDao;
	private UserMapper userDao;
	private Order_Table_DetailMapper table_detailDao;
	private TableMapper tableDao;
	private FoodMapper foodDao;
	private Order_Food_DetailMapper food_detailDao;
	

	public Order_Table_DetailMapper getTable_detailDao() {
		return table_detailDao;
	}

	public void setTable_detailDao(Order_Table_DetailMapper table_detailDao) {
		this.table_detailDao = table_detailDao;
	}

	public Order_Food_DetailMapper getFood_detailDao() {
		return food_detailDao;
	}

	public void setFood_detailDao(Order_Food_DetailMapper food_detailDao) {
		this.food_detailDao = food_detailDao;
	}

	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	public TableMapper getTableDao() {
		return tableDao;
	}

	public void setTableDao(TableMapper tableDao) {
		this.tableDao = tableDao;
	}

	public UserMapper getUserDao() {
		return userDao;
	}

	public void setUserDao(UserMapper userDao) {
		this.userDao = userDao;
	}


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

	@Override
	public Map<String, String> addWaitingOrder(Order order) {
		Map<String, String> updateStatusMap = new HashMap<String, String>();
		
		try {
			//check user existed in client data and DB data
			if (order.getUser() != null && order.getUser().getId() != null) {
				User tempUser = userDao.selectByPrimaryKey(order.getUser().getId());
				if (tempUser != null && tempUser.getId() != null) {
					order.setUser_id(tempUser.getId());
					
					List<Order_Table_Detail> table_Details = order.getTable_details();
					
					if (table_Details != null && table_Details.size() > 0) {
						
						//insert order in DB
						order.setStatus(Constants.ORDER_NEW);
						order.setSubmit_time(new Date().getTime());
						order.setModifyTime(order.getSubmit_time());
						orderDao.insertSelective(order);
						updateStatusMap.put("order_status", "true");
						
						for (int i = 0; i < table_Details.size(); i++) {
							Order_Table_Detail tempTable_Detail = table_Details.get(i);
							
							//check table existed in client data and DB data
							if (tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
								Table tempTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
								if(tempTable != null && tempTable.getId() != null){
									
									//update table status in DB
									tempTable.setStatus(Constants.TABLE_USING);
									tableDao.updateByPrimaryKeySelective(tempTable);
									updateStatusMap.put("table_status_" + tempTable.getId().toString(), "true");
								
									//insert table_Detail in DB
									tempTable_Detail.setOrder_id(order.getId());
									tempTable_Detail.setTable_id(tempTable.getId());
									tempTable_Detail.setEnabled(true);
									table_detailDao.insertSelective(tempTable_Detail);
									updateStatusMap.put("table_detail_status", "true");
									
								}
							}
							
						}
					}
					
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("-------- addWaitingOrder failed in OrderService.");
		}
		
		return updateStatusMap;
	}



	@Override
	public Map<String, String> updateWaitingOrderToConfirmed(Order order) {
		Map<String, String> updateStatusMap = new HashMap<String, String>();
		double totalPrice = 0;
		
		//check user existed in client data and DB data
		if (order.getUser() != null && order.getUser().getId() != null) {
			User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
			if (realUser != null && realUser.getId() != null) {
				
				
				
				//check order existed in client data and DB data
				if(order.getId() != null) {
					Order realOrder = orderDao.selectByPrimaryKey(order.getId());
					if(realOrder != null && realOrder.getId() != null){
						
						//check customer existed in client data and DB data
//						if (order.getCustomer_count() != null && order.getCustomer_count() > 0) {
//							if (order.getCustomer_count() != realOrder.getCustomer_count()) {
//								
//							}
//						}
						
						List<Order_Food_Detail> food_Details = order.getFood_details();
						if (food_Details != null && food_Details.size() > 0) {
							
							//check food list existed in client data
							for (int i = 0; i < food_Details.size(); i++) {
								Order_Food_Detail tempFood_Detail = food_Details.get(i);
								
								//validate food count is not 0
								if (tempFood_Detail.getCount() != null && tempFood_Detail.getCount() > 0) {
									Food tempFood = tempFood_Detail.getFood();
									
									//check food existed in client data and DB data
									if (tempFood != null && tempFood.getId() != null) {
										Food realFood = foodDao.selectByPrimaryKey(tempFood.getId());
										if (realFood != null) {
											
											try {
												//update food status and insert in DB
												tempFood_Detail.setFood_id(tempFood.getId());
												tempFood_Detail.setEnabled(true);
												tempFood_Detail.setStatus(Constants.FOOD_COMFIRMED);
												tempFood_Detail.setOrder_id(order.getId());
												tempFood_Detail.setLast_modify_time(new Date().getTime());
												
												food_detailDao.insertSelective(tempFood_Detail);
												
												//total price increase and get success info of insert into DB table.
												totalPrice = totalPrice + realFood.getPrice();
												updateStatusMap.put("food_status_" + tempFood.getId().toString(), "true");
											} catch (Exception e) {
												//show error log
											}
											
										}
									}
								}
							}
						}
						
						//update order status in DB
						if(updateStatusMap.size() > 0) {
							try {
								order.setModifyTime(new Date().getTime());
								order.setStatus(Constants.ORDER_COMFIRMED);
								order.setTotal_price(totalPrice);
								orderDao.updateByPrimaryKeySelective(order);
								updateStatusMap.put("order_status", "true");
								updateStatusMap.put("order_total_price", "" + totalPrice);
							} catch (Exception e) {
								// TODO: handle exception
							}
							
						}else {
							updateStatusMap.put("order_status", "false");
						}
					}
				}
				
			}
		}
		
		
		/*
		 * updateStatusMap is empty,it means order is not exist.
		 * order_status is false in updateStatusMap,it means insert food list false
		 * 
		 */
		
		return updateStatusMap;
	}

	@Override
	public boolean addNewOrder(Order order) {
		if (order.getUser_id() != null) {
			//check waiter info is exist in order
			
			List<Order_Table_Detail> table_Details = order.getTable_details();
			
			if (table_Details != null && table_Details.size() > 0) {
				for (int i = 0; i < table_Details.size(); i++) {
					Order_Table_Detail table_Detail = table_Details.get(i);
					if(table_Detail.getTable_id() != null){
						//check table info is exist in order
						
						
						orderDao.insertSelective(order);
						
						table_Detail.setOrder_id(order.getId());
						
						table_detailDao.insertSelective(table_Detail);
						
						Table table = new Table();
						table.setId(table_Detail.getTable_id());
						table.setStatus(Constants.TABLE_USING);
						
						tableDao.updateByPrimaryKeySelective(table);
						
						
					}
				}
				return true;
			}
			
		}
		return false;
	}
}
