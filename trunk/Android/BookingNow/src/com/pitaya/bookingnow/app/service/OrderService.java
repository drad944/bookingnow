package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.*;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.util.Constants;

public class OrderService {
	
	public static void getOrderByStatus(ArrayList<Integer> statuses, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jparams = new JSONObject(); 
		try {
			jparams.put("orderStatusList", new JSONArray(statuses));
			jparams.put("user_id", UserManager.getUserId());
			jreq.put("params", jparams);
			HttpService.post("searchByStatusOfOrder.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void submitWaitingOrder(String customer, String phone, int count, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jorder = new JSONObject();
		try {
			JSONObject juser = new JSONObject();
			juser.put("id", UserManager.getUserId());
			jorder.put("user", juser);
			JSONObject jcustomer = new JSONObject();
			jcustomer.put("name", customer);
			jcustomer.put("phone", phone);
			jorder.put("customer", jcustomer);
			jorder.put("customer_count", count);
			jreq.put("order", jorder);
			HttpService.post("submitWaitingOrder.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void submitOrder(ArrayList<Table> tables, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jorder = new JSONObject();
		try {
			JSONArray jtables = new JSONArray();
			for(int i=0; i < tables.size(); i++){
				JSONObject jtabledetail = new JSONObject();
				JSONObject jtable = new JSONObject();
				jtable.put("id", tables.get(i).getId());
				jtabledetail.put("table", jtable);
				jtables.put(i, jtabledetail);
			} 
			jorder.put("table_details", jtables);
			JSONObject juser = new JSONObject();
			juser.put("id", UserManager.getUserId());
			jorder.put("user", juser);
			jreq.put("order", jorder);
			HttpService.post("submitNewOrder.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateOrder(Order order, HttpHandler callback){
		
	}
	
	public static void updateFoodsOfOrder(Order order, HttpHandler callback){
		Map<String, ArrayList<UpdateFood>> foods = order.getUpdateFoods();
		JSONObject jreq = new JSONObject();
		JSONObject jupdatefoods = new JSONObject();
		JSONArray jfoods;
		if(foods.get(Order.getUpdateType(Order.ADDED)) != null && foods.get(Order.getUpdateType(Order.ADDED)).size() > 0){
			jfoods = new JSONArray();
			ArrayList<UpdateFood> newFoods = foods.get(Order.getUpdateType(Order.ADDED));
			int i = 0;
			try {
				for(UpdateFood food : newFoods){
					JSONObject jorder_food = new JSONObject();
					JSONObject jfood = new JSONObject();
					jfood.put("id", Long.parseLong(food.getFoodKey()));
					jfood.put("version", food.getVersion());
					jorder_food.put("food", jfood);
					jorder_food.put("count", food.getQuantity());
					jorder_food.put("isFree", food.isFree());
					jfoods.put(i++, jorder_food);
				}
				jupdatefoods.put(Order.getUpdateType(Order.ADDED), jfoods);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(foods.get(Order.getUpdateType(Order.UPDATED)) != null && foods.get(Order.getUpdateType(Order.UPDATED)).size() > 0){
			jfoods = new JSONArray();
			ArrayList<UpdateFood> updateFoods = foods.get(Order.getUpdateType(Order.UPDATED));
			int i = 0;
			try {
				for(UpdateFood food : updateFoods){
					JSONObject jorder_food = new JSONObject();
					JSONObject jfood = new JSONObject();
					jfood.put("id", Long.parseLong(food.getFoodKey()));
					jfood.put("version", food.getVersion());
					jorder_food.put("food", jfood);
					jorder_food.put("id", food.getRefId());
					jorder_food.put("count", food.getQuantity());
					jorder_food.put("isFree", food.isFree());
					jfoods.put(i++, jorder_food);
				}
				jupdatefoods.put(Order.getUpdateType(Order.UPDATED), jfoods);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if(foods.get(Order.getUpdateType(Order.REMOVED)) != null && foods.get(Order.getUpdateType(Order.REMOVED)).size() > 0){
			jfoods = new JSONArray();
			ArrayList<UpdateFood> deleteFoods = foods.get(Order.getUpdateType(Order.REMOVED));
			int i = 0;
			try {
				for(UpdateFood food : deleteFoods){
					JSONObject jorder_food = new JSONObject();
					JSONObject jfood = new JSONObject();
					jfood.put("id", Long.parseLong(food.getFoodKey()));
					jorder_food.put("food", jfood);
					jorder_food.put("id", food.getRefId());
					jfoods.put(i++, jorder_food);
				}
				jupdatefoods.put(Order.getUpdateType(Order.REMOVED), jfoods);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			jreq.put("changeFoods", jupdatefoods);
			jreq.put("orderId", Long.parseLong(order.getOrderKey()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			HttpService.post("updateFoodsOfFood_Detail.action", new StringEntity(jreq.toString()), callback);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void commitWaitingOrder(Order order, HttpHandler callback){
		JSONObject orderDetail = new JSONObject();
		JSONObject orderJson = new JSONObject();
		try {
			orderDetail.put("id", Long.parseLong(order.getOrderKey()));
			orderDetail.put("status", Constants.ORDER_COMMITED);
			JSONArray tableids = new JSONArray(order.getTables());
			orderDetail.put("table_details", tableids);
			JSONObject user = new JSONObject();
			user.put("id", order.getSubmitterId());
			orderDetail.put("user", user);
			orderJson.put("order", orderDetail);
			HttpService.post("commitOrder.action", new StringEntity(orderJson.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void commitNewOrder(Context context, Order order, HttpHandler callback){
		JSONObject jorder = new JSONObject();
		JSONObject jreq = new JSONObject();
		try {
			jorder.put("id", Long.parseLong(order.getOrderKey()));
			JSONObject juser = new JSONObject();
			juser.put("id", UserManager.getUserId());
			jorder.put("user", juser);
			if(order.getFoods() != null && order.getFoods().size() > 0){
				Map<String, Long> versions = new HashMap<String, Long>();
				for(Entry<Food, Integer> food_entry : order.getFoods().entrySet()){
					versions.put(food_entry.getKey().getKey(), 0L);
				}
				DataService.getFoodVersions(context, versions);
				JSONArray food_details = new JSONArray();
				int index = 0;
				for(Entry<Food, Integer> food_entry : order.getFoods().entrySet()){
					JSONObject jorder_food = new JSONObject();
					JSONObject jfood = new JSONObject();
					jfood.put("id", Long.parseLong(food_entry.getKey().getKey()));
					jfood.put("version", versions.get(food_entry.getKey().getKey()));
					jorder_food.put("count", food_entry.getValue());
					jorder_food.put("food", jfood);
					jorder_food.put("isFree", food_entry.getKey().isFree());
					food_details.put(index++, jorder_food);
				}
				jorder.put("food_details", food_details);
			}
			jreq.put("order", jorder);
			if(order.getTableNum() != null){
				jorder.put("status", Constants.ORDER_COMMITED);
				HttpService.post("commitOrder.action", new StringEntity(jreq.toString()), callback);
			} else {
				jorder.put("status", Constants.ORDER_WAITING);
				HttpService.post("updateFoodsOfWaitingOrder.action", new StringEntity(jreq.toString()), callback);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void getAvailableTables(int status, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jtable = new JSONObject();
		try{
			jtable.put("status", status);
			jreq.put("table", jtable);
			HttpService.post("searchTable.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
