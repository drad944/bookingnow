package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.entity.Order_Food_Detail_Table;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public class TestOrder_Food_DetailService {
	private IOrder_Food_DetailService food_detailService;

	public IOrder_Food_DetailService getFood_detailService() {
		return food_detailService;
	}

	public void setFood_detailService(
			IOrder_Food_DetailService food_detailService) {
		this.food_detailService = food_detailService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext(
				"src/applicationContext.xml");
		IOrder_Food_DetailService food_detailService = aCtx
				.getBean(IOrder_Food_DetailService.class);
		assertNotNull(food_detailService);
		this.food_detailService = food_detailService;
	}

	public void showFood_Detail(Order_Food_Detail food_Detail) {
		if (food_Detail != null) {
			System.out.println("Id : " + food_Detail.getId());
			System.out.println("Count : " + food_Detail.getCount());
			System.out.println("Food_id : " + food_Detail.getFood_id());
			System.out.println("Last_modify_time : "
					+ food_Detail.getLast_modify_time());
			System.out.println("Order_id : " + food_Detail.getOrder_id());
			System.out.println("Preference : " + food_Detail.getPreference());
			System.out.println("Status : " + food_Detail.getStatus());
			System.out.println("Enabled : " + food_Detail.getEnabled());
			System.out.println("IsFree : " + food_Detail.getIsFree());
			if (food_Detail.getFood() != null) {
				Food realFood = food_Detail.getFood();
				String space = "    ";
				System.out.println(space + "Id : " + realFood.getId());
				System.out.println(space + "Category : "
						+ realFood.getCategory());
				System.out.println(space + "Description : "
						+ realFood.getDescription());
				System.out.println(space + "Large_image_absolute_path : "
						+ realFood.getLarge_image_absolute_path());
				System.out.println(space + "Large_image_relative_path : "
						+ realFood.getLarge_image_relative_path());
				System.out.println(space + "Name : " + realFood.getName());
				System.out.println(space + "Small_image_absolute_path : "
						+ realFood.getSmall_image_absolute_path());
				System.out.println(space + "Small_image_relative_path : "
						+ realFood.getSmall_image_relative_path());
				System.out.println(space + "Large_image_size : "
						+ realFood.getLarge_image_size());
				System.out.println(space + "Period : " + realFood.getPeriod());
				System.out.println(space + "Price : " + realFood.getPrice());
				System.out.println(space + "Recommendation : "
						+ realFood.getRecommendation());
				System.out.println(space + "Small_image_size : "
						+ realFood.getSmall_image_size());
				System.out.println(space + "Status : " + realFood.getStatus());
				System.out
						.println(space + "Version : " + realFood.getVersion());
				System.out
						.println(space + "Enabled : " + realFood.getEnabled());
				System.out.println(space + "Large_image : "
						+ realFood.getLarge_image());
				System.out.println(space + "Small_image : "
						+ realFood.getSmall_image());
			}
			System.out.println();
		}
	}
	
	@Test
	public void testPowerSearchFood_Details() {
		SearchParams params = new SearchParams();
		params.setFood_detail_id(2l);
		params.setOffset(1);
		params.setRowCount(5);
		List<Integer> food_detailStatusList = new ArrayList<Integer>();
		food_detailStatusList.add(Constants.FOOD_NEW);
		food_detailStatusList.add(Constants.FOOD_CONFIRMED);
		
		params.setFood_detailStatusList(food_detailStatusList);

		List<Order_Food_Detail_Table> realFood_Details = food_detailService.powerSearchFood_Details(params);
		for (int i = 0; i < realFood_Details.size(); i++) {
			Order_Food_Detail_Table realFood_Detail = realFood_Details.get(i);
			System.out.println(realFood_Detail.getTable_id());
			System.out.println(realFood_Detail.getTable_address());
		}
	}

	@Test
	public void testSearchFood_Details() {
		SearchParams params = new SearchParams();
		params.setFood_detail_id(2l);
		params.setOffset(1);
		params.setRowCount(5);
		List<Integer> food_detailStatusList = new ArrayList<Integer>();
		food_detailStatusList.add(Constants.FOOD_NEW);
		food_detailStatusList.add(Constants.FOOD_CONFIRMED);
		food_detailService.searchFood_Details(params);
		params.setFood_detailStatusList(food_detailStatusList);

		List<Order_Food_Detail> realFood_Details = food_detailService.searchFood_Details(params);
		for (int i = 0; i < realFood_Details.size(); i++) {
			Order_Food_Detail realFood_Detail = realFood_Details.get(i);
			showFood_Detail(realFood_Detail);
		}
	}

	@Test
	public void testSearchOrder_Food_Details() {
		Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
		// tempFood_Detail.setStatus(Constants.FOOD_CONFIRMED);

		Food tempFood = new Food();
		tempFood.setPrice(12.0);
		tempFood_Detail.setFood(tempFood);

		List<Order_Food_Detail> realFood_Details = food_detailService
				.searchOrder_Food_Details(tempFood_Detail);
		for (int i = 0; i < realFood_Details.size(); i++) {
			Order_Food_Detail realFood_Detail = realFood_Details.get(i);
			showFood_Detail(realFood_Detail);
		}
	}

	@Test
	public void testSearchFullByPrimaryKeyAndOrderId() {
		SearchParams params = new SearchParams();
		params.setOrder_id((long) 1);
		params.setFood_detail_id((long) 2);

		Order_Food_Detail realFood_Detail = food_detailService
				.searchFullByPrimaryKeyAndOrderId(params);
		showFood_Detail(realFood_Detail);
	}

	@Test
	public void testUpdateFoodStatus() {
		Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
		tempFood_Detail.setId((long) 25);
		tempFood_Detail.setStatus(Constants.FOOD_COOKING);

		MyResult result = food_detailService.updateFoodStatus(tempFood_Detail);
		if (result.isExecuteResult()) {

			System.out.println("add new order successfully!");
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
						.next();
				System.out.println(entry.getKey().toString() + " : "
						+ entry.getValue().toString());
			}

		}

	}

	@Test
	public void testUpdateFoods() {
		Map<String, List<Order_Food_Detail>> changeFoods = new HashMap<String, List<Order_Food_Detail>>();
		Long orderId = 1L;
		List<Order_Food_Detail> newFood_Details = new ArrayList<Order_Food_Detail>();
		List<Order_Food_Detail> deleteFood_Details = new ArrayList<Order_Food_Detail>();
		List<Order_Food_Detail> updateFood_Details = new ArrayList<Order_Food_Detail>();

		for (int i = 0; i < 2; i++) {
			Order_Food_Detail newFood_Detail = new Order_Food_Detail();
			Food newFood = new Food();
			newFood.setId((long) (i + 4));
			newFood_Detail.setFood(newFood);

			newFood_Details.add(newFood_Detail);
		}

		for (int i = 0; i < 2; i++) {
			Order_Food_Detail deleteFood_Detail = new Order_Food_Detail();

			Food deleteFood = new Food();
			deleteFood.setId((long) (i + 2));
			deleteFood_Detail.setFood(deleteFood);
			deleteFood_Detail.setId((long) (i + 2));

			deleteFood_Details.add(deleteFood_Detail);
		}

		Order_Food_Detail updateFood_Detail = new Order_Food_Detail();
		Food updateFood = new Food();
		updateFood.setId((long) 1);
		updateFood_Detail.setId((long) 1);
		updateFood_Detail.setCount(10);
		updateFood_Detail.setFood(updateFood);
		updateFood_Details.add(updateFood_Detail);

		changeFoods.put("new", newFood_Details);
		changeFoods.put("delete", deleteFood_Details);
		changeFoods.put("update", updateFood_Details);

		MyResult result = food_detailService.updateFoods(changeFoods, orderId);
		if (result.isExecuteResult()) {
			for (int i = 0; i < newFood_Details.size(); i++) {
				showFood_Detail(newFood_Details.get(i));
			}
		} else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getErrorDetails();
			Iterator iter = falseResults.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
						.next();
				System.out.println(entry.getKey().toString() + " : "
						+ entry.getValue().toString());
			}

		}

	}

}
