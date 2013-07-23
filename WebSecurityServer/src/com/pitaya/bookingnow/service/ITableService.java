package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.util.SearchParams;


public interface ITableService {
	
	boolean add(Table table);
	
	boolean removeTableById(Long id);
	
	boolean remove(Table table);

	boolean modify(Table table);
	
	Table searchByPrimaryKey(Long id);
	
	List<Table> searchTables(Table table);
	
	List<Table> searchAvailableTables(SearchParams params);
}