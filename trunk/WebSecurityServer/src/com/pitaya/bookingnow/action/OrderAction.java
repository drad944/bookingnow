package com.pitaya.bookingnow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public class OrderAction extends BaseAction{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	
	private SearchParams params;
	
	private Order resultOrder;
	
	private Map<String, List<Order>> matchedOrders;
	
	public SearchParams getParams() {
		return params;
	}
	public void setParams(SearchParams params) {
		this.params = params;
	}
	public Map<String, List<Order>> getMatchedOrders() {
		return matchedOrders;
	}
	public void setMatchedOrders(Map<String, List<Order>> matchedOrders) {
		this.matchedOrders = matchedOrders;
	}
	
	public Order getResultOrder() {
		return resultOrder;
	}
	public void setResultOrder(Order resultOrder) {
		this.resultOrder = resultOrder;
	}
	
	public IOrderService getOrderService() {
		return orderService;
	}
	public void setOrderService(IOrderService orderService) {
		this.orderService = orderService;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public String searchByStatusOfOrder() {
		if (params != null && (params.getUser_id() != null || params.getOrderStatusList() != null)) {
			
			List<Order> orders = orderService.searchFullOrdersWithoutFoods(params);
			matchedOrders = new HashMap<String, List<Order>>();
			matchedOrders.put("result", orders);
			return "searchOrderSuccess";
		}
		
		result.setErrorType(Constants.FAIL);
		result.setExecuteResult(false);
		result.getErrorDetails().put("order_exist", "can not find order info in client data");
		return "searchOrderFail";
	}
	
	public String searchFoodsInOrder(){
		if (params != null && params.getOrder_id() != null) {
			order = orderService.searchOrderById(params.getOrder_id());
			return "searchFoodsInOrderSuccess";
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	public String searchOrder() {
		
		if (order != null) {
			
			List<Order> orders = orderService.searchFullOrdersByFullOrder(order);
			if (matchedOrders == null) {
				matchedOrders = new HashMap<String, List<Order>>();
			}
			matchedOrders.put("result", orders);
			return "searchOrderSuccess";
		}
		if (result == null) {
			result = new MyResult();
		}
		result.setErrorType(Constants.FAIL);
		result.setExecuteResult(false);
		result.getErrorDetails().put("order_exist", "can not find order info in client data");
		return "searchOrderFail";
	}
	
	public String addOrder() {
		if (order != null) {
			orderService.add(order);
			return "addSuccess";
		}
		
		return "addFail";
	}
	
	public String updateOrder() {
		if (order != null) {
			orderService.modify(order);
			return "updateSuccess";
		}
		
		return "updateFail";
	}
	
	public String removeOrder() {
		if (order != null) {
			orderService.remove(order);
			return "removeSuccess";
		}
		
		return "removeFail";
	}
	
	/*		
	 * 		submit a welcomer_new order
	 */
	public String submitWaitingOrder(){
		if (order != null) {
			result = orderService.addWaitingOrder(order);
			
			if (result.isExecuteResult()) {
				order = result.getOrder();
				return "submitWaitingSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	/*		
	 * 		update a welcomer_new order to waiting
	 */
	public String updateFoodsOfWaitingOrder(){
		if (order != null) {
			result = orderService.updateFoodsOfWaitingOrder(order);
			
			if (result.isExecuteResult()) {
				order = result.getOrder();
				return "updateFoodsOfWaitingOrderSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	/*		
	 * 		submit a new order
	 */
	
	public String submitNewOrder(){
		if (order != null) {
			result = orderService.addNewOrder(order);
			if (result.isExecuteResult()) {
				resultOrder = result.getOrder();
				return "submitNewSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	/*		
	 * 		update welcomer_new order to new
	 */
	public String updateTablesOfWaitingOrder() {
		if (order != null) {
			if(order.getTable_details() != null){
				result = orderService.updateTablesOfWaitingOrder(order);
				if (result.isExecuteResult()) {
					order = result.getOrder();
					return "commitWaitingOrderSuccess";
				} else {
					this.getResult().setExecuteResult(false);
					this.getResult().setShortDetail(null);
					return "Fail";
				}
			} else {
				this.getResult().setExecuteResult(false);
				this.getResult().setShortDetail("To  order, it must contain food list or table details");
				return "Fail";
			}
		} else {
			this.getResult().setExecuteResult(false);
			this.getResult().setShortDetail("Parameter order is missing");
			return "Fail";
		}
	}
	
	/*
	 * 		Update new or waiting order to committed 
	 */
	public String commitOrder(){
		if (order != null) {
			if(order.getFood_details() != null){
				result = orderService.updateNewOrderToConfirmed(order);
				if (result.isExecuteResult()) {
					order = result.getOrder();
					return "commitNewOrderSuccess";
				} else {
					this.getResult().setShortDetail(null);
					return "Fail";
				}
			} else if(order.getTable_details() != null){
				result = orderService.updateWaitingOrderToConfirmed(order);
				if (result.isExecuteResult()) {
					order = result.getOrder();
					return "commitWaitingOrderSuccess";
				} else {
					this.getResult().setShortDetail(null);
					return "Fail";
				}
			} else {
				this.getResult().setExecuteResult(false);
				this.getResult().setShortDetail("To commit order, it must contain food list or table details");
				return "Fail";
			}
		} else {
			this.getResult().setExecuteResult(false);
			this.getResult().setShortDetail("Parameter order is missing");
			return "Fail";
		}

	}
	
	public String updateFoodsOfConfirmedOrder(){
		if (order != null) {
			result = orderService.updateFoodsInConfirmedOrder(order);
			
			if (result.isExecuteResult()) {
				return "updateFoodsOfConfirmedSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	public String calculateOrder(){
		if (order != null && order.getId() != null) {
			result = orderService.calculateOrder(order);
			
			if (result.isExecuteResult()) {
				order = result.getOrder();
				return "calculateOrderSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	
}
