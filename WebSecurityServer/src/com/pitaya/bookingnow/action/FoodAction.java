package com.pitaya.bookingnow.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.service.socket.EnhancedMessageService;
import com.pitaya.bookingnow.service.socket.IMessageService;
import com.pitaya.bookingnow.service.IFoodService;
import com.pitaya.bookingnow.util.Constants;

public class FoodAction extends BaseAction{
	private static final long serialVersionUID = -675955597630577906L;
	private IFoodService foodService;
	private IMessageService messageService;
	private Food food;
	private List<Food> clientMenuFoods;
	private List<Food> foodList;
	private Map<String, List<Food>> newMenuFood;
	
	private boolean isSuccess;
	private String detail;
	
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
	public IMessageService getMessageService(){
		return this.messageService;
	}
	public void setMessageService(IMessageService ms){
		this.messageService = ms;
	}
	public Food getFood() {
		return food;
	}
	public void setFood(Food food) {
		this.food = food;
	}
	
	public void setIsSuccess(boolean b){
		this.isSuccess = b;
	}
	
	public boolean getIsSuccess(){
		return this.isSuccess;
	}
	
	public void setDetail(String detail){
		this.detail = detail;
	}
	
	public String getDetail(){
		return this.detail;
	}
	
	public void setFoodList(List<Food> foods){
		this.foodList = foods;
	}
	
	public List<Food> getFoodList(){
		return this.foodList;
	}

	public String searchFood() {
		if (food != null) {
			foodService.searchFoods(food);
			
			return "findSuccess";
		} else {
			this.foodList = foodService.searchALLFoods();
			if(this.foodList != null){
				return "findAllFoodsSuccess";
			}
		}
		
		return "findFail";
	}
	
	public String addFood() {
		if (food != null) {
			try {
				this.result = foodService.add(food);
				if(this.result.isExecuteResult()){
					this.setIsSuccess(true);
					this.setDetail(this.result.getShortDetail());
					return "Success";
				}
			} catch (RuntimeException e){
				this.setIsSuccess(false);
				this.setDetail(e.getMessage());
				return "Fail";
			}
		}
		this.setIsSuccess(false);
		this.setDetail(this.result.getErrorDetails().get("Error"));
		return "Fail";
	}
	
	public String removeFood() {
		if (food != null) {
			try {
				this.result = foodService.disableFood(food);
				if(this.result.isExecuteResult()){
					this.setIsSuccess(true);
					return "Success";
				}
			} catch (RuntimeException e){
				this.setIsSuccess(false);
				this.setDetail(e.getMessage());
				return "Fail";
			}
		}
		this.setIsSuccess(false);
		this.setDetail(this.result.getErrorDetails().get("Error"));
		return "Fail";
	}
	
	public String updateFood() {
		if (food != null) {
			try {
				this.result = foodService.modify(food);
				if(this.result.isExecuteResult()){
					this.setIsSuccess(true);
					this.setDetail(this.result.getShortDetail());
					return "Success";
				}
			} catch (RuntimeException e){
				this.setIsSuccess(false);
				this.setDetail(e.getMessage());
				return "Fail";
			}
		}
		this.setIsSuccess(false);
		this.setDetail(this.result.getErrorDetails().get("Error"));
		return "Fail";
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
	
	public String updateClientsFood(){
		FoodMessage message = new FoodMessage();
		message.setHasNew(true);
		this.messageService.sendMessageToAll(message);
		this.isSuccess = true;
		return "Success";
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
