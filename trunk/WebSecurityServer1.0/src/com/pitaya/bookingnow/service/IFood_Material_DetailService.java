package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Food_Material_Detail;


public interface IFood_Material_DetailService {
	
	boolean add(Food_Material_Detail food_material_detail);
	
	boolean removeFood_Material_DetailById(Long id);
	
	boolean remove(Food_Material_Detail food_material_detail);

	boolean modify(Food_Material_Detail food_material_detail);
	
	List<Food_Material_Detail> searchFood_Material_Details(Food_Material_Detail food_material_detail);
	
	
}