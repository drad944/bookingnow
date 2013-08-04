package com.pitaya.bookingnow.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import com.pitaya.bookingnow.dao.CustomerMapper;
import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.OrderMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.dao.Order_Table_DetailMapper;
import com.pitaya.bookingnow.dao.TableMapper;
import com.pitaya.bookingnow.dao.security.UserMapper;
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

public class OrderService implements IOrderService{

	private OrderMapper orderDao;
	private UserMapper userDao;
	private Order_Table_DetailMapper table_detailDao;
	private TableMapper tableDao;
	private FoodMapper foodDao;
	private Order_Food_DetailMapper food_detailDao;
	private CustomerMapper customerDao;
	

	public CustomerMapper getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerMapper customerDao) {
		this.customerDao = customerDao;
	}

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
		List<Order> orders = orderDao.searchOrders(order);
		
		return orders;
	}

	@Override
	public MyResult addNewOrder(Order order) {
		/*
		 * waiter add a order for customers in table
		 * in:table_id,user_id,order status:new
		 * 
		 */
		
		MyResult result = new MyResult();
		
		//check user existed in client data and DB data
		if (order != null && order.getUser() != null && order.getUser().getId() != null) {
			User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
			if (realUser != null && realUser.getId() != null) {
				order.setUser_id(realUser.getId());
				
				List<Order_Table_Detail> tempTable_Details = order.getTable_details();
				
				if (tempTable_Details != null && tempTable_Details.size() > 0) {
					
					//insert order in DB
					order.setStatus(Constants.ORDER_NEW);
					order.setSubmit_time(new Date().getTime());
					order.setModifyTime(order.getSubmit_time());
					if (orderDao.insertSelective(order) == 1) {
						
					}else {
						throw new RuntimeException("-------- failed to insert order in DB.");
					}
					
					for (int i = 0; i < tempTable_Details.size(); i++) {
						Order_Table_Detail tempTable_Detail = tempTable_Details.get(i);
						
						//check table existed in client data and DB data
						if (tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
							Table realTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
							if(realTable != null && realTable.getId() != null){
								
								//update table status in DB
								realTable.setStatus(Constants.TABLE_USING);
								
								if(tableDao.updateByPrimaryKeySelective(realTable) == 1) {
									
								}else {
									throw new RuntimeException("-------- failed to update table in DB.");
								}
							
								//insert table_Detail in DB
								tempTable_Detail.setOrder_id(order.getId());
								tempTable_Detail.setTable_id(realTable.getId());
								tempTable_Detail.setEnabled(true);
								if (table_detailDao.insertSelective(tempTable_Detail) == 1) {
									
								}else {
									throw new RuntimeException("-------- failed to insert table detail in DB.");
								}
								
								//store the count of true result in for loop
								result.setSubTrueCount(result.getSubTrueCount() + 1);
							}else {
								result.getErrorDetails().put("table_exist", "can not find table in DB");
								break;
							}
							
						}else {
							result.getErrorDetails().put("table_exist", "can not find table in client data");
							break;
						}
						
					}
					if (tempTable_Details.size() == result.getSubTrueCount()) {
						result.setExecuteResult(true);
						result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(order.getId()));
					}else {
						//do nothing
					}
					
					
				}else {
					result.getErrorDetails().put("food_exist", "can not find detail in client data");
				}
				
			}else {
				result.getErrorDetails().put("user_exist", "can not find in DB.");
			}
		}else{
			result.getErrorDetails().put("user_exist", "can not find in client data");
		}
	
		return result;
	}



	@Override		
	public MyResult updateNewOrderToConfirmed(Order order) {
		/*
		 * waiter update order to confirm  food list with customer which in table.
		 * in: food list,order_id,order status:confirmed,food status:confirmed
		 * 
		 */
		
		MyResult result = new MyResult();
				
		//check order existed in client data and DB data
		if(order.getId() != null) {
			Order realOrder = orderDao.selectByPrimaryKey(order.getId());
			if(realOrder != null && realOrder.getId() != null){
				
				List<Order_Food_Detail> tempFood_Details = order.getFood_details();
				if (tempFood_Details != null && tempFood_Details.size() > 0) {
					
					//check food list existed in client data
					for (int i = 0; i < tempFood_Details.size(); i++) {
						Order_Food_Detail tempFood_Detail = tempFood_Details.get(i);
						
						//validate food count is not 0
						if (tempFood_Detail.getCount() != null && tempFood_Detail.getCount() > 0) {
							Food tempFood = tempFood_Detail.getFood();
							
							//check food existed in client data and DB data
							if (tempFood != null && tempFood.getId() != null) {
								Food realFood = foodDao.selectByPrimaryKey(tempFood.getId());
								if (realFood != null) {
									if(realFood.getVersion() != null && tempFood.getVersion() != null) {
										if(realFood.getVersion().equals(tempFood.getVersion())) {
											//update food status and insert in DB
											tempFood_Detail.setFood_id(tempFood.getId());
											tempFood_Detail.setEnabled(true);
											tempFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
											tempFood_Detail.setOrder_id(order.getId());
											tempFood_Detail.setLast_modify_time(new Date().getTime());
											
											if(food_detailDao.insertSelective(tempFood_Detail) == 1) {
												//total price increase and get success info of insert into DB table.
												result.setSubTrueCount(result.getSubTrueCount() + 1);
											}else {
												//do nothing
											}
											
											
										}else {
											//notice user to update food list for food price.
											result.getErrorDetails().put("food_version", "food version is not the same between client and DB");
										}
									}else {
										result.getErrorDetails().put("food_version", "food version is null in client or DB");
									}
									
								}else {
									result.getErrorDetails().put("food_exist", "can not find food in DB.");
								}
							}else {
								result.getErrorDetails().put("food_exist", "can not find food in client data");
							}
						}else {
							result.getErrorDetails().put("food_count", "food count is 0");
						}
					}
					
					//update order status in DB
					if(tempFood_Details.size() == result.getSubTrueCount()) {
							order.setModifyTime(new Date().getTime());
							order.setStatus(Constants.ORDER_COMMITED);
							if (realOrder.getAllowance() == null && order.getAllowance() == null) {
								order.setAllowance(1.0);
							}
							if (orderDao.updateByPrimaryKeySelective(order) == 1) {
								result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(order.getId()));
								result.setExecuteResult(true);
							}
						
					}else {
						result.getErrorDetails().put("order_status", "can not update all food in DB");
					}
				}else {
					result.getErrorDetails().put("food_detail", "food detail is empty");
				}
			}else {
				result.getErrorDetails().put("order_exist", "can not find order in DB.");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order in client data");
		}
				
		
		return result;
	}
	
	
	
	
	@Override
	public MyResult updateOrderToFinished(Order order) {
		/*
		 * cashier update order status and all food status to finished,table status to empty if customer pay for the order
		 * in:order_id,user_id,order status:finished
		 * 
		 */
		MyResult result = new MyResult();
		try {
			if (order != null && order.getId() != null) {
				Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
				
				if (order.getUser() != null && order.getUser().getId() != null) {
					
					
					List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
					if (realFood_Details != null && realFood_Details.size() > 0) {
						for (int i = 0; i < realFood_Details.size(); i++) {
							Order_Food_Detail realFood_Detail = realFood_Details.get(i);
								
							if (realFood_Detail != null && realFood_Detail.getId() != null) {
								Food realFood = realFood_Detail.getFood();
								if (realFood != null && realFood.getId() != null) {
									
									realFood_Detail.setEnabled(false);
									realFood_Detail.setLast_modify_time(new Date().getTime());
									realFood_Detail.setStatus(Constants.FOOD_FINISHED);
									if (food_detailDao.updateByPrimaryKeySelective(realFood_Detail) == 1) {
										result.setSubTrueCount(result.getSubTrueCount() + 1);
										
									}else {
										throw new RuntimeException("can not update food detail.");
									}
									
								}else {
									result.getErrorDetails().put("food_exist", "can not find food in DB data");
									break;
								}
							}else {
								result.getErrorDetails().put("food_detail_exist", "can not find food detail in DB data");
								break;
							}
					
						}
						if (result.getSubTrueCount() < realFood_Details.size()) {
							throw new RuntimeException("can not update all food detail");
						}else {
							result.setSubTrueCount(0);
						}
						
					}else {
						result.getErrorDetails().put("food_detail_exist", "can not find food detail in client data");
					}
					
					List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
					if (realTable_Details != null && realTable_Details.size() > 0) {
						for (int i = 0; i < realTable_Details.size(); i++) {
							Order_Table_Detail realTable_Detail = realTable_Details.get(i);
							if (realTable_Detail != null && realTable_Detail.getId() != null) {
								Table realTable = realTable_Detail.getTable();
								if (realTable != null && realTable.getId() != null) {
									if (realTable.getStatus() == Constants.TABLE_USING) {
										realTable.setStatus(Constants.TABLE_EMPTY);
										if (tableDao.updateByPrimaryKeySelective(realTable) == 1) {
											
											realTable_Detail.setEnabled(false);
											if (table_detailDao.updateByPrimaryKeySelective(realTable_Detail) == 1) {
												result.setSubTrueCount(result.getSubTrueCount() + 1);
											}else {
												throw new RuntimeException("failed to update table detail status");
											}
											
										}else {
											throw new RuntimeException("failed to update table status");
										}
									}
									
								}else {
									result.getErrorDetails().put("table_exist", "can not find table or it's id in DB.");
								}
								
							}else {
								result.getErrorDetails().put("table_detail_exist", "can not find table or it's id in DB.");
							}
						}
						
						if (result.getSubTrueCount() < realTable_Details.size()) {
							throw new RuntimeException("can not update all table detail");
						}else {
							result.setSubTrueCount(0);
						}
						
						realOrder.setEnabled(false);
						realOrder.setModifyTime(new Date().getTime());
						realOrder.setStatus(Constants.ORDER_FINISHED);
						if (orderDao.updateByPrimaryKeySelective(realOrder) == 1) {
							result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(realOrder.getId()));
							result.setExecuteResult(true);
						}else {
							throw new RuntimeException("can not update order status.");
						}
						
					}else {
						result.getErrorDetails().put("table_detail_exist", "can not find table detail in client data");
					}
				}else {
					result.getErrorDetails().put("user_exist", "can not find user or user's id in client data");
				}
			}else {
				result.getErrorDetails().put("order_exist", "can not find order or order's id in client data");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("update order to finished error.");
		}
		return result;
	}
	


	@Override
	public MyResult addWaitingOrder(Order order) {
		/*
		 * welcomer add a new order for waiting customer outside
		 * in:name,phone,customer count,order status:new
		 * 
		 */
		
		
		MyResult result = new MyResult();
		
		if (order != null) {
			if (order.getUser() != null && order.getUser().getId() != null) {
				User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
				if (realUser != null) {
					order.setUser_id(realUser.getId());
					
					Customer tempCustomer = order.getCustomer();
					if (tempCustomer != null && tempCustomer.getId() != null) {
						Customer realCustomer = customerDao.selectByPrimaryKey(order.getCustomer().getId());
						if (realCustomer != null && realCustomer.getId() != null) {
							if (order.getCustomer_count() != null && order.getCustomer_count() > 0) {
								order.setCustomer_id(realCustomer.getId());
								order.setModifyTime(new Date().getTime());
								order.setStatus(Constants.ORDER_WELCOMER_NEW);
								
								if(orderDao.insert(order) == 1) {
									result.setExecuteResult(true);
								}else {
									throw new RuntimeException("failed to exec method updateWaitingOrderToConfirmed in OrderService.java");
								}
								
							}else {
								result.getErrorDetails().put("customer_count", "customer count is 0 in client data");
							}
						}else {
							result.getErrorDetails().put("customer_exist", "can not find customer in DB.");
						}
						
					}else {
						if (tempCustomer != null && tempCustomer.getName() != null && tempCustomer.getPhone() != null) {
							
							if (order.getCustomer_count() != null && order.getCustomer_count() > 0) {
								tempCustomer.setAccount(tempCustomer.getName());
								tempCustomer.setEnabled(true);
								if (customerDao.insert(tempCustomer) == 1) {
									
								}else {
									throw new RuntimeException("failed to insert customer in DB");
								}
								order.setCustomer_id(tempCustomer.getId());
								order.setSubmit_time(new Date().getTime());
								order.setModifyTime(order.getSubmit_time());
								order.setStatus(Constants.ORDER_WELCOMER_NEW);
								
								
								if (orderDao.insert(order) == 1) {
									result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(order.getId()));
									result.setExecuteResult(true);
									
								}else {
									throw new RuntimeException("failed to insert order in DB");
								}
								
							}else {
								result.getErrorDetails().put("customer_count", "customer count is 0 in client data");
							}
							
						}else if (tempCustomer != null && tempCustomer.getName() == null) {
							result.getErrorDetails().put("customer_exist", "can not find customer name in client data");
						}else {
							result.getErrorDetails().put("customer_exist", "can not find customer phone in client data");
						}
					}
					
				}else {
					result.getErrorDetails().put("user_exist", "can not find user info in DB data");
				}
			}else {
				result.getErrorDetails().put("user_exist", "can not find user info in client data");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order info in client data");
		}
		
			
		return result;
	}
	
	@Override
	public MyResult updateFoodsOfWaitingOrder(Order order){
		/*
		 * welcomer update order with food list when customer is waiting table and choose food
		 * in:order_id,user_id,customer_id,food list,order status:waiting,food status:new
		 * 
		 */
		MyResult result = new MyResult();
		//check order existed in client data and DB data
		if(order != null && order.getId() != null) {
			//check user existed in client data and DB data
			if (order.getUser() != null && order.getUser().getId() != null) {
				List<Order_Food_Detail> tempFood_Details = order.getFood_details();
				if (tempFood_Details != null && tempFood_Details.size() > 0) {
				
					Order realOrder = orderDao.selectByPrimaryKey(order.getId());
					if(realOrder != null && realOrder.getId() != null){
				
						User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
						if (realUser != null && realUser.getId() != null) {
							
							//check food list existed in client data
							for (int i = 0; i < tempFood_Details.size(); i++) {
								Order_Food_Detail tempFood_Detail = tempFood_Details.get(i);
								
								//validate food count is not 0
								if (tempFood_Detail.getCount() == null) {
									tempFood_Detail.setCount(1);
								}
								Food tempFood = tempFood_Detail.getFood();
								
								//check food existed in client data and DB data
								if (tempFood != null && tempFood.getId() != null) {
									Food realFood = foodDao.selectByPrimaryKey(tempFood.getId());
									if (realFood != null) {
										if(realFood.getVersion() != null && tempFood.getVersion() != null) {
											if(realFood.getVersion().equals(tempFood.getVersion()) && tempFood.getVersion() > 0) {
												//update food status and insert in DB
												tempFood_Detail.setFood_id(tempFood.getId());
												tempFood_Detail.setEnabled(true);
												tempFood_Detail.setStatus(Constants.FOOD_NEW);
												tempFood_Detail.setOrder_id(order.getId());
												tempFood_Detail.setLast_modify_time(new Date().getTime());
												
												if(food_detailDao.insertSelective(tempFood_Detail) == 1) {
													//total price increase and get success info of insert into DB table.
													result.setSubTrueCount(result.getSubTrueCount() + 1);
												}else {
													//do nothing
												}
												
											}else {
												//notice user to update food list for food price.
												result.getErrorDetails().put("food_version", "food version is not the same between client and DB");
											}
										}else {
											result.getErrorDetails().put("food_price", "food price is null in client or DB");
										}
										
									}else {
										result.getErrorDetails().put("food_exist", "can not find food in DB.");
									}
								}else {
									result.getErrorDetails().put("food_exist", "can not find food in client data");
								}
							}
							
							//update order status in DB
							if(tempFood_Details.size() == result.getSubTrueCount()) {
									order.setModifyTime(new Date().getTime());
									order.setStatus(Constants.ORDER_WAITING);
									if (orderDao.updateByPrimaryKeySelective(order) == 1) {
										result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(order.getId()));
										result.setExecuteResult(true);
									}
									
									//do we need to add food price in result.
								
							}else {
								result.getErrorDetails().put("order_status", "can not update all food in DB");
							}
						}else {
							//result.getErrorDetails().put("food_detail", "food detail is empty");
							result.getErrorDetails().put("user_exist", "can not find user in DB data");
						}
						
					}else {
						result.getErrorDetails().put("order_exist", "can not find order in DB data");
					}
				}else {
					result.getErrorDetails().put("food_detail", "food detail is empty");
					
				}
			}else {
				result.getErrorDetails().put("user_exist", "can not find user in client data");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order in client data");
		}
		
		return result;
	}
	

	
	@Override
	public MyResult updateWaitingOrderToConfirmed(Order order) {
		/*
		 * waiter update order to take customer to table to seat
		 * in:table_id,order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		
		MyResult result = new MyResult();
		// check order existed in client data and DB data.
		if (order != null && order.getId() != null) {
			Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			
			if (realOrder != null && realOrder.getId() != null) {
				//check user existed in client data and DB data
				if (order.getUser() != null && order.getUser().getId() != null) {
					User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
					if (realUser != null && realUser.getId() != null) {
						realOrder.setUser_id(realUser.getId());
						
						List<Order_Table_Detail> table_Details = order.getTable_details();
						
						if (table_Details != null && table_Details.size() > 0) {
							for (int i = 0; i < table_Details.size(); i++) {
								Order_Table_Detail tempTable_Detail = table_Details.get(i);
								
								//check table existed in client data and DB data
								if (tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
									Table realTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
									if(realTable != null && realTable.getId() != null){
										
										//update table status in DB
										if (realTable.getStatus() == Constants.TABLE_EMPTY) {
											realTable.setStatus(Constants.TABLE_USING);
										}else {
											throw new RuntimeException("-------- failed to update table status,table is not empty now.");
										}
										
										if(tableDao.updateByPrimaryKeySelective(realTable) == 1) {
											
										}else {
											throw new RuntimeException("-------- failed to update table in DB.");
										}
									
										//insert table_Detail in DB
										tempTable_Detail.setOrder_id(order.getId());
										tempTable_Detail.setTable_id(realTable.getId());
										tempTable_Detail.setEnabled(true);
										if (table_detailDao.insertSelective(tempTable_Detail) == 1) {
											
										}else {
											throw new RuntimeException("-------- failed to insert table detail in DB.");
										}
										
										//store the count of true result in for loop
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}else {
										result.getErrorDetails().put("table_exist", "can not find table in DB");
										break;
									}
									
								}else {
									result.getErrorDetails().put("table_exist", "can not find table in client data");
									break;
								}
								
							}
							if (table_Details.size() == result.getSubTrueCount()) {
								//update food status in DB.
								if (realOrder.getFood_details() != null && realOrder.getFood_details().size() > 0) {
									for (int i = 0; i < realOrder.getFood_details().size(); i++) {
										Order_Food_Detail realFood_Detail = realOrder.getFood_details().get(i);
										if (realFood_Detail.getStatus() == Constants.FOOD_NEW) {
											realFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
											if (food_detailDao.updateByPrimaryKeySelective(realFood_Detail) == 1) {
												
											}else {
												throw new RuntimeException("-------- can not update food status in DB.");
											}
										}
									}
								}else {
									throw new RuntimeException("-------- can not find food in DB.");
								}
								
								
								//update order in DB
								realOrder.setStatus(Constants.ORDER_COMMITED);
								realOrder.setModifyTime(new Date().getTime());
								if (orderDao.updateByPrimaryKeySelective(realOrder) == 1) {
									result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(realOrder.getId()));
									result.setExecuteResult(true);
								}else {
									throw new RuntimeException("-------- failed to insert order in DB.");
								}
								
							}else {
								//do nothing
							}
							
						}else {
							result.getErrorDetails().put("table_exist", "can not find table detail in client data");
						}
						
					}else {
						result.getErrorDetails().put("user_exist", "can not find in DB.");
					}
				}else{
					result.getErrorDetails().put("user_exist", "can not find in client data");
				}
			}else {
				result.getErrorDetails().put("order_exist", "can not find order or order id in DB data");
			}
			
		}else {
			result.getErrorDetails().put("order_exist", "can not find order or order id in client data");
		}
		
		return result;
	}
	
	@Override
	public MyResult updateFoodsInConfirmedOrder(Order order) {
		/*
		 * waiter update order to update food list after customer seat in table and want to modify food list.
		 * in:order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		MyResult result = new MyResult();
		if(order != null && order.getId() != null) {
			Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			if(realOrder != null && realOrder.getId() != null){
				
				if (order.getUser() != null && order.getUser().getId() != null) {
					User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
					if (realUser != null && realUser.getId() != null) {
						
						//check order existed in client data and DB data
								
						List<Order_Food_Detail> tempFood_Details = order.getFood_details();
						List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
						
						if (tempFood_Details != null && tempFood_Details.size() > 0) {
							
							//check food list existed in client data
							for (int i = 0; i < tempFood_Details.size(); i++) {
								Order_Food_Detail tempFood_Detail = tempFood_Details.get(i);
								
								if (tempFood_Detail.getStatus() != null && tempFood_Detail.getStatus() == Constants.FOOD_NEW) {
									//add new food
									Food tempFood = tempFood_Detail.getFood();
									
									//check food existed in client data and DB data
									if (tempFood != null && tempFood.getId() != null) {
										Food realFood = foodDao.selectByPrimaryKey(tempFood.getId());
										if (realFood != null) {
											if(realFood.getVersion() != null && tempFood.getVersion() != null) {
												if(realFood.getVersion().equals(tempFood.getVersion())) {
													//update food status and insert in DB
													tempFood_Detail.setFood_id(tempFood.getId());
													tempFood_Detail.setEnabled(true);
													tempFood_Detail.setOrder_id(order.getId());
													tempFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
													tempFood_Detail.setCount(1);
													tempFood_Detail.setLast_modify_time(new Date().getTime());
													
													if(food_detailDao.insertSelective(tempFood_Detail) == 1) {
														//total price increase and get success info of insert into DB table.
														result.setSubTrueCount(result.getSubTrueCount() + 1);
													}else {
														//do nothing
													}
													
												}else{
													//notice user to check food price is 0 in client and in database.
													result.getErrorDetails().put("food_version", "food version is not the same between client and DB");
												}
											}else {
												result.getErrorDetails().put("food_version", "food version is null in client or DB");
											}
											
												
											
										}else {
											result.getErrorDetails().put("food_exist", "can not find food in DB.");
										}
									}else {
										result.getErrorDetails().put("food_exist", "can not find food in client data");
									}
								}else if(tempFood_Detail.getStatus() != null && tempFood_Detail.getStatus() == Constants.FOOD_UNAVAILABLE){
									//remove food which have been booked.
									
									if (realFood_Details != null && realFood_Details.size() > 0) {
										int realFoodSize = realFood_Details.size();
										for (int j = 0; j < realFoodSize; j++) {
											if (tempFood_Detail.getFood() != null && tempFood_Detail.getFood().getId()!= null && tempFood_Detail.getFood().getId().equals(realFood_Details.get(j).getFood().getId())
													&& realFood_Details.get(j).getStatus() == Constants.FOOD_CONFIRMED) {
												if (food_detailDao.deleteByPrimaryKey(realFood_Details.get(j).getId()) == 1) {
													realFood_Details.remove(j);
													result.setSubTrueCount(result.getSubTrueCount() + 1);
													break;
												}else {
													throw new RuntimeException("failed to delete food in DB.");
												}
												
											}
										}
										if (realFoodSize == (realFood_Details.size() + 1 )) {
											
										}else {
											result.getErrorDetails().put("food_status", "some food is cooking,can not delete it");
										}
									}else {
										throw new RuntimeException("can not find food list in DB.");
									}
								}else {
									result.getErrorDetails().put("food_count", "food count is 0");
								}
							}
							if (tempFood_Details.size() == result.getSubTrueCount()) {
								result.setOrder(orderDao.selectFullOrderByPrimaryKey(order.getId()));
								result.setExecuteResult(true);
							}else {
								throw new RuntimeException("can not find food list in DB.");
							}
							
						}else {
							result.getErrorDetails().put("food_detail", "food detail is empty");
						}
						
					}else {
						result.getErrorDetails().put("user_exist", "can not find user in DB.");
					}
				}else {
					result.getErrorDetails().put("user_exist", "can not find user in client data");
				}	
			}else {
				result.getErrorDetails().put("order_exist", "can not find order in DB.");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order in client data");
		}
		
		return result;
	}
	
	@Override
	public MyResult cancelOrder(Order order) {
		/*
		 * welcomer cancel order which is order status is new,waiting,
		 * in:order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		MyResult result = new MyResult();
		if(order != null && order.getId() != null) {
			Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			if(realOrder != null && realOrder.getId() != null){
				if ((realOrder.getStatus() != null && realOrder.getStatus() == Constants.ORDER_NEW)
						|| (realOrder.getStatus() != null && realOrder.getStatus() == Constants.ORDER_WAITING)){
					
					//check order existed in client data and DB data
					List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
					
					if (realFood_Details != null && realFood_Details.size() > 0) {
						
						//check food list existed in client data
						for (int i = 0; i < realFood_Details.size(); i++) {
							Order_Food_Detail realFood_Detail = realFood_Details.get(i);
							
							//validate food count is not 0
							if (realFood_Detail != null && realFood_Detail.getId() != null){
								//add new food
								Food realFood = realFood_Detail.getFood();
								
								//check food existed in client data and DB data
								if (realFood != null && realFood.getId() != null) {
									realFood_Detail.setEnabled(false);
									realFood_Detail.setStatus(Constants.FOOD_UNAVAILABLE);
									if(food_detailDao.updateByPrimaryKeySelective(realFood_Detail) == 1) {
										//total price increase and get success info of insert into DB table.
										
									}else {
										throw new RuntimeException("failed to delete food detail in DB.");
									}
								}else {
									result.getErrorDetails().put("food_exist", "can not find food in client data");
								}
							}else {
								result.getErrorDetails().put("food_detail_exist", "can not find food detail or id in DB.");
							}
						}
					}else {
						result.getErrorDetails().put("food_details_exist", "can not find food details in DB.");
					}
					
					List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
					if (realTable_Details != null && realTable_Details.size() > 0) {
						for (int i = 0; i < realTable_Details.size(); i++) {
							Order_Table_Detail realTable_Detail = realTable_Details.get(i);
							if (realTable_Detail != null && realTable_Detail.getId() != null) {
								Table realTable = realTable_Detail.getTable();
								if (realTable != null && realTable.getId() != null) {
									realTable.setStatus(Constants.TABLE_EMPTY);
									if (tableDao.updateByPrimaryKeySelective(realTable) == 1) {
										realTable_Detail.setEnabled(false);
										
										if (table_detailDao.updateByPrimaryKeySelective(realTable_Detail) == 1) {
											result.setSubTrueCount(result.getSubTrueCount() + 1);
										}else {
											throw new RuntimeException("failed to delete table detail in DB.");
										}
									}else {
										throw new RuntimeException("failed to update table to empty status in DB.");
									}
								}else {
									result.getErrorDetails().put("table_exist", "can not find table in DB data");
								}
								
							} else {
								result.getErrorDetails().put("table_detail_exist", "can not find table detail or id in DB.");
							}
						}
						
						
					}else {
						result.getErrorDetails().put("table_details_exist", "can not find table details in DB.");
					}
					
					Customer realCustomer = realOrder.getCustomer();
					if (realCustomer != null && realCustomer.getId() != null) {
						realCustomer.setEnabled(false);
						if (customerDao.updateByPrimaryKeySelective(realCustomer) == 1) {
							
						}else {
							throw new RuntimeException("failed to update customer to unavailable in DB.");
						}
					}else {
						result.getErrorDetails().put("customer_exist", "can not find customer in DB.");
					}
					
					order.setEnabled(false);
					order.setStatus(Constants.ORDER_UNAVAILABLE);
					if (orderDao.updateByPrimaryKeySelective(order) == 1) {
						result.setExecuteResult(true);
					}else {
						throw new RuntimeException("can not delete order in DB.");
					}
				}else {
					result.getErrorDetails().put("order_status", "can not cancel order in DB because order status is not new or waiting.");
				}
			}else {
				result.getErrorDetails().put("order_exist", "can not find order in DB.");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order in client data");
		}
		return result;
	}
	
	@Override
	public MyResult deleteOrder(Order order) {
		/*
		 * welcomer cancel order which is order status is new,waiting,
		 * in:order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		MyResult result = new MyResult();
		if(order != null && order.getId() != null) {
			Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			if(realOrder != null && realOrder.getId() != null){
				if ((realOrder.getStatus() != null && realOrder.getStatus() == Constants.ORDER_UNAVAILABLE)
						|| (realOrder.getStatus() != null && realOrder.getStatus() == Constants.ORDER_FINISHED)){
					
					//check order existed in client data and DB data
					List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
					
					if (realFood_Details != null && realFood_Details.size() > 0) {
						
						//check food list existed in client data
						for (int i = 0; i < realFood_Details.size(); i++) {
							Order_Food_Detail realFood_Detail = realFood_Details.get(i);
							
							//validate food count is not 0
							if (realFood_Detail != null && realFood_Detail.getId() != null){
								//add new food
								Food realFood = realFood_Detail.getFood();
								
								//check food existed in client data and DB data
								if (realFood != null && realFood.getId() != null) {
									if(food_detailDao.deleteByPrimaryKey(realFood_Detail.getId()) == 1) {
										//total price increase and get success info of insert into DB table.
										
									}else {
										throw new RuntimeException("failed to delete food detail in DB.");
									}
								}else {
									result.getErrorDetails().put("food_exist", "can not find food in client data");
								}
							}else {
								result.getErrorDetails().put("food_detail_exist", "can not find food detail or id in DB.");
							}
						}
					}else {
						result.getErrorDetails().put("food_details_exist", "can not find food details in DB.");
					}
					
					List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
					if (realTable_Details != null && realTable_Details.size() > 0) {
						for (int i = 0; i < realTable_Details.size(); i++) {
							Order_Table_Detail realTable_Detail = realTable_Details.get(i);
							if (realTable_Detail != null && realTable_Detail.getId() != null) {
								Table realTable = realTable_Detail.getTable();
								if (realTable != null && realTable.getId() != null) {
									realTable.setStatus(Constants.TABLE_EMPTY);
									if (tableDao.updateByPrimaryKeySelective(realTable) == 1) {
										
										if (table_detailDao.deleteByPrimaryKey(realTable_Detail.getId()) == 1) {

										}else {
											throw new RuntimeException("failed to delete table detail in DB.");
										}
									}else {
										throw new RuntimeException("failed to update table to empty status in DB.");
									}
								}else {
									result.getErrorDetails().put("table_exist", "can not find table in DB data");
								}
								
							} else {
								result.getErrorDetails().put("table_detail_exist", "can not find table detail or id in DB.");
							}
						}
						
						
					}else {
						result.getErrorDetails().put("table_details_exist", "can not find table details in DB.");
					}
					
					Customer realCustomer = realOrder.getCustomer();
					if (realCustomer != null && realCustomer.getId() != null) {
						if (customerDao.deleteByPrimaryKey(realCustomer.getId()) == 1) {
							
						}else {
							throw new RuntimeException("failed to update customer to unavailable in DB.");
						}
					}else {
						result.getErrorDetails().put("customer_exist", "can not find customer in DB.");
					}
					
					if (orderDao.deleteByPrimaryKey(order.getId()) == 1) {
						result.setExecuteResult(true);
					}else {
						throw new RuntimeException("can not delete order in DB.");
					}
				}else {
					result.getErrorDetails().put("order_status", "can not cancel order in DB because order status is not new or waiting.");
				}
			}else {
				result.getErrorDetails().put("order_exist", "can not find order in DB.");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order in client data");
		}
		return result;
	}
	
	@Override
	public MyResult cancelFoods(Order order) {
		/*
		 * waiter cancel food list which customer do not want to eat and chef do not cook
		 * in:order_id,user_id,food list
		 * 
		 */
		MyResult result = new MyResult();
		
		return result;
	}
	
	@Override
	public MyResult updateOrder(Order order) {
		/*
		 * customer and waiter update food list,table
		 * in:order_id,user_id,food list or table
		 * 
		 */
		MyResult result = new MyResult();
		
		return result;
	}

	@Override
	public MyResult calculateOrder(Order order) {
		//use another sevice method to finished order,set all table to empty and closed all food details.close order.
		//updateOrderToFinished
		/*
		 * total price = (each table.indoorprice + each unfree food.price * food.count ) * order.allowance
		 */
		MyResult result = new MyResult();
		Order realOrder =  null;
		if (order != null && order.getId() != null) {
			realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			if (realOrder != null && realOrder.getId() != null) {
					
				List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
				
				if (realTable_Details != null && realTable_Details.size() > 0) {
					for (int i = 0; i < realTable_Details.size(); i++) {
						Order_Table_Detail realTable_Detail = realTable_Details.get(i);
						if (realTable_Detail.getTable() != null && realTable_Detail.getTable().getIndoorPrice() != null) {
							//plus indoor price
							result.setTotalPriceOfOrder(result.getTotalPriceOfOrder() + realTable_Detail.getTable().getIndoorPrice());
							//use another sevice method to finished order,set all table to empty and closed all food details.close order.
						}else {
							result.setTotalPriceOfOrder(-1);
							result.getErrorDetails().put("indoor_price_exist", "can not find indoor price in DB data");
							return result;
						}
					}
					
				}else {
					result.setTotalPriceOfOrder(-1);
					result.getErrorDetails().put("table_detail_exist", "can not find table detail in DB data");
					return result;
				}
				
				List<Order_Food_Detail> realFood_Details = realOrder.getFood_details();
				if (realFood_Details != null && realFood_Details.size() > 0) {
					for (int i = 0; i < realFood_Details.size(); i++) {
						Order_Food_Detail realFood_Detail = realFood_Details.get(i);
						if (realFood_Detail.getIsFree() != null && realFood_Detail.getIsFree() == true) {
							result.setSubTrueCount(result.getSubTrueCount() + 1);
						}else {
							if (realFood_Detail.getCount() != null && realFood_Detail.getFood() != null && realFood_Detail.getFood().getPrice() != null) {
								if (realFood_Detail.getCount() > 0 && realFood_Detail.getFood().getPrice() > 0) {
									//plus food price * food count
									result.setTotalPriceOfOrder(result.getTotalPriceOfOrder() + realFood_Detail.getCount() * realFood_Detail.getFood().getPrice());
									result.setSubTrueCount(result.getSubTrueCount() + 1);
								}else {
									result.setTotalPriceOfOrder(-1);
									result.getErrorDetails().put("food_count_negative", "food count or price is a negative number in DB data");
									return result;
								}
							}else {
								result.setTotalPriceOfOrder(-1);
								result.getErrorDetails().put("food_price_exist", "can not find food price in DB data");
								return result;
							}
						}
					}
					
				}else {
					result.setTotalPriceOfOrder(-1);
					result.getErrorDetails().put("food_detail_exist", "can not find food detail in DB data");
					return result;
				}
				
				if (realOrder.getAllowance() != null && realOrder.getAllowance() >= 0 && realOrder.getAllowance() <= 1) {
					result.setTotalPriceOfOrder(new Double(new DecimalFormat(".00").format(result.getTotalPriceOfOrder() * realOrder.getAllowance())));
					realOrder.setTotal_price(result.getTotalPriceOfOrder());
					result.setOrder(realOrder);
					result.setExecuteResult(true);
					
					order.setTotal_price(result.getTotalPriceOfOrder());
					orderDao.updateByPrimaryKeySelective(order);
					
					return result;
				}else {
					result.setTotalPriceOfOrder(-1);
					result.getErrorDetails().put("order_allowance", "order allowance is a invalid number a in DB data");
					return result;
				}
				
			}else {
				result.setTotalPriceOfOrder(-1);
				result.getErrorDetails().put("order_exist", "can not find order or order id in DB data");
				return result;
			}
			
		}else {
			result.setTotalPriceOfOrder(-1);
			result.getErrorDetails().put("order_exist", "can not find order or order id in client data");
			return result;
		}
	}

	@Override
	public Order searchOrderById(Long id) {
		if (id != null) {
			Order realOrder= orderDao.selectFoodsInOrderByPrimaryKey(id);
			return realOrder;
		}
		return null;
	}

	@Override
	public List<Order> searchFullOrdersByFullOrder(Order order) {
		return orderDao.searchFullOrdersByFullOrderInfo(order);
	}

	@Override
	public List<Order> searchOrdersByFullOrder(Order order) {
		return orderDao.searchOrdersByFullOrderInfo(order);
	}

	@Override
	public List<Order> searchFullOrders(SearchParams params) {
		return orderDao.searchFullOrders(params);
	}

	@Override
	public List<Order> searchFullOrdersWithoutFoods(SearchParams params) {
		return orderDao.searchFullOrdersWithoutFoods(params);
	}

	@Override
	public MyResult updateTablesOfWaitingOrder(Order order) {
		/*
		 * waiter take customer which serviced by welcomer to seat in table,but not book foods.
		 * in:table_id,order_id,user_id,order status:new
		 * 
		 */
		
		MyResult result = new MyResult();
		// check order existed in client data 
		if (order != null && order.getId() != null) {
			if (order.getUser() != null && order.getUser().getId() != null) {
				Order realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
				
				if (realOrder != null && realOrder.getId() != null) {
					User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
					if (realUser != null && realUser.getId() != null) {
						realOrder.setUser_id(realUser.getId());
						
						List<Order_Table_Detail> table_Details = order.getTable_details();
						
						if (table_Details != null && table_Details.size() > 0) {
							for (int i = 0; i < table_Details.size(); i++) {
								Order_Table_Detail tempTable_Detail = table_Details.get(i);
								
								//check table existed in client data and DB data
								if (tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
									Table realTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
									if(realTable != null && realTable.getId() != null){
										
										//update table status in DB
										if (realTable.getStatus() == Constants.TABLE_EMPTY) {
											realTable.setStatus(Constants.TABLE_USING);
										}else {
											throw new RuntimeException("-------- failed to update table status,table is not empty now.");
										}
										
										if(tableDao.updateByPrimaryKeySelective(realTable) == 1) {
											
										}else {
											throw new RuntimeException("-------- failed to update table in DB.");
										}
									
										//insert table_Detail in DB
										tempTable_Detail.setOrder_id(order.getId());
										tempTable_Detail.setTable_id(realTable.getId());
										tempTable_Detail.setEnabled(true);
										if (table_detailDao.insertSelective(tempTable_Detail) == 1) {
											
										}else {
											throw new RuntimeException("-------- failed to insert table detail in DB.");
										}
										
										//store the count of true result in for loop
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}else {
										result.getErrorDetails().put("table_exist", "can not find table in DB");
										break;
									}
									
								}else {
									result.getErrorDetails().put("table_exist", "can not find table in client data");
									break;
								}
								
							}
							if (table_Details.size() == result.getSubTrueCount()) {
								
								//update order in DB
								realOrder.setStatus(Constants.ORDER_NEW);
								realOrder.setModifyTime(new Date().getTime());
								if (orderDao.updateByPrimaryKeySelective(realOrder) == 1) {
									result.setOrder(orderDao.selectMinFullOrderByPrimaryKey(realOrder.getId()));
									result.setExecuteResult(true);
								}else {
									throw new RuntimeException("-------- failed to insert order in DB.");
								}
								
							}else {
								//do nothing
							}
							
						}else {
							result.getErrorDetails().put("table_exist", "can not find table detail in client data");
						}
						
					}else {
						result.getErrorDetails().put("user_exist", "can not find in DB.");
					}
					
				}else {
					result.getErrorDetails().put("order_exist", "can not find order or order id in DB data");
				}
			}else{
				result.getErrorDetails().put("user_exist", "can not find in client data");
			}
		}else {
			result.getErrorDetails().put("order_exist", "can not find order or order id in client data");
		}
		
		return result;
	}

}
