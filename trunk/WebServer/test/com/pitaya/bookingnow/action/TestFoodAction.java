package com.pitaya.bookingnow.action;


import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.Food;

public class TestFoodAction {
	ApplicationContext ctx = null;
	
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
	}
	
	@Test
	public void testAddFood() {
		FoodAction foodAction = (FoodAction) ctx.getBean("foodAction");
		Food food = new Food();
		food.setFid(1);
		
		foodAction.setFood(food);
		
		String result  = foodAction.addFood();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testFindFood() {
		FoodAction foodAction = (FoodAction) ctx.getBean("foodAction");
		Food food = new Food();
		food.setFid(1);
		
		foodAction.setFood(food);
		
		String result  = foodAction.findFood();
		System.out.println("result:" + result);
	}

	@Test
	public void testUpdateAccount() {
		FoodAction foodAction = (FoodAction) ctx.getBean("foodAction");
		Food newFood = new Food();
		newFood.setFid(1);
		newFood.setMaterial("猪猪");
		foodAction.setFood(newFood);
		
		
		String result  = foodAction.updateFood();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testRemoveAccount() {
		FoodAction foodAction = (FoodAction) ctx.getBean("foodAction");
		Food newFood = new Food();
		newFood.setFid(1);
		foodAction.setFood(newFood);
		
		
		String result  = foodAction.removeFood();
		System.out.println("result:" + result);
	}
}
