package com.pitaya.bookingnow.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;

public class OrderAction extends ActionSupport{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	private int allowance;
	private List<Order> matchedOrders;
	
	
	public List<Order> getMatchedOrders() {
		return matchedOrders;
	}
	public void setMatchedOrders(List<Order> matchedOrders) {
		this.matchedOrders = matchedOrders;
	}
	public int getAllowance() {
		return allowance;
	}
	public void setAllowance(int allowance) {
		this.allowance = allowance;
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
		if (allowance != -1) {
			order = new Order();
			order.setAllowance(allowance);
			
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
	
	
}
