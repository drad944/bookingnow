package com.pitaya.bookingnow.message;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.Constants;

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
	
	public OrderMessage(String action, Order order){
		super(Constants.ORDER_MESSAGE);
		this.orderId = order.getId();
		this.status = order.getStatus();
		this.customer = order.getCustomer().getName();
		this.phone = order.getCustomer().getPhone();
		this.peopleCount = order.getCustomer_count();
		this.timestamp = order.getSubmit_time();
		this.action = action;
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
}
