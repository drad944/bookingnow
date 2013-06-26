package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Food_Picture;

public interface Food_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food_Picture food_picture);

    int insertSelective(Food_Picture food_picture);

    Food_Picture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food_Picture food_picture);

    int updateByPrimaryKeyWithBLOBs(Food_Picture food_picture);

    int updateByPrimaryKey(Food_Picture food_picture);
    
    List<Food_Picture> searchFood_Pictures(Food_Picture food_picture);
}