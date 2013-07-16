package com.pitaya.bookingnow.util;

import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookingnow.entity.Order;

public class MyResult {
	int errorType = 0;
	
	boolean executeResult = false;
	
	Map<String, String> errorDetails = new HashMap<String, String>();
	
	int subTrueCount = 0;
	
	int subFalseCount = 0;
	
	double totalPriceOfOrder = 0d;
	
	Order order = new Order();

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
	
}
