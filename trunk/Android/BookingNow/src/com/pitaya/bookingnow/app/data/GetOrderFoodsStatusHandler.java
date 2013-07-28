package com.pitaya.bookingnow.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.pitaya.bookingnow.app.data.GetOrderFoodsHandler.AfterGetFoodsListener;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.service.DataService;

public class GetOrderFoodsStatusHandler extends HttpHandler{
	
	private Context mContext;
	private Order mOrder;
	private AfterGetFoodsStatusListener mListener;
	
	public GetOrderFoodsStatusHandler(Context c, Order order){
		this.mContext = c;
		this.mOrder = order;
	}
	
	public void setAfterGetFoodsStatusListener(AfterGetFoodsStatusListener l){
		this.mListener = l;
	}
	
	@Override
	public void onSuccess(String action, String response) {
		try {
			JSONObject jresp = new JSONObject(response);
			if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
				//TODO handle fail
			} else {
				JSONArray jorder_details = jresp.getJSONArray("food_details");
				for(int i=0; i < jorder_details.length(); i++){
					JSONObject jorder_detail = jorder_details.getJSONObject(i);
					JSONObject jfood = jorder_detail.getJSONObject("food");
					Order.Food food = mOrder.searchFood(String.valueOf(jfood.get("id")));
					if(food != null){
						food.setStatus(jorder_detail.getInt("status"));
					}
				}
				if(mListener != null){
					mListener.afterGetFoodsStatus();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFail(String action, int statuscode) {
		Log.e("getOrderFoodsHandler", "[OrderService.getFoodsOfOrder] Network error:" + statuscode);
	}
	
	public interface AfterGetFoodsStatusListener{
		public void afterGetFoodsStatus();
	}
}
