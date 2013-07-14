package com.pitaya.bookingnow.service.impl;

import java.util.Date;
import java.util.List;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class Order_Food_DetailService implements IOrder_Food_DetailService{
	private Order_Food_DetailMapper food_DetailDao;
	
	private FoodMapper foodDao;

	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	public Order_Food_DetailMapper getFood_DetailDao() {
		return food_DetailDao;
	}

	public void setFood_DetailDao(Order_Food_DetailMapper food_DetailDao) {
		this.food_DetailDao = food_DetailDao;
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
	public List<Order_Food_Detail> searchOrder_Food_Details(
			Order_Food_Detail food_detail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyResult updateFoodStatus(Order_Food_Detail food_detail) {
		/*
		 * chef update food status to cooking,ready which is confirmed.
		 * in: food status,food id 
		 */
		MyResult result = new MyResult();
		
		if (food_detail != null && food_detail.getFood() != null) {
			if (food_detail.getFood().getId() != null) {
				Order_Food_Detail realFood_Detail = food_DetailDao.selectFullByFoodId(food_detail.getFood().getId());
				if (realFood_Detail != null && realFood_Detail.getId() != null) {
					Food realFood = realFood_Detail.getFood();
					if (realFood != null) {
						if (realFood_Detail.getStatus()!= null && food_detail.getStatus() != null) {
							realFood_Detail.setStatus(food_detail.getStatus());
							realFood_Detail.setLast_modify_time(new Date().getTime());
								
							if (food_DetailDao.updateByPrimaryKeySelective(realFood_Detail) == 1) {
								result.setResult(true);
							}else {
								throw new RuntimeException("failed to update food detail status in DB");
							}
							
						}else {
							result.getResultDetails().put("food_status", "can not find confirmed food in DB data");
						}
					}else {
						result.getResultDetails().put("food_exist", "can not find food in DB data");
					}
				}else {
					result.getResultDetails().put("food_detail_exist", "can not find food in DB data");
				}
				
				
			}else {
				result.getResultDetails().put("food_exist", "can not find food in DB data");
			}
			
		}else {
			result.getResultDetails().put("food_detail_exist", "can not find food in client data");
		}
		return result;
	}

	
}
