package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.entity.Food_PictureWithBLOBs;

public interface Food_PictureMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food_PictureWithBLOBs record);

    int insertSelective(Food_PictureWithBLOBs record);

    Food_PictureWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food_PictureWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(Food_PictureWithBLOBs record);

    int updateByPrimaryKey(Food_Picture record);
}