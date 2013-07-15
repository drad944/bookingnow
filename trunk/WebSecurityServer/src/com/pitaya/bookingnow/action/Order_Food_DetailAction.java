package com.pitaya.bookingnow.action;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class Order_Food_DetailAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2333024942129449802L;
	
	private IOrder_Food_DetailService food_detailService;
	
	private Map<String, List<Order_Food_Detail>> changeFoods;
	
	private Long orderId;
	
	private Order_Food_Detail food_detail;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Order_Food_Detail getFood_detail() {
		return food_detail;
	}

	public void setFood_detail(Order_Food_Detail food_detail) {
		this.food_detail = food_detail;
	}

	public Map<String, List<Order_Food_Detail>> getChangeFoods() {
		return changeFoods;
	}

	public void setChangeFoods(Map<String, List<Order_Food_Detail>> changeFoods) {
		this.changeFoods = changeFoods;
	}

	public IOrder_Food_DetailService getFood_detailService() {
		return food_detailService;
	}

	public void setFood_detailService(IOrder_Food_DetailService food_detailService) {
		this.food_detailService = food_detailService;
	}
	
	public String updateStatusOfFood_Detail() {
		if (food_detail != null) {
			MyResult result = food_detailService.updateFoodStatus(food_detail);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateStatusOfFood_Detail";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	public String updateFoodsOfFood_Detail() {
		if (changeFoods != null) {
			MyResult result = food_detailService.updateFoods(changeFoods, orderId);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateStatusOfFood_Detail";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
}
