package com.pitaya.bookingnow.service.impl;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.model.Food;
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
	public int deleteByPrimaryKey(Integer fid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Food record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(Food record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Food selectByPrimaryKey(Integer fid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(Food record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(Food record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(Food record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
