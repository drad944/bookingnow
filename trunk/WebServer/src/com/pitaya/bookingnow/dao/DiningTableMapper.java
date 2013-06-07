package com.pitaya.bookingnow.dao;

import com.pitaya.bookingnow.model.DiningTable;

public interface DiningTableMapper {
    int deleteByPrimaryKey(Integer tid);

    int insert(DiningTable record);

    int insertSelective(DiningTable record);

    DiningTable selectByPrimaryKey(Integer tid);

    int updateByPrimaryKeySelective(DiningTable record);

    int updateByPrimaryKey(DiningTable record);
}