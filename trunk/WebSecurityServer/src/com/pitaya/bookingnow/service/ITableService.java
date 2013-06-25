package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.entity.Table;


public interface ITableService {
	
	boolean add(Table table);
	
	boolean removeTableById(Long id);
	
	boolean remove(Table table);

	boolean modify(Table table);
	
	List<Table> searchTables(Table table);
	
	
}