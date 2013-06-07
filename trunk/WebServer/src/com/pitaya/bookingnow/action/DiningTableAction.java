package com.pitaya.bookingnow.action;

import com.opensymphony.xwork2.ActionSupport;
import com.pitaya.bookingnow.model.DiningTable;
import com.pitaya.bookingnow.service.IDiningTableService;

public class DiningTableAction extends ActionSupport{
	private static final long serialVersionUID = 3960950307222927497L;
	private IDiningTableService tableService;
	private DiningTable table;
	
	public IDiningTableService getTableService() {
		return tableService;
	}
	public void setTableService(IDiningTableService tableService) {
		this.tableService = tableService;
	}
	public DiningTable getTable() {
		return table;
	}
	public void setTable(DiningTable table) {
		this.table = table;
	}
	
	public String findTable() {
		if (table != null) {
			tableService.selectByPrimaryKey(table.getTid());
			return "findSuccess";
		}
		
		return "findFail";
	}
	
	public String bookTable() {
		if (table != null) {
			tableService.insert(table);
			return "bookSuccess";
		}
		
		return "bookFail";
	}
	
	public String cancelTable() {
		if (table != null) {
			tableService.deleteByPrimaryKey(table.getTid());
			return "cancelSuccess";
		}
		return "cancelFail";
	}
	
	public String usingTable() {
		if (table != null) {
			tableService.updateByPrimaryKey(table);
			return "usingSuccess";
		}
		return "usingFail";
	}
}
