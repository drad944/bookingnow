package com.pitaya.bookingnow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.ITableService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;
import com.pitaya.bookingnow.util.SearchParams;

public class TableAction extends BaseAction{

	private static final long serialVersionUID = 3183091140754786231L;
	
	private ITableService tableService;
	
	private Table table;
	
	private SearchParams params;
	
	private Map<String,List<Table>> matchedTables;

	public ITableService getTableService() {
		return tableService;
	}

	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

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

	public String existTable() {
		if (result == null) {
			result = new MyResult();
		}
		
		if (table != null && table.getAddress() != null) {
			
			if (tableService.existTable(table)) {
				result.setExecuteResult(true);
				return "existTableSuccess";
			}else {
				result.setExecuteResult(false);
				
			}
		}
        return "Fail"; 
	}
	
	public String searchTable() {
		if (table != null) {
			
			List<Table> tables = tableService.searchTables(table);
			if (matchedTables == null) {
				matchedTables = new HashMap<String,List<Table>>();
			}
			matchedTables.put("result", tables);
			
			return "searchTableSuccess";
		}
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail"; 
	}
	
	public String addTable() {
		if (table != null) {
			result = tableService.add(table);
			
			if(result.isExecuteResult()){ 
				table = result.getTable();
	            return "addTableSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail"; 
		
	}
	
	public String updateTable() {
		if(table != null) {
			result = tableService.modify(table);
			
			if(result.isExecuteResult()){ 
				table = result.getTable();
	            return "updateTableSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
        return "Fail";
	}
	
	public String removeTable() {
		if (table != null) {
			result = tableService.removeTableById(table.getId());
			if(result.isExecuteResult()){ 
	            return "removeUserSuccess";  
	        }else{  
	            return "Fail";  
	        }  
        }
		if (result == null) {
			result = new MyResult();
			result.setErrorType(Constants.FAIL);
		}
		return "Fail";  
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
		if (result == null) {
			result = new MyResult();
			this.getResult().setExecuteResult(false);
			result.setErrorType(Constants.FAIL);
		}
		return "Fail";
		
	}
	
}
