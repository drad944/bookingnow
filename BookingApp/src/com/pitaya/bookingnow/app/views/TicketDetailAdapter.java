package com.pitaya.bookingnow.app.views;

import java.util.Map.Entry;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.domain.Ticket.Food;

public class TicketDetailAdapter extends BaseAdapter {
    
    public static final int FOOD_ITEM = 0;  
    public static final int OPERATIONS = 1;
	protected Context mContext;
	protected View mView;
	protected EditText mEditText;
	protected Ticket mTicket;
    
    public TicketDetailAdapter(Context c, View view, Ticket ticket) throws IllegalArgumentException, IllegalAccessException{  
        this.mContext = c;
        this.mView = view;
        this.mTicket = ticket;
    }
    
    @Override  
    public int getViewTypeCount() {
        return 2;
    }
    
	@Override
	public int getCount() {
		return mTicket.getFoods().size() + 1;
	}
    
    @Override  
    public int getItemViewType(int position) {
        if(position >= mTicket.getFoods().size()) {
            return OPERATIONS;  
        } else {
            return FOOD_ITEM;  
        }  
    }
	
	@Override
	public Object getItem(int position) {
		int index = 0;
		for(Entry<Ticket.Food, Integer> entry : mTicket.getFoods().entrySet()){
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;
		Item item = (Item)this.getItem(position);
		final Food food = item.food;
		int quantity = item.quantity;
		itemView = View.inflate(mContext, R.layout.ticketitem, null);
		RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.ticketitem);
		
        TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
        nameText.setText(food.getName());
        final TextView totalPriceText = (TextView) fooditemRL.findViewById(R.id.totalprice);
        totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
        //add foodstepper to the list item
     	View foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
        RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
        		 ViewGroup.LayoutParams.WRAP_CONTENT,
                 ViewGroup.LayoutParams.WRAP_CONTENT);
        fsRL_LP.addRule(RelativeLayout.RIGHT_OF, nameText.getId());
        fsRL_LP.addRule(RelativeLayout.LEFT_OF, totalPriceText.getId());
        fooditemRL.addView(foodstepper, fsRL_LP);
        //render foodstepper ui
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
				}
				if(mTicket.addFood(food.getKey(), food.getName(), food.getPrice(), quantity)){
					//remove this item from list view
					TicketDetailAdapter.this.notifyDataSetChanged();
				} else {
					//change the item total price
					totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
				}
				//change summary price
				if(mView.findViewById(R.id.ticketbottom) != null){
					((TextView)mView.findViewById(R.id.ticketbottom).findViewById(R.id.summary)).setText("合计"+mTicket.getTotalPrice()+"元");
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
		return itemView;
	}
	
	private class Item{
		Food food;
		int quantity;
		
		Item(Food food, int quantity){
			this.food = food;
			this.quantity = quantity;
		}
	}

}