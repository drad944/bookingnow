package com.pitaya.bookingnow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.dao.FoodMapper;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.service.IFoodService;
import com.pitaya.bookingnow.util.MyResult;

public class FoodService implements IFoodService{

	private FoodMapper foodDao;
	
	public FoodMapper getFoodDao() {
		return foodDao;
	}

	public void setFoodDao(FoodMapper foodDao) {
		this.foodDao = foodDao;
	}

	@Override
	public MyResult add(Food food) {
		MyResult result = new MyResult();
		
		if(food != null) {
			if(foodDao.insertSelective(food) == 1) {
				result.setExecuteResult(true);
				result.setFood(foodDao.selectFullByPrimaryKey(food.getId()));
			}else {
				throw new RuntimeException("fail to insert food into DB.");
			}
		}else {
			result.getErrorDetails().put("food_exist", "can not find food in client data.");
		}
		
		return result;
	}


	@Override
	public MyResult remove(Food food) {
		MyResult result = new MyResult();
		
		if(food != null && food.getId() != null) {
			if(foodDao.deleteByPrimaryKey(food.getId()) == 1) {
				result.setExecuteResult(true);
			}else {
				throw new RuntimeException("fail to delete food into DB.");
			}
		}else {
			result.getErrorDetails().put("food_exist", "can not find food in client data.");
		}
		
		return result;
	}

	@Override
	public MyResult modify(Food food) {
		MyResult result = new MyResult();
		
		if(food != null && food.getId() != null) {
			Long version = System.currentTimeMillis();
			food.setVersion(version);
			if(food.getLarge_image_relative_path() != null){
				//TODO save image
				food.setImage_version(version);
			}
			if(foodDao.updateByPrimaryKeySelective(food) == 1) {
				result.setExecuteResult(true);
				//result.setFood(foodDao.selectFullByPrimaryKey(food.getId()));
			}else {
				throw new RuntimeException("fail to update food into DB.");
			}
		}else {
			result.getErrorDetails().put("food_exist", "can not find food in client data.");
		}
		
		return result;
	}

	@Override
	public List<Food> searchFoods(Food food) {
		// TODO Auto-generated method stub
		return foodDao.selectFullFoods(food);
	}

	@Override
	public MyResult removeFoodById(Long id) {
		MyResult result = new MyResult();
		
		if(id != null) {
			if(foodDao.deleteByPrimaryKey(id) == 1) {
				result.setExecuteResult(true);
			}else {
				throw new RuntimeException("fail to delete food into DB.");
			}
		}else {
			result.getErrorDetails().put("food_exist", "can not find food in client data.");
		}
		
		return result;
	}
	
	public Map<String, List<Food>> updateMenuFoods(List<Food> clientFoods) {
		/*
		 * client update to lastest food menu when login or open client app
		 * 
		 */
		Map<String, List<Food>> newMenuFoods = new HashMap<String, List<Food>>();
		
		List<Food> newFoods = null;
		List<Food> deleteFoods = null;
		List<Food> updateFoods = new ArrayList<Food>();
		List<Food> allDBFoods = foodDao.selectAllFoods();

		
		for (int j = clientFoods.size() - 1; j >= 0; j--){
			Food clientFood = clientFoods.get(j);
			for(int i = allDBFoods.size()-1; i >= 0; i--){
				Food DBFood = allDBFoods.get(i);
				if(DBFood.getId().equals(clientFood.getId())){
					if(DBFood.getVersion() > clientFood.getVersion()){
						//this food need update
						//for one food,version is always >= image_version.
						//when food info changed,version change,image_version do nothing,version > image_version
						//when food picture changed,version and image_version change,version == image_version
						
						if (DBFood.getImage_version() >= clientFood.getImage_version()) {
							
						}else {
							DBFood.setImage_version(0L);
						}
						updateFoods.add(DBFood);
					}
					allDBFoods.remove(i);
					clientFoods.remove(j);
					break;
				}
			}
		}
		
		//the remain in allDBFoods should be of new food 
		newFoods = allDBFoods;
		
		//the remain in clientFoods should be of delete food
		deleteFoods = clientFoods;
		newMenuFoods.put("new", newFoods);
		newMenuFoods.put("update", updateFoods);
		newMenuFoods.put("delete", deleteFoods);
		
		return newMenuFoods;
	}
	
	

	@Override
	public List<Food> searchALLFoods() {
		return foodDao.selectAllFoods();
	}

	@Override
	public List<Food> searchFoodsWithoutImage(Food food) {
		/*
		 * chef get all food which need to cook
		 * in:food status:confirmed
		 * 
		 */
		
		return foodDao.selectFoodsWithoutImage(food);
	}

	@Override
	public List<Food> searchALLFoodsWithoutImage() {
		return foodDao.selectAllFoodsWithoutImage();
	}

	@Override
	public Food searchSmallPictureByFood(Food food) {
		if (food != null && food.getId() != null) {
			Food realFood = foodDao.selectSmallImageByFoodId(food.getId());
			if (realFood != null) {
				realFood.renderPicture();
				return realFood;
			}
		}
		return null;
	}

	@Override
	public Food searchLargePictureByFood(Food food) {
		if (food != null && food.getId() != null) {
			Food realFood = foodDao.selectLargeImageByFoodId(food.getId());
			if (realFood != null) {
				realFood.renderPicture();
				return realFood;
			}
		}
		return null;
	}

}
