package com.pitaya.bookingnow.app.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Ticket implements Serializable{
	
	public static final int ALL = -1;
	//ticket status
	public static final int NEW = 0;
	public static final int COMMITED = NEW + 1;
	public static final int PAYING = COMMITED + 1;
	public static final int FINISHED = PAYING + 1;
	public static final int WAITING = FINISHED + 1;
	public static final int COOKING = WAITING + 1;
	public static final int UNAVAILABLE = COOKING + 1;
	
	//results
	public static final int ADDED = 0;
	public static final int REMOVED = ADDED + 1;
	public static final int UPDATED = REMOVED + 1;
	public static final int IGNORED = -1;
	
	//food status

	
	
	private static final long serialVersionUID = -7178941729755818383L;
	
	private Map<Ticket.Food, Integer> foods;
	private String tableNum;
	private String submitter;
	private String ticketkey;
	private Long modification_ts;
	private Long commit_ts;
	private String phoneNumber;
	private String customername;
	private int peoplecount;
	private int status;
	private volatile boolean isDirty;
	private transient OnDirtyChangedListener mOnDirtyChangedListener;
	private transient OnTicketStatusChangedListener mOnStatusChangedListener;
	
	public Ticket(){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.markDirty(false);
	}
	
	public Ticket(String phone, String name, int count){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.peoplecount = count;
		this.phoneNumber = phone;
		this.customername = name;
		this.tableNum = null;
		this.submitter = null;
		this.status = Ticket.NEW;
		this.ticketkey = UUID.randomUUID().toString();
		this.modification_ts = System.currentTimeMillis();
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
	
	public static String getTicketStatusString(int status){
		switch(status){
			case Ticket.NEW:
				return "新建";
			case Ticket.COMMITED:
				return "已提交";
			case Ticket.PAYING:
				return "结帐中";
			case Ticket.FINISHED:
				return "完成";
		}
		return "未知状态";
	}
	
	public static String getFoodStatusString(int status){
		switch(status){
			case Ticket.NEW:
				return "新提交";
			case Ticket.WAITING:
				return "等待加工";
			case Ticket.COOKING:
				return "加工中";
			case Ticket.FINISHED:
				return "出菜";
			case Ticket.UNAVAILABLE:
				return "售完";
		}
		return "未知状态";
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
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public String getCustomerName(){
		return this.customername;
	}
	
	public int getPeopleCount(){
		return this.peoplecount;
	}
	
	public int getStatus(){
		return this.status;
	}

	public float getTotalPrice(){
		float summary = 0f;
		for(Entry<Food, Integer> entry : this.foods.entrySet()){
			if(entry.getKey().getStatus() == Ticket.UNAVAILABLE || entry.getKey().isFree()){
				continue;
			}
			summary += entry.getKey().getPrice()*entry.getValue();
		}
		return summary;
	}
	
	public boolean isDirty(){
		return this.isDirty;
	}
	
	public void markDirty(boolean flag){
		if(this.isDirty == flag){
			return;
		} else {
			this.isDirty = flag;
			if(this.mOnDirtyChangedListener != null){
				this.mOnDirtyChangedListener.onDirtyChanged(this, this.isDirty);
			}
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
		if(this.status != status){
			this.status = status;
			if(this.status == Ticket.COMMITED){
				for(Entry<Food, Integer> entry : this.getFoods().entrySet()){
					entry.getKey().setStatus(Ticket.WAITING);
				}
			}
			if(this.mOnStatusChangedListener != null){
				this.mOnStatusChangedListener.onTicketStatusChanged(this, this.getStatus());
			}
		}
	}
	
	public void setOnDirtyChangedListener(OnDirtyChangedListener listener){
		this.mOnDirtyChangedListener = listener;
	}
	
	public void setOnStatusChangedListener(OnTicketStatusChangedListener listener){
		this.mOnStatusChangedListener = listener;
	}
	
	public void removeAllFood(){
		this.foods = new LinkedHashMap<Ticket.Food, Integer>();
		this.isDirty = true;
	}
	
//	public synchronized int addFood(String key, String name, float price, int quantity, boolean isFree){
//		Ticket.Food food = this.new Food(key, name, price);
//		food.setFree(isFree);
//		return addFood(food, quantity);
//	}
	
	public synchronized int addFood(Food food, int quantity){
		if(quantity <= 0){
			if(this.foods.get(food) != null){
				if(this.getStatus() == NEW){
					
				} else {
					markDirty(true);
				}
				this.foods.remove(food);
				return REMOVED;
			} else {
				return IGNORED;
			}
		} else {
			Integer current_q = this.foods.get(food);
			if(current_q != null){
				if(quantity != current_q){
					this.foods.put(food, quantity);
					markDirty(true);
					return UPDATED;
				} else {
					return IGNORED;
				}
			} else {
				this.foods.put(food, quantity);
				markDirty(true);
				return ADDED;
			}
		}
	}
	
	public synchronized int addFood(String key, String name, float price, int quantity){
		Ticket.Food food = this.new Food(key, name, price); 
		return addFood(food, quantity);
	}
	
	public class Food implements Serializable{
		/**
		 * 
		 */
		
		private static final long serialVersionUID = -8964367536151118314L;
		private String key;
		private String name;
		private float price;
		private int status;
		private boolean isFree;
		private transient OnFoodStatusChangedListener listener;
		
		public Food(String key, String name, float price){
			this.key = key;
			this.name = name;
			this.price = price;
			this.status = NEW;
			this.isFree = false;
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
		
		public int getStatus(){
			return this.status;
		}
		
		public boolean isFree(){
			return this.isFree;
		}
		
		public void setFree(boolean flag){
			this.isFree = flag;
		}
		
		public void setStatus(int status){
			if(this.status != status){
				int old_status = this.status;
				this.status = status;
				if(this.listener != null){
					this.listener.onFoodStatusChanged(this, this.getStatus(), old_status);
				}
			}
		}
		
		public void setOnFoodStatusChangedListener(OnFoodStatusChangedListener listener){
			this.listener = listener;
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
	
	public interface OnTicketStatusChangedListener{
		
		public void onTicketStatusChanged(Ticket tikcet, int status);
		
	}
	
	public interface OnFoodStatusChangedListener{
		
		public void onFoodStatusChanged(Food food, int status, int old_status);
		
	}
}
