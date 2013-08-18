package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.CookingItemsAdapter;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.service.CookingItemService;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.util.Constants;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class CookingItemsListView extends RelativeLayout{
	
	public static int TOTAL_RECORDS_THRESHOLD;
	public static int INIT_RECORDS_THRESHOLD;
	public static int STEP_RECORDS_THRESHOLD;
	protected static String TAG = "FoodListView";
	
	protected View mHeaderView;
	protected ListView mListView;
	protected View mBottomView;
	protected List<CookingItem> mCookingItemsList;
	protected CookingItemsAdapter mAdapter;
	
	private Long mBeginId;
	private ArrayList<Integer> mStatusList;
	private boolean isUpdating = false;
	
	static {
		TOTAL_RECORDS_THRESHOLD = 100;
		INIT_RECORDS_THRESHOLD = 20;
		STEP_RECORDS_THRESHOLD = 5;
	}
	
	public CookingItemsListView(Context context){
        super(context);
        mCookingItemsList = Collections.synchronizedList(new ArrayList<CookingItem>());
        mStatusList = new ArrayList<Integer>();
        mStatusList.add(Constants.FOOD_CONFIRMED);
        mStatusList.add(Constants.FOOD_COOKING);
        this.mBeginId = 0L;
        this.setupViews();
    }
      
    public CookingItemsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCookingItemsList = new ArrayList<CookingItem>();
        mStatusList = new ArrayList<Integer>();
        mStatusList.add(Constants.FOOD_CONFIRMED);
        mStatusList.add(Constants.FOOD_COOKING);
        this.mBeginId = 0L;
        this.setupViews();
    }
    
    public void refresh(){
    	this.mAdapter.notifyDataSetChanged();
    }
    
    public void updateItems(List<CookingItem> items){
    	for(CookingItem updateItem : items){
        	for(CookingItem currentItem : this.mCookingItemsList){
        		if(currentItem.getId().equals(updateItem.getId())){
        			currentItem.setQuantity(updateItem.getQuantity());
        		}
        	}	
    	}
    }
    
    public void removeItems(List<CookingItem> items){
    	for(CookingItem removeItem : items){
    		for(int i =this.mCookingItemsList.size() - 1; i >=0; i--){
    			if(removeItem.getId().equals(this.mCookingItemsList.get(i).getId())){
    				this.mCookingItemsList.remove(i);
    			}
    		}
    	}
    }
    
    public void setupViews(){
    	this.mHeaderView = View.inflate(this.getContext(), R.layout.cookingitemlistheader, null);
    	this.mHeaderView.setId(1);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(this.mHeaderView, lp);
        this.mListView = new ListView(this.getContext());
        mListView.setId(2);
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.BELOW, this.mHeaderView.getId());
        addView(mListView, lp);
        mListView.setOnScrollListener(new OnScrollListener(){

        	private boolean flag = false;
        	
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(visibleItemCount == 0 || totalItemCount == 0){
					return;
				}
				if(view == mListView){
					//Log.i(TAG, "on scroll:" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);
					if(firstVisibleItem != 0 && firstVisibleItem + visibleItemCount == totalItemCount){
						flag = true;
					} else {
						flag = false;
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING){
					if(this.flag == true){
						getNextCookingItems(true, 0L);
					}
				}
			}
        	
        });
        
        mBottomView = new TextView(getContext());
        ((TextView)mBottomView).setTextSize(25);
        ((TextView)mBottomView).setGravity(Gravity.CENTER_HORIZONTAL);
        ((TextView)mBottomView).setBackgroundColor(this.getContext().getResources().getColor(R.color.common_background));
        this.mListView.addFooterView(mBottomView);
        
        ((TextView)this.mHeaderView.findViewById(R.id.nametitle)).setText(R.string.cookingitem_title_name);
        ((TextView)this.mHeaderView.findViewById(R.id.quantitytitle)).setText(R.string.cookingitem_title_quantity);
        ((TextView)this.mHeaderView.findViewById(R.id.timertitle)).setText(R.string.cookingitem_title_timer);
        ((TextView)this.mHeaderView.findViewById(R.id.statustitle)).setText(R.string.cookingitem_title_status);
        ((TextView)this.mHeaderView.findViewById(R.id.operationstitle)).setText(R.string.cookingitem_title_operations);
        this.getNextCookingItems(false, 0L);
    }
    
    public void getNextCookingItems(boolean isForce, Long delay){
    	if(this.mCookingItemsList.size() >= TOTAL_RECORDS_THRESHOLD || this.isUpdating == true){
    		return;
    	}    	
		int count = 0;
		if(isForce){
			//User try to get next records
			count = STEP_RECORDS_THRESHOLD;
		} else {
			//After finish some items or there are new items committed, try to get next records if current size is not enough
			count = INIT_RECORDS_THRESHOLD - this.mCookingItemsList.size();
		}
		if(count > 0){
			isUpdating = true;
			Handler handler = new Handler();
			final int total = count;
			handler.postDelayed(new Runnable(){

				@Override
				public void run() {
		    		toggleLoadingMsg(true);
		    		CookingItemService.getCookingItems(mStatusList, mBeginId, total, new HttpHandler(){
		        		
		            	@Override
		            	public void onSuccess(String action, String response) {
		            		try {
		            			JSONObject jresp = new JSONObject(response);
		            			if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
		            				//TODO handle fail
		            			} else {
		            				JSONArray jcookingitems = jresp.getJSONArray("result");
		            				for(int i = 0; i < jcookingitems.length(); i++){
		            					JSONObject jcookingitem = jcookingitems.getJSONObject(i);
		            					JSONObject jfood = jcookingitem.getJSONObject("food");
		            					CookingItem cookingitem = new CookingItem(jcookingitem.getLong("id"),
		            							jfood.getLong("id"), jcookingitem.getLong("order_id"), jfood.getString("name"), 
		            							jcookingitem.getInt("status"), jcookingitem.getInt("count"),
		            							jcookingitem.getBoolean("isFree"), null, jcookingitem.getLong("last_modify_time"));
		            					mCookingItemsList.add(cookingitem);
		            					if(jcookingitem.getLong("id") > mBeginId){
		            						mBeginId = jcookingitem.getLong("id");
		            					}
		            				}
		            				if(jcookingitems.length() > 0){
		            					onGetCookingItems();
		            				}
		            			}
		            		} catch (JSONException e) {
		            			e.printStackTrace();
		            		}
		        			isUpdating = false;
		            		toggleLoadingMsg(false);
		            	}

		            	@Override
		            	public void onFail(String action, int statuscode) {
		            		Log.e(TAG, "[CookingItemService.getCookingItems] Network error:" + statuscode);
		        			isUpdating = false;
		                	toggleLoadingMsg(false);
		            	}
		            	
		            });
				}
				
			}, delay);
		}

    }
    
    private void onGetCookingItems(){
    	if(mAdapter == null){
    		mAdapter = new CookingItemsAdapter(this.mListView, this);
    		mAdapter.setCookingItems(mCookingItemsList);
    		this.mListView.setAdapter(mAdapter);
    	} else {
			mAdapter.notifyDataSetChanged();	
    	}
    }
    
    private void toggleLoadingMsg(boolean flag){
    	if(flag){
    		((TextView)mBottomView).setText(R.string.loading);
    	} else {
    		((TextView)mBottomView).setText("");
    	}
    }
}
