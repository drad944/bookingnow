package com.pitaya.bookingnow.app.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aphidmobile.utils.UI;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;

public class FoodBookAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int repeatCount = 1;
	private List<Food> foodList;
	private Order mOrder;
	private Map<Integer, TextWatcher> watchers;
	
	public FoodBookAdapter(Context context, List<Food> foodlist, Order order){
		inflater = LayoutInflater.from(context);
		this.foodList = foodlist;
		this.mOrder = order;
		this.watchers = new HashMap<Integer, TextWatcher>();
	}
	
	@Override
	public int getCount() {
		return this.foodList.size() * repeatCount;
	}
	
	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
	   this.repeatCount = repeatCount;
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
		View layout = convertView;
	    if (convertView == null) {
		      layout = inflater.inflate(R.layout.foodbooklayout, null);
	    }
	    final Food food = this.foodList.get(position % foodList.size());
	    
//	    UI
//        .<ImageView>findViewById(layout, R.id.photo)
//        .setImageBitmap(BitmapFactory.decodeByteArray(food.getLargeImage(), 0, food.getLargeImage().length));

	    UI
        .<TextView>findViewById(layout, R.id.foodname)
        .setText(Html.fromHtml(food.getName()));
	    
	    RelativeLayout fooditemRL = (RelativeLayout) layout.findViewById(R.id.fooditem);
	    View foodstepper = fooditemRL.findViewById(R.id.food_stepper);
	    RelativeLayout fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
     	((EditText)fsRL.findViewById(R.id.quantity)).removeTextChangedListener(watchers.get(layout.hashCode()));
	    
     	TextView priceText = (TextView) fsRL.findViewById(R.id.price);
     	priceText.setTextSize(20);
     	priceText.setText(String.valueOf(food.getPrice())+"元/份");
	    
     	final EditText quantityText = (EditText)fsRL.findViewById(R.id.quantity);
        watchers.put(layout.hashCode(), new TextWatcher(){
        	
			@Override
			public void afterTextChanged(Editable text) {
				int quantity = 0;
				try{
					quantity = Integer.parseInt(text.toString());
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 0;
				}
				mOrder.addFood(food.getKey(), food.getName(), food.getPrice(), quantity);
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
     	
        quantityText.addTextChangedListener(watchers.get(layout.hashCode()));
        
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
        for(Entry<com.pitaya.bookingnow.app.model.Order.Food, Integer> entry : mOrder.getFoods().entrySet()){
        	if(entry.getKey().getKey().equals(food.getKey())){
        		 quantityText.setText(String.valueOf(entry.getValue()));
        		 hasFound = true;
        		 break;
        	}
        }
        if(!hasFound){
        	quantityText.setText("0");
        }
        
	    return layout;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View layout = convertView;
//	    if (convertView == null) {
//		      layout = inflater.inflate(R.layout.foodbooklayout, null);
//	    }
//	    final Food food = this.foodList.get(position % foodList.size());
//	    UI
//        .<ImageView>findViewById(layout, R.id.photo)
//        .setImageBitmap(BitmapFactory.decodeByteArray(food.getLargeImage(), 0, food.getLargeImage().length));
//
//	    UI
//        .<TextView>findViewById(layout, R.id.foodname)
//        .setText(Html.fromHtml(food.getName()));
//	    
//	    RelativeLayout fooditemRL = (RelativeLayout) layout.findViewById(R.id.fooditem);
//	    View foodstepper = fooditemRL.findViewById(R.id.food_stepper);
//	    RelativeLayout fsRL = null;
//	    
//	    if(foodstepper == null){
//     		//First time add foodstepper on the view
//     		foodstepper = inflater.inflate(R.layout.foodstepper, null);
//            RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
//            		 ViewGroup.LayoutParams.WRAP_CONTENT,
//                     ViewGroup.LayoutParams.WRAP_CONTENT);
//            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
//            foodstepper.setLayoutParams(fsRL_LP);
//            fooditemRL.addView(foodstepper);
//         	fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
//     	} else {
//     		fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
//     		((EditText)fsRL.findViewById(R.id.quantity)).removeTextChangedListener(watchers.get(layout.hashCode()));
//     	}
//	    
//     	TextView priceText = (TextView) fsRL.findViewById(R.id.price);
//     	priceText.setTextSize(20);
//     	priceText.setText(String.valueOf(food.getPrice())+"元/份");
//	    
//     	final EditText quantityText = (EditText)fsRL.findViewById(R.id.quantity);
//        watchers.put(layout.hashCode(), new TextWatcher(){
//        	
//			@Override
//			public void afterTextChanged(Editable text) {
//				int quantity = 0;
//				try{
//					quantity = Integer.parseInt(text.toString());
//				} catch(Exception e){
//					Log.e("FoodMenuView", "Fail to parse food quantity");
//					quantity = 0;
//				}
//				mOrder.addFood(food.getKey(), food.getName(), food.getPrice(), quantity);
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence text, int arg1,
//					int arg2, int arg3) {
//				quantityText.setSelection(text.length());
//			}
//
//			@Override
//			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
//			}
//        	
//        });
//     	
//        quantityText.addTextChangedListener(watchers.get(layout.hashCode()));
//        
//        ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){
//        	
//			@Override
//			public void onClick(View v) {
//				String current = quantityText.getText().toString();
//				int quantity = 0;
//				try{
//					quantity = Integer.parseInt(current) - 1;
//				} catch(Exception e){
//					Log.e("FoodMenuView", "Fail to parse food quantity");
//				}
//				if(quantity < 0){
//					return;
//				}
//				quantityText.setText(String.valueOf(quantity));
//			}
//        	
//        });
//        
//        ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				String current = quantityText.getText().toString();
//				int quantity = 0;
//				try{
//					quantity = Integer.parseInt(current) + 1;
//				} catch(Exception e){
//					Log.e("FoodMenuView", "Fail to parse food quantity");
//					quantity = 1;
//				}
//				quantityText.setText(String.valueOf(quantity));
//			}
//        	
//        });
//
//        boolean hasFound = false;
//        for(Entry<com.pitaya.bookingnow.app.domain.Order.Food, Integer> entry : mOrder.getFoods().entrySet()){
//        	if(entry.getKey().getKey().equals(food.getKey())){
//        		 quantityText.setText(String.valueOf(entry.getValue()));
//        		 hasFound = true;
//        		 break;
//        	}
//        }
//        if(!hasFound){
//        	quantityText.setText("0");
//        }
//        
//	    return layout;
//	}
	
	 public void removeData(int index) {
		 if (foodList.size() > 1) {
			 foodList.remove(index);
		 }
	 }

}
