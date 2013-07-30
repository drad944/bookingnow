package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Food;

public interface FoodMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food food);

    int insertSelective(Food food);

    Food selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food food);

    int updateByPrimaryKey(Food food);
    
    List<Food> selectFoods(Food food);
    
    List<Food> selectFoodsWithoutImage(Food food);
    
    List<Food> selectAllFoods();
    
    List<Food> selectAllFoodsWithoutImage();
    
    
    Food selectFullByPrimaryKey(Long id);
    
    List<Food> selectAllFullFoods();
    
    List<Food> selectFullFoods(Food food);
    
    Food selectSmallImageByFoodId(Long id);
    
    Food selectLargeImageByFoodId(Long id);
    
}