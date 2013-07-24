package com.pitaya.bookingnow.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.OrderDetailPreviewActivity;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnDirtyChangedListener;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;

public class OrderDetailPreviewAdapter extends OrderDetailAdapter {
	
	private static final String TAG = "OrderDetailPreviewAdapter";
	
	public OrderDetailPreviewAdapter(Context c, View view, Order order)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, order);
	}
	
	private void setViewByOrderStatus(final View itemView){
		if(itemView == null || this.mOrder == null){
			return;
		}
		((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
		((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
		final Button confirmBtn = (Button)itemView.findViewById(R.id.action1);
		final Button resetBtn = (Button)itemView.findViewById(R.id.action2);
		switch(mOrder.getStatus()){
			case Constants.ORDER_NEW:
				confirmBtn.setText(R.string.commit);
				resetBtn.setText(R.string.reset);

				confirmBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
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
											int status = jorder_food.getInt("status");
											JSONObject jfood = jorder_food.getJSONObject("food");
											String food_id = String.valueOf(jfood.getLong("id"));
											Order.Food food = mOrder.searchFood(food_id);
											if(food != null){
												food.setId(jorder_food.getLong("id"));
												food.setStatus(status);
												DataService.saveFoodId(mContext, mOrder, food);
											} else {
												Log.e(TAG, "Can not find food in order");
											}
										}
										mOrder.setStatus(jorder.getInt("status"));
										mOrder.markDirty(mContext, false);
										ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
						    }
							
							@Override
							public void onFail(String action, int statusCode){
								ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitfail), Toast.LENGTH_SHORT);
							}
							
						});
					}
					
				});
				
				resetBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
						DataService.removeFoodsOfOrder(mContext, mOrder.getOrderKey());
						mOrder.removeAllFood();
						OrderDetailPreviewAdapter.this.notifyDataSetChanged();
					}
					
				});
				break;
			case Constants.ORDER_WAITING:
			case Constants.ORDER_COMMITED:
				confirmBtn.setText(R.string.commitupdate);
				resetBtn.setText(R.string.cancelupdate);
				if(mOrder.isDirty()){
					confirmBtn.setAlpha(1f);
					resetBtn.setAlpha(1f);
					confirmBtn.setClickable(true);
					resetBtn.setClickable(true);
					confirmBtn.setOnClickListener(new OnClickListener(){
						
						@Override
						public void onClick(View v) {
							//TODO update order to server
							OrderService.updateFoodsOfOrder(mOrder, new UpdateFoodsHttpHandler(mContext, mOrder, OrderDetailPreviewAdapter.this));
						}
						
					});
					
					resetBtn.setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							//TODO restore order content from server
							OrderDetailPreviewAdapter.this.notifyDataSetChanged();
						}
						
					});
				} else {
					confirmBtn.setAlpha(0.5f);
					resetBtn.setAlpha(0.5f);
					confirmBtn.setClickable(false);
					resetBtn.setClickable(false);
				}
				break;
		}
	}
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
	    	final View itemView = View.inflate(mContext, R.layout.orderbottom, null);
	    	((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
			if(mOrder.getFoods().size() == 0){
				((Button)itemView.findViewById(R.id.action1)).setVisibility(View.GONE);
				((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setText(R.string.hintinmenu);
			} else {
		    	setViewByOrderStatus(itemView);
				mOrder.addOnStatusChangedListener(new OnOrderStatusChangedListener(){

					@Override
					public void onOrderStatusChanged(Order tikcet, int status) {
						setViewByOrderStatus(itemView);
					}
					
				});
				mOrder.setOnDirtyChangedListener(new Order.OnDirtyChangedListener(){

					@Override
					public void onDirtyChanged(Order order, boolean flag) {
						setViewByOrderStatus(mView.findViewById(R.id.orderbottom));
					}
					
				});
			}
			return itemView;
    	} else {
    		return null;
    	}
    }

}
