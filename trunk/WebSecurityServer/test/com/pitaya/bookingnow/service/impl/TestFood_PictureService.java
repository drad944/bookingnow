package com.pitaya.bookingnow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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
				
				File file = new File("WebContent/images/Small/" + (i + 1) + ".png");
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
				
				File file = new File("WebContent/images/Large/" + (i + 1) + ".jpg");
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
				
				newPicture.setBig_image(pictureImage);
				newPicture.setVersion(new Date().getTime());
				
				food_pictureService.modify(newPicture);
				newPicture.setBig_image(null);
				
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
		
		System.out.println("");
		
	}
	
	
}
