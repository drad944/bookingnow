package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.entity.Table;

public interface TableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Table table);

    int insertSelective(Table table);

    Table selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Table table);

    int updateByPrimaryKey(Table table);
    
    List<Table> searchTables(Table table);
    
    List<Table> searchAllTables();
}