package com.pitaya.bookingnow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.SearchParams;

public class Order_Food_DetailAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2333024942129449802L;
	
	private IOrder_Food_DetailService food_detailService;
	
	private Map<String, List<Order_Food_Detail>> changeFoods;
	
	private Map<String, List<Order_Food_Detail>> matchedFoods;
	
	private SearchParams params;
	
	private Long orderId;
	
	private Long userId;
	
	private Order_Food_Detail food_detail;

	public Map<String, List<Order_Food_Detail>> getMatchedFoods() {
		return matchedFoods;
	}

	public void setMatchedFoods(Map<String, List<Order_Food_Detail>> matchedFoods) {
		this.matchedFoods = matchedFoods;
	}

	public SearchParams getParams() {
		return params;
	}

	public void setParams(SearchParams params) {
		this.params = params;
	}

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
	
	public String searchFoodsInFood_Detail() {
		if (params != null) {
			List<Order_Food_Detail> foods = food_detailService.searchFood_Details(params);
			if(matchedFoods == null) {
				matchedFoods = new HashMap<String, List<Order_Food_Detail>>();
			}
			matchedFoods.put("result", foods);
			return "searchFoodsInFood_DetailSuccess";
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	public String updateStatusOfFood_Detail() {
		if (food_detail != null && params != null) {
			result = food_detailService.updateFoodStatus(params, food_detail);
			
			if (result.isExecuteResult()) {
				food_detail = result.getFood_Detail();
				return "updateStatusOfFood_DetailSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	public String updateFoodsOfFood_Detail() {
		if (changeFoods != null) {
			result = food_detailService.updateFoods(changeFoods, orderId);
			
			if (result.isExecuteResult()) {
				return "updateFoodsOfFood_DetailSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
}
