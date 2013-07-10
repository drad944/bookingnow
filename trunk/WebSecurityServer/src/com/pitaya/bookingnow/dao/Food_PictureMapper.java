package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Food_Picture;

public interface Food_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food_Picture picture);

    int insertSelective(Food_Picture picture);

    Food_Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food_Picture picture);

    int updateByPrimaryKeyWithBLOBs(Food_Picture picture);

    int updateByPrimaryKey(Food_Picture picture);
    
    List<Food_Picture> searchFood_Pictures(Food_Picture picture);
    List<Food_Picture> searchFood_PicturesWithoutImage(Food_Picture picture);
    
    Food_Picture selectByFoodId(Long foodId);
    
    List<Food_Picture> searchAllFood_Pictures();
    List<Food_Picture> searchAllFood_PicturesWithoutImage();
    
    
    
}