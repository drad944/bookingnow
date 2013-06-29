package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Food_Material_Detail;

public interface Food_Material_DetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Food_Material_Detail meterial_detail);

    int insertSelective(Food_Material_Detail meterial_detail);

    Food_Material_Detail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Food_Material_Detail meterial_detail);

    int updateByPrimaryKey(Food_Material_Detail meterial_detail);
}