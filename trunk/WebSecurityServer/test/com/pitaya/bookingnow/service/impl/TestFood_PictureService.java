package com.pitaya.bookingnow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.service.IFood_PictureService;

public class TestFood_PictureService {
	private IFood_PictureService food_pictureService;

	 public IFood_PictureService getFood_pictureService() {
		return food_pictureService;
	}

	public void setFood_pictureService(IFood_PictureService food_pictureService) {
		this.food_pictureService = food_pictureService;
	}

	@Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
	  IFood_PictureService food_pictureService = aCtx.getBean(IFood_PictureService.class);
	  this.food_pictureService = food_pictureService;
	 }
	
	@Test
	public void add() {
		
		Food_Picture picture = new Food_Picture();
		picture.setEnabled(true);
		picture.setVersion(new Date().getTime());
		picture.setName("回锅炒肉");
		picture.setSmall_image(new byte[]{1,2,3});
		
		int result = food_pictureService.add(picture);
		System.out.println(result);
		
	}
	
	@Test
	public void update() {
		List<Food_Picture> pictures = food_pictureService.searchAllFood_PicturesWithoutImage();
		
		
		for (int i = 0; i < pictures.size(); i++) {
			try {
				Food_Picture newPicture = pictures.get(i);
				
				File file = new File("WebContent/images/small/" + (i + 1) + ".png");
				FileInputStream fis = new FileInputStream(file);
				
				byte[] buffer = new byte[1024];
				byte[] pictureImage = new byte[1024*1024*5];
				
				int len = 0;
				int startIndex = 0;
				while((len = fis.read(buffer)) > 0) {
					
					System.arraycopy(buffer, 0, pictureImage, startIndex, len);
					startIndex = startIndex + len;
				}
				fis.close();
				
				newPicture.setSmall_image(pictureImage);
				newPicture.setVersion(new Date().getTime());
				food_pictureService.modify(newPicture);
				newPicture.setSmall_image(null);
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
		pictures = food_pictureService.searchAllFood_PicturesWithoutImage();
		
		for (int i = 0; i < pictures.size(); i++) {
			try {
				Food_Picture newPicture = pictures.get(i);
				
				File file = new File("WebContent/images/large/" + (i + 1) + ".jpg");
				FileInputStream fis = new FileInputStream(file);
				
				byte[] buffer = new byte[1024];
				byte[] pictureImage = new byte[1024*1024*5];
				
				int len = 0;
				int startIndex = 0;
				while((len = fis.read(buffer)) > 0) {
					
					System.arraycopy(buffer, 0, pictureImage, startIndex, len);
					startIndex = startIndex + len;
				}
				fis.close();
				
				newPicture.setVersion(new Date().getTime());
				
				food_pictureService.modify(newPicture);
				
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
		
		System.out.println("");
		
	}
	
	@Test
	public void testSearchSmallPictureByFoodId() {
		
		Long food_id = 2l;
		
		Food_Picture result = food_pictureService.searchSmallPictureByFoodId(food_id);
		showPicture(result);
		result = food_pictureService.searchLargePictureByFoodId(food_id);
		showPicture(result);
	}
	
	public void showPicture(Food_Picture picture) {
		if (picture != null) {
			System.out.println("Id : " + picture.getId());
			System.out.println("Name : " + picture.getName());
			System.out.println("Large_image_absolute_path : " + picture.getLarge_image_absolute_path());
			System.out.println("Large_image_relative_path : " + picture.getLarge_image_relative_path());
			System.out.println("Small_image_absolute_path : " + picture.getSmall_image_absolute_path());
			System.out.println("Small_image_relative_path : " + picture.getSmall_image_relative_path());
			System.out.println("Large_image_size : " + picture.getLarge_image_size());
			System.out.println("Small_image_size : " + picture.getSmall_image_size());
			System.out.println("Version : " + picture.getVersion());
			System.out.println("Enabled : " + picture.getEnabled());
			System.out.println("Large_image length : " + picture.getLarge_image());
			System.out.println("Small_image length : " + picture.getSmall_image());
			System.out.println();
		}
		
	}
	
}
