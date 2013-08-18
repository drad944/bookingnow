package com.pitaya.bookingnow.app.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.CookingItem;

public class CookingItemService {
	
	public static void getCookingItems(ArrayList<Integer> status, Long begin_id, int count, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jparams = new JSONObject();
		try {
			jparams.put("food_detailStatusList", new JSONArray(status));
			jparams.put("food_detail_id", begin_id);
			jparams.put("rowCount", count);
			jreq.put("params", jparams);
			HttpService.post("searchFoodsInFood_Detail.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateStatus(Long id, int status, HttpHandler callback){
		JSONObject jreq = new JSONObject();
		JSONObject jfood_detail = new JSONObject();
		try {
			jfood_detail.put("id", id);
			jfood_detail.put("status", status);
			jreq.put("food_detail", jfood_detail);
			HttpService.post("updateStatusOfFood_Detail.action", new StringEntity(jreq.toString()), callback);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
