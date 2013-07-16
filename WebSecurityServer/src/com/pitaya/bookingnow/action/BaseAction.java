package com.pitaya.bookingnow.action;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.util.MyResult;

public class BaseAction extends ActionSupport{
	MyResult result;
	
	public MyResult getResult() {
		return result;
	}
	
	public void setResult(MyResult result) {
		this.result = result;
	}
}
