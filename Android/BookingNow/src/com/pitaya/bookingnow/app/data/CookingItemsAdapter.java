package com.pitaya.bookingnow.app.data;

import java.util.ArrayList;
import java.util.List;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter.Item;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.views.CookingItemsListView;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CookingItemsAdapter extends BaseAdapter {
	
	private CookingItemsListView mParent;
	private List<CookingItem> cookingItems;
	private ListView mView;
	private Context mContext;
	
	public CookingItemsAdapter(ListView view, CookingItemsListView parent){
		cookingItems = new ArrayList<CookingItem>();
		this.mParent = parent;
		this.mView = view;
		this.mContext = view.getContext();
	}
	
	public List<CookingItem> getCookingItems(){
		return this.cookingItems;
	}
	
	public void setCookingItems(List<CookingItem> items){
		if(this.cookingItems != null){
			this.cookingItems = null;
		}
		this.cookingItems = items;
	}
	
	@Override
	public int getCount() {
		return cookingItems.size();
	}

	@Override
	public Object getItem(int position) {
		return cookingItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = View.inflate(mContext, R.layout.cookingitem, null);
		final CookingItem item = (CookingItem)this.getItem(position);
		final RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.fooditemdetail);
		
		TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
        nameText.setText(item.getFoodName());
        TextView quanText = (TextView) fooditemRL.findViewById(R.id.quantity);
        quanText.setText(String.valueOf(item.getQuantity()));
		final TextView statusText = (TextView) fooditemRL.findViewById(R.id.foodstatus);
        statusText.setText(Order.getFoodStatusString(item.getStatus()));
        final TextView timerText = (TextView) fooditemRL.findViewById(R.id.timer);
        
        final Button updateStatusBtn = (Button) fooditemRL.findViewById(R.id.updatestatusbtn);
        updateStatusButton(updateStatusBtn, item);
        final Button unavailableBtn = (Button) fooditemRL.findViewById(R.id.unavailablebtn);
        unavailableBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				item.setStatus(Constants.FOOD_UNAVAILABLE);
			}
	        	
        });
        
        if(item.getStatus() != Constants.FOOD_FINISHED){
        	timerText.setVisibility(View.VISIBLE);
        	unavailableBtn.setVisibility(View.VISIBLE);
	        final long beginTime = item.getSubmitTime();
	        final Handler handler = new Handler(); 
	        final Runnable timer = new Runnable(){
		        @Override 
		        public void run() {
		        	long current = System.currentTimeMillis();
		        	long seconds = (current - beginTime)/1000;
		        	if(seconds >= 36000){
		        		//10 hours
		        		timerText.setText(">10小时");
		        		timerText.setTextColor(mContext.getResources().getColor(R.color.red));
		        	} else {
		        		timerText.setText((seconds/3600 < 10 ? "0" + String.valueOf(seconds/3600) : String.valueOf(seconds/3600)) 
		        				+ ":" + ((seconds%3600)/60 < 10 ? "0" + String.valueOf((seconds%3600)/60) : String.valueOf((seconds%3600)/60))
		        				+ ":" + ((seconds%3600)%60 < 10 ? "0" + String.valueOf((seconds%3600)%60) : String.valueOf((seconds%3600)%60)));
		        		if(seconds > 120){
		        			timerText.setTextColor(mContext.getResources().getColor(R.color.red));
		        		} else if(seconds > 60){
		        			timerText.setTextColor(mContext.getResources().getColor(R.color.yellow));
		        		} else {
		        			timerText.setTextColor(mContext.getResources().getColor(R.color.green));
		        		}
		        		handler.postDelayed(this, 1000); 
		        	}
		        }
	        };
	        handler.post(timer);
	    	item.setOnStatusChangedListener(new CookingItem.OnStatusChangedListener() {
				
	    		@Override
				public void onStatusChanged(CookingItem item, int status, int old_status) {
					updateStatusView(statusText, status, old_status);
					if(status == Constants.FOOD_FINISHED){
						handler.removeCallbacks(timer);
						timerText.setVisibility(View.INVISIBLE);
						unavailableBtn.setVisibility(View.INVISIBLE);
					} else if(status == Constants.FOOD_UNAVAILABLE) {
						handler.removeCallbacks(timer);
						timerText.setVisibility(View.INVISIBLE);
					} else if(old_status == Constants.FOOD_UNAVAILABLE) {
						handler.post(timer);
						timerText.setVisibility(View.VISIBLE);
					}
					updateStatusButton(updateStatusBtn, item);
				}
	    		
	    	});
	        
        } else {
        	timerText.setVisibility(View.INVISIBLE);
        	unavailableBtn.setVisibility(View.INVISIBLE);
        }
		return itemView;
	}
	
	private void updateStatusView(TextView statusText, int status, int old_status){
		statusText.setText(Order.getFoodStatusString(status));
	}
	
	private void updateStatusButton(final Button changeStatusBtn, final CookingItem item){
		changeStatusBtn.setVisibility(View.VISIBLE);
		switch(item.getStatus()){
			case Constants.FOOD_NEW:
				changeStatusBtn.setText("新提交");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO update cooking item status
						item.setStatus(Constants.FOOD_CONFIRMED);
					}
					
				});
				break;
			case Constants.FOOD_UNAVAILABLE:
			case Constants.FOOD_CONFIRMED:
				changeStatusBtn.setText("开始加工");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						item.setStatus(Constants.FOOD_COOKING);
					}
					
				});
				break;
			case Constants.FOOD_COOKING:
				changeStatusBtn.setText("完成");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						item.setStatus(Constants.FOOD_FINISHED);
						for(int i = 0; i < cookingItems.size(); i++){
							if(item == cookingItems.get(i)){
								cookingItems.remove(i);
								break;
							}
						}
						CookingItemsAdapter.this.notifyDataSetChanged();
						CookingItemsAdapter.this.mParent.getNextCookingItems(false);
					}
					
				});
				break;
			case Constants.FOOD_FINISHED:
				changeStatusBtn.setVisibility(View.INVISIBLE);
				break;
		}
	}
	
}
