package com.pitaya.bookingnow.app.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Order implements Serializable{
	
	public static final int ALL = -1;
	//order status
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
	
	private Map<Order.Food, Integer> foods;
	private String tableNum;
	private String submitter;
	private String orderkey;
	private Long modification_ts;
	private Long commit_ts;
	private String phoneNumber;
	private String customername;
	private int peoplecount;
	private int status;
	private volatile boolean isDirty;
	private transient OnDirtyChangedListener mOnDirtyChangedListener;
	private transient OnOrderStatusChangedListener mOnStatusChangedListener;
	
	public Order(){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.markDirty(false);
	}
	
	public Order(String phone, String name, int count){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.peoplecount = count;
		this.phoneNumber = phone;
		this.customername = name;
		this.tableNum = null;
		this.submitter = null;
		this.status = Order.NEW;
		this.orderkey = UUID.randomUUID().toString();
		this.modification_ts = System.currentTimeMillis();
		this.markDirty(false);
	}
	
	public Order(String tn, String submitter){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.tableNum = tn;
		this.submitter = submitter;
		this.orderkey = UUID.randomUUID().toString();
		this.modification_ts = System.currentTimeMillis();
		this.status = Order.NEW;
		this.markDirty(false);
	}
	
	public static String getOrderStatusString(int status){
		switch(status){
			case Order.NEW:
				return "新建";
			case Order.COMMITED:
				return "已提交";
			case Order.PAYING:
				return "结帐中";
			case Order.FINISHED:
				return "完成";
		}
		return "未知状态";
	}
	
	public static String getFoodStatusString(int status){
		switch(status){
			case Order.NEW:
				return "新提交";
			case Order.WAITING:
				return "等待加工";
			case Order.COOKING:
				return "加工中";
			case Order.FINISHED:
				return "出菜";
			case Order.UNAVAILABLE:
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
	
	public String getOrderKey(){
		return this.orderkey;
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
			if(entry.getKey().getStatus() == Order.UNAVAILABLE || entry.getKey().isFree()){
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
		this.orderkey = key;
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
			if(this.status == Order.COMMITED){
				for(Entry<Food, Integer> entry : this.getFoods().entrySet()){
					entry.getKey().setStatus(Order.WAITING);
				}
			}
			if(this.mOnStatusChangedListener != null){
				this.mOnStatusChangedListener.onOrderStatusChanged(this, this.getStatus());
			}
		}
	}
	
	public void setOnDirtyChangedListener(OnDirtyChangedListener listener){
		this.mOnDirtyChangedListener = listener;
	}
	
	public void setOnStatusChangedListener(OnOrderStatusChangedListener listener){
		this.mOnStatusChangedListener = listener;
	}
	
	public void removeAllFood(){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.isDirty = true;
	}
	
//	public synchronized int addFood(String key, String name, float price, int quantity, boolean isFree){
//		Order.Food food = this.new Food(key, name, price);
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
		Order.Food food = this.new Food(key, name, price); 
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
			if(!(obj instanceof Order.Food)){
				return false;
			}
			return this.key.equals(((Order.Food)obj).getKey());
		}
		
		@Override
		public int hashCode(){
			return this.key.hashCode();
		}
	}
	
	public interface OnDirtyChangedListener{
		
		public void onDirtyChanged(Order order, boolean flag);
		
	}
	
	public interface OnOrderStatusChangedListener{
		
		public void onOrderStatusChanged(Order tikcet, int status);
		
	}
	
	public interface OnFoodStatusChangedListener{
		
		public void onFoodStatusChanged(Food food, int status, int old_status);
		
	}
}
