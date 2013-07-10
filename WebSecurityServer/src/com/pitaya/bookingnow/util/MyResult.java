package com.pitaya.bookingnow.util;

import java.util.HashMap;
import java.util.Map;

public class MyResult {
	int resultType = 0;
	
	boolean result = false;
	
	int subTrueCount = 0;
	
	int subFalseCount = 0;
	
	double totalPriceOfOrder = 0d;

	public double getTotalPriceOfOrder() {
		return totalPriceOfOrder;
	}

	public void setTotalPriceOfOrder(double totalPriceOfOrder) {
		this.totalPriceOfOrder = totalPriceOfOrder;
	}

	Map<String, String> resultDetails = new HashMap<String, String>();

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
	
	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Map<String, String> getResultDetails() {
		return resultDetails;
	}

	public void setResultDetails(Map<String, String> resultDetails) {
		this.resultDetails = resultDetails;
	}
	
	
}
