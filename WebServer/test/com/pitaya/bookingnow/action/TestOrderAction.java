package com.pitaya.bookingnow.action;


import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.Order;

public class TestOrderAction {
	ApplicationContext ctx = null;
	
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
	}
	
	@Test
	public void testAddOrder() {
		OrderAction orderAction = (OrderAction) ctx.getBean("orderAction");
		Order order = new Order();
		order.setOid(1);
		order.setAllowance(100);
		order.setModifyTime(new Date());
		order.setOrderstatus(6);
		
		orderAction.setOrder(order);
		String result  = orderAction.addOrder();
		System.out.println("result:" + result);
	}

	@Test
	public void testFindOrder() {
		OrderAction orderAction = (OrderAction) ctx.getBean("orderAction");
		Order order = new Order();
		order.setOid(1);
		
		orderAction.setOrder(order);
		String result  = orderAction.findOrder();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testUpdateOrder() {
		OrderAction orderAction = (OrderAction) ctx.getBean("orderAction");
		Order order = new Order();
		order.setOid(1);
		order.setAllowance(99);
		
		orderAction.setOrder(order);
		String result  = orderAction.updateOrder();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testRemoveOrder() {
		OrderAction orderAction = (OrderAction) ctx.getBean("orderAction");
		Order order = new Order();
		order.setOid(1);
		
		orderAction.setOrder(order);
		String result  = orderAction.removeOrder();
		System.out.println("result:" + result);
	}
	
}
