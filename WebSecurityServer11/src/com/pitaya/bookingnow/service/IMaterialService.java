package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Material;


public interface IMaterialService {
	
	boolean add(Material material);
	
	boolean removeMaterialById(Long id);
	
	boolean remove(Material material);

	boolean modify(Material material);
	
	List<Material> searchMaterials(Material material);
	
	
}