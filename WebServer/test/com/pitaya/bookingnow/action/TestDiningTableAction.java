package com.pitaya.bookingnow.action;


import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.DiningTable;

public class TestDiningTableAction {
	ApplicationContext ctx = null;
	
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
	}
	
	@Test
	public void testBookTable() {
		DiningTableAction tableAction = (DiningTableAction) ctx.getBean("DiningTableAction");
		DiningTable table = new DiningTable();
		table.setTid(1);
		table.setMaxCustomerCount(3);
		table.setPlace(4);
		table.setRealCustomerCount(2);
		table.setSeatCount(4);
		table.setTablestatus(5);
		
		tableAction.setTable(table);
		
		String result  = tableAction.bookTable();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testFindTable() {
		DiningTableAction tableAction = (DiningTableAction) ctx.getBean("DiningTableAction");
		DiningTable table = new DiningTable();
		table.setTid(1);
		
		tableAction.setTable(table);
		
		
		String result  = tableAction.findTable();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testUsingTable() {
		DiningTableAction tableAction = (DiningTableAction) ctx.getBean("DiningTableAction");
		DiningTable table = new DiningTable();
		table.setTid(1);
		table.setRealCustomerCount(100);
		
		tableAction.setTable(table);
		
		
		String result  = tableAction.usingTable();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testCancelTable() {
		DiningTableAction tableAction = (DiningTableAction) ctx.getBean("DiningTableAction");
		DiningTable table = new DiningTable();
		table.setTid(1);
		
		tableAction.setTable(table);
		
		
		String result  = tableAction.cancelTable();
		System.out.println("result:" + result);
	}
}
