package com.pitaya.bookingnow.service.impl;

import java.util.Date;
import java.util.List;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.Order_Food_DetailMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
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
						result.setResult(true);
					}else {
						throw new RuntimeException("failed to update food detail status in DB");
					}
					
				}else {
					result.getResultDetails().put("food_status", "can not find food status in DB data");
				}
			}else {
				result.getResultDetails().put("food_status", "can not find food status in client data");
			}
		}else {
			result.getResultDetails().put("food_detail_exist", "can not find food in client data");
		}
		return result;
	}
	
}
