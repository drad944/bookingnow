package com.pitaya.bookingnow.action;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.model.Food;
import com.pitaya.bookingnow.service.IFoodService;

public class FoodAction extends ActionSupport{
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
	
	public String findFood() {
		if (food != null) {
			foodService.selectByPrimaryKey(food.getFid());
			
			return "findSuccess";
		}
		
		return "findFail";
	}
	
	public String addFood() {
		if (food != null) {
			foodService.insert(food);
			
			return "addSuccess";
		}
		
		return "addFail";
	}
	
	public String removeFood() {
		if (food != null) {
			foodService.deleteByPrimaryKey(food.getFid());
			
			return "removeSuccess";
		}
		
		return "removeFail";
	}
	
	public String updateFood() {
		if (food != null) {
			foodService.updateByPrimaryKey(food);
			
			return "updateSuccess";
		}
		
		return "updateFail";
	}
	
}
