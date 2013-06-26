package com.pitaya.bookingnow.app.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.RoleManager;

public class OrderDetailAdapter extends BaseAdapter {
    
    public static final int FOOD_ITEM = 0;  
    public static final int OPERATIONS = 1;
	protected Context mContext;
	protected View mView;
	protected EditText mEditText;
	protected Order mOrder;
    
    public OrderDetailAdapter(Context c, View view, Order order) throws IllegalArgumentException, IllegalAccessException{  
        this.mContext = c;
        this.mView = view;
        this.mOrder = order;
    }
    
    @Override  
    public int getViewTypeCount() {
        return 2;
    }
    
	@Override
	public int getCount() {
		return mOrder.getFoods().size() + 1;
	}
    
    @Override  
    public int getItemViewType(int position) {
        if(position >= mOrder.getFoods().size()) {
            return OPERATIONS;  
        } else {
            return FOOD_ITEM;  
        }  
    }
	
	@Override
	public Object getItem(int position) {
		int index = 0;
		for(Entry<Order.Food, Integer> entry : mOrder.getFoods().entrySet()){
			if(index ++ == position){
				return new Item(entry.getKey(), entry.getValue());
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private void updateSummaryPrice(){
		if(mView.findViewById(R.id.orderbottom) != null){
			((TextView)mView.findViewById(R.id.orderbottom).findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
		}
	}
	
	private void updateFoodStatus(int index, Order.Food food, int status, int old_status){
		View view = ((ListView)mView).getChildAt(index);
		if(status == Order.UNAVAILABLE){
			if(view != null && view.findViewById(R.id.totalprice) != null){
				((TextView) view.findViewById(R.id.totalprice)).setText("0元");
			}
		} else if(old_status == Order.UNAVAILABLE){
			if(view != null && view.findViewById(R.id.totalprice) != null){
				((TextView) view.findViewById(R.id.totalprice)).setText(food.getPrice()*mOrder.getFoods().get(food) + "元");
			}
		}
		if(view != null){
			TextView statusText = (TextView) view.findViewById(R.id.foodstatus);
			if(statusText != null){
				statusText.setText(Order.getFoodStatusString(status));
			}
		}
		updateSummaryPrice();
	}

	private void setupFoodStatusButton(final Button changeStatusBtn, final Order.Food food){
		changeStatusBtn.setVisibility(View.VISIBLE);
		switch(food.getStatus()){
			case Order.NEW:
				changeStatusBtn.setText("开始");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						food.setStatus(Order.WAITING);
						setupFoodStatusButton(changeStatusBtn, food);
					}
					
				});
				break;
			case Order.UNAVAILABLE:
			case Order.WAITING:
				changeStatusBtn.setText("开始加工");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						food.setStatus(Order.COOKING);
						setupFoodStatusButton(changeStatusBtn, food);
					}
					
				});
				break;
			case Order.COOKING:
				changeStatusBtn.setText("完成");
				changeStatusBtn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						food.setStatus(Order.FINISHED);
						setupFoodStatusButton(changeStatusBtn, food);
					}
					
				});
				break;
			case Order.FINISHED:
				changeStatusBtn.setVisibility(View.INVISIBLE);
				break;
		}
	}
	
	//Setup the food item view for kitchen role
	private void setupKitchenView(final View itemView, final int index){
		Item item = (Item)this.getItem(index);
		final Order.Food food = item.food;
		final RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.fooditemdetail);
		TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
        nameText.setText(food.getName());
        TextView quanText = (TextView) fooditemRL.findViewById(R.id.quantity);
        quanText.setText(String.valueOf(item.quantity));
		final TextView statusText = (TextView) fooditemRL.findViewById(R.id.foodstatus);
        statusText.setText(Order.getFoodStatusString(food.getStatus()));
        Button updateStatusBtn = (Button) fooditemRL.findViewById(R.id.updatestatusbtn);
        setupFoodStatusButton(updateStatusBtn, food);
        final Button unavailableBtn = (Button) fooditemRL.findViewById(R.id.unavailablebtn);
        unavailableBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				food.setStatus(Order.UNAVAILABLE);
			}
        	
        });
        
        final TextView timerText = (TextView) fooditemRL.findViewById(R.id.timer);

        if(food.getStatus() != Order.FINISHED){
        	//TODO use this to instead later final long beginTime = this.mOrder.getCommitTime();
	        final long beginTime = this.mOrder.getModificationTime();
	        final Handler handler = new Handler(); 
	        final Runnable timer = new Runnable(){
		        @Override 
		        public void run() {
		        	long current = System.currentTimeMillis();
		        	long seconds = (current - beginTime)/1000;
		        	if(seconds >= 36000){
		        		//10 hours
		        		timerText.setText("等待时间 >10小时");
		        		timerText.setTextColor(mContext.getResources().getColor(R.color.red));
		        	} else {
		        		//20 mins
		        		timerText.setText("等待时间 " + (seconds/3600 < 10 ? "0" + String.valueOf(seconds/3600) : String.valueOf(seconds/3600)) 
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
	
	    	food.setOnFoodStatusChangedListener(new Order.OnFoodStatusChangedListener() {
				
				@Override
				public void onFoodStatusChanged(Order.Food food, int status, int old_status) {
					updateFoodStatus(index, food, status, old_status);
					if(status == Order.FINISHED){
						handler.removeCallbacks(timer);
						timerText.setVisibility(View.INVISIBLE);
						unavailableBtn.setVisibility(View.INVISIBLE);
					} else if(status == Order.UNAVAILABLE) {
						timerText.setVisibility(View.INVISIBLE);
					} else {
						timerText.setVisibility(View.VISIBLE);
					}
				}
	    		
	    	});
        } else {
        	timerText.setVisibility(View.INVISIBLE);
        }
    	
	}

	//Setup the food item view for waiter role
	private void setupWaiterView(final View itemView, final int index){
		final Item item = (Item)this.getItem(index);
		final Order.Food food = item.food;
		final int quantity = item.quantity;
		RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.fooditemdetail);
		
        TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
        nameText.setText(food.getName());
        final TextView totalPriceText = (TextView) fooditemRL.findViewById(R.id.totalprice);
        if(food.isFree()){
        	totalPriceText.setText("0元");
        } else {
        	totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
        }
        final TextView statusText = (TextView) fooditemRL.findViewById(R.id.foodstatus);
        if(mOrder.getStatus() != Order.NEW){
        	statusText.setText(Order.getFoodStatusString(food.getStatus()));
        }
        TextView freeStatusText = (TextView) fooditemRL.findViewById(R.id.freestatus);
        final Button freeBtn = (Button) fooditemRL.findViewById(R.id.freebtn);
        if(this instanceof OrderDetailFragmentAdapter){
        	freeBtn.setVisibility(View.VISIBLE);
        	freeStatusText.setVisibility(View.GONE);
	        if(food.isFree()){
	        	freeBtn.setText(R.string.cancelfreebtn);
	        } else {
	        	freeBtn.setText(R.string.freebtn);
	        }
	        freeBtn.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View v) {
					if(food.isFree()){
						food.setFree(false);
						totalPriceText.setText(food.getPrice() * mOrder.getFoods().get(food) + "元");
						freeBtn.setText(R.string.freebtn);
					} else {
						food.setFree(true);
						freeBtn.setText(R.string.cancelfreebtn);
						totalPriceText.setText("0元");
					}
					if(mOrder.getStatus() == Order.NEW){
						DataService.updateFoodFreeStatus(mContext, mOrder, food);
					}
					if(mView.findViewById(R.id.orderbottom) != null){
						((TextView)mView.findViewById(R.id.orderbottom).findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
					}
				}
	        	
	        });
        } else if(this instanceof OrderDetailPreviewAdapter){
        	freeBtn.setVisibility(View.GONE);
        	freeStatusText.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(totalPriceText.getLayoutParams().width
            		, totalPriceText.getLayoutParams().height);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            totalPriceText.setLayoutParams(lp);
        	if(food.isFree()){
        		freeStatusText.setText("赠菜");
        		freeStatusText.setTextColor(mContext.getResources().getColor(R.color.green));
        	}
        }
        
    	food.setOnFoodStatusChangedListener(new Order.OnFoodStatusChangedListener() {
			
			@Override
			public void onFoodStatusChanged(Order.Food food, int status, int old_status) {
				updateFoodStatus(index, food, status, old_status);
			}
    		
    	});
    	
     	View foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
        RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
        		 250,
                 ViewGroup.LayoutParams.WRAP_CONTENT);
        fsRL_LP.addRule(RelativeLayout.RIGHT_OF, nameText.getId());
        fooditemRL.addView(foodstepper, fsRL_LP);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(statusText.getLayoutParams().width
        		, statusText.getLayoutParams().height);
        lp.addRule(RelativeLayout.RIGHT_OF, foodstepper.getId());
        statusText.setLayoutParams(lp);

        RelativeLayout fsRL = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
     	TextView priceText = (TextView) fsRL.findViewById(R.id.price);
     	priceText.setText(String.valueOf(food.getPrice())+"元/份");
     	
     	final EditText quantityText = (EditText)fsRL.findViewById(R.id.quantity);
     	
     	quantityText.addTextChangedListener(new TextWatcher(){
        	
			@Override
			public void afterTextChanged(Editable text) {
				if(text.toString().trim().equals(""))
					return;
				int quantity = 0;
				try{
					quantity = Integer.parseInt(text.toString());
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 0;
					return;
				}
				
				if(mOrder.getStatus() == Order.NEW){
					//store to local data base
					if(DataService.updateOrderDetails(mContext, mOrder, food, quantity) == Order.REMOVED){
						OrderDetailAdapter.this.notifyDataSetChanged();
						return;
					} 
				} else if(mOrder.addFood(food, quantity) == Order.REMOVED){
					//just store in memory
					OrderDetailAdapter.this.notifyDataSetChanged();
					return;
				}
				
				if(food.getStatus() != Order.UNAVAILABLE && !food.isFree()){
					totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
					OrderDetailAdapter.this.updateSummaryPrice();
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence text, int arg1,
					int arg2, int arg3) {
				quantityText.setSelection(text.length());
			}

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
			}
        	
        });
     	
     	quantityText.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					mEditText = quantityText;
				} else {
					mEditText = null;
				}
			}
        	
        });

        quantityText.setText(String.valueOf(quantity));
        
        mView.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
        	    if(v instanceof EditText){
        	    	if(mEditText != null){
        	    		mEditText.clearFocus();
        	    	}
        	    	return false;
        	    } else if(mEditText != null){
        	    	mEditText.clearFocus();
        	    }
			    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
        	    if(imm.isActive()){
        	    	imm.hideSoftInputFromWindow(mView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        	    }
				return false;
			}
        });
        
        ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String current = quantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) - 1;
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
				}
				if(quantity < 0){
					return;
				}
				quantityText.setText(String.valueOf(quantity));
			}
        	
        });
        
        ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String current = quantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) + 1;
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 1;
				}
				quantityText.setText(String.valueOf(quantity));
			}
        	
        });
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//This implementation is only for food item, you have to implement bottom buttons or header view in subclass
		View itemView = null;
		switch(RoleManager.getRole()){
			case RoleManager.WAITER:
				itemView = View.inflate(mContext, R.layout.fooditem_waiter, null);
				setupWaiterView(itemView, position);
				break;
			case RoleManager.KITCHEN:
				itemView = View.inflate(mContext, R.layout.fooditem_kitchen, null);
				setupKitchenView(itemView, position);
				break;
			case RoleManager.CASHER:
				break;
			case RoleManager.WELCOMER:
				break;
		}
		return itemView;
	}
	
	private class Item{
		Order.Food food;
		int quantity;
		
		Item(Order.Food food, int quantity){
			this.food = food;
			this.quantity = quantity;
		}
	}

}