package com.pitaya.bookingnow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		if (id != null) {
			Food food = foodDao.selectByPrimaryKey(id);
			return this.remove(food);
		}
		return false;
	}
	
	public Map<String, List<Food>> updateMenuFoods(List<Food> clientFoods) {
		Map<String, List<Food>> newMenuFoods = new HashMap<String, List<Food>>();
		
		List<Food> newFoods = new ArrayList<Food>();
		List<Food> deleteFoods = new ArrayList<Food>();
		List<Food> updateFoods = new ArrayList<Food>();
		List<Food> allDBFoods = new ArrayList<Food>();
		
		for (int i = 0; i < clientFoods.size(); i++) {
			Food clientFood = clientFoods.get(i);
			if(clientFood.getId() != null) {
				Food databaseFood = foodDao.selectByPrimaryKey(clientFood.getId());
				if (databaseFood != null) {
					//check food between clientfood and databasefood
					if (databaseFood.getVersion() > clientFood.getVersion()) {
						
					}
				}else {
					//can not find in db,delete it
					deleteFoods.add(clientFood);
				}
			}
		}
		
		
		return newMenuFoods;
	}

}
