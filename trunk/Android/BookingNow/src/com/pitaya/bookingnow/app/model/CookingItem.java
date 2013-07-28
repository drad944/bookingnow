package com.pitaya.bookingnow.app.model;

public class CookingItem {
	
	private Long id;
	private String food_id;
	private String order_id;
	private String food_name;
	private int status;
	private int quantity;
	private boolean free;
	private String preference;
	private Long submit_time;
	
	private OnStatusChangedListener mListener;
	
	public CookingItem(Long id, String fid, String oid, int status, int quantity, boolean f, String preference, Long time){
		this.id = id;
		this.food_id = fid;
		this.order_id = oid;
		this.status = status;
		this.quantity = quantity;
		this.free = f;
		this.preference = preference;
		this.submit_time = time;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public String getFoodId(){
		return this.food_id;
	}
	
	public String getOrderId(){
		return this.order_id;
	}
	
	public String getFoodName(){
		return this.food_name;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public boolean isFree(){
		return this.free;
	}
	
	public String getPreference(){
		return this.preference;
	}
	
	public int getQuantity(){
		return this.quantity;
	}
	
	public Long getSubmitTime(){
		return this.submit_time;
	}
	
	public void setStatus(int newstatus){
		if(this.status != newstatus){
			int old_status = this.status;
			this.status = newstatus;
			if(this.mListener != null){
				this.mListener.onStatusChanged(this, this.getStatus(), old_status);
			}
		}
	}
	
	public void setOnStatusChangedListener(OnStatusChangedListener listener){
		this.mListener = listener;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof CookingItem)){
			return false;
		}
		return this.id.equals(((CookingItem)obj).getId());
	}
	
	@Override
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public interface OnStatusChangedListener{
		
		public void onStatusChanged(CookingItem item, int status, int old_status);

	}
}
