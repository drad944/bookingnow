package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.IFoodService;


public class TestFoodService {
	private IFoodService foodService;
		
	public IFoodService getFoodService() {
		return foodService;
	}

	public void setFoodService(IFoodService foodService) {
		this.foodService = foodService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext(
				"src/applicationContext.xml");
		IFoodService foodService = aCtx.getBean(IFoodService.class);
		assertNotNull(foodService);
		this.foodService = foodService;
	}
	 
	 @Test
	 public void testAdd() {
		 Food newFood = new Food();
		 newFood.setCategory("sichuan food");
		 newFood.setDescription("I don't know what it is");
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(new Date().getTime());
		 newFood.setPrice(52.15);
		 newFood.setVersion(new Date().getTime());
		 
		 foodService.add(newFood);
		 
		 Food newFood2 = new Food();
		 newFood2.setCategory("sichuan food");
		 newFood2.setDescription("卤猪蹄，好吃得很。");
		 newFood2.setName("卤猪蹄");
		 newFood2.setPeriod(new Date().getTime());
		 newFood2.setPrice(35.00);
		 newFood2.setVersion(new Date().getTime());
		 
		 Food_Picture picture = new Food_Picture();
		 picture.setEnabled(true);
		 picture.setName("new pi");
		 picture.setBig_image(new byte[]{4,45,6});
		 picture.setVersion(new Date().getTime());
		 newFood2.setPicture(picture);
		 
		 foodService.add(newFood2);
		 
	 }
	 
	 @Test
	 public void testRemove() {
		 //food have not id
		 Food newFood = new Food();
		 newFood.setName("红烧猪蹄");
		 
		 foodService.remove(newFood);
		 
		 //food have not picture
		 newFood.setId((long) 23);
		 foodService.remove(newFood);
		 
		 //food have picture
		 Food newFood2 = new Food();
		 newFood2.setName("红烧猪蹄");
		 newFood.setId((long) 23);
		 
		 Food_Picture picture = new Food_Picture();
		 picture.setEnabled(true);
		 picture.setName("new pi");
		 
		 newFood2.setPicture(picture);
		 
		 foodService.remove(newFood);
		 System.out.println("remove test.");
		 
	 }
	 
	 @Test
	 public void testModify() {
		 Food newFood = new Food();
		 newFood.setCategory("sichuan food");
		 newFood.setDescription("I don't know what it is");
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(new Date().getTime());
		 newFood.setPrice(52.15);
		 newFood.setVersion(new Date().getTime());
		 
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
		 newFood.setCategory("sichuan food");
		 newFood.setDescription("I don't know what it is");
		 newFood.setName("红烧猪蹄");
		 newFood.setPeriod(new Date().getTime());
		 newFood.setPrice(52.15);
		 newFood.setVersion(new Date().getTime());
		 
		 foodService.add(newFood);
		 newFood = foodService.searchFoods(newFood).get(0);
		 
		 Food_Picture picture = new Food_Picture();
		 picture.setEnabled(true);
		 picture.setName("new pi");
		 
		 newFood.setPicture(picture);
		 
	 }
	 
	 @Test
	 public void testRemoveFoodById() {
		 foodService.removeFoodById((long)23);
		 
	 }
	 
	 
}
