package com.pitaya.bookingnow.app.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Ticket implements Serializable{
	
	public static final int ALL = -1;
	public static final int NEW = 0;
	public static final int BOOKING = NEW + 1;
	public static final int COMMITED = BOOKING + 1;
	public static final int PAYING = COMMITED + 1;
	public static final int FINISHED = PAYING + 1;
	public static final int SAVED = FINISHED + 1;
	
	private static final long serialVersionUID = -7178941729755818383L;
	
	private Map<Ticket.Food, Integer> foods;
	private String tableNum;
	private String submitter;
	private String ticketkey;
	private Long modification_ts;
	private Long commit_ts;
	private int status;
	private volatile boolean isDirty;
	private transient OnDirtyChangedListener mOnDirtyChangedListener;
	
	public Ticket(){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.markDirty(false);
	}
	
	public Ticket(String tn, String submitter){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.tableNum = tn;
		this.submitter = submitter;
		this.ticketkey = UUID.randomUUID().toString();
		this.modification_ts = System.currentTimeMillis();
		this.status = Ticket.NEW;
		this.markDirty(false);
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
	
	public Long getModificationTime(){
		return this.modification_ts;
	}
	
	public Long getCommitTime(){
		return this.commit_ts;
	}
	
	public int getStatus(){
		return this.status;
	}

	public float getTotalPrice(){
		float summary = 0f;
		for(Entry<Food, Integer> entry : this.foods.entrySet()){
			summary += entry.getKey().getPrice()*entry.getValue();
		}
		return summary;
	}
	
	public boolean isDirty(){
		return this.isDirty;
	}
	
	public void markDirty(boolean flag){
		this.isDirty = flag;
		if(this.mOnDirtyChangedListener != null){
			this.mOnDirtyChangedListener.onDirtyChanged(this, this.isDirty);
		}
	}
	
	public void setKey(String key){
		this.ticketkey = key;
	}
	
	public void setSubmitter(String submitter){
		this.submitter = submitter;
	}
	
	public void setTableNumber(String number){
		this.tableNum = number;
	}
	
	public void setCommitTime(Long ts){
		this.commit_ts = ts;
	}
	
	public void setLastModifyTime(Long ts){
		this.modification_ts = ts;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setOnDirtyChangedListener(OnDirtyChangedListener listener){
		this.mOnDirtyChangedListener = listener;
	}
	
	public void removeAllFood(){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.isDirty = true;
	}
	/*
	 * return true if the food is removed
	 */
	public synchronized boolean addFood(String key, String name, float price, int quantity){
		Ticket.Food food = this.new Food(key, name, price); 
		if(quantity <= 0){
			if(this.foods.get(food) != null){
				this.foods.remove(food);
				markDirty(true);
				return true;
			} else {
				return false;
			}
		} else {
			Integer current_q = this.foods.get(food);
			if(current_q != null){
				if(quantity != current_q){
					this.foods.put(food, quantity);
					markDirty(true);
				}
			} else {
				this.foods.put(food, quantity);
				markDirty(true);
			}

			return false;
		}
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
	
	public interface OnDirtyChangedListener{
		
		public void onDirtyChanged(Ticket ticket, boolean flag);
		
	}
}
