package com.pitaya.bookingnow.service.impl;

import com.pitaya.bookingnow.dao.FoodProcessMapper;
import com.pitaya.bookingnow.model.FoodProcess;
import com.pitaya.bookingnow.service.IFoodProcessService;

public class FoodProcessService implements IFoodProcessService{

	private FoodProcessMapper foodProcessDao;
	
	public FoodProcessMapper getFoodProcessDao() {
		return foodProcessDao;
	}

	public void setFoodProcessDao(FoodProcessMapper foodProcessDao) {
		this.foodProcessDao = foodProcessDao;
	}

	@Override
	public int deleteByPrimaryKey(Integer pid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(FoodProcess record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(FoodProcess record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FoodProcess selectByPrimaryKey(Integer pid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(FoodProcess record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(FoodProcess record) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
