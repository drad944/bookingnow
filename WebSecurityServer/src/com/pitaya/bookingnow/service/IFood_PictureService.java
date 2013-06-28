package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Food_Picture;


public interface IFood_PictureService {
	
	int add(Food_Picture food_picture);
	
	boolean removeFood_PictureById(Long id);
	
	boolean remove(Food_Picture food_picture);

	boolean modify(Food_Picture food_picture);
	
	List<Food_Picture> searchFood_Pictures(Food_Picture food_picture);
	
	
}