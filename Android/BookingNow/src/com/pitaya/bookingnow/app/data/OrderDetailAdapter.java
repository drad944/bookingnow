package com.pitaya.bookingnow.app.data;

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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.model.Order.OnFoodStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ContentUtil;

public class OrderDetailAdapter extends BaseAdapter {
    
    public static final int FOOD_ITEM = 0;  
    public static final int OPERATIONS = 1;
	protected Context mContext;
	protected View mView;
	protected EditText mEditText;
	protected Order mOrder;
	protected DataSetChangedListener mListener;
    
    public OrderDetailAdapter(Context c, View view, Order order) throws IllegalArgumentException, IllegalAccessException{  
        this.mContext = c;
        this.mView = view;
        this.mOrder = order;
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
    }
    
	public void setDataSetChangedListener(DataSetChangedListener l){
		this.mListener = l;
	}
    
	public void setOrder(Order order){
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
		if(status == Constants.FOOD_UNAVAILABLE){
			if(view != null && view.findViewById(R.id.totalprice) != null){
				((TextView) view.findViewById(R.id.totalprice)).setText("0元");
			}
		} else if(old_status == Constants.FOOD_UNAVAILABLE){
			if(view != null && view.findViewById(R.id.totalprice) != null){
				((TextView) view.findViewById(R.id.totalprice)).setText(food.getPrice()*mOrder.getFoods().get(food) + "元");
			}
		}
		if(view != null){
			TextView statusText = (TextView) view.findViewById(R.id.foodstatus);
			if(statusText != null){
				statusText.setText(Order.getFoodStatusString(status));
				statusText.setVisibility(View.VISIBLE);
			}
		}
		updateSummaryPrice();
	}
	
	private void updateFoodStatus(ViewHolder viewHolder, int status, int old_status){
		if(status == Constants.FOOD_UNAVAILABLE){
			viewHolder.totalPriceText.setText("0元");
		} else if(old_status == Constants.FOOD_UNAVAILABLE){
			viewHolder.totalPriceText.setText(viewHolder.food.getPrice()*mOrder.getFoods().get(viewHolder.food) + "元");
		}
		viewHolder.statusText.setText(Order.getFoodStatusString(status));
		viewHolder.statusText.setVisibility(View.VISIBLE);
		updateSummaryPrice();
	}
	
	private void setupConvertView(final View itemView, final ViewHolder viewHolder){
		RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.fooditemdetail);
		viewHolder.nameText = (TextView) fooditemRL.findViewById(R.id.name);
		viewHolder.totalPriceText = (TextView) fooditemRL.findViewById(R.id.totalprice);
		viewHolder.statusText = (TextView) fooditemRL.findViewById(R.id.foodstatus);
		viewHolder.freeStatusText = (TextView) fooditemRL.findViewById(R.id.freestatus);
		viewHolder.freeBtn = (Button) fooditemRL.findViewById(R.id.freebtn);
		
        if(this instanceof WorkerOrderDetailAdapter){
        	viewHolder.freeBtn.setVisibility(View.VISIBLE);
        	viewHolder.freeStatusText.setVisibility(View.GONE);
        } else if(this instanceof CustomerOrderDetailAdapter){
        	viewHolder.freeBtn.setVisibility(View.GONE);
        	viewHolder.freeStatusText.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.totalPriceText.getLayoutParams().width
            		, viewHolder.totalPriceText.getLayoutParams().height);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            viewHolder.totalPriceText.setLayoutParams(lp);
        }
		View foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
        RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(ContentUtil.getPixelsByDP(250),
        		ViewGroup.LayoutParams.WRAP_CONTENT);
        fsRL_LP.addRule(RelativeLayout.RIGHT_OF, viewHolder.nameText.getId());
        fooditemRL.addView(foodstepper, fsRL_LP);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.statusText.getLayoutParams().width
        		, viewHolder.statusText.getLayoutParams().height);
        lp.addRule(RelativeLayout.RIGHT_OF, foodstepper.getId());
        viewHolder.statusText.setLayoutParams(lp);
        RelativeLayout fsRL = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
        viewHolder.priceText = (TextView) fsRL.findViewById(R.id.price);
        viewHolder.quantityText = (EditText)fsRL.findViewById(R.id.quantity);
        
        viewHolder.quantityText.addTextChangedListener(new TextWatcher(){
        	
			@Override
			public void afterTextChanged(Editable text) {
				if(text.toString().trim().equals(""))
					return;
				int quantity = 0;
				try{
					quantity = Integer.parseInt(text.toString());
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					return;
				}
				if(mOrder.getStatus() != Constants.ORDER_PAYING && mOrder.getStatus() != Constants.ORDER_FINISHED){
					int result = DataService.updateOrderDetails(mContext, mOrder, viewHolder.food, quantity);
					if(result != Order.IGNORED && (mOrder.getStatus() == Constants.ORDER_COMMITED
							|| mOrder.getStatus() == Constants.ORDER_WAITING)){
						mOrder.addUpdateFoods(mContext, result, viewHolder.food, quantity);
						mOrder.markDirty(mContext, true);
					}
					if(result == Order.REMOVED){
						OrderDetailAdapter.this.notifyDataSetChanged();
						if(OrderDetailAdapter.this.mListener != null){
							mListener.OnDataSetChanged();
						}
						return;
					}
				}
				
				if(viewHolder.food.getStatus() != Constants.FOOD_UNAVAILABLE && !viewHolder.food.isFree()){
					viewHolder.totalPriceText.setText(String.valueOf(viewHolder.food.getPrice() * quantity) + "元");
					OrderDetailAdapter.this.updateSummaryPrice();
				}
				
			}

			@Override
			public void beforeTextChanged(CharSequence text, int arg1,
					int arg2, int arg3) {
				viewHolder.quantityText.setSelection(text.length());
			}

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
			}
        	
        });
     	
        viewHolder.quantityText.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					mEditText = viewHolder.quantityText;
				} else {
					mEditText = null;
				}
			}
        	
        });
        
        ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String current = viewHolder.quantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) - 1;
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
				}
				if(quantity < 0){
					return;
				}
				viewHolder.quantityText.setText(String.valueOf(quantity));
			}
        	
        });
        
        ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String current = viewHolder.quantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) + 1;
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 1;
				}
				viewHolder.quantityText.setText(String.valueOf(quantity));
			}
        	
        });
        
        itemView.setTag(viewHolder);
	}

	//Setup the food item view for waiter role
	private void setupView(final View itemView, final int index){
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
        if(mOrder.getStatus() == Constants.ORDER_NEW || mOrder.getStatus() == Constants.ORDER_COMMITED){
        	food.setOnFoodStatusChangedListener(new Order.OnFoodStatusChangedListener() {
    			
    			@Override
    			public void onFoodStatusChanged(Order.Food food, int status, int old_status) {
    				updateFoodStatus(index, food, status, old_status);
    			}
        		
        	});
        	if(mOrder.getStatus() == Constants.ORDER_COMMITED){
        		statusText.setText(Order.getFoodStatusString(food.getStatus()));
        	}
        } else {
        	statusText.setVisibility(View.GONE);
        }
    	
        TextView freeStatusText = (TextView) fooditemRL.findViewById(R.id.freestatus);
        final Button freeBtn = (Button) fooditemRL.findViewById(R.id.freebtn);
        if(this instanceof WorkerOrderDetailAdapter){
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
					if(mOrder.getStatus() != Constants.ORDER_PAYING && mOrder.getStatus() != Constants.ORDER_FINISHED){
						DataService.updateFoodFreeStatus(mContext, mOrder, food);
						if(mOrder.getStatus() == Constants.ORDER_COMMITED || mOrder.getStatus() == Constants.ORDER_WAITING){
							mOrder.addUpdateFoods(mContext, Order.UPDATED, food, quantity);
							mOrder.markDirty(mContext, true);
						}
						if(mView.findViewById(R.id.orderbottom) != null){
							((TextView)mView.findViewById(R.id.orderbottom).findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
						}
					}
				}
	        	
	        });
        } else if(this instanceof CustomerOrderDetailAdapter){
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
					return;
				}
				if(mOrder.getStatus() != Constants.ORDER_PAYING && mOrder.getStatus() != Constants.ORDER_FINISHED){
					int result = DataService.updateOrderDetails(mContext, mOrder, food, quantity);
					if(result != Order.IGNORED && (mOrder.getStatus() == Constants.ORDER_COMMITED
							|| mOrder.getStatus() == Constants.ORDER_WAITING)){
						mOrder.addUpdateFoods(mContext, result, food, quantity);
						mOrder.markDirty(mContext, true);
					}
					if(result == Order.REMOVED){
						OrderDetailAdapter.this.notifyDataSetChanged();
						if(OrderDetailAdapter.this.mListener != null){
							mListener.OnDataSetChanged();
						}
						return;
					}
				}
				
				if(food.getStatus() != Constants.FOOD_UNAVAILABLE && !food.isFree()){
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
		View itemView = convertView;
		if(itemView == null){
			itemView = View.inflate(mContext, R.layout.fooditem_waiter, null);
			setupConvertView(itemView, new ViewHolder());
		}
		//setupView(itemView, position);
		
		Item item = (Item)this.getItem(position);
		Order.Food food = item.food;
		final int quantity = item.quantity;
		final ViewHolder holder = (ViewHolder)itemView.getTag();
		holder.food = food;
		holder.nameText.setText(food.getName());
        if(food.isFree()){
        	holder.totalPriceText.setText("0元");
        } else {
        	holder.totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
        }
        
        if(mOrder.getStatus() == Constants.ORDER_NEW || mOrder.getStatus() == Constants.ORDER_COMMITED){
        	food.setOnFoodStatusChangedListener(new Order.OnFoodStatusChangedListener() {
    			
    			@Override
    			public void onFoodStatusChanged(Order.Food food, int status, int old_status) {
    				updateFoodStatus(holder, status, old_status);
    			}
        		
        	});
        	if(mOrder.getStatus() == Constants.ORDER_COMMITED){
        		holder.statusText.setText(Order.getFoodStatusString(food.getStatus()));
        	}
        } else {
        	holder.statusText.setVisibility(View.GONE);
        }
        
        if(this instanceof WorkerOrderDetailAdapter){
	        if(food.isFree()){
	        	holder.freeBtn.setText(R.string.cancelfreebtn);
	        } else {
	        	holder.freeBtn.setText(R.string.freebtn);
	        }
	        holder.freeBtn.setOnClickListener(new OnClickListener(){
	
				@Override
				public void onClick(View v) {
					if(holder.food.isFree()){
						holder.food.setFree(false);
						holder.totalPriceText.setText(holder.food.getPrice() * mOrder.getFoods().get(holder.food) + "元");
						holder.freeBtn.setText(R.string.freebtn);
					} else {
						holder.food.setFree(true);
						holder.freeBtn.setText(R.string.cancelfreebtn);
						holder.totalPriceText.setText("0元");
					}
					if(mOrder.getStatus() != Constants.ORDER_PAYING && mOrder.getStatus() != Constants.ORDER_FINISHED){
						DataService.updateFoodFreeStatus(mContext, mOrder, holder.food);
						if(mOrder.getStatus() == Constants.ORDER_COMMITED || mOrder.getStatus() == Constants.ORDER_WAITING){
							mOrder.addUpdateFoods(mContext, Order.UPDATED, holder.food, mOrder.getFoods().get(holder.food));
							mOrder.markDirty(mContext, true);
						}
						updateSummaryPrice();
					}
				}
	        	
	        });
        } else if(this instanceof CustomerOrderDetailAdapter){
        	if(food.isFree()){
        		holder.freeStatusText.setText("赠菜");
        		holder.freeStatusText.setTextColor(mContext.getResources().getColor(R.color.green));
        	}
        }
		
        holder.priceText.setText(String.valueOf(food.getPrice())+"元/份");
        holder.quantityText.setText(String.valueOf(quantity));
		return itemView;
	}
	
	private static class ViewHolder{
		 TextView nameText;
		 TextView totalPriceText;
		 TextView statusText;
		 TextView freeStatusText;
		 Button freeBtn;
		 TextView priceText;
		 EditText quantityText;
		 Order.Food food;
	}
	
	protected class Item{
		Order.Food food;
		int quantity;
		
		Item(Order.Food food, int quantity){
			this.food = food;
			this.quantity = quantity;
		}
	}
	
    public interface DataSetChangedListener{
    	public void OnDataSetChanged();
    }
}