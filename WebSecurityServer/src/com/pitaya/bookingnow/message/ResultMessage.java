package com.pitaya.bookingnow.message;



public class ResultMessage extends Message{
	
	private static String TAG = "BaseResultMessage";
	private static final long serialVersionUID = 6351604296315217738L;
	
	private int requestType;
	private int result;
	private String detail;
	
	public ResultMessage(){}
	
	public ResultMessage(String key, int reqtype, int result, String detail) {
		super(key);
		this.result = result;
		this.detail = detail;
		this.requestType = reqtype;
	}

	public void setResult(int rs){
		this.result = rs;
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
	
	public void setRequestType(int type){
		this.requestType = type;
	}
	
	public int getRequestType(){
		return this.requestType;
	}
		
}
