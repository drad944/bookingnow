package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Food;


public interface IFoodService {
	
	boolean add(Food food);
	
	boolean removeFoodById(Long id);
	
	boolean remove(Food food);

	boolean modify(Food food);
	
	List<Food> searchFoods(Food food);
	
	
}