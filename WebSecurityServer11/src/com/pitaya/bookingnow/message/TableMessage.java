package com.pitaya.bookingnow.message;

import java.util.Map;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.util.Constants;

public class TableMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1986562133322559438L;
	private Table table;
	private Order order;
	
	public TableMessage(){
		super(Constants.TABLE_MESSAGE);
	}
	
	public TableMessage(Table table, Order order){
		super(Constants.TABLE_MESSAGE);
		this.table = table;
		this.order = order;
	}
	
	public void setTable(Table t){
		this.table = t;
	}
	
	public Table getTable(){
		return this.table;
	}
	
	public void setOrder(Order o){
		this.order = o;
	}
	
	public Order getOrder(){
		return this.order;
	}
	
}
