package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.util.Constants;

public class OrderMessage extends Message{

	private static final long serialVersionUID = -2448833662538683270L;

	private Long orderId;
	private String action;
	private int status;
	//For welcome_new or waiting order info
	private String customer;
	private String phone;
	private int peopleCount;
	private Long timestamp;
	
	public OrderMessage(){
		super(Constants.ORDER_MESSAGE);
	}
	
	public void setAction(String act){
		this.action = act;
	}
	
	public String getAction(){
		return this.action;
	}
	
	public void setOrderId(Long id){
		this.orderId = id;
	}
	
	public Long getOrderId(){
		return this.orderId;
	}
	
	public void setStatus(int s){
		this.status = s;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public void setCustomer(String name){
		this.customer = name;
	}
	
	public String getCustomer(){
		return this.customer;
	}
	
	public void setPhone(String p){
		this.phone =p;
	}
	
	public String getPhone(){
		return this.phone;
	}
	
	public void setPeopleCount(int count){
		this.peopleCount = count;
	}
	
	public int getPeopleCount(){
		return this.peopleCount;
	}
	
	public void setTimestamp(Long t){
		this.timestamp = t;
	}
	
	public Long getTimestamp(){
		return this.timestamp;
	}

	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		super.fromJSONObject(jsonObj);
		try {
			this.setAction(jsonObj.getString("action"));
			this.setOrderId(jsonObj.getLong("orderId"));
			if(jsonObj.has("status")){
				this.setStatus(jsonObj.getInt("status"));
			}
			if(jsonObj.has("customer")){
				this.setCustomer(jsonObj.getString("customer"));
				this.setPhone(jsonObj.getString("phone"));
				this.setPeopleCount(jsonObj.getInt("peopleCount"));
				this.setTimestamp(jsonObj.getLong("timestamp"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
