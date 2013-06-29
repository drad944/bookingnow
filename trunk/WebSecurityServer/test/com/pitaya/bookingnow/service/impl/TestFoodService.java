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


public class TestFoodService {
	private FoodService foodService;
		
	 @Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
	  FoodService foodService = (FoodService) aCtx.getBean("foodService");
	  assertNotNull(foodService);
	  this.foodService = foodService;
	 }
	 
	 @Test
	 public void testAdd() {
		 Food newFood = new Food();
		 newFood.setCategory(2);
		 newFood.setDescription("I don't know what it is");
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(new Date());
		 newFood.setPrice(new BigDecimal(52.15));
		 newFood.setVersion(2);
		 
		 foodService.add(newFood);
		 
		 Food newFood2 = new Food();
		 newFood2.setCategory(2);
		 newFood2.setDescription("卤猪蹄，好吃得很。");
		 newFood2.setName("卤猪蹄");
		 newFood2.setPeriod(new Date());
		 newFood2.setPrice(new BigDecimal(35.00));
		 newFood2.setVersion(2);
		 
		 Food_Picture picture = new Food_Picture();
		 picture.setEnabled(true);
		 picture.setName("new pi");
		 picture.setBig_image(new byte[]{4,45,6});
		 picture.setVersion(new Date());
		 newFood2.setPicture(picture);
		 
		 foodService.add(newFood2);
		 
	 }
	 
	 @Test
	 public void testRemove() {
		 Food newFood = new Food();
		 newFood.setCategory(2);
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
	 
	 @Test
	 public void testModify() {
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
	 
	 @Test
	 public void testSearchFoods() {
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
	 @Test
	 public void testRemoveFoodById() {
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
