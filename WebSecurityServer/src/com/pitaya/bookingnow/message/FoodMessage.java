package com.pitaya.bookingnow.message;

import com.pitaya.bookingnow.util.Constants;

public class FoodMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5081500912835364921L;
	private boolean hasNew;
	
	public FoodMessage(){
		super(Constants.FOOD_MESSAGE);
	}
	
	public void setHasNew(boolean  b){
		this.hasNew = b;
	}
	
	public boolean getHasNew(){
		return this.hasNew;
	}
	
}
