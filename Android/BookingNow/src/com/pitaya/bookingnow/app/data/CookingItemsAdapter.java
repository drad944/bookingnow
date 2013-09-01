package com.pitaya.bookingnow.app.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter.Item;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.CookingItemService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.views.CookingItemsListView;


import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CookingItemsAdapter extends BaseAdapter {
	
	private static final String TAG = "CookingItemsAdapter";
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
		View itemView = convertView;
		final CookingItem item = (CookingItem)this.getItem(position);
		if(itemView == null){
			itemView = View.inflate(mContext, R.layout.cookingitem, null);
			final RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.fooditemdetail);
			ViewHolder holder = new ViewHolder();
			itemView.setTag(holder);
			holder.nameText = (TextView) fooditemRL.findViewById(R.id.name);
			holder.quanText =  (TextView) fooditemRL.findViewById(R.id.quantity);
			holder.statusText = (TextView) fooditemRL.findViewById(R.id.foodstatus);
			holder.timerText = (TextView) fooditemRL.findViewById(R.id.timer);
			holder.updateStatusBtn = (Button) fooditemRL.findViewById(R.id.updatestatusbtn);
			holder.unavailableBtn = (Button) fooditemRL.findViewById(R.id.unavailablebtn);
			holder.handler = new Handler();
		}

		final ViewHolder viewHolder = (ViewHolder)itemView.getTag();
		viewHolder.nameText.setText(item.getFoodName());
		viewHolder.quanText.setText(String.valueOf(item.getQuantity()));
		viewHolder.statusText.setText(Order.getFoodStatusString(item.getStatus()));
        updateStatusButton(viewHolder.updateStatusBtn, item);
        viewHolder.unavailableBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				CookingItemService.updateStatus(mContext, item.getId(), Constants.FOOD_UNAVAILABLE, new HttpHandler(){
					@Override
			    	public void onSuccess(String action, String response) {
			    		try {
							JSONObject jresp = new JSONObject(response);
							if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == true){
								item.setStatus(Constants.FOOD_UNAVAILABLE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
			    	}

			    	@Override
			    	public void onFail(String action, int statuscode) {
			    		Log.e(TAG, "[update cooking item status] Network error:" + statuscode);
			    	}
			    	
				});
			}
	        	
        });
        
        if(item.getStatus() != Constants.FOOD_FINISHED && item.getStatus() != Constants.FOOD_UNAVAILABLE){
        	viewHolder.timerText.setVisibility(View.VISIBLE);
    		viewHolder.unavailableBtn.setVisibility(View.VISIBLE);
        	if(viewHolder.timer != null){
        		viewHolder.handler.removeCallbacks(viewHolder.timer);
        		viewHolder.timer = null;
        	}
	        final long beginTime = item.getSubmitTime();
	        String result = setElapsedTime(beginTime, viewHolder.timerText);
	        if(result.equals("<")){
	        	viewHolder.timer = getTimer(beginTime, viewHolder);
		        viewHolder.handler.post(viewHolder.timer);
	        }
        } else {
        	viewHolder.timerText.setVisibility(View.INVISIBLE);
        	viewHolder.unavailableBtn.setVisibility(View.INVISIBLE);
        	if(viewHolder.timer != null){
        		viewHolder.handler.removeCallbacks(viewHolder.timer);
        		viewHolder.timer = null;
        	}
        }
        
        if(item.getStatus() != Constants.FOOD_FINISHED){
        	item.setOnStatusChangedListener(new CookingItem.OnStatusChangedListener() {
				
	    		@Override
				public void onStatusChanged(CookingItem item, int status, int old_status) {
					updateStatusView(viewHolder.statusText, status, old_status);
					switch(status){
						case Constants.FOOD_CONFIRMED:
						case Constants.FOOD_COOKING:
							viewHolder.unavailableBtn.setVisibility(View.VISIBLE);
							if(old_status == Constants.FOOD_UNAVAILABLE){
								viewHolder.timerText.setVisibility(View.VISIBLE);
								Long beginTime = item.getSubmitTime();
								String result = setElapsedTime(beginTime, viewHolder.timerText);
								if(result.equals("<")){
									viewHolder.handler.post(getTimer(beginTime, viewHolder));
								}
							}
							break;
						case Constants.FOOD_FINISHED:
							viewHolder.handler.removeCallbacks(viewHolder.timer);
							viewHolder.timer = null;
							viewHolder.timerText.setVisibility(View.INVISIBLE);
							viewHolder.unavailableBtn.setVisibility(View.INVISIBLE);
							break;
						case Constants.FOOD_UNAVAILABLE:
							if(viewHolder.timer != null){
								viewHolder.handler.removeCallbacks(viewHolder.timer);
								viewHolder.timer = null;
							}
							viewHolder.timerText.setVisibility(View.INVISIBLE);
							viewHolder.unavailableBtn.setVisibility(View.INVISIBLE);
							break;
					}
					updateStatusButton(viewHolder.updateStatusBtn, item);
				}
	    		
	    	});
        }
        
		return itemView;
	}
	
	private Runnable getTimer(final Long beginTime, final ViewHolder viewHolder){
		
		return new Runnable(){
	        @Override 
	        public void run() {
	        	long current = System.currentTimeMillis();
	        	long seconds = (current - beginTime)/1000;
	        	if(seconds >= 36000){
	        		//10 hours
	        		viewHolder.timerText.setText(">10小时");
	        		viewHolder.timerText.setTextColor(mContext.getResources().getColor(R.color.red));
	        	} else {
	        		String [] times = viewHolder.timerText.getText().toString().split(":");
	        		if(times.length != 3){
	        			viewHolder.timerText.setText("未知");
	        		} else {
	        			int hour = Integer.parseInt(times[0]);
		        		int min = Integer.parseInt(times[1]);
		        		int sec = Integer.parseInt(times[2]);
		        		String hourstr = times[0];
		        		String minstr = times[1];
		        		String secstr = formatNextTime(sec, 59, 1);
		        		
		        		if(secstr.equals("00")){
		        			minstr = formatNextTime(min, 59, 1);
			        		if(minstr.equals("00")){
			        			hourstr = formatNextTime(hour, 23, 1);
			        		}
		        		}
		        		viewHolder.timerText.setText(hourstr + ":" + minstr + ":" + secstr);
		        		
		        		if(seconds > 120){
		        			viewHolder.timerText.setTextColor(mContext.getResources().getColor(R.color.red));
		        		} else if(seconds > 60){
		        			viewHolder.timerText.setTextColor(mContext.getResources().getColor(R.color.yellow));
		        		} else {
		        			viewHolder.timerText.setTextColor(mContext.getResources().getColor(R.color.green));
		        		}
		        		viewHolder.handler.postDelayed(this, 999); 
	        		}
	        	}
	        }
        };
	}
	
	private String formatNextTime(int value, int max, int step){
		if(value == max){
			return "00";
		} else if(value >= 9){
			value += step;
			return String.valueOf(value);
		} else {
			value += step;
			return String.valueOf("0" + value);
		}
	}
	
	private String setElapsedTime(long beginTime, TextView text){
		long current = System.currentTimeMillis();
    	long seconds = (current - beginTime)/1000;
    	if(seconds >= 36000){
    		//10 hours
    		text.setText(">10小时");
    		text.setTextColor(mContext.getResources().getColor(R.color.red));
    		return (">");
    	} else if(seconds > 0){
    		text.setText((seconds/3600 < 10 ? "0" + String.valueOf(seconds/3600) : String.valueOf(seconds/3600))
    				+ ":" + ((seconds%3600)/60 < 10 ? "0" + String.valueOf((seconds%3600)/60) : String.valueOf((seconds%3600)/60))
    				+ ":" + ((seconds%3600)%60 < 10 ? "0" + String.valueOf((seconds%3600)%60) : String.valueOf((seconds%3600)%60)));
    		if(seconds > 120){
    			text.setTextColor(mContext.getResources().getColor(R.color.red));
    		} else if(seconds > 60){
    			text.setTextColor(mContext.getResources().getColor(R.color.yellow));
    		} else {
    			text.setTextColor(mContext.getResources().getColor(R.color.green));
    		}
    		return "<";
    	} else {
    		text.setText("00:00:00");
    		text.setTextColor(mContext.getResources().getColor(R.color.green));
    		return "<";
    	}
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
						CookingItemService.updateStatus(mContext, item.getId(), Constants.FOOD_CONFIRMED, new HttpHandler(){
					    	
							@Override
					    	public void onSuccess(String action, String response) {
					    		try {
									JSONObject jresp = new JSONObject(response);
									if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == true){
										item.setStatus(Constants.FOOD_CONFIRMED);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
					    	}

					    	@Override
					    	public void onFail(String action, int statuscode) {
					    		Log.e(TAG, "[update cooking item status] Network error:" + statuscode);
					    	}
					    	
						});
					}
				});
				break;
			case Constants.FOOD_UNAVAILABLE:
			case Constants.FOOD_CONFIRMED:
				changeStatusBtn.setText("开始加工");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						CookingItemService.updateStatus(mContext, item.getId(), Constants.FOOD_COOKING, new HttpHandler(){
							@Override
					    	public void onSuccess(String action, String response) {
					    		try {
									JSONObject jresp = new JSONObject(response);
									if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == true){
										item.setStatus(Constants.FOOD_COOKING);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
					    	}

					    	@Override
					    	public void onFail(String action, int statuscode) {
					    		Log.e(TAG, "[update cooking item status] Network error:" + statuscode);
					    	}
					    	
						});
					}
					
				});
				break;
			case Constants.FOOD_COOKING:
				changeStatusBtn.setText("完成");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						CookingItemService.updateStatus(mContext, item.getId(), Constants.FOOD_FINISHED, new HttpHandler(){
							@Override
					    	public void onSuccess(String action, String response) {
					    		try {
									JSONObject jresp = new JSONObject(response);
									if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == true){
										item.setStatus(Constants.FOOD_FINISHED);
										for(int i = 0; i < cookingItems.size(); i++){
											if(item == cookingItems.get(i)){
												cookingItems.remove(i);
												break;
											}
										}
										CookingItemsAdapter.this.notifyDataSetChanged();
										CookingItemsAdapter.this.mParent.getNextCookingItems(false, 0L);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
					    	}
	
					    	@Override
					    	public void onFail(String action, int statuscode) {
					    		Log.e(TAG, "[update cooking item status] Network error:" + statuscode);
					    	}
					    	
						});
					}
					
				});
				break;
			case Constants.FOOD_FINISHED:
				changeStatusBtn.setVisibility(View.INVISIBLE);
				break;
		}
	}
	
	private static class ViewHolder{
		TextView nameText;
		TextView quanText;
		TextView statusText;
		TextView timerText;
		Button updateStatusBtn;
		Button unavailableBtn;
		Handler handler;
		Runnable timer;
	}
	
}
