package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.TableMapper;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.ITableService;
import com.pitaya.bookingnow.util.SearchParams;

public class TableService implements ITableService{
	
	private TableMapper tableDao;
	
	public TableMapper getTableDao() {
		return tableDao;
	}

	public void setTableDao(TableMapper tableDao) {
		this.tableDao = tableDao;
	}

	@Override
	public boolean add(Table table) {
		int result = tableDao.insertSelective(table);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeTableById(Long id) {
		int result = tableDao.deleteByPrimaryKey(id);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Table table) {
		int result = tableDao.deleteByPrimaryKey(table.getId());
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean modify(Table table) {
		int result = tableDao.updateByPrimaryKeySelective(table);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Table> searchTables(Table table) {
		
		return tableDao.searchTables(table);
	}

	@Override
	public List<Table> searchAvailableTables(SearchParams params) {
		if (params != null) {
			return tableDao.searchAvailableTables(params);
		}
		return null;
	}

	@Override
	public Table searchByPrimaryKey(Long id) {
		if (id != null) {
			return tableDao.selectByPrimaryKey(id);
		}
		return null;
	}

}
