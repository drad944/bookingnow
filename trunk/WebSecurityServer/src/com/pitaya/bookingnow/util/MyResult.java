package com.pitaya.bookingnow.util;

import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookingnow.entity.Customer;
import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.entity.security.User;

public class MyResult {
	int errorType = 0;
	
	boolean executeResult = false;
	
	String shortDetail = "";
	Map<String, String> errorDetails = new HashMap<String, String>();
	
	int subTrueCount = 0;
	
	int subFalseCount = 0;
	
	double totalPriceOfOrder = 0d;
	
	Order order = new Order();
	
	User user = new User();

	Food food = new Food();
	
	Customer customer = new Customer();
	
	Table table = new Table();
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public int getErrorType() {
		return errorType;
	}

	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

	public boolean isExecuteResult() {
		return executeResult;
	}

	public void setExecuteResult(boolean executeResult) {
		this.executeResult = executeResult;
	}

	public Map<String, String> getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(Map<String, String> errorDetails) {
		this.errorDetails = errorDetails;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public double getTotalPriceOfOrder() {
		return totalPriceOfOrder;
	}

	public void setTotalPriceOfOrder(double totalPriceOfOrder) {
		this.totalPriceOfOrder = totalPriceOfOrder;
	}

	public int getSubTrueCount() {
		return subTrueCount;
	}

	public void setSubTrueCount(int subTrueCount) {
		this.subTrueCount = subTrueCount;
	}

	public int getSubFalseCount() {
		return subFalseCount;
	}

	public void setSubFalseCount(int subFalseCount) {
		this.subFalseCount = subFalseCount;
	}
	
	public void setShortDetail(String detail){
		this.shortDetail = detail;
	}
	
	public String getShortDetail(){
		return this.shortDetail;
	}
	
	
}
