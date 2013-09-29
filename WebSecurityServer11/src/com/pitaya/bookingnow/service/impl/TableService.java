package com.pitaya.bookingnow.service.impl;

import java.util.List;

import com.pitaya.bookingnow.dao.TableMapper;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.ITableService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
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
	public MyResult add(Table table) {
		MyResult result = new MyResult();
		if (table != null && table.getAddress() != null) {
			if (existTable(table)) {
				result.getErrorDetails().put("table_exist", "table have been registered.");
			}else {
				if (table.getStatus() == null) {
					table.setStatus(Constants.TABLE_EMPTY);
				}
				if (tableDao.insertSelective(table) == 1) {
					result.setExecuteResult(true);
					result.setTable(tableDao.selectByPrimaryKey(table.getId()));
					return result;
				}else {
					throw new RuntimeException("fail to insert table to DB.");
				}
			}
			
		}else {
			result.getErrorDetails().put("table_exist", "can not find table info in client data.");
		}
		return result;
	}

	@Override
	public MyResult removeTableById(Long id) {
		MyResult result = new MyResult();
		if (id != null) {
			if (tableDao.deleteByPrimaryKey(id) == 1) {
				
				result.setExecuteResult(true);
				return result;
			}else {
				throw new RuntimeException("fail to delete table in DB.");
			}
		}else {
			result.getErrorDetails().put("table_exist", "can not find table id in client data.");
		}
		return result;
	}

	@Override
	public MyResult remove(Table table) {
		MyResult result = new MyResult();
		if (table != null && table.getId() != null) {
			result = removeTableById(table.getId());
		}
		return result;
	}

	@Override
	public MyResult modify(Table table) {
		MyResult result = new MyResult();
		if (table != null && table.getId() != null) {
			if (tableDao.updateByPrimaryKeySelective(table) == 1) {
				
				result.setExecuteResult(true);
				result.setTable(tableDao.selectByPrimaryKey(table.getId()));
				return result;
			}else {
				throw new RuntimeException("fail to update table info in DB.");
			}
		}else {
			result.getErrorDetails().put("table_exist", "can not find table id in client data.");
		}
		return result;
	}

	@Override
	public List<Table> searchTables(Table table) {
		if (table != null) {
			return tableDao.searchTables(table);
		}
		return null;
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

	@Override
	public Boolean existTable(Table table) {
		if (table != null && table.getAddress() != null && (table.getAddress().equals("") == false)) {
			Table existTable =  tableDao.existTable(table);
			if (existTable != null && existTable.getId() != null) {
				return true;
			}
		}
		return false;
	}

}
