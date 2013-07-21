package com.pitaya.bookingnow.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import android.content.Context;

import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookinnow.app.util.Constants;

public class Order implements Serializable{
		
	public static final int ADDED = 0;
	public static final int REMOVED = ADDED + 1;
	public static final int UPDATED = REMOVED + 1;
	public static final int IGNORED = UPDATED + 1;

	private static final long serialVersionUID = -7178941729755818383L;
	
	private Map<Order.Food, Integer> foods;
	private String tableNum;
	private String submitter;
	private String orderkey;
	private Long modification_ts;
	private Long submit_ts;
	private String phoneNumber;
	private String customername;
	private Long customer_id;
	private Long user_id;
	private List<Table> tables;
	private int peoplecount;
	private int status;
	//This is to tell whether a committed order is modified on local
	private volatile boolean isDirty;
	private transient OnDirtyChangedListener mOnDirtyChangedListener;
	private transient ArrayList<OnOrderStatusChangedListener> mOnStatusChangedListeners;
	private Map<String, ArrayList<UpdateFood>> updateFoods;
	
	public Order(){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.isDirty = false;
	}
	
	public Order(Long orderid, Long user_id, String username, String phone, String name, int count){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.peoplecount = count;
		this.phoneNumber = phone;
		this.customername = name;
		this.submitter = username;
		this.user_id = user_id;
		this.orderkey = String.valueOf(orderid);
		this.status = Constants.ORDER_NEW;
		this.modification_ts = System.currentTimeMillis();
		this.isDirty = false;
	}
	
	public Order(List<Table> tables, Long orderid, Long user_id, String username, Long timestamp){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
		this.tables = tables;
		this.tableNum = "";
		for(int i=0; i < tables.size(); i++){
			this.tableNum += tables.get(i).getLabel() + ",";
		}
		this.tableNum = this.tableNum.substring(0, this.tableNum.length() - 1);
		this.submitter = username;
		this.user_id = user_id;
		this.orderkey = String.valueOf(orderid);
		this.modification_ts = timestamp;
		this.status = Constants.ORDER_NEW;
		this.isDirty = false;
	}
	
	public static String getOrderStatusString(int status){
		switch(status){
			case Constants.ORDER_NEW:
				return "新建";
			case Constants.ORDER_COMMITED:
				return "已提交";
			case Constants.ORDER_PAYING:
				return "结帐中";
			case Constants.ORDER_WAITING:
				return "等候中";
			case Constants.ORDER_FINISHED:
				return "完成";
		}
		return "未知状态";
	}
	
	public static String getFoodStatusString(int status){
		switch(status){
			case Constants.FOOD_NEW:
			case Constants.FOOD_CONFIRMED:
				return "新提交";
			case Constants.FOOD_WAITING:
				return "等待加工";
			case Constants.FOOD_COOKING:
				return "加工中";
			case Constants.FOOD_FINISHED:
				return "出菜";
			case Constants.FOOD_UNAVAILABLE:
				return "售完";
		}
		return "未知状态";
	}
	
	public static String getUpdateType(int status){
		switch(status){
			case Order.REMOVED:
				return "delete";
			case Order.UPDATED:
				return "update";
			case Order.ADDED:
				return "new";
		}
		return "unknown";
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
	
	public Long getSubmitTime(){
		return this.submit_ts;
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
	
	public Long getCustomerId(){
		return this.customer_id;
	}
	
	public Long getSubmitterId(){
		return this.user_id;
	}
	
	public List<Table> getTables(){
		return this.tables;
	}
	
	public int getStatus(){
		return this.status;
	}

	public float getTotalPrice(){
		float summary = 0f;
		for(Entry<Food, Integer> entry : this.foods.entrySet()){
			if(entry.getKey().getStatus() == Constants.FOOD_UNAVAILABLE || entry.getKey().isFree()){
				continue;
			}
			summary += entry.getKey().getPrice()*entry.getValue();
		}
		return summary;
	}
	
	public boolean isDirty(){
		return this.isDirty;
	}
	
	public void markDirty(Context context, boolean flag){
		if(this.isDirty == flag){
			return;
		} else {
			this.isDirty = flag;
			DataService.setOrderDirty(context, this);
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
	
	public void setSubmitTime(Long ts){
		this.submit_ts = ts;
	}
	
	public void setLastModifyTime(Long ts){
		this.modification_ts = ts;
	}
	
	public void setCustomerId(Long id){
		this.customer_id = id;
	}
	
	public void setUserId(Long id){
		this.user_id = id;
	}
	
	public void setDirty(boolean flag){
		this.isDirty = flag;
	}
	
	public void addTable(Table table){
		if(this.tables == null){
			this.tables = new ArrayList<Table>();
		}
		this.tables .add(table);
	}
	
	public void setStatus(int status){
		if(this.status != status){
			this.status = status;
			if(this.status == Constants.ORDER_COMMITED){
				for(Entry<Food, Integer> entry : this.getFoods().entrySet()){
					entry.getKey().setStatus(Constants.FOOD_WAITING);
				}
			}
			if(this.mOnStatusChangedListeners != null){
				for(OnOrderStatusChangedListener listener : this.mOnStatusChangedListeners){
					if(listener != null){
						listener.onOrderStatusChanged(this, this.getStatus());
					}
				}
			}
		}
	}
	
	public void resetUpdateFoods(Context context){
		this.updateFoods = null;
		DataService.resetOrderUpdateDetails(context, this);
	}
	
	public Map<String, ArrayList<UpdateFood>> getUpdateFoods(){
		
		return this.updateFoods;
	}
	
	private void searchAndRemove(Context context, int type, UpdateFood food){
		ArrayList<UpdateFood> foods = this.updateFoods.get(getUpdateType(type));
		int i=0;
		for(; i < foods.size(); i++){
			UpdateFood temp = foods.get(i);
			if(food.getRefId() != null){
				if(temp.getRefId().equals(food.getRefId())){
					break;
				}
			} else if(temp.getFoodKey().equals(food.getFoodKey())){
				break;
			}
		}
		if(i != foods.size()){
			foods.remove(i);
			DataService.removeOrderUpdateDetail(context, this.getOrderKey(), food.getFoodKey());
		}
	}
	
	private void searchAndReplace(Context context, int srctype, int desttype, UpdateFood food){
		this.searchAndRemove(context, srctype, food);
		ArrayList<UpdateFood> destfoods = this.updateFoods.get(getUpdateType(desttype));
		destfoods.add(food);
		DataService.saveOrderUpdateDetail(context, desttype, this.getOrderKey(), food);
	}
	
	public void enrichFoods(Context context){
		if(this.foods == null || this.foods.size() == 0){
			DataService.getFoodsOfOrder(context, this);
		}
	}
	
	public void enrichUpdateFoods(Context context){
		if(this.updateFoods == null){
			this.initUpdateFoods();
			DataService.enrichOrderUpdateDetails(context, this);
		}
	}
	
	public void addUpdateFoods(Context context, int type, Order.Food food, int quantity){
		if(this.updateFoods == null){
			this.initUpdateFoods();
		}
		UpdateFood updateFood = new UpdateFood(food.getKey(), food.getId(), food.getVersion(), food.isFree(), quantity);
		switch(type){
			case Order.ADDED:
				//Always add a food item
				this.updateFoods.get(getUpdateType(Order.ADDED)).add(updateFood);
				DataService.saveOrderUpdateDetail(context, type, this.getOrderKey(), updateFood);
				break;
			case Order.UPDATED:
				if(updateFood.getRefId() != null){
					//Update a committed food item
					this.searchAndReplace(context, Order.UPDATED, Order.UPDATED, updateFood);
				} else {
					//To update a new food item, move it to add list
					this.searchAndReplace(context, Order.ADDED, Order.ADDED, updateFood);
				}
				break;
			case Order.REMOVED:
				if(updateFood.getRefId() != null){
					//To remove a committed food item, remove it from the update list firstly if necessary
					this.searchAndReplace(context, Order.UPDATED, Order.REMOVED, updateFood);
				} else {
					//Remove a new food item from add list
					this.searchAndRemove(context, Order.ADDED, updateFood);
				}
				break;
		}
	}
	
	public void setOnDirtyChangedListener(OnDirtyChangedListener listener){
		this.mOnDirtyChangedListener = listener;
	}
	
	public void addOnStatusChangedListener(OnOrderStatusChangedListener listener){
		if(this.mOnStatusChangedListeners == null){
			this.mOnStatusChangedListeners = new ArrayList<OnOrderStatusChangedListener>();
		}
		this.mOnStatusChangedListeners.add(listener);
	}
	
	public void removeOnStatusChangedListeners(){
		this.mOnStatusChangedListeners = null;
	}
	
	public void removeAllFood(){
		this.foods = new LinkedHashMap<Order.Food, Integer>();
	}
	
	public Order.Food searchFood(String food_key){
		for(Entry<Food, Integer> entry : this.getFoods().entrySet()){
			if(entry.getKey().getKey().equals(food_key)){
				return entry.getKey();
			}
		}
		return null;
	}
	
	public synchronized int addFood(Food food, int quantity){
		Food current_food = searchFood(food.getKey());
		if(quantity <= 0){
			if(current_food != null){
				food.setId(current_food.getId());
				this.foods.remove(food);
				return REMOVED;
			} else {
				return IGNORED;
			}
		} else {
			if(current_food != null){
				Integer current_q = this.foods.get(current_food);
				if(quantity != current_q){
					food.setId(current_food.getId());
					this.foods.put(food, quantity);
					return UPDATED;
				} else {
					return IGNORED;
				}
			} else {
				this.foods.put(food, quantity);
				return ADDED;
			}
		}
	}
	
	public synchronized int addFood(String key, String name, double price, int quantity){
		Order.Food food = this.new Food(key, name, price); 
		return addFood(food, quantity);
	}
	
	private void initUpdateFoods(){
		this.updateFoods = new HashMap<String, ArrayList<UpdateFood>>();
		this.updateFoods.put(getUpdateType(Order.UPDATED), new ArrayList<UpdateFood>());
		this.updateFoods.put(getUpdateType(Order.ADDED), new ArrayList<UpdateFood>());
		this.updateFoods.put(getUpdateType(Order.REMOVED), new ArrayList<UpdateFood>());
	}
	
	public class Food implements Serializable{
		
		private static final long serialVersionUID = -8964367536151118314L;
		private String key;
		private String name;
		private double price;
		private int status;
		private boolean isFree;
		private Long id;
		private Long version;
		private transient OnFoodStatusChangedListener listener;
		
		public Food(String key, String name, double price){
			this.key = key;
			this.name = name;
			this.price = price;
			this.status = Constants.FOOD_NEW;
			this.isFree = false;
		}
		
		public void setId(Long id){
			this.id = id;
		}
		
		public Long getId(){
			return this.id;
		}
		
		public String getKey(){
			return this.key;
		}
		
		public String getName(){
			return this.name;
		}
		
		public double getPrice(){
			return this.price;
		}
		
		public int getStatus(){
			return this.status;
		}
		
		public Long getVersion(){
			return this.version;
		}
		
		public void setVersion(Long v){
			this.version = v;
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
