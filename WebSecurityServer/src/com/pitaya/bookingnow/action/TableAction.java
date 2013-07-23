package com.pitaya.bookingnow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.impl.TableService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.SearchParams;

public class TableAction extends BaseAction{

	private static final long serialVersionUID = 3183091140754786231L;
	
	private TableService tableService;
	
	private Table table;
	
	private SearchParams params;
	
	
	private Map<String,List<Table>> matchedTables;

	
	public Map<String, List<Table>> getMatchedTables() {
		return matchedTables;
	}

	public void setMatchedTables(Map<String, List<Table>> matchedTables) {
		this.matchedTables = matchedTables;
	}

	public SearchParams getParams() {
		return params;
	}

	public void setParams(SearchParams params) {
		this.params = params;
	}


	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public TableService getTableService() {
		return tableService;
	}

	public void setTableService(TableService tableService) {
		this.tableService = tableService;
	}
	
	public String searchTable() {
		if (table != null) {
			
			List<Table> tables = tableService.searchTables(table);
			matchedTables.put("result", tables);
			
			return "searchSuccess";
		}
		
		return "searchFail";
	}
	
	public String addTable() {
		if (table != null) {
			boolean result = tableService.add(table);
			if (result) {
				return "addSuccess";
			}
			return "addFail";
		}
		
		return "addFail";
	}
	
	public String updateTable() {
		if (table != null) {
			boolean result = tableService.modify(table);
			if (result) {
				return "updateSuccess";
			}
			return "updateFail";
		}
		
		return "updateFail";
	}
	
	public String removeTable() {
		if (table != null) {
			boolean result = tableService.remove(table);
			if (result) {
				return "removeSuccess";
			}
			return "removeFail";
		}
		
		return "removeFail";
	}
	
	public String findAvailableTables() {
		
		if (params != null) {
			List<Table> tables = tableService.searchAvailableTables(params);
			if (matchedTables == null) {
				matchedTables = new HashMap<String, List<Table>>();
			}
			matchedTables.put("result", tables);
			return "findAvailableTablesSuccess";
		}
		this.getResult().setExecuteResult(false);
		this.getResult().setErrorType(Constants.FAIL);
		return "Fail";
		
	}
	
}
