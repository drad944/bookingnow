package com.pitaya.bookingnow.message;

import java.util.List;

import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.util.Constants;

public class OrderDetailMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1791996146552713312L;
	
	protected boolean hasNew;
	protected List<Order_Food_Detail> updateItems;
	protected List<Order_Food_Detail> removeItems;
	
	public OrderDetailMessage() {
		super(Constants.ORDER_MESSAGE);
	}
	
	public void setUpdateItems(List<Order_Food_Detail> items){
		this.updateItems = items;
	}

	public List<Order_Food_Detail> getUpdateItems(){
		return this.updateItems;
	}
	
	public void setRemoveItems(List<Order_Food_Detail> items){
		this.removeItems = items;
	}

	public List<Order_Food_Detail> getRemoveItems(){
		return this.removeItems;
	}
	
	public void setHasNew(boolean flag){
		this.hasNew = flag;
	}
	
	public boolean getHasNew(){
		return this.hasNew;
	}
	
}
