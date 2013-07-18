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
import com.pitaya.bookinnow.app.util.Constants;

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
			jorder.put("status", Constants.ORDER_COMMITED);
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
					jfood.put("price", food_entry.getKey().getPrice());
					jfood.put("version", versions.get(food_entry.getKey().getKey()));
					jorder_food.put("count", food_entry.getValue());
					jorder_food.put("food", jfood);
					jorder_food.put("isFree", food_entry.getKey().isFree());
					food_details.put(index++, jorder_food);
				}
				jorder.put("food_details", food_details);
			}
			jreq.put("order", jorder);
			HttpService.post("commitOrder.action", new StringEntity(jreq.toString()), callback);
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
