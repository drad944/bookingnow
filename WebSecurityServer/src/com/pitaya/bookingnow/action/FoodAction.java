package com.pitaya.bookingnow.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.service.IFoodService;
import com.pitaya.bookingnow.util.Constants;

public class FoodAction extends BaseAction{
	private static final long serialVersionUID = -675955597630577906L;
	private IFoodService foodService;
	private Food food;
	private List<Food> clientMenuFoods;
	private Map<String, List<Food>> newMenuFood;
	
	private InputStream largeImageStream;
	
	private InputStream smallImageStream;
	
	public InputStream getLargeImageStream() {
		return largeImageStream;
	}
	public void setLargeImageStream(InputStream largeImageStream) {
		this.largeImageStream = largeImageStream;
	}
	public InputStream getSmallImageStream() {
		return smallImageStream;
	}
	public void setSmallImageStream(InputStream smallImageStream) {
		this.smallImageStream = smallImageStream;
	}
	public Map<String, List<Food>> getNewMenuFood() {
		return newMenuFood;
	}
	public void setNewMenuFood(Map<String, List<Food>> newMenuFood) {
		this.newMenuFood = newMenuFood;
	}
	public List<Food> getClientMenuFoods() {
		return clientMenuFoods;
	}
	public void setClientMenuFoods(List<Food> clientMenuFoods) {
		this.clientMenuFoods = clientMenuFoods;
	}
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
	
	public String updateMenuFood() {
		if (clientMenuFoods != null) {
			newMenuFood = foodService.updateMenuFoods(clientMenuFoods);
			return "updateMenuSuccess";
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	public String findSmallPictureOfFood() {
		//try to use FileInputStream with picture path directly
		if(this.food != null && food.getId() != null){
			food = foodService.searchSmallPictureByFood(food);
			if(food != null && food.getSmall_image() != null) {
				smallImageStream = new ByteArrayInputStream(food.getSmall_image());
				return "findSmallImageSuccess";
			}
			
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}

	public String findLargePictureOfFood(){
		//try to use FileInputStream with picture path directly
		if(this.food != null && food.getId() != null){
			food = foodService.searchLargePictureByFood(food);
			if(food != null && food.getLarge_image() != null) {
				largeImageStream = new ByteArrayInputStream(food.getLarge_image());
				return "findLargeImageSuccess";
			}
			
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
}
