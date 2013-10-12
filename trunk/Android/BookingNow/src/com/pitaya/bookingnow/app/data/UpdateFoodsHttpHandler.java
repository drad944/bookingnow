package com.pitaya.bookingnow.app.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.UpdateFood;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.util.ToastUtil;

public class UpdateFoodsHttpHandler extends HttpHandler{
	
	private final static String TAG = "UpdateFoodsHttpHandler";
	private Context mContext;
	private Order mOrder;
	private OrderDetailAdapter mAdapter;
	
	public UpdateFoodsHttpHandler(Context c, Order order, OrderDetailAdapter adapter){
		this.mContext = c;
		this.mOrder = order;
		this.mAdapter = adapter;
	}
	
	@Override  
    public void onSuccess(String action, String response) {
		boolean fail = false;
		boolean needrestore = false;
		try {
			JSONObject jresp = new JSONObject(response);
			if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
				fail = true;
			} else {
				if(jresp.has("new")){
					JSONArray jnew_foods = jresp.getJSONArray("new");
					for(int i=0; i < jnew_foods.length(); i++){
						JSONObject jorder_food = jnew_foods.getJSONObject(i);
						JSONObject jfood = jorder_food.getJSONObject("food");
						String food_id = String.valueOf(jfood.getLong("id"));
						Order.Food food = mOrder.searchFood(food_id);
						if(food != null){
							food.setId(jorder_food.getLong("id"));
							food.setStatus(jorder_food.getInt("status"));
							DataService.saveFoodId(mContext, mOrder, food);
						} else {
							Log.e(TAG, "Can not find food in order");
						}
					}
				}
				if(jresp.has("update")){
					//Foods fail to update because it's in cooking status
					JSONArray jupdate_foods = jresp.getJSONArray("update");
					if(jupdate_foods.length() > 0){
						needrestore = true;
						for(int i=0; i < jupdate_foods.length(); i++){
							JSONObject jorder_food = jupdate_foods.getJSONObject(i);
							JSONObject jfood = jorder_food.getJSONObject("food");
							String food_id = String.valueOf(jfood.getLong("id"));
							UpdateFood updateFood = new UpdateFood(food_id, jorder_food.getLong("id"), 
									null, jorder_food.getBoolean("isFree"), jorder_food.getInt("count"));
							if(jorder_food.has("preference")){
								updateFood.setPreference(jorder_food.getString("preference"));
							}
							DataService.updateFoodOfOrder(mContext, mOrder, updateFood);
						}
					}
				}
				if(jresp.has("delete")){
					//Foods fail to delete because it's in cooking status
					JSONArray jremove_foods = jresp.getJSONArray("delete");
					if(jremove_foods.length() > 0){
						needrestore = true;
						for(int i=0; i < jremove_foods.length(); i++){
							JSONObject jorder_food = jremove_foods.getJSONObject(i);
							JSONObject jfood = jorder_food.getJSONObject("food");
							String food_id = String.valueOf(jfood.getLong("id"));
							int quantity = jorder_food.getInt("count");
							Order.Food food = mOrder.new Food(food_id, null, 0f);
							food.setId(jorder_food.getLong("id"));
							food.setFree(jorder_food.getBoolean("isFree"));
							DataService.addFoodOfOrder(mContext, mOrder, food, quantity);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			fail = true;
		}
		if(!fail) {
			mOrder.markDirty(mContext, false);
			mOrder.resetUpdateFoods(mContext);
			if(needrestore){
				mOrder.enrichFoods(mContext);
				this.mAdapter.notifyDataSetChanged();
				ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitpartsuccess), Toast.LENGTH_LONG);
			} else {
				ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
			}
		} else {
			ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.operationerror), 
					Toast.LENGTH_LONG);
		}
	}
	
	@Override
	public void onFail(String action, int statusCode){
		ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.operationfail),
				Toast.LENGTH_SHORT);
	}
}
