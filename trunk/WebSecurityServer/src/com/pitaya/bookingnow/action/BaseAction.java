package com.pitaya.bookingnow.action;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport{

	protected int result;
	
	protected String detail;
	
	public void setResult(int result){
		this.result = result;
	}
	
	public int getResult(){
		return this.result;
	}
	
	public void setDetail(String detail){
		this.detail = detail;
	}
	
	public String getDetail(){
		return this.detail;
	}

}
