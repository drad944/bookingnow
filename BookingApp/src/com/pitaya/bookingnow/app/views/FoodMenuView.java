package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pitaya.bookingnow.app.FoodBookActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.Food;
import com.pitaya.bookingnow.app.domain.Ticket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * This is each Food menu view
 */
public class FoodMenuView extends FrameLayout{
	
	private GridView mGridView;
	private FoodMenuContentView mContentContainer;
	private ArrayList<Food> foodList = new ArrayList<Food>();
	private Map<Integer, TextWatcher> watchers;
	private ImageAdapter mFoodMenuAdapter;
	
    public FoodMenuView(Context context){
        super(context);
    	this.watchers = new HashMap<Integer, TextWatcher>();
    }
      
    public FoodMenuView(Context context, AttributeSet attrs) {  
        super(context, attrs);
    	this.watchers = new HashMap<Integer, TextWatcher>();
    }
    
    public void setContentContainer(FoodMenuContentView v){
    	mContentContainer = v;
    }
    
    public void setupViews(ArrayList<Food> foods){
    	this.foodList = foods;
        LayoutInflater inflater = LayoutInflater.from(getContext());  
        View view = inflater.inflate(R.layout.foodmenuview, null);
        mGridView = (GridView)view.findViewById(R.id.gridview);
		try {
			mFoodMenuAdapter = new ImageAdapter(getContext(), mGridView);
			mGridView.setAdapter(mFoodMenuAdapter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        }
        addView(view);
    }
    
    public void refresh(){
    	mFoodMenuAdapter.notifyDataSetChanged();
    }
    
    public void recycle(){}  
    
	private class ImageAdapter extends BaseAdapter{  
        
		private Context mContext;
		private View mView;
		private EditText mEditText;
       
        public ImageAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
            mContext = c;
            this.mView = view;
        }
        
        @Override  
        public int getCount() {  
            return foodList.size();
        }  
  
        @Override  
        public Object getItem(int position) {
            return position;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {
	         final Food food = foodList.get(position);
	         final int index = position;
	         View view = convertView;
	         if(view == null){
	        	view = View.inflate(mContext, R.layout.foodview, null);
	         }
			 RelativeLayout fooditemRL = (RelativeLayout) view.findViewById(R.id.fooditem);
	         ImageView image = (ImageView) fooditemRL.findViewById(R.id.image);
	         image.setImageBitmap(BitmapFactory.decodeByteArray(food.getSmallImage(), 0, food.getSmallImage().length));
	         TextView text = (TextView) fooditemRL.findViewById(R.id.introduction);
	         text.setTextSize(20);
	         String name = food.getName();
	         String desc = food.getDescription();
	         text.setText(name + "\n\n" + desc);
	         
	         if(food.getCategory() != null && !food.getCategory().equals("")){
	         	
	         	image.setOnClickListener(new OnClickListener(){
	             	
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("category", food.getCategory());
						bundle.putInt("index", index);
						bundle.putSerializable("ticket", mContentContainer.getTicket());
						Intent intent = new Intent(FoodMenuView.this.getContext(), FoodBookActivity.class);
						intent.putExtras(bundle);
						FoodMenuView.this.getContext().startActivity(intent);
					}
	         	
	         	});
	         	View foodstepper = fooditemRL.findViewById(R.id.food_stepper);
	         	RelativeLayout fsRL = null;
	         	if(foodstepper == null){
	         		//First time add foodstepper on the view
	         		foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
		            RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
		            		 ViewGroup.LayoutParams.WRAP_CONTENT,
		                     ViewGroup.LayoutParams.WRAP_CONTENT);
		            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
		            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		            foodstepper.setLayoutParams(fsRL_LP);
		            fooditemRL.addView(foodstepper);
		         	fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
	         	} else {
	         		fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
	         		((EditText)fsRL.findViewById(R.id.quantity)).removeTextChangedListener(watchers.get(view.hashCode()));
	         	}
         	
	         	TextView priceText = (TextView) fsRL.findViewById(R.id.price);
	         	priceText.setTextSize(25);
	         	priceText.setText(String.valueOf(food.getPrice())+"元/份");
	         	
	         	final EditText quantityText = (EditText)fsRL.findViewById(R.id.quantity);
	            watchers.put(view.hashCode(), new TextWatcher(){
	            	
					@Override
					public void afterTextChanged(Editable text) {
						Ticket ticket = mContentContainer.getTicket();
						int quantity = 0;
						try{
							quantity = Integer.parseInt(text.toString());
						} catch(Exception e){
							Log.e("FoodMenuView", "Fail to parse food quantity");
							quantity = 0;
						}
						ticket.addFood(food.getKey(), food.getName(), food.getPrice(), quantity);
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
	         	
	            quantityText.addTextChangedListener(watchers.get(view.hashCode()));
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

	            boolean hasFound = false;
	            for(Entry<com.pitaya.bookingnow.app.domain.Ticket.Food, Integer> entry : mContentContainer.getTicket().getFoods().entrySet()){
	            	if(entry.getKey().getKey().equals(food.getKey())){
	            		 quantityText.setText(String.valueOf(entry.getValue()));
	            		 hasFound = true;
	            		 break;
	            	}
	            }
	            if(!hasFound){
	            	quantityText.setText("0");
	            }
	        } else {
        		image.setOnClickListener(new OnClickListener(){
             	
					@Override
					public void onClick(View v) {
						mContentContainer.selectPage(index + 1);
					}
	         	
	         	});
	        }
            return view;
            
        }
          
    }
}
