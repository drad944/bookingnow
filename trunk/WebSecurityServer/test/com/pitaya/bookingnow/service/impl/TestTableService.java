package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.Table;
import com.pitaya.bookingnow.service.ITableService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.SearchParams;


public class TestTableService {
	private ITableService tableService;
		
	public ITableService getTableService() {
		return tableService;
	}

	public void setTableService(ITableService tableService) {
		this.tableService = tableService;
	}

	@Before
	public void init() {

		ApplicationContext aCtx = new FileSystemXmlApplicationContext(
				"src/applicationContext.xml");
		ITableService tableService = aCtx.getBean(ITableService.class);
		assertNotNull(tableService);
		this.tableService = tableService;
	}
	 public void showTable(Table table) {
		 if (table != null) {
			System.out.println("Id : " + table.getId());
			System.out.println("Address : " + table.getAddress());
			System.out.println("IndoorPrice : " + table.getIndoorPrice());
			System.out.println("MaxCustomerCount : " + table.getMaxCustomerCount());
			System.out.println("MinCustomerCount : " + table.getMinCustomerCount());
			System.out.println("Status : " + table.getStatus());
			System.out.println();
		}
	 }
	 @Test
	 public void testAdd() {
		 Table newTable = new Table();
		 newTable.setAddress("P1");
		 newTable.setIndoorPrice(55.00);
		 newTable.setMaxCustomerCount(6);
		 newTable.setMinCustomerCount(4);
		 newTable.setStatus(Constants.TABLE_EMPTY);
		 
		 tableService.add(newTable);
		 
	 }
	 
	 @Test
	 public void testRemove() {
		 //table have not id
		 Table newTable = new Table();
		 newTable.setAddress("P1");
		 newTable.setIndoorPrice(55.0);
		 Table realTable = tableService.searchTables(newTable).get(0);
		 
		 showTable(realTable);
		 
		 tableService.remove(realTable);
	 }
	 
	 @Test
	 public void testModify() {
		 Table newTable = new Table();
		 newTable.setStatus(Constants.TABLE_EMPTY);
		 Table realTable = tableService.searchTables(newTable).get(0);
		 
		 showTable(realTable);
		 realTable.setAddress("XXX");
		 tableService.modify(realTable);
		 Table anotherTable = tableService.searchByPrimaryKey(realTable.getId());
		 System.out.println("after modify,table is : ");
		 showTable(anotherTable);
	 }
	 
	 @Test
	 public void testSearchTables() {
		 Table newTable = new Table();
		 newTable.setStatus(Constants.TABLE_EMPTY);
		 
		 List<Table> realTables = tableService.searchTables(newTable);
		 for (int i = 0; i < realTables.size(); i++) {
			showTable(realTables.get(i));
		}
		 
	 }
	 
	 @Test
	 public void testSearchAvailableTables() {
		 SearchParams params = new SearchParams();
		 params.setCustomer_count(10);
		// params.setStatus(Constants.TABLE_EMPTY);
		 
		 List<Table> realTables = tableService.searchAvailableTables(params);
		 for (int i = 0; i < realTables.size(); i++) {
			showTable(realTables.get(i));
		}
		 
	 }
	 
	 @Test
	 public void testRemoveTableById() {
		 tableService.removeTableById((long)12);
		 
	 }
	 
	 
}
