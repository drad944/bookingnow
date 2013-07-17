package com.pitaya.bookingnow.action;

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
	private List<Integer> orderStatusList;
	
	private Long user_id;
	
	private Long order_id;
	
	private Order resultOrder;
	
	
	private Map<String, List<Order>> matchedOrders;
	
	
	public Map<String, List<Order>> getMatchedOrders() {
		return matchedOrders;
	}
	public void setMatchedOrders(Map<String, List<Order>> matchedOrders) {
		this.matchedOrders = matchedOrders;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Long order_id) {
		this.order_id = order_id;
	}
	public List<Integer> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(List<Integer> orderStatusList) {
		this.orderStatusList = orderStatusList;
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
		if (user_id != null || orderStatusList != null) {
			SearchParams params = new SearchParams();
			params.setUser_id(user_id);
			params.setOrderStatusList(orderStatusList);
			
			List<Order> orders = orderService.searchFullOrders(params);
			matchedOrders.put("result", orders);
			return "searchOrderSuccess";
		}
		
		result.setErrorType(Constants.FAIL);
		result.setExecuteResult(false);
		result.getErrorDetails().put("order_exist", "can not find order info in client data");
		return "searchOrderFail";
	}
	
	public String searchOrder() {
		
		if (order != null) {
			
			List<Order> orders = orderService.searchFullOrdersByFullOrder(order);
			
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
	 * 		update a new or waiting order to committed
	 */
	public String commitOrder(){
		if (order != null) {
			if(order.getFood_details() != null){
				result = orderService.updateNewOrderToConfirmed(order);
				if (result.isExecuteResult()) {
					this.getResult().setExecuteResult(true);
					return "commitNewOrderSuccess";
				} else {
					this.getResult().setExecuteResult(false);
					this.getResult().setShortDetail(null);
					return "Fail";
				}
			} else if(order.getTable_details() != null){
				result = orderService.updateWaitingOrderToConfirmed(order);
				if (result.isExecuteResult()) {
					this.getResult().setExecuteResult(true);
					return "commitWaitingOrderSuccess";
				} else {
					this.getResult().setExecuteResult(false);
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
	
}
