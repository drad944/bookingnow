package com.pitaya.bookingnow.app.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pitaya.bookingnow.app.*;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnDirtyChangedListener;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.HttpService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.views.OrderDetailFragment;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookinnow.app.util.Constants;
import com.pitaya.bookinnow.app.util.ToastUtil;

public class OrderDetailFragmentAdapter extends OrderDetailAdapter{
	
	private static final String TAG = "OrderDetailFragmentAdapter";
	private Context mContext;
	
	public OrderDetailFragmentAdapter(Context c, View view, Order order)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, order);
		this.mContext = c;
	}
	
	public void setOrder(Order order){
		this.mOrder = order;
	}
	
	private void setViewByOrderStatus(View view){
		if(view == null || this.mOrder == null){
			return;
		}
		final View itemView = view;
		final Button actBtn1 = (Button)itemView.findViewById(R.id.action1);
		final Button actBtn2 = (Button)itemView.findViewById(R.id.action2);
		final Button actBtn3 = (Button)itemView.findViewById(R.id.action3);
		TextView hinttext = ((TextView)itemView.findViewById(R.id.hint));
		switch(this.mOrder.getStatus()){
			case Constants.ORDER_NEW:
				this.mOrder.setOnDirtyChangedListener(null);
				actBtn1.setText(R.string.commit);
				actBtn2.setText(R.string.modification);
				actBtn3.setText(R.string.cancelorder);
				hinttext.setVisibility(View.GONE);
				actBtn1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO commit the order and remove it from local database
						OrderService.commitNewOrder(mContext, mOrder, new HttpHandler(){
						    
							@Override  
						    public void onSuccess(String action, String response) {
								try {
									JSONObject jresp = new JSONObject(response);
									if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
										//TODO handle fail
									} else {
										JSONObject jorder = jresp.getJSONObject("order");
										JSONArray jorder_foods = jorder.getJSONArray("food_details");
										for(int i=0; i < jorder_foods.length(); i++){
											JSONObject jorder_food = jorder_foods.getJSONObject(i);
											JSONObject jfood = jorder_food.getJSONObject("food");
											String food_id = String.valueOf(jfood.getLong("id"));
											Order.Food food = mOrder.searchFood(food_id);
											if(food != null){
												food.setId(jorder_food.getLong("id"));
											} else {
												Log.e(TAG, "Can not find food in order");
											}
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								//DataService.removeOrder(mContext, mOrder.getOrderKey());
								ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
								mOrder.setStatus(Constants.ORDER_COMMITED);
						    }
							
							@Override
							public void onFail(String action, int statusCode){
								ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitfail), Toast.LENGTH_SHORT);
							}
							
						});
					}
					
				});
				actBtn2.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						((OrderDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
								.findFragmentById(R.id.orderdetail)).modifyOrder();
					}
					
				});
				actBtn3.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO send request to cancel a order and remove it from local database
						DataService.removeOrder(mContext, mOrder.getOrderKey());
					}
					
				});
				break;
			case Constants.ORDER_COMMITED:
				hinttext.setVisibility(View.GONE);
				mOrder.setOnDirtyChangedListener(new Order.OnDirtyChangedListener(){

					@Override
					public void onDirtyChanged(Order order, boolean flag) {
						 setViewByOrderStatus(itemView);
					}
					
				});
				if(this.mOrder.isDirty()){
    				actBtn1.setText(R.string.commitupdate);
    				actBtn2.setText(R.string.cancelupdate);
    				actBtn3.setVisibility(View.GONE);
    				actBtn1.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO update the order to server
							ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
							mOrder.markDirty(false);
						}
    					
    				});
    				actBtn2.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO send request to get the order info again and restore mOrder
							OrderDetailFragmentAdapter.this.notifyDataSetChanged();
						}
    					
    				});
				} else {
    				actBtn1.setText(R.string.pay);
    				actBtn2.setText(R.string.modification);
    				actBtn3.setText(R.string.cancelorder);
    				actBtn3.setVisibility(View.VISIBLE);
    				actBtn1.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO request to pay for the order
							mOrder.setStatus(Constants.ORDER_PAYING);
						}
    					
    				});
    				actBtn2.setOnClickListener(new OnClickListener(){
    					
						@Override
						public void onClick(View v) {
							((OrderDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
									.findFragmentById(R.id.orderdetail)).modifyOrder();
						}
    					
    				});
    				actBtn3.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO send request to cancel a order, if any food is in cooking, this will fail
						}
    					
    				});
				}
				break;
			case Constants.ORDER_PAYING:
				actBtn1.setVisibility(View.GONE);
				actBtn2.setVisibility(View.GONE);
				actBtn3.setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.VISIBLE);
				((TextView)itemView.findViewById(R.id.hint)).setText(R.string.paying);
				break;
			case Constants.ORDER_FINISHED:
				actBtn1.setVisibility(View.GONE);
				actBtn2.setVisibility(View.GONE);
				actBtn3.setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.VISIBLE);
				((TextView)itemView.findViewById(R.id.hint)).setText(R.string.finish);
				break;
		};
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
    		final View itemView = View.inflate(mContext, R.layout.orderbottom, null);
    		if(mOrder.getFoods().size() != 0){
    			((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
    			this.setViewByOrderStatus(itemView);
    			mOrder.addOnStatusChangedListener(new OnOrderStatusChangedListener(){

					@Override
					public void onOrderStatusChanged(Order tikcet, int status) {
						setViewByOrderStatus(itemView);
					}
    				
    			});
    		} else {
    			Button act1Btn = ((Button)itemView.findViewById(R.id.action1));
    			act1Btn.setText(R.string.hintinorder);
    			act1Btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						((OrderDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
								.findFragmentById(R.id.orderdetail)).modifyOrder();
					}
    				
    			});
    			((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
				((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
    		}
    		return itemView;
    	} else {
    		return null;
    	}
	}
    	
}
