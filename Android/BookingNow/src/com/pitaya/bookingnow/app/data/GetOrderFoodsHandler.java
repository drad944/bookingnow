package com.pitaya.bookingnow.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.service.DataService;

public class GetOrderFoodsHandler extends HttpHandler{
	
	private Context mContext;
	private Order mOrder;
	private AfterGetFoodsListener mListener;
	
	public GetOrderFoodsHandler(Context c, Order order){
		this.mContext = c;
		this.mOrder = order;
	}
	
	public void setAfterGetFoodsListener(AfterGetFoodsListener l){
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
				mOrder.resetUpdateFoods(mContext);
				mOrder.resetAllFoods(mContext);
				for(int i=0; i < jorder_details.length(); i++){
					JSONObject jorder_detail = jorder_details.getJSONObject(i);
					JSONObject jfood = jorder_detail.getJSONObject("food");
					Order.Food food = mOrder.new Food(String.valueOf(jfood.getLong("id")), null, 0f);
					DataService.getFoodDetails(mContext, food);
					food.setId(jorder_detail.getLong("id"));
					food.setFree(jorder_detail.getBoolean("isFree"));
					food.setStatus(jorder_detail.getInt("status"));
					DataService.updateOrderDetails(mContext, mOrder, food, jorder_detail.getInt("count"));
				}
				if(mListener != null){
					mListener.afterGetFoods();
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
	
	public interface AfterGetFoodsListener{
		public void afterGetFoods();
	}
	
}
