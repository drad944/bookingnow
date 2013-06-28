package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food_Picture;

public class TestFood_PictureService {
	private Food_PictureService food_pictureService;

	public Food_PictureService getFood_pictureService() {
		return food_pictureService;
	}

	public void setFood_pictureService(Food_PictureService food_pictureService) {
		this.food_pictureService = food_pictureService;
	}
	
	 @Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
	  Food_PictureService food_pictureService = (Food_PictureService) aCtx.getBean("food_pictureService");
	  this.food_pictureService = food_pictureService;
	 }
	
	@Test
	public void add() {
		
		Food_Picture picture = new Food_Picture();
		picture.setEnabled(true);
		picture.setLast_modify_time(new Date());
		picture.setName("回锅炒肉");
		picture.setSmall_image(new byte[]{1,2,3});
		
		int result = food_pictureService.add(picture);
		System.out.println(result);
		
	}
}
