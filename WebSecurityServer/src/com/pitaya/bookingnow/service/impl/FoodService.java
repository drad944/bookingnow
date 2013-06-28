package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.dao.Food_PictureMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.IFoodService;

public class FoodService implements IFoodService{

	private FoodMapper foodDao;
	
	private Food_PictureMapper food_pictureDao;

	public Food_PictureMapper getFood_pictureDao() {
		return food_pictureDao;
	}

	public void setFood_pictureDao(Food_PictureMapper food_pictureDao) {
		this.food_pictureDao = food_pictureDao;
	}

	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	@Override
	public boolean add(Food food) {
		if(food != null) {
			Food_Picture picture = food.getPicture();
			if(picture != null) {
				food_pictureDao.insertSelective(picture);
				
				if(picture.getId() != null && picture.getId() > 0) {
					food.setPicture_id(picture.getId());
				}
			}
			
			foodDao.insertSelective(food);
		}
		
		if(food.getId() != null && food.getId() > 0) {
			return true;
		}
		return false;
	}


	@Override
	public boolean remove(Food food) {
		if(food != null && food.getId() != null) {
			
			if(food.getPicture() != null && food.getPicture_id() != null) {
				food_pictureDao.deleteByPrimaryKey(food.getPicture_id());
    		}else if(food.getPicture() != null && food.getPicture().getId() != null) {
    			food_pictureDao.deleteByPrimaryKey(food.getPicture().getId());
    		}
    		
			int result = foodDao.deleteByPrimaryKey(food.getId());
			if(result > 0) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean modify(Food food) {
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
