package com.pitaya.bookingnow.action;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class OrderAction extends BaseAction{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	
	private Order resultOrder;
	
	private List<Order> matchedOrders;
	
	
	public Order getResultOrder() {
		return resultOrder;
	}
	public void setResultOrder(Order resultOrder) {
		this.resultOrder = resultOrder;
	}
	public List<Order> getMatchedOrders() {
		return matchedOrders;
	}
	public void setMatchedOrders(List<Order> matchedOrders) {
		this.matchedOrders = matchedOrders;
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
	
	public String searchOrder() {
		
		if (order != null) {
			
			matchedOrders = orderService.searchFullOrdersByFullOrder(order);
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
	 * 		waiter submit a new waiting order which contains customer info
	 */
	public String submitWaitingOrder(){
		if (order != null) {
			result = orderService.addWaitingOrder(order);
			
			if (result.isExecuteResult()) {
				return "submitWaitingSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	/*		
	 * 		waiter update a waiting order with food list
	 */
	public String updateWaitingOfWaitingOrder(){
		if (order != null) {
			result = orderService.updateWaitingOrderToWaiting(order);
			
			if (result.isExecuteResult()) {
				return "updateWaitingOfWaitingSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
	/*		
	 * 		waiter update a waiting order with food list
	 */
	public String updateConfirmedOfWaitingOrder(){
		if (order != null) {
			result = orderService.updateWaitingOrderToConfirmed(order);
			
			if (result.isExecuteResult()) {
				return "updateConfirmedOfWaitingSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
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
	
	/*		
	 * 		submit a new created order which contains user id and table id
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
	 * 		update a new order which contains food list
	 */
	public String updateConfirmedOfNewOrder(){
		if (order != null) {
			result = orderService.updateNewOrderToConfirmed(order);
			
			if (result.isExecuteResult()) {
				return "updateConfirmedOfNewSuccess";
			}
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
	}
	
}
