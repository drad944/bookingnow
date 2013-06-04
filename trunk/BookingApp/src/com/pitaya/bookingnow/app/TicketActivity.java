package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.domain.Ticket.Food;

public class TicketActivity extends ListActivity  {

	private Ticket mTicket;
	private TicketListAdapter mTicketAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
		this.setView();
	}
	
//	@Override
//	protected void onNewIntent(Intent intent){
//		super.onNewIntent(intent);
//		setIntent(intent);
//		this.setView();
//	}
	
	
	private void setView(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null && bundle.getSerializable("ticket") != null){
			mTicket = (Ticket)bundle.getSerializable("ticket");
	          
	        try {
				mTicketAdapter = new TicketListAdapter(this, this.getListView());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        setListAdapter(mTicketAdapter);
		} else {
			this.finish();
		}
	}
	
	@Override
	protected void onPause() {
		 super.onPause();
		 Intent intent = new Intent(this, HomeActivity.class);
		 Bundle bundle = new Bundle();
		 bundle.putSerializable("ticket", mTicket);
		 intent.putExtras(bundle);
		 startActivity(intent);
	}
	
	private class TicketListAdapter extends BaseAdapter {
		
        public static final int FOOD_ITEM = 0;  
        public static final int OPERATION_ITEM = 1;
        
		private Context mContext;
		private View mView;
		private EditText mEditText;
		//private Map<Integer, TextWatcher> watchers;
        
        public TicketListAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
            this.mContext = c;
            this.mView = view;
        }
		
		@Override
		public int getCount() {
			return mTicket.getFoods().size() + 1;
		}
		
	    @Override  
        public int getViewTypeCount() {
            return 2;
        }
	    
	    @Override  
        public int getItemViewType(int position) {
            if(position >= mTicket.getFoods().size()) {
                return OPERATION_ITEM;  
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
			if(getItemViewType(position) == FOOD_ITEM){
				Item item = (Item)this.getItem(position);
				final Food food = item.food;
				int quantity = item.quantity;
				itemView = View.inflate(mContext, R.layout.ticketitem, null);
				RelativeLayout fooditemRL = (RelativeLayout) itemView.findViewById(R.id.ticketitem);
				
		        TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
		        nameText.setText(food.getName());
		        TextView totalPriceText = (TextView) fooditemRL.findViewById(R.id.totalprice);
		        totalPriceText.setText(String.valueOf(food.getPrice() * quantity));
		        //add foodstepper
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
						int quantity = 0;
						try{
							quantity = Integer.parseInt(text.toString());
						} catch(Exception e){
							Log.e("FoodMenuView", "Fail to parse food quantity");
							quantity = 0;
						}
						mTicket.addFood(food.getKey(), food.getName(), food.getPrice(), quantity);
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
	            	    	mEditText.clearFocus();
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
			} else {
				ListView.LayoutParams operationLL_LP = new ListView.LayoutParams(
						ListView.LayoutParams.WRAP_CONTENT,
						ListView.LayoutParams.WRAP_CONTENT);
				itemView = new LinearLayout(mContext);
				((LinearLayout)itemView).setLayoutParams(operationLL_LP);
				//((LinearLayout)itemView).setOrientation(ListView..HORIZONTAL);
				Button confirmBtn = new Button(mContext);
				confirmBtn.setText("完成");
				((LinearLayout)itemView).addView(confirmBtn);
				Button resetBtn = new Button(mContext);
				resetBtn.setText("清空");
				((LinearLayout)itemView).addView(resetBtn);
			}
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
}
