package com.pitaya.bookingnow.action;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.service.IOrderService;
import com.pitaya.bookingnow.util.Constants;

public class OrderAction extends BaseAction{
	
	private static final long serialVersionUID = 6767573103054031438L;
	private IOrderService orderService;
	private Order order;
	private List<Order> matchedOrders;
	
	private Double allowance;
	
	
	public Double getAllowance() {
		return allowance;
	}
	public void setAllowance(Double allowance) {
		this.allowance = allowance;
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
		if (allowance != null) {
			order = new Order();
			order.setAllowance(allowance);
		}
		
		if (order != null) {
			
			matchedOrders = orderService.searchOrders(order);
			
			this.setResult(Constants.SUCCESS);
			this.setDetail("123123");
			//return "searchSuccess";
			return "SUCCESS";
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
	 * 		submit a new created order which contains user id and table id
	 */
	public String submitWaitingOrder(){
		if (order != null) {
			Map<String, String> updateStatusMap = orderService.addWaitingOrder(order);
			
			if (updateStatusMap.get("order_status").equals("true")) {
				this.setResult(Constants.SUCCESS);
				return "commitSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		submit a new created order which contains food list
	 */
	public String submitNewOrder(){
		if (order != null) {
			this.setResult(Constants.SUCCESS);
			return "commitSuccess";
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
}
