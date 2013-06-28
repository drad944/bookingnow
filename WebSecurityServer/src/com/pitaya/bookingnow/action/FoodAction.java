package com.pitaya.bookingnow.action;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.service.IFoodService;

public class FoodAction extends BaseAction{
	private static final long serialVersionUID = -675955597630577906L;
	private IFoodService foodService;
	private Food food;
	
	public IFoodService getFoodService() {
		return foodService;
	}
	public void setFoodService(IFoodService foodService) {
		this.foodService = foodService;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	
	public String searchFood() {
		if (food != null) {
			foodService.searchFoods(food);
			
			return "findSuccess";
		}
		
		return "findFail";
	}
	
	public String addFood() {
		if (food != null) {
			foodService.add(food);
			
			return "addSuccess";
		}
		
		return "addFail";
	}
	
	public String removeFood() {
		if (food != null) {
			foodService.removeFoodById(food.getId());
			
			return "removeSuccess";
		}
		
		return "removeFail";
	}
	
	public String updateFood() {
		if (food != null) {
			foodService.modify(food);
			
			return "updateSuccess";
		}
		
		return "updateFail";
	}
	
}
