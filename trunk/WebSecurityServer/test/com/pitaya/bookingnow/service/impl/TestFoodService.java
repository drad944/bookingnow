package com.pitaya.bookingnow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.service.IFoodService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;


public class TestFoodService extends TestCase{
	public TestFoodService(String testMethod) {
	       super(testMethod);
	    }
	private IFoodService foodService;
		
	public IFoodService getFoodService() {
		return foodService;
	}

	public void setFoodService(IFoodService foodService) {
		this.foodService = foodService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		IFoodService foodService = aCtx.getBean(IFoodService.class);
		assertNotNull(foodService);
		this.foodService = foodService;
	}
	
	 public void showFood(Food food) {
	    	if (food != null) {
				System.out.println("Id : " + food.getId());
				System.out.println("Category : " + food.getCategory());
				System.out.println("Description : " + food.getDescription());
				System.out.println("Large_image_absolute_path : " + food.getLarge_image_absolute_path());
				System.out.println("Large_image_relative_path : " + food.getLarge_image_relative_path());
				System.out.println("Name : " + food.getName());
				System.out.println("Small_image_absolute_path : " + food.getSmall_image_absolute_path());
				System.out.println("Small_image_relative_path : " + food.getSmall_image_relative_path());
				System.out.println("Large_image_size : " + food.getLarge_image_size());
				System.out.println("Period : " + food.getPeriod());
				System.out.println("Price : " + food.getPrice());
				System.out.println("Recommendation : " + food.getRecommendation());
				System.out.println("Small_image_size : " + food.getSmall_image_size());
				System.out.println("Status : " + food.getStatus());
				System.out.println("Version : " + food.getVersion());
				System.out.println("Enabled : " + food.getEnabled());
				System.out.println("Large_image : " + food.getLarge_image());
				System.out.println("Small_image : " + food.getSmall_image());
			}
	    }
	 
	 @Test
	 public void testAdd() {
		 init();
		 Long timeLong = new Date().getTime();
		 Food newFood = new Food();
		 newFood.setCategory("sichuan food");
		 newFood.setDescription("I don't know what it is");
		 newFood.setEnabled(true);
		 newFood.setLarge_image_absolute_path("large image absolute path");
		 newFood.setLarge_image_relative_path("large image relative path");
		 newFood.setLarge_image_size(2000);
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(timeLong);
		 newFood.setPrice(52.15);
		 newFood.setRecommendation(2);
		 newFood.setSmall_image_absolute_path("small image absolute path");
		 newFood.setSmall_image_relative_path("small image relative path");
		 newFood.setSmall_image_size(200);
		 newFood.setStatus(Constants.FOOD_NEW);
		 newFood.setVersion(timeLong + 1l);
		 
		 MyResult result = foodService.add(newFood);
		 
		if (result.isExecuteResult()) {
			Food realFood = result.getFood();
			showFood(realFood);
			assertEquals("sichuan food", realFood.getCategory());
			assertEquals("I don't know what it is", realFood.getDescription());
			assertEquals(true, realFood.getEnabled().booleanValue());
			assertEquals("large image absolute path", realFood.getLarge_image_absolute_path());
			assertEquals("large image relative path", realFood.getLarge_image_relative_path());
			assertEquals(2000, realFood.getLarge_image_size().intValue());
			assertEquals("红烧猪蹄", realFood.getName());
			assertEquals(timeLong, realFood.getPeriod());
			assertEquals(52.15, realFood.getPrice());
			assertEquals(2, realFood.getRecommendation().intValue());
			assertEquals("small image absolute path", realFood.getSmall_image_absolute_path());
			assertEquals("small image relative path", realFood.getSmall_image_relative_path());
			assertEquals(200, realFood.getSmall_image_size().intValue());
			assertEquals(Constants.FOOD_NEW, realFood.getStatus().intValue());
			assertEquals(timeLong + 1l, realFood.getVersion().longValue());
		}else {
			assertEquals(true, false);
		}
		 
	 }
	 
	 @Test
	 public void testModify() {
		 init();
		 Long timeLong = new Date().getTime();
		 Food newFood = new Food();
		 newFood.setCategory("sichuan food");
		 newFood.setDescription("I don't know what it is");
		 newFood.setEnabled(true);
		 newFood.setLarge_image_absolute_path("large image absolute path");
		 newFood.setLarge_image_relative_path("large image relative path");
		 newFood.setLarge_image_size(2000);
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(timeLong);
		 newFood.setPrice(22.15);
		 newFood.setRecommendation(2);
		 newFood.setSmall_image_absolute_path("small image absolute path");
		 newFood.setSmall_image_relative_path("small image relative path");
		 newFood.setSmall_image_size(200);
		 newFood.setStatus(Constants.FOOD_NEW);
		 newFood.setVersion(timeLong + 1l);
		 
		 foodService.add(newFood);
		 
		 Food realFood = foodService.searchFoods(newFood).get(0);
		 realFood.setCategory("sichuan foodx");
		 realFood.setDescription("I don't know what it isx");
		 realFood.setEnabled(false);
		 realFood.setLarge_image_absolute_path("large image absolute pathx");
		 realFood.setLarge_image_relative_path("large image relative pathx");
		 realFood.setLarge_image_size(20001);
		 realFood.setName("红烧猪蹄x");
		 realFood.setPeriod(timeLong + 1l);
		 realFood.setPrice(23.0);
		 realFood.setRecommendation(3);
		 realFood.setSmall_image_absolute_path("small image absolute pathx");
		 realFood.setSmall_image_relative_path("small image relative pathx");
		 realFood.setSmall_image_size(2001);
		 realFood.setStatus(Constants.FOOD_COOKING);
		 realFood.setVersion(timeLong + 11l);
		 
		 MyResult result = foodService.modify(realFood);
		 
			if (result.isExecuteResult()) {
				realFood = result.getFood();
				showFood(realFood);
				assertEquals("sichuan foodx", realFood.getCategory());
				assertEquals("I don't know what it isx", realFood.getDescription());
				assertEquals(false, realFood.getEnabled().booleanValue());
				assertEquals("large image absolute pathx", realFood.getLarge_image_absolute_path());
				assertEquals("large image relative pathx", realFood.getLarge_image_relative_path());
				assertEquals(20001, realFood.getLarge_image_size().intValue());
				assertEquals("红烧猪蹄x", realFood.getName());
				assertEquals(timeLong + 1l, realFood.getPeriod().longValue());
				assertEquals(23.0, realFood.getPrice());
				assertEquals(3, realFood.getRecommendation().intValue());
				assertEquals("small image absolute pathx", realFood.getSmall_image_absolute_path());
				assertEquals("small image relative pathx", realFood.getSmall_image_relative_path());
				assertEquals(2001, realFood.getSmall_image_size().intValue());
				assertEquals(Constants.FOOD_COOKING, realFood.getStatus().intValue());
				assertEquals(timeLong + 11l, realFood.getVersion().longValue());
			}else {
				assertEquals(true, false);
			}
		 
	 }
	 
	 @Test
	 public void testSearchFoods() {
		 init();
		 Food newFood = new Food();
		 newFood.setName("红烧猪蹄x");
		 newFood.setPrice(23.0);
		 List<Food> foods = foodService.searchFoods(newFood);
		 for (int i = 0; i < foods.size(); i++) {
			Food realFood = foods.get(i);
			assertEquals("sichuan foodx", realFood.getCategory());
			assertEquals("I don't know what it isx", realFood.getDescription());
			assertEquals(false, realFood.getEnabled().booleanValue());
			assertEquals("large image absolute pathx", realFood.getLarge_image_absolute_path());
			assertEquals("large image relative pathx", realFood.getLarge_image_relative_path());
			assertEquals(20001, realFood.getLarge_image_size().intValue());
			assertEquals("红烧猪蹄x", realFood.getName());
			assertEquals(23.0, realFood.getPrice());
			assertEquals(3, realFood.getRecommendation().intValue());
			assertEquals("small image absolute pathx", realFood.getSmall_image_absolute_path());
			assertEquals("small image relative pathx", realFood.getSmall_image_relative_path());
			assertEquals(2001, realFood.getSmall_image_size().intValue());
			assertEquals(Constants.FOOD_COOKING, realFood.getStatus().intValue());
		}
		 
	 }
	 
	 @Test
	 public void testRemoveFoodById() {
		 init();
		 Food newFood = new Food();
		 newFood.setName("红烧猪蹄x");
		 newFood.setPrice(23.0);
		 List<Food> foods = foodService.searchFoods(newFood);
		 MyResult result = foodService.removeFoodById(foods.get(0).getId());
		 assertEquals(true, result.isExecuteResult());
	 }
	 
	 @Test
	 public void testUpdateMenuFoods() {
		 init();
		 List<Food> clientFoods = new ArrayList<Food>();
		 for (int i = 0; i < 15; i++) {
			 Food clientFood = new Food();
			 clientFood.setId((long) (i + 1));
			if (1== (i % 3)) {
				clientFood.setVersion((long) 1351232321);
			}else {
				clientFood.setVersion((long) 135);
			}
			
			if (i == 2) {
				clientFood.setImage_version((long) 13);
			}else {
				clientFood.setImage_version((long) 1351232321);
			}
			 
		}
		 Food newFood = new Food();
		 newFood.setName("红烧猪蹄x");
		 newFood.setPrice(23.0);
		 List<Food> foods = foodService.searchFoods(newFood);
		 MyResult result = foodService.removeFoodById(foods.get(0).getId());
		 assertEquals(true, result.isExecuteResult());
	 }
	 
}
