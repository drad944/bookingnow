package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.*;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookinnow.app.util.Constants;

public class OrderService {
	
	public static void commitWaitingOrder(Order order, HttpHandler callback){
		JSONObject orderDetail = new JSONObject();
		JSONObject orderJson = new JSONObject();
		try {
			orderDetail.put("id", Long.parseLong(order.getOrderKey()));
			orderDetail.put("status", Constants.ORDER_COMMITED);
			JSONArray tableids = new JSONArray(order.getTableIds());
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
	
	public static void commitOrder(Order order, HttpHandler callback){
		JSONObject orderDetail = new JSONObject();
		JSONObject orderJson = new JSONObject();
		try {
			orderDetail.put("id", order.getOrderKey());
			orderDetail.put("status", Constants.ORDER_COMMITED);
			JSONArray food_details = new JSONArray();
			int index = 0;
			for(Entry<Food, Integer> food_entry : order.getFoods().entrySet()){
				JSONObject order_food_json = new JSONObject();
				order_food_json.put("food_id", food_entry.getKey());
				order_food_json.put("count", food_entry.getValue());
				food_details.put(index++, order_food_json);
			}
			orderDetail.put("food_details", food_details);
			
			orderJson.put("order", orderDetail);
			HttpService.post("commitOrder.action", new StringEntity(orderJson.toString()), callback);
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
