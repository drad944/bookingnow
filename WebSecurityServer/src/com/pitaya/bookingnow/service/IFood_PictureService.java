package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;


public interface IFood_PictureService {
	
	public int add(Food_Picture food_picture);
	
	public boolean removeFood_PictureById(Long id);
	
	public boolean remove(Food_Picture food_picture);

	public boolean modify(Food_Picture food_picture);
	
	public List<Food_Picture> searchFood_Pictures(Food_Picture food_picture);
	public List<Food_Picture> searchFood_PicturesWithoutImage(Food_Picture food_picture);
	public Food_Picture searchPictureByFoodId(Long id);
	
	public List<Food_Picture> searchAllFood_Pictures();
	
	public List<Food_Picture> searchAllFood_PicturesWithoutImage();
	
	
}