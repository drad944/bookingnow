package com.pitaya.bookingnow.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.util.MyResult;

public class BaseAction extends ActionSupport{
	
	protected static Log logger =  LogFactory.getLog(BaseAction.class);
	MyResult result;
	
	public MyResult getResult() {
		return result;
	}
	
	public void setResult(MyResult result) {
		this.result = result;
	}
}
