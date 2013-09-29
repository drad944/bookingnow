package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;


public interface ITableService {
	Boolean existTable(Table table);
	MyResult add(Table table);
	
	MyResult removeTableById(Long id);
	
	MyResult remove(Table table);
	
	MyResult modify(Table table);
	
	Table searchByPrimaryKey(Long id);
	
	List<Table> searchTables(Table table);
	
	List<Table> searchAvailableTables(SearchParams params);
}