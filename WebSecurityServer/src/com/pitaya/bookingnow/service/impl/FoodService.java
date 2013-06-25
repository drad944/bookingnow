package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.service.IFoodService;

public class FoodService implements IFoodService{

	private FoodMapper foodDao;

	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	@Override
	public boolean add(com.pitaya.bookingnow.entity.Food food) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean remove(com.pitaya.bookingnow.entity.Food food) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modify(com.pitaya.bookingnow.entity.Food food) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Food> searchFoods(Food food) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeFoodById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
