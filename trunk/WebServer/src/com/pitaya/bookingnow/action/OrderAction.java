package com.pitaya.bookingnow.action;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.model.Order;
import com.pitaya.bookingnow.service.IOrderService;

public class OrderAction extends ActionSupport{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	
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
	
	public String findOrder() {
		if (order != null) {
			orderService.selectByPrimaryKey(order.getOid());
			return "findSuccess";
		}
		
		return "findFail";
	}
	
	public String addOrder() {
		if (order != null) {
			orderService.insert(order);
			return "addSuccess";
		}
		
		return "addFail";
	}
	
	public String updateOrder() {
		if (order != null) {
			orderService.updateByPrimaryKey(order);
			return "updateSuccess";
		}
		
		return "updateFail";
	}
	
	public String removeOrder() {
		if (order != null) {
			orderService.deleteByPrimaryKey(order.getOid());
			return "removeSuccess";
		}
		
		return "removeFail";
	}
	
	
}
