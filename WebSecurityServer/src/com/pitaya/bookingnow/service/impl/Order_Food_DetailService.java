package com.pitaya.bookingnow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class Order_Food_DetailService implements IOrder_Food_DetailService{
	private Order_Food_DetailMapper food_detailDao;
	
	private FoodMapper foodDao;

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
			System.out.println("can not find food detail in client data");
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
					realFood_Detail.setStatus(food_detail.getStatus());
					realFood_Detail.setLast_modify_time(new Date().getTime());
						
					if (food_detailDao.updateByPrimaryKeySelective(realFood_Detail) == 1) {
						result.setExecuteResult(true);
					}else {
						throw new RuntimeException("failed to update food detail status in DB");
					}
					
				}else {
					result.getErrorDetails().put("food_status", "can not find food status in DB data");
				}
			}else {
				result.getErrorDetails().put("food_status", "can not find food status in client data");
			}
		}else {
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
		
		Map<String, List<Order_Food_Detail>> resultFoods = new HashMap<String, List<Order_Food_Detail>>();
				
		List<Order_Food_Detail> newFood_Details = null;
		List<Order_Food_Detail> deleteFood_Details = null;
		List<Order_Food_Detail> updateFood_Details = null;

		if (changeFoods != null && changeFoods.size() > 0 && orderId != null) {
			
			newFood_Details = changeFoods.get("newFoods");
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
								
								if (food_detailDao.insertSelective(tempNewFood_Detail) == 1) {
									result.setSubTrueCount(result.getSubTrueCount() + 1);
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
			
			deleteFood_Details = changeFoods.get("deleteFoods");
			if (deleteFood_Details != null && deleteFood_Details.size() > 0) {
				result.setSubFalseCount(result.getSubFalseCount() + deleteFood_Details.size());
				for (int i = 0; i < deleteFood_Details.size(); i++) {
					Order_Food_Detail tempDeleteFood_Detail = deleteFood_Details.get(i);
					if (tempDeleteFood_Detail != null && tempDeleteFood_Detail.getId() != null) {
						
						Food tempDeleteFood = tempDeleteFood_Detail.getFood();
						if (tempDeleteFood != null && tempDeleteFood.getId() != null) {
							Order_Food_Detail realFood_Detail = food_detailDao.selectFullByPrimaryKey(tempDeleteFood_Detail.getId());
							if (realFood_Detail != null) {
								Food realFood = realFood_Detail.getFood();
								if (realFood != null && realFood.getId() == tempDeleteFood.getId()) {
									
									if (food_detailDao.deleteByPrimaryKey(tempDeleteFood_Detail.getId()) == 1) {
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}else {
										throw new RuntimeException("failed to delete food detail in DB.");
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
			
			updateFood_Details = changeFoods.get("updateFoods");
			if (updateFood_Details != null && updateFood_Details.size() > 0) {
				result.setSubFalseCount(result.getSubFalseCount() + updateFood_Details.size());
				for (int i = 0; i < updateFood_Details.size(); i++) {
					Order_Food_Detail tempUpdateFood_Detail = updateFood_Details.get(i);
					if (tempUpdateFood_Detail != null && tempUpdateFood_Detail.getId() != null) {
						
						Food tempUpdateFood = tempUpdateFood_Detail.getFood();
						if (tempUpdateFood != null && tempUpdateFood.getId() != null) {
							Order_Food_Detail realFood_Detail = food_detailDao.selectFullByPrimaryKey(tempUpdateFood_Detail.getId());
							if (realFood_Detail != null) {
								Food realFood = realFood_Detail.getFood();
								if (realFood != null && realFood.getId() == tempUpdateFood.getId()) {
									
									if (food_detailDao.updateByPrimaryKeySelective(tempUpdateFood_Detail) == 1) {
										result.setSubTrueCount(result.getSubTrueCount() + 1);
									}else {
										throw new RuntimeException("failed to update food detail in DB.");
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
		}else {
			result.getErrorDetails().put("changeFoods_exist", "can not find changeFoods in client data.");
		}
		if (result.getSubTrueCount() == result.getSubFalseCount()) {
			result.setExecuteResult(true);
		}
		return result;
	}
}
