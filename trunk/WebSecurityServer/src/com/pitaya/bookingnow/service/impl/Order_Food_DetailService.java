package com.pitaya.bookingnow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.OrderMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.message.OrderDetailMessage;
import com.pitaya.bookingnow.service.socket.EnhancedMessageService;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public class Order_Food_DetailService implements IOrder_Food_DetailService{
	private static Log logger =  LogFactory.getLog(Order_Food_DetailService.class);
	
	private Order_Food_DetailMapper food_detailDao;
	
	private FoodMapper foodDao;
	private OrderMapper orderDao;
	private EnhancedMessageService messageService;

	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	public Order_Food_DetailMapper getFood_detailDao() {
		return food_detailDao;
	}

	public void setFood_detailDao(Order_Food_DetailMapper food_detailDao) {
		this.food_detailDao = food_detailDao;
	}
	
	public void setOrderDao(OrderMapper m){
		this.orderDao = m;
	}
	
	public OrderMapper getOrderDao(){
		return this.orderDao;
	}

	public void setMessageService(EnhancedMessageService ms){
		this.messageService = ms;
	}
	
	public EnhancedMessageService getMessageService(){
		return this.messageService;
	}
	
	@Override
	public boolean add(Order_Food_Detail food_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeOrder_Food_DetailById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Order_Food_Detail food_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(Order_Food_Detail food_detail) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Order_Food_Detail> searchOrder_Food_Details(Order_Food_Detail food_detail) {
		if (food_detail != null) {
				List<Order_Food_Detail> realFood_Details = food_detailDao.selectFullBySelective(food_detail);
				return realFood_Details;
		}else {
			logger.error("can not find food detail in client data");
		}
		return null;
	}
	
	@Override
	public List<Order_Food_Detail> searchFood_Details(SearchParams params) {
		if (params != null) {
			if(params.getFood_detail_id() != null 
					&& params.getFood_detailStatusList() != null && params.getFood_detailStatusList().size() > 0 
					&& params.getRowCount() != null){
				List<Order_Food_Detail> realFood_Details = food_detailDao.selectByParams(params);
				return realFood_Details;
			}else {
				logger.error("searchFood_Details:missing food_detail id in parameters");
			}
		}else {
			logger.error("searchFood_Details: missing parameters");
		}
		return null;
	}

	@Override
	public MyResult updateFoodStatus(Order_Food_Detail food_detail) {
		/*
		 * chef update food status to cooking,ready which is confirmed.
		 * in: food status,food_detail id 
		 */
		MyResult result = new MyResult();
		
		if (food_detail != null && food_detail.getId() != null) {
			if (food_detail.getStatus() != null) {
				Order_Food_Detail realFood_Detail = food_detailDao.selectByPrimaryKey(food_detail.getId());
				
				if (realFood_Detail != null && realFood_Detail.getStatus() != null) {
					/*
					 *  Comment out by runmeng, this time will be used to compute waiting time in UI,
					 *  so can not be update at this time
					 */
					//realFood_Detail.setLast_modify_time(new Date().getTime());
						
					if (food_detailDao.updateByPrimaryKeySelective(food_detail) == 1) {
						result.setFood_Detail(food_detail);
						result.setExecuteResult(true);
						Order order = orderDao.selectByPrimaryKey(realFood_Detail.getOrder_id());
						if(order != null){
							//Send message to notify user that the status update of cooking item
							List<Order_Food_Detail> detailist = new ArrayList<Order_Food_Detail>();
							Order_Food_Detail detail = new Order_Food_Detail();
							detail.setId(realFood_Detail.getId());
							detail.setOrder_id(realFood_Detail.getOrder_id());
							detail.setStatus(food_detail.getStatus());
							detailist.add(detail);
							OrderDetailMessage message = new OrderDetailMessage();
							message.setUpdateItems(detailist);
							this.messageService.sendMessageToOne(order.getUser_id(), message);
							//this.messageService.sendMessageToGroup(Constants.ROLE_CHEF, message);
						}
					} else {
						throw new RuntimeException("failed to update food detail status in DB");
					}
					
				} else {
					result.getErrorDetails().put("food_status", "can not find food status in DB data");
				}
			} else {
				result.getErrorDetails().put("food_status", "can not find food status in client data");
			}
		} else {
			result.getErrorDetails().put("food_detail_exist", "can not find food in client data");
		}
		return result;
	}
	
	@Override
	public MyResult updateFoods(Map<String, List<Order_Food_Detail>> changeFoods,Long orderId) {
		/*
		 * waiter update food list which customer want.
		 * in: food list,orderId 
		 */
		MyResult result = new MyResult();
		SearchParams params = new SearchParams();
				
		List<Order_Food_Detail> newFood_Details = null;
		List<Order_Food_Detail> deleteFood_Details = null;
		List<Order_Food_Detail> updateFood_Details = null;
		
//		Order resultOrder = new Order();
//		resultOrder.setId(orderId);
		OrderDetailMessage orderDetailUpdateMessage = new OrderDetailMessage();
		
		if (changeFoods != null && changeFoods.size() > 0 && orderId != null) {
			orderDetailUpdateMessage.setHasNew(false);
			newFood_Details = changeFoods.get("new");
			if (newFood_Details != null && newFood_Details.size() > 0) {
				result.setSubFalseCount(result.getSubFalseCount() + newFood_Details.size());
				for (int i = 0; i < newFood_Details.size(); i++) {
					Order_Food_Detail tempNewFood_Detail = newFood_Details.get(i);
					if (tempNewFood_Detail != null && tempNewFood_Detail.getFood() != null) {
						Food tempNewFood = tempNewFood_Detail.getFood();
						if (tempNewFood != null && tempNewFood.getId() != null) {
							Food realFood = foodDao.selectByPrimaryKey(tempNewFood.getId());
							if (realFood != null) {
								tempNewFood_Detail.setFood_id(tempNewFood.getId());
								tempNewFood_Detail.setLast_modify_time(new Date().getTime());
								tempNewFood_Detail.setOrder_id(orderId);
								if (tempNewFood_Detail.getEnabled() == null) {
									tempNewFood_Detail.setEnabled(true);
								}
								if (tempNewFood_Detail.getStatus() == null) {
									tempNewFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
								}
								
								if (food_detailDao.insertSelective(tempNewFood_Detail) == 1) {
									result.setSubTrueCount(result.getSubTrueCount() + 1);
									if(tempNewFood_Detail.getStatus() == Constants.FOOD_CONFIRMED){
										orderDetailUpdateMessage.setHasNew(true);
									}
								}else {
									throw new RuntimeException("failed to insert food detail in DB.");
								}
								
							}else {
								result.getErrorDetails().put("newFood_exist", "can not find new food in DB data.");
							}
							
						}else {
							result.getErrorDetails().put("newFood_exist", "can not find new food in client data.");
						}
						
					}else {
						result.getErrorDetails().put("newFood_Detail_exist", "can not find new food detail in client data.");
					}
					
				}
				
			}
			
			/*
			 * need add code to check delete and update is belong to order or not?
			 */
			
//			resultOrder.setFood_details(newFood_Details);
//			result.setOrder(resultOrder);
			
			List<Order_Food_Detail> removeItems = new ArrayList<Order_Food_Detail>();
			deleteFood_Details = changeFoods.get("delete");
			if (deleteFood_Details != null && deleteFood_Details.size() > 0) {
				result.setSubFalseCount(result.getSubFalseCount() + deleteFood_Details.size());
				for (int i = deleteFood_Details.size() - 1; i >= 0; i--) {
					Order_Food_Detail tempDeleteFood_Detail = deleteFood_Details.get(i);
					if (tempDeleteFood_Detail != null && tempDeleteFood_Detail.getId() != null) {
						Food tempDeleteFood = tempDeleteFood_Detail.getFood();
						if (tempDeleteFood != null && tempDeleteFood.getId() != null) {
							params.setFood_detail_id(tempDeleteFood_Detail.getId());
							params.setOrder_id(orderId);
							Order_Food_Detail realFood_Detail = food_detailDao.selectFullByPrimaryKeyAndOrderId(params);
							params.cleanup();
							if (realFood_Detail != null) {
								Food realFood = realFood_Detail.getFood();
								if (realFood != null && realFood.getId().equals(tempDeleteFood.getId())) {
									if(realFood_Detail.getStatus() == Constants.FOOD_NEW || realFood_Detail.getStatus() == Constants.FOOD_CONFIRMED) {
										if (food_detailDao.deleteByPrimaryKey(tempDeleteFood_Detail.getId()) == 1) {
											deleteFood_Details.remove(i);
											result.setSubTrueCount(result.getSubTrueCount() + 1);
											removeItems.add(tempDeleteFood_Detail);
										}else {
											throw new RuntimeException("failed to delete food detail in DB.");
										}
									} else {
										deleteFood_Details.set(i, realFood_Detail);
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}
								}else {
									result.getErrorDetails().put("deleteFood_exist", "can not find delete food in DB data.");
								}
							}else {
								result.getErrorDetails().put("deleteFood_Detail_exist", "can not find delete food detail in DB data.");
							}
							
						}else {
							result.getErrorDetails().put("deleteFood_exist", "can not find delete food in client data.");
						}
						
					}else {
						result.getErrorDetails().put("deleteFood_Detail_exist", "can not find delete food detail in client data.");
					}
					
				}
			}

			List<Order_Food_Detail> updateItems = new ArrayList<Order_Food_Detail>();
			updateFood_Details = changeFoods.get("update");
			if (updateFood_Details != null && updateFood_Details.size() > 0) {
				result.setSubFalseCount(result.getSubFalseCount() + updateFood_Details.size());
				for (int i = updateFood_Details.size() - 1; i >= 0 ; i--) {
					Order_Food_Detail tempUpdateFood_Detail = updateFood_Details.get(i);
					if (tempUpdateFood_Detail != null && tempUpdateFood_Detail.getId() != null) {
						
						Food tempUpdateFood = tempUpdateFood_Detail.getFood();
						if (tempUpdateFood != null && tempUpdateFood.getId() != null) {
							params.setFood_detail_id(tempUpdateFood_Detail.getId());
							params.setOrder_id(orderId);
							Order_Food_Detail realFood_Detail = food_detailDao.selectFullByPrimaryKeyAndOrderId(params);
							params.cleanup();
							if (realFood_Detail != null) {
								Food realFood = realFood_Detail.getFood();
								if (realFood != null && realFood.getId().equals(tempUpdateFood.getId())) {
									if(tempUpdateFood_Detail.getCount() >= realFood_Detail.getCount() ||
											(realFood_Detail.getStatus() == Constants.FOOD_NEW  || realFood_Detail.getStatus() == Constants.FOOD_CONFIRMED)) {
										if (food_detailDao.updateByPrimaryKeySelective(tempUpdateFood_Detail) == 1) {
											result.setSubTrueCount(result.getSubTrueCount() + 1);
											updateFood_Details.remove(i);
											updateItems.add(tempUpdateFood_Detail);
										} else {
											throw new RuntimeException("failed to update food detail in DB.");
										}
									} else {
										updateFood_Details.set(i, realFood_Detail);
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}
								}else {
									result.getErrorDetails().put("updateFood_exist", "can not find update food in DB data.");
								}
							}else {
								result.getErrorDetails().put("updateFood_Detail_exist", "can not find update food detail in DB data.");
							}
							
						}else {
							result.getErrorDetails().put("updateFood_exist", "can not find update food in client data.");
						}
						
					}else {
						result.getErrorDetails().put("updateFood_Detail_exist", "can not find update food detail in client data.");
					}
					
				}
			}
			if(orderDetailUpdateMessage.getHasNew() == true || updateItems.size() > 0 || removeItems.size() > 0){
				if(updateItems.size() > 0){
					orderDetailUpdateMessage.setUpdateItems(updateItems);
				}
				if(removeItems.size() > 0){
					orderDetailUpdateMessage.setRemoveItems(removeItems);
				}
				this.messageService.sendMessageToGroup(Constants.ROLE_CHEF, orderDetailUpdateMessage);
			}
		}else {
			result.getErrorDetails().put("changeFoods_exist", "can not find changeFoods or order id in client data.");
		}
		if (result.getSubTrueCount() == result.getSubFalseCount()) {
			result.setExecuteResult(true);
		}
		return result;
	}

	@Override
	public Order_Food_Detail searchFullByPrimaryKeyAndOrderId(
			SearchParams params) {
		if (params != null) {
			return food_detailDao.selectFullByPrimaryKeyAndOrderId(params);
		}
		return null;
	}
}
