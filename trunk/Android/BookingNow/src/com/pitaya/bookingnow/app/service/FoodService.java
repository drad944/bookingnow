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
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookinnow.app.util.Constants;
import com.pitaya.bookinnow.app.util.FileUtil;

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
	
	private static void getFoodImages(Context context, Food food, ProgressHandler handler){
		JSONObject jparam = new JSONObject();
		JSONObject jfood = new JSONObject();
		try {
			jfood.put("id", Long.parseLong(food.getKey()));
			jparam.put("food", jfood);
			HttpService.downloadFile("getSmallFood_Picture.action", context, 
					food.getSmallImageName(), new StringEntity(jparam.toString()), handler);
			HttpService.downloadFile("getLargeFood_Picture.action", context, 
					food.getLargeImageName(), new StringEntity(jparam.toString()), handler);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateMenuFoods(Context context, Map<String, ArrayList<Food>> foodsToUpdate, ProgressHandler handler){

		int index = 1;
		if(foodsToUpdate.get("new") != null){
			for(Food food : foodsToUpdate.get("new")){
				handler.setIndex(index++);
				getFoodImages(context, food, handler);
				DataService.addNewFood(context, food);
			}
		} else if(foodsToUpdate.get("delete") != null){
			for(Food food : foodsToUpdate.get("delete")){
				handler.setIndex(index++);
				DataService.removeFood(context, food.getKey());
				FileUtil.removeFile(context, food.getSmallImageName());
				FileUtil.removeFile(context, food.getLargeImageName());
				android.os.Message amsg = new android.os.Message();
	        	Bundle bundle = new Bundle();
	        	bundle.putInt("result", Constants.SUCCESS);
		        bundle.putString("detail", "success to remove food images");
		        amsg.setData(bundle);
		        handler.sendMessage(amsg);
			}
		} else if(foodsToUpdate.get("update") != null){
			for(Food food : foodsToUpdate.get("update")){
				handler.setIndex(index++);
				DataService.updateFood(context, food);
				if(food.getImageVersion() != null){
					getFoodImages(context, food, handler);
				} else {
					Log.i(TAG, "No need to update image of this food");
					android.os.Message amsg = new android.os.Message();
		        	Bundle bundle = new Bundle();
		        	bundle.putInt("result", Constants.SUCCESS);
			        bundle.putString("detail", "success to update food");
			        amsg.setData(bundle);
			        handler.sendMessage(amsg);
				}
			}
		}
	}
	
}
