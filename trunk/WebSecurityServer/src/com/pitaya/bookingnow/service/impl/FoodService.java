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
		int result = foodDao.updateByPrimaryKeySelective(food);
		if(result > 0 ) {
			return true;
		}
		return false;
	}

	@Override
	public List<Food> searchFoods(Food food) {
		// TODO Auto-generated method stub
		return foodDao.selectFoods(food);
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
		
		List<Food> newFoods = null;
		List<Food> deleteFoods = null;
		List<Food> updateFoods = new ArrayList<Food>();
		List<Food> allDBFoods = foodDao.selectAllFoodsWithoutImage();

		
		for (int j = clientFoods.size() - 1; j >= 0; j--){
			Food clientFood = clientFoods.get(j);
			for(int i = allDBFoods.size()-1; i >= 0; i--){
				Food DBFood = allDBFoods.get(i);
				if(DBFood.getId().equals(clientFood.getId())){
					if(DBFood.getVersion() > clientFood.getVersion()){
						if(DBFood.getPicture().getVersion() > clientFood.getPicture().getVersion()) {
							
						}else {
							//do not return picture object in food
							DBFood.setPicture(null);
							
						}
						updateFoods.add(DBFood);
					}
					allDBFoods.remove(i);
					clientFoods.remove(j);
					break;
				}
			}
		}
		
//		for (int i = 0; i < allDBFoods.size(); i++) {
//			Food DBFood = allDBFoods.get(i);
//			
//			 
//			for (int j = 0; j < clientFoods.size(); j++) {
//				Food clientFood = clientFoods.get(j);
//				
//				if(clientFood.getId().equals(DBFood.getId())) {
//					//find the same id of food
//					if (DBFood.getVersion().equals(clientFood.getVersion())) {
//						//version is the same,do nothing
//						allDBFoods.remove(i);
//						clientFoods.remove(j);
//						break;
//					} else if (DBFood.getVersion() > clientFood.getVersion()) {
//						//get need update food
//						updateFoods.add(DBFood);
//						allDBFoods.remove(i);
//						clientFoods.remove(j);
//						break;
//					}
//				}
//				
//				
//			}
//		}
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
		return foodDao.selectFoodsWithoutImage(food);
	}

	@Override
	public List<Food> searchALLFoodsWithoutImage() {
		return foodDao.selectAllFoodsWithoutImage();
	}

}
