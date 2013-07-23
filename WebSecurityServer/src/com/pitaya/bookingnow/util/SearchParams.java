package com.pitaya.bookingnow.util;

import java.util.List;

public class SearchParams {
	
	private Long user_id;
	private Long customer_id;
	private List<Integer> orderStatusList;
	
	private Integer customer_count;
	
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCustomer_count() {
		return customer_count;
	}
	public void setCustomer_count(Integer customer_count) {
		this.customer_count = customer_count;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public Long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}
	public List<Integer> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(List<Integer> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	
	
}
