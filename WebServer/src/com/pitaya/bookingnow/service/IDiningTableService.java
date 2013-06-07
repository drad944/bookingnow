package com.pitaya.bookingnow.service;

import com.pitaya.bookingnow.model.DiningTable;

public interface IDiningTableService {
    int deleteByPrimaryKey(Integer tid);

    int insert(DiningTable record);

    int insertSelective(DiningTable record);

    DiningTable selectByPrimaryKey(Integer tid);

    int updateByPrimaryKeySelective(DiningTable record);

    int updateByPrimaryKey(DiningTable record);
}