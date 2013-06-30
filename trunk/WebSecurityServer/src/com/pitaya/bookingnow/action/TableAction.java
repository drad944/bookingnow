package com.pitaya.bookingnow.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.impl.TableService;

public class TableAction extends ActionSupport{

	private static final long serialVersionUID = 3183091140754786231L;
	
	private TableService tableService;
	
	private Table table;
	
	private List<Table> matchedTables;

	public List<Table> getMatchedTables() {
		return matchedTables;
	}

	public void setMatchedTables(List<Table> matchedTables) {
		this.matchedTables = matchedTables;
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
			
			matchedTables = tableService.searchTables(table);
			
			return "searchSuccess";
		}
		
		return "findFail";
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
	
	
}
