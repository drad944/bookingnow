package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.ProgressHandler;
import com.pitaya.bookingnow.app.data.FileDownloadHandler;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.FileUtil;

public class FoodService {
	
	private static final String TAG = "FoodService";
	
	private static ArrayList<Food> getFoodListFromJSONArray(JSONArray foodArray){
		ArrayList<Food> foods = new ArrayList<Food>();
		for(int i=0; i < foodArray.length(); i++){
			JSONObject jfood = null;
			try {
				jfood = foodArray.getJSONObject(i);
			} catch (JSONException e) {
				Log.e(TAG, "Fail to get food json object from json array: " + foodArray.toString());
				e.printStackTrace();
				continue;
			}
			Food food = new Food();
			food.fromJSONObject(jfood);
			foods.add(food);
		}
		return foods;
	}
	
	public static Map<String, ArrayList<Food>> getUpdatedFoodsList(String response) throws JSONException{
		JSONObject jresp = new JSONObject(response);
		if(jresp.has("result") && jresp.getInt("result") == Constants.FAIL){
			Log.e(TAG, "Fail to get updated food list response");
			return null;
		}
		Map<String, ArrayList<Food>> foodsToUpdate = new HashMap<String, ArrayList<Food>>();
		if(jresp.getJSONArray("new") != null){
			JSONArray newFoodArray =  jresp.getJSONArray("new");
			foodsToUpdate.put("new", getFoodListFromJSONArray(newFoodArray));
		}
		if(jresp.getJSONArray("delete") != null){
			JSONArray deleteFoodArray =  jresp.getJSONArray("delete");
			foodsToUpdate.put("delete", getFoodListFromJSONArray(deleteFoodArray));
		}
		if(jresp.getJSONArray("update") != null){
			JSONArray updateFoodArray =  jresp.getJSONArray("update");
			foodsToUpdate.put("update", getFoodListFromJSONArray(updateFoodArray));
		}
		return foodsToUpdate;
	}
	
	public static void checkUpdate(ArrayList<Food> foods, HttpHandler callback){
		 JSONObject jparam = new JSONObject();
		 JSONArray check_foods = new JSONArray();
		 for(int i = 0; i < foods.size(); i++) {
			 	Food food = foods.get(i);
				JSONObject jfood = new JSONObject();
				food.toJSONObject(jfood);
				try {
					check_foods.put(i, jfood);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		 }
		 try {
			jparam.put("clientMenuFoods", check_foods);
			HttpService.post("updateMenuFood.action", new StringEntity(jparam.toString()), callback);
		 } catch (JSONException e) {
			e.printStackTrace();
		 } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		 }
	}
	
	private static void getFoodImages(final Context context, final Food food, final ProgressHandler handler, final int type){
		final JSONObject jparam = new JSONObject();
		JSONObject jfood = new JSONObject();
		android.os.Message amsg = new android.os.Message();
        Bundle bundle = new Bundle();
        amsg.setData(bundle);
        boolean error = false;
        try {
			jfood.put("id", Long.parseLong(food.getKey()));
			jparam.put("food", jfood);
			
			FileDownloadHandler fileHandler = new FileDownloadHandler(){
				
				@Override
				public void onSuccess(String action, String response){
					Log.i(TAG, "["+food.getKey()+"][" + action + "] response: " + response);
		        	android.os.Message amsg = new android.os.Message();
			        Bundle bundle = new Bundle();
			        amsg.setData(bundle);
			        bundle.putString(HttpHandler.ACTION_TYPE, action);
			        bundle.putString("key", food.getKey());
			        String filename = null;
			        if(action.equals("findSmallFood_Picture.action")){
			        	filename = food.getSmallImageName();
			        } else if(action.equals("findLargeFood_Picture.action")){
			        	filename =  food.getLargeImageName();
			        }
					if(FileUtil.writeFile(context, filename, this.fileBytes)){
				        if(action.equals("findSmallFood_Picture.action")){
				        	try {
								HttpService.getFileViaPost("findLargeFood_Picture.action", new StringEntity(jparam.toString()), this);
							} catch (UnsupportedEncodingException e) {
								bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
								bundle.putInt(HttpHandler.ERROR_CODE, Constants.GET_FOOD_IMAGE_ERROR);
								handler.sendMessage(amsg);
								e.printStackTrace();
							}
				        } else if(action.equals("findLargeFood_Picture.action")){
				        	if(type == 0){
				        		DataService.addNewFood(context, food);
				        	} else {
				        		DataService.updateFood(context, food);
				        	}
				        	bundle.putInt(HttpHandler.RESULT, Constants.SUCCESS);
				        	handler.sendMessage(amsg);
				        }
					} else {
						bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
						bundle.putInt(HttpHandler.ERROR_CODE, Constants.GET_FOOD_IMAGE_ERROR);
			        	handler.sendMessage(amsg);
					}
					this.fileBytes = null;
				}
				
				@Override
				public void onFail(String action, int statuscode){
		        	android.os.Message amsg = new android.os.Message();
			        Bundle bundle = new Bundle();
			        bundle.putString("key", food.getKey());
			        bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
			        bundle.putString(HttpHandler.ACTION_TYPE, action);
			        bundle.putInt(HttpHandler.ERROR_CODE, statuscode);
			        amsg.setData(bundle);
			        handler.sendMessage(amsg);
				}
				
			};
			
			HttpService.getFileViaPost("findSmallFood_Picture.action", new StringEntity(jparam.toString()), fileHandler);
			
		} catch (NumberFormatException e) {
			error = true;
			e.printStackTrace();
		} catch (JSONException e) {
			error = true;
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			error = true;
			e.printStackTrace();
		}
        if(error){
			bundle.putInt(HttpHandler.RESULT, Constants.FAIL);
			bundle.putInt(HttpHandler.ERROR_CODE, Constants.GET_FOOD_IMAGE_ERROR);
		    handler.sendMessage(amsg);
        }
	}
	
	public static void updateMenuFoods(Context context, Map<String, ArrayList<Food>> foodsToUpdate, ProgressHandler handler){

		if(foodsToUpdate.get("delete") != null){
			for(Food food : foodsToUpdate.get("delete")){
				FileUtil.removeFile(context, food.getSmallImageName());
				FileUtil.removeFile(context, food.getLargeImageName());
				DataService.removeFood(context, food.getKey());
				android.os.Message amsg = new android.os.Message();
	        	Bundle bundle = new Bundle();
	        	bundle.putInt(HttpHandler.RESULT, Constants.SUCCESS);
	        	bundle.putString("key", food.getKey());
		        bundle.putString(HttpHandler.RESPONSE, "success to remove food");
		        amsg.setData(bundle);
		        handler.sendMessage(amsg);
			}
		}
		if(foodsToUpdate.get("update") != null){
			for(Food food : foodsToUpdate.get("update")){
				if(food.getImageVersion() != null){
					getFoodImages(context, food, handler, 1);
				} else {
					Log.i(TAG, "The image of this food is latest");
					DataService.updateFood(context, food);
					android.os.Message amsg = new android.os.Message();
		        	Bundle bundle = new Bundle();
		        	bundle.putInt(HttpHandler.RESULT, Constants.SUCCESS);
		        	bundle.putString("key", food.getKey());
			        bundle.putString(HttpHandler.RESPONSE, "success to update food");
			        amsg.setData(bundle);
			        handler.sendMessage(amsg);
				}
			}
		}
		if(foodsToUpdate.get("new") != null){
			for(Food food : foodsToUpdate.get("new")){
				getFoodImages(context, food, handler, 0);
			}
		}
	}
	
}
