package com.pitaya.bookingnow.service;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.util.MyResult;


public interface IFoodService {
	
	MyResult add(Food food);
	
	MyResult removeFoodById(Long id);
	
	MyResult disableFood(Food food);
	
	MyResult remove(Food food);

	MyResult modify(Food food);
	
	List<Food> searchFoods(Food food);
	List<Food> searchFoodsWithoutImage(Food food);
	
	List<Food> searchALLFoods();
	List<Food> searchALLFoodsWithoutImage();
	
	public Map<String, List<Food>> updateMenuFoods(List<Food> clientFoods);
	
	Food searchSmallPictureByFood(Food food);
	
	Food searchLargePictureByFood(Food food);
}