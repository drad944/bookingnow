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
	 * 		waiter submit a new waiting order which contains customer info
	 */
	public String submitWaitingOrder(){
		if (order != null) {
			MyResult result = orderService.addWaitingOrder(order);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "submitWaitingSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		waiter update a waiting order with food list
	 */
	public String updateWaitingOfWaitingOrder(){
		if (order != null) {
			MyResult result = orderService.updateWaitingOrderToWaiting(order);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateWaitingOfWaitingSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		waiter update a waiting order with food list
	 */
	public String updateConfirmedOfWaitingOrder(){
		if (order != null) {
			MyResult result = orderService.updateWaitingOrderToConfirmed(order);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateConfirmedOfWaitingSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	public String updateFoodsOfConfirmedOrder(){
		if (order != null) {
			MyResult result = orderService.updateFoodsInConfirmedOrder(order);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateFoodsOfConfirmedSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		submit a new created order which contains user id and table id
	 */
	
	public String submitNewOrder(){
		if (order != null) {
			MyResult result = orderService.addNewOrder(order);
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "submitNewSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
	/*		
	 * 		update a new order which contains food list
	 */
	public String updateConfirmedOfNewOrder(){
		if (order != null) {
			MyResult result = orderService.updateNewOrderToConfirmed(order);
			
			if (result.isResult()) {
				this.setResult(Constants.SUCCESS);
				return "updateConfirmedOfNewSuccess";
			}
		}
		this.setResult(Constants.FAIL);
		this.setDetail("Some reason cause the operation fail");
		return "Fail";
	}
	
}
