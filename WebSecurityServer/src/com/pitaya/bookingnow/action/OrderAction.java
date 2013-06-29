package com.pitaya.bookingnow.action;

import java.util.List;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;

public class OrderAction extends BaseAction{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	private List<Order> matchedOrders;
	
	
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
			
			matchedOrders = orderService.searchOrders(order);
			
			return "searchSuccess";
		}
		
		
		if (order != null) {
			orderService.searchOrders(order);
			return "findSuccess";
		}
		
		return "findFail";
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
	 * 		Commit a new created order which contains user id and table id
	 */
	public String commitWaitingOrder(){
		if (order != null) {
			this.setResult(Constants.SUCCESS);
			return "commitSuccess";
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		Commit a new created order which contains food list
	 */
	public String commitNewOrder(){
		if (order != null) {
			this.setResult(Constants.SUCCESS);
			return "commitSuccess";
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
}
