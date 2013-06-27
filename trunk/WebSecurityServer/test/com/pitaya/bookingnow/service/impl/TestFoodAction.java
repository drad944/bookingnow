package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;


public class TestFoodAction {
	private FoodService foodService;
		
	 @Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
	  FoodService foodService = (FoodService) aCtx.getBean("foodService");
	  assertNotNull(foodService);
	  this.foodService = foodService;
	 }
	 
	 @Test
	 public void testAddFood() {
		 Food newFood = new Food();
		 newFood.setCategory(2);
		 newFood.setDescription("I don't know what it is");
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(new Date());
		 newFood.setPrice(new BigDecimal(52.15));
		 newFood.setVersion(2);
		 
		 foodService.add(newFood);
		 newFood = foodService.searchFoods(newFood).get(0);
		 
		 Food_Picture picture = new Food_Picture();
		 picture.setEnabled(true);
		 picture.setName("new pi");
		 
		 newFood.setPicture(picture);
		 
	 }
}
