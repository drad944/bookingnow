package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.entity.Table;

public interface TableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Table record);

    int insertSelective(Table record);

    Table selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Table record);

    int updateByPrimaryKey(Table record);
}