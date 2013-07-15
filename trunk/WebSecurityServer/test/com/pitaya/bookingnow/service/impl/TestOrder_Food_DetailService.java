package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Order_Food_Detail;
import com.pitaya.bookingnow.service.IOrder_Food_DetailService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;


public class TestOrder_Food_DetailService {
	private IOrder_Food_DetailService food_detailService;
		
	public IOrder_Food_DetailService getFood_detailService() {
		return food_detailService;
	}

	public void setFood_detailService(IOrder_Food_DetailService food_detailService) {
		this.food_detailService = food_detailService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		IOrder_Food_DetailService food_detailService = aCtx.getBean(IOrder_Food_DetailService.class);
		assertNotNull(food_detailService);
		this.food_detailService = food_detailService;
	}
	 
	
	@Test
	 public void testSearchOrder_Food_Details() {
		 Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
		 //tempFood_Detail.setStatus(Constants.FOOD_CONFIRMED);
		 
		 Food tempFood = new Food();
		 tempFood.setPrice(12.0);
		 tempFood_Detail.setFood(tempFood);
		 
		 List<Order_Food_Detail> realFood_Details = food_detailService.searchOrder_Food_Details(tempFood_Detail);
		 for (int i = 0; i < realFood_Details.size(); i++) {
			 Order_Food_Detail realFood_Detail = realFood_Details.get(i);
			 if (realFood_Detail != null) {
				System.out.println("Id : " + realFood_Detail.getId());
				System.out.println("Count : " + realFood_Detail.getCount());
				System.out.println("Food_id : " + realFood_Detail.getFood_id());
				System.out.println("Last_modify_time : " + realFood_Detail.getLast_modify_time());
				System.out.println("Order_id : " + realFood_Detail.getOrder_id());
				System.out.println("Preference : " + realFood_Detail.getPreference());
				System.out.println("Status : " + realFood_Detail.getStatus());
				System.out.println("Enabled : " + realFood_Detail.getEnabled());
				System.out.println("IsFree : " + realFood_Detail.getIsFree());
				if (realFood_Detail.getFood() != null) {
					Food realFood = realFood_Detail.getFood();
					String space = "    ";
					System.out.println(space + "id : " + realFood.getId());
					System.out.println(space + "Category : " + realFood.getCategory());
					System.out.println(space + "Description : " + realFood.getDescription());
					System.out.println(space + "Name : " + realFood.getName());
					System.out.println(space + "Period : " + realFood.getPeriod());
					System.out.println(space + "Picture_id : " + realFood.getPicture_id());
					System.out.println(space + "Price : " + realFood.getPrice());
					System.out.println(space + "Recommendation : " + realFood.getRecommendation());
					System.out.println(space + "Status : " + realFood.getStatus());
					System.out.println(space + "Version : " + realFood.getVersion());
				}
				System.out.println();
			}
		}
	 }
	 
	 
	 @Test
	 public void testUpdateFoodStatus() {
		 Order_Food_Detail tempFood_Detail = new Order_Food_Detail();
		 tempFood_Detail.setId((long) 25);
		 tempFood_Detail.setStatus(Constants.FOOD_COOKING);
		 
		 MyResult result = food_detailService.updateFoodStatus(tempFood_Detail);
		 if (result.isResult()) {
				
				System.out.println("add new order successfully!");
		 } else {
			System.out.println("add new order failed!");
			Map<String, String> falseResults = result.getResultDetails();
			Iterator iter = falseResults.entrySet().iterator(); 
			while (iter.hasNext()) { 
			    Map.Entry<String,String> entry = (Map.Entry<String,String>) iter.next();
			    System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
			} 
			
		}
		 
	 }
	 
}
