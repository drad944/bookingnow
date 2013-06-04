package com.pitaya.bookingnow.app.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Ticket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7178941729755818383L;
	
	private Map<Ticket.Food, Integer> foods;
	private String tableNum;
	private String submitter;
	private String ticketkey;
	
	public Ticket(){}
	
	public Ticket(String tn, String submitter){
		this.foods = new HashMap<Ticket.Food, Integer>();
		this.tableNum = tn;
		this.submitter = submitter;
		this.ticketkey = UUID.randomUUID().toString();
	}
	
	public Map<Food, Integer> getFoods(){
		return this.foods;
	}
	
	public String getTableNum(){
		return this.tableNum;
	}
	
	public String getSubmitter(){
		return this.submitter;
	}
	
	public String getTicketKey(){
		return this.ticketkey;
	}
	
	public float getTotalPrice(){
		float summary = 0f;
		for(Entry<Food, Integer> entry : this.foods.entrySet()){
			summary += entry.getKey().getPrice()*entry.getValue();
		}
		return summary;
	}
	
	public void removeAllFood(){
		this.foods = new HashMap<Ticket.Food, Integer>();
	}
	
	public synchronized boolean addFood(String key, String name, float price, int quantity){
		Ticket.Food food = this.new Food(key, name, price); 
		if(quantity <= 0){
			if(this.foods.get(food) != null){
				this.foods.remove(food);
				return true;
			}
		} else {
			this.foods.put(food, quantity);
		}
		return false;
	}
	
	public class Food implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8964367536151118314L;
		private String key;
		private String name;
		private float price;
		
		private Food(String key, String name, float price){
			this.key = key;
			this.name = name;
			this.price = price;
		}
		
		public String getKey(){
			return this.key;
		}
		
		public String getName(){
			return this.name;
		}
		
		public float getPrice(){
			return this.price;
		}
		
		@Override
		public boolean equals(Object obj){
			if(this == obj)
				return true;
			if(obj == null)
				return false;
			if(!(obj instanceof Ticket.Food)){
				return false;
			}
			return this.key.equals(((Ticket.Food)obj).getKey());
		}
		
		@Override
		public int hashCode(){
			return this.key.hashCode();
		}
	}
}
