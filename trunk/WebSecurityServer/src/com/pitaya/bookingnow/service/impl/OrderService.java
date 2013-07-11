package com.pitaya.bookingnow.service.impl;

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
	public MyResult addWaitingOrder(Order order) {
		/*
		 * waiter add a order for customers in table
		 * in:table_id,user_id,order status:new
		 * 
		 */
		
		MyResult result = new MyResult();
		
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
					if (orderDao.insertSelective(order) == 1) {
						
					}else {
						throw new RuntimeException("-------- failed to insert order in DB.");
					}
					
					for (int i = 0; i < table_Details.size(); i++) {
						Order_Table_Detail tempTable_Detail = table_Details.get(i);
						
						//check table existed in client data and DB data
						if (tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
							Table tempTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
							if(tempTable != null && tempTable.getId() != null){
								
								//update table status in DB
								tempTable.setStatus(Constants.TABLE_USING);
								
								if(tableDao.updateByPrimaryKeySelective(tempTable) == 1) {
									
								}else {
									throw new RuntimeException("-------- failed to update table in DB.");
								}
							
								//insert table_Detail in DB
								tempTable_Detail.setOrder_id(order.getId());
								tempTable_Detail.setTable_id(tempTable.getId());
								tempTable_Detail.setEnabled(true);
								if (table_detailDao.insertSelective(tempTable_Detail) == 1) {
									
								}else {
									throw new RuntimeException("-------- failed to insert table detail in DB.");
								}
								
								//store the count of true result in for loop
								result.setSubTrueCount(result.getSubTrueCount() + 1);
							}else {
								result.getResultDetails().put("table_exist", "can not find table in DB");
								break;
							}
							
						}else {
							result.getResultDetails().put("table_exist", "can not find table in client data");
							break;
						}
						
					}
					if (table_Details.size() == result.getSubTrueCount()) {
						result.setResult(true);
					}else {
						//do nothing
					}
					
					
				}else {
					result.getResultDetails().put("food_exist", "can not find detail in client data");
				}
				
			}else {
				result.getResultDetails().put("user_exist", "can not find in DB.");
			}
		}else{
			result.getResultDetails().put("user_exist", "can not find in client data");
		}
	
		return result;
	}



	@Override
	public MyResult updateWaitingOrderToConfirmed(Order order) {
		/*
		 * waiter update order to confirm  food list with customer which in table.
		 * in: food list,order_id,user_id,customer count,order status:confirmed,food status:confirmed
		 * 
		 */
		
		MyResult result = new MyResult();
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
						if((order.getCustomer_count() != null && order.getCustomer_count() <= 0)
								|| (realOrder.getCustomer_count() != null && realOrder.getCustomer_count() <= 0)) {
							//notice client need to set customer count
							result.getResultDetails().put("customer_count", "customer count is 0.");
						}else {
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
												if(realFood.getPrice() != null && tempFood.getPrice() != null) {
													if(realFood.getPrice().equals(tempFood.getPrice()) && tempFood.getPrice() > 0) {
														//update food status and insert in DB
														tempFood_Detail.setFood_id(tempFood.getId());
														tempFood_Detail.setEnabled(true);
														tempFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
														tempFood_Detail.setOrder_id(order.getId());
														tempFood_Detail.setLast_modify_time(new Date().getTime());
														
														if(food_detailDao.insertSelective(tempFood_Detail) == 1) {
															//total price increase and get success info of insert into DB table.
															totalPrice = totalPrice + realFood.getPrice() * tempFood_Detail.getCount();
															result.setSubTrueCount(result.getSubTrueCount() + 1);
														}else {
															//do nothing
														}
														
														
													}else if(tempFood.getPrice() == 0 || realFood.getPrice() == 0) {
														//notice user to check food price is 0 in client and in database.
														result.getResultDetails().put("food_price", "food price is 0 in client or DB");
													}else {
														//notice user to update food list for food price.
														result.getResultDetails().put("food_price", "food price is negative number in client or DB");
													}
												}else {
													result.getResultDetails().put("food_price", "food price is null in client or DB");
												}
												
													
												
											}else {
												result.getResultDetails().put("food_exist", "can not find food in DB.");
											}
										}else {
											result.getResultDetails().put("food_exist", "can not find food in client data");
										}
									}else {
										result.getResultDetails().put("food_count", "food count is 0");
									}
								}
								
								//update order status in DB
								if(food_Details.size() == result.getSubTrueCount()) {
										order.setModifyTime(new Date().getTime());
										order.setStatus(Constants.ORDER_CONFIRMED);
										order.setTotal_price(totalPrice);
										if (orderDao.updateByPrimaryKeySelective(order) == 1) {
											result.setResult(true);
										}
										
										//do we need to add food price in result.
									
								}else {
									result.getResultDetails().put("order_status", "can not update all food in DB");
								}
							}else {
								result.getResultDetails().put("food_detail", "food detail is empty");
							}
						}
						
					}else {
						result.getResultDetails().put("order_exist", "can not find order in DB.");
					}
				}else {
					result.getResultDetails().put("order_exist", "can not find order in client data");
				}
				
			}else {
				result.getResultDetails().put("user_exist", "can not find user in DB.");
			}
		}else {
			result.getResultDetails().put("user_exist", "can not find user in client data");
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
				Order realOrder = orderDao.selectByPrimaryKey(order.getId());
				
				if (order.getUser() != null && order.getUser().getId() != null) {
					
					
					List<Order_Food_Detail> tempFood_Details = order.getFood_details();
					if (tempFood_Details != null && tempFood_Details.size() > 0) {
						for (int i = 0; i < tempFood_Details.size(); i++) {
							Order_Food_Detail tempFood_Detail = tempFood_Details.get(i);
							if (tempFood_Detail != null && tempFood_Detail.getFood() != null && tempFood_Detail.getFood().getId() != null) {
								Order_Food_Detail realFood_Detail = food_detailDao.selectByFoodId(tempFood_Detail.getFood().getId());
								
								if (realFood_Detail != null && realFood_Detail.getId() != null) {
									Food realFood = foodDao.selectByPrimaryKey(tempFood_Detail.getFood().getId());
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
										result.getResultDetails().put("food_exist", "can not find food in DB data");
										break;
									}
								}else {
									result.getResultDetails().put("food_detail_exist", "can not find food detail in DB data");
									break;
								}
								
								
							}else {
								result.getResultDetails().put("food_exist", "can not find food in client data");
								break;
							}
						}
						if (result.getSubTrueCount() < tempFood_Details.size()) {
							throw new RuntimeException("can not update all food detail");
						}else {
							result.setSubTrueCount(0);
						}
						
						
					}else {
						result.getResultDetails().put("food_detail_exist", "can not find food detail in client data");
					}
					
					List<Order_Table_Detail> tempTable_Details = order.getTable_details();
					if (tempTable_Details != null && tempTable_Details.size() > 0) {
						for (int i = 0; i < tempTable_Details.size(); i++) {
							Order_Table_Detail tempTable_Detail = tempTable_Details.get(i);
							if (tempTable_Detail != null && tempTable_Detail.getTable() != null && tempTable_Detail.getTable().getId() != null) {
								Order_Table_Detail realTable_Detail = table_detailDao.selectByTableId(tempTable_Detail.getTable().getId());
								if (realTable_Detail != null && realTable_Detail.getId() != null) {
									Table realTable = tableDao.selectByPrimaryKey(tempTable_Detail.getTable().getId());
									if (realTable != null && realTable.getId() != null) {
										
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
										
									}else {
										result.getResultDetails().put("table_exist", "can not find table or it's id in DB.");
									}
									
								}else {
									result.getResultDetails().put("table_detail_exist", "can not find table or it's id in DB.");
								}
								
							}else {
								result.getResultDetails().put("table_detail_exist", "can not find table or it's id in client data");
							}
						}
						
						if (result.getSubTrueCount() < tempTable_Details.size()) {
							throw new RuntimeException("can not update all table detail");
						}else {
							result.setSubTrueCount(0);
						}
						
						realOrder.setEnabled(false);
						realOrder.setModifyTime(new Date().getTime());
						realOrder.setStatus(Constants.ORDER_FINISHED);
						if (orderDao.updateByPrimaryKeySelective(realOrder) == 1) {
							result.setResult(true);
							return result;
						}else {
							throw new RuntimeException("can not update order status.");
						}
						
					}else {
						result.getResultDetails().put("table_detail_exist", "can not find table detail in client data");
					}
					
					
				}else {
					result.getResultDetails().put("user_exist", "can not find user or user's id in client data");
				}
			}else {
				result.getResultDetails().put("order_exist", "can not find order or order's id in client data");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("update order to finished error.");
		}
		return result;
	}
	


	@Override
	public MyResult addNewOrder(Order order) {
		/*
		 * welcomer add a new order for waiting customer outside
		 * in:name,phone,customer count,order status:new
		 * 
		 */
		
		
		MyResult result = new MyResult();
		
		Customer tempCustomer = order.getCustomer();
		if (tempCustomer != null && tempCustomer.getId() != null) {
			Customer realCustomer = customerDao.selectByPrimaryKey(order.getCustomer().getId());
			if (realCustomer != null && realCustomer.getId() != null) {
				if (order.getCustomer_count() != null && order.getCustomer_count() > 0) {
					order.setCustomer_id(realCustomer.getId());
					order.setModifyTime(new Date().getTime());
					order.setStatus(Constants.ORDER_NEW);
					
					if(orderDao.insert(order) == 1) {
						result.setResult(true);
					}else {
						throw new RuntimeException("failed to exec method updateWaitingOrderToConfirmed in OrderService.java");
					}
					
				}else {
					result.getResultDetails().put("customer_count", "customer count is 0 in client data");
				}
			}else {
				result.getResultDetails().put("customer_exist", "can not find customer in DB.");
			}
			
		}else {
			if (tempCustomer != null && tempCustomer.getName() != null && tempCustomer.getPhone() != null) {
				
				if (order.getCustomer_count() != null && order.getCustomer_count() > 0) {
					tempCustomer.setAccount(tempCustomer.getName());
					
					if (customerDao.insert(tempCustomer) == 1) {
						
					}else {
						throw new RuntimeException("failed to insert customer in DB");
					}
					order.setCustomer_id(tempCustomer.getId());
					order.setSubmit_time(new Date().getTime());
					order.setModifyTime(order.getSubmit_time());
					order.setStatus(Constants.ORDER_NEW);
					
					
					if (orderDao.insert(order) == 1) {
						result.setResult(true);
						
					}else {
						throw new RuntimeException("failed to insert order in DB");
					}
					
				}else {
					result.getResultDetails().put("customer_count", "customer count is 0 in client data");
				}
				
			}else if (tempCustomer != null && tempCustomer.getName() == null) {
				result.getResultDetails().put("customer_exist", "can not find customer name in client data");
			}else {
				result.getResultDetails().put("customer_exist", "can not find customer phone in client data");
			}
		}
			
		return result;
	}
	
	@Override
	public MyResult updateNewOrderToWaiting(Order order){
		/*
		 * welcomer update order with food list when customer is waiting table and choose food
		 * in:order_id,customer_id,food list,order status:waiting,food status:new
		 * 
		 */
		MyResult result = new MyResult();
		
		double totalPrice = 0;
		//check user existed in client data and DB data
		if (order.getUser() != null && order.getUser().getId() != null) {
			User realUser = userDao.selectByPrimaryKey(order.getUser().getId());
			if (realUser != null && realUser.getId() != null) {
				
				//check order existed in client data and DB data
				if(order.getId() != null) {
					Order realOrder = orderDao.selectByPrimaryKey(order.getId());
					if(realOrder != null && realOrder.getId() != null){
						
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
											if(realFood.getPrice() != null && tempFood.getPrice() != null) {
												if(realFood.getPrice().equals(tempFood.getPrice()) && tempFood.getPrice() > 0) {
													//update food status and insert in DB
													tempFood_Detail.setFood_id(tempFood.getId());
													tempFood_Detail.setEnabled(true);
													tempFood_Detail.setStatus(Constants.FOOD_NEW);
													tempFood_Detail.setOrder_id(order.getId());
													tempFood_Detail.setLast_modify_time(new Date().getTime());
													
													if(food_detailDao.insertSelective(tempFood_Detail) == 1) {
														//total price increase and get success info of insert into DB table.
														totalPrice = totalPrice + realFood.getPrice() * tempFood_Detail.getCount();
														result.setSubTrueCount(result.getSubTrueCount() + 1);
													}else {
														//do nothing
													}
													
													
												}else if(tempFood.getPrice() == 0 || realFood.getPrice() == 0) {
													//notice user to check food price is 0 in client and in database.
													result.getResultDetails().put("food_price", "food price is 0 in client or DB");
												}else {
													//notice user to update food list for food price.
													result.getResultDetails().put("food_price", "food price is negative number in client or DB");
												}
											}else {
												result.getResultDetails().put("food_price", "food price is null in client or DB");
											}
											
												
											
										}else {
											result.getResultDetails().put("food_exist", "can not find food in DB.");
										}
									}else {
										result.getResultDetails().put("food_exist", "can not find food in client data");
									}
								}else {
									result.getResultDetails().put("food_count", "food count is 0");
								}
							}
							
							//update order status in DB
							if(food_Details.size() == result.getSubTrueCount()) {
									order.setModifyTime(new Date().getTime());
									order.setStatus(Constants.ORDER_WAITING);
									order.setTotal_price(totalPrice);
									if (orderDao.updateByPrimaryKeySelective(order) == 1) {
										result.setResult(true);
									}
									
									//do we need to add food price in result.
								
							}else {
								result.getResultDetails().put("order_status", "can not update all food in DB");
							}
						}else {
							result.getResultDetails().put("food_detail", "food detail is empty");
						}
						
					}else {
						result.getResultDetails().put("order_exist", "can not find order in DB.");
					}
				}else {
					result.getResultDetails().put("order_exist", "can not find order in client data");
				}
				
			}else {
				result.getResultDetails().put("user_exist", "can not find user in DB.");
			}
		}else {
			result.getResultDetails().put("user_exist", "can not find user in client data");
		}
		
		return result;
	}
	

	
	@Override
	public MyResult updateNewOrderToConfirmed(Order order) {
		/*
		 * waiter update order to take customer to table to seat
		 * in:table_id,order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		
		MyResult result = new MyResult();
		// check order existed in client data and DB data.
		if (order != null && order.getId() != null) {
			Order realOrder = orderDao.selectByPrimaryKey(order.getId());
			
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
										result.getResultDetails().put("table_exist", "can not find table in DB");
										break;
									}
									
								}else {
									result.getResultDetails().put("table_exist", "can not find table in client data");
									break;
								}
								
							}
							if (table_Details.size() == result.getSubTrueCount()) {
								//update order in DB
								realOrder.setStatus(Constants.ORDER_CONFIRMED);
								realOrder.setModifyTime(new Date().getTime());
								if (orderDao.updateByPrimaryKeySelective(realOrder) == 1) {
									
								}else {
									throw new RuntimeException("-------- failed to insert order in DB.");
								}
								
								result.setResult(true);
							}else {
								//do nothing
							}
							
						}else {
							result.getResultDetails().put("table_exist", "can not find table detail in client data");
						}
						
					}else {
						result.getResultDetails().put("user_exist", "can not find in DB.");
					}
				}else{
					result.getResultDetails().put("user_exist", "can not find in client data");
				}
			}else {
				result.getResultDetails().put("order_exist", "can not find order or order id in DB data");
			}
			
		}else {
			result.getResultDetails().put("order_exist", "can not find order or order id in client data");
		}
		
		return result;
	}
	
	@Override
	public MyResult updateFoodInConfirmedOrder(Order order) {
		/*
		 * waiter update order to update food list after customer seat in table and want to modify food list.
		 * in:order_id,user_id,order status:confirmed,food status:confirmed
		 * 
		 */
		MyResult result = new MyResult();
		
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
		MyResult result = new MyResult();
		Order realOrder =  null;
		if (order != null && order.getId() != null) {
			realOrder = orderDao.selectFullOrderByPrimaryKey(order.getId());
			if (realOrder != null && realOrder.getId() != null) {
				if (realOrder.getCustomer_count() != null) {
					//plus tableware * customer count
					result.setTotalPriceOfOrder(result.getTotalPriceOfOrder() + realOrder.getCustomer_count() * 5);
					
					List<Order_Table_Detail> realTable_Details = realOrder.getTable_details();
					
					if (realTable_Details != null && realTable_Details.size() > 0) {
						for (int i = 0; i < realTable_Details.size(); i++) {
							Order_Table_Detail realTable_Detail = realTable_Details.get(i);
							if (realTable_Detail.getTable() != null && realTable_Detail.getTable().getIndoorPrice() != null) {
								//plus indoor price
								result.setTotalPriceOfOrder(result.getTotalPriceOfOrder() + realTable_Detail.getTable().getIndoorPrice());
							}else {
								result.setTotalPriceOfOrder(-1);
								result.getResultDetails().put("indoor_price_exist", "can not find indoor price in DB data");
								return result;
							}
						}
						
					}else {
						result.setTotalPriceOfOrder(-1);
						result.getResultDetails().put("table_detail_exist", "can not find table detail in DB data");
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
										result.getResultDetails().put("food_count_negative", "food count or price is a negative number in DB data");
										return result;
									}
								}else {
									result.setTotalPriceOfOrder(-1);
									result.getResultDetails().put("food_price_exist", "can not find food price in DB data");
									return result;
								}
							}
						}
						
					}else {
						result.setTotalPriceOfOrder(-1);
						result.getResultDetails().put("food_detail_exist", "can not find food detail in DB data");
						return result;
					}
					
					if (realOrder.getAllowance() != null && realOrder.getAllowance() >= 0 && realOrder.getAllowance() <= 1) {
						result.setTotalPriceOfOrder(result.getTotalPriceOfOrder() * realOrder.getAllowance());
						result.setResult(true);
						return result;
					}else {
						result.setTotalPriceOfOrder(-1);
						result.getResultDetails().put("order_allowance", "order allowance is a invalid number a in DB data");
						return result;
					}
				}else {
					result.setTotalPriceOfOrder(-1);
					result.getResultDetails().put("customer_count", "can not find customer count id in DB data");
					return result;
				}
			}else {
				result.setTotalPriceOfOrder(-1);
				result.getResultDetails().put("order_exist", "can not find order or order id in DB data");
				return result;
			}
			
		}else {
			result.setTotalPriceOfOrder(-1);
			result.getResultDetails().put("order_exist", "can not find order or order id in client data");
			return result;
		}
	}

	@Override
	public Order searchOrderById(Long id) {
		Order realOrder= orderDao.selectFullOrderByPrimaryKey(id);
		return realOrder;
	}

	@Override
	public List<Order> searchOrdersByCustomer(Customer customer) {
		if (customer != null) {
			return orderDao.searchOrdersByCustomer(customer);
		}
		return null;
	}

}
