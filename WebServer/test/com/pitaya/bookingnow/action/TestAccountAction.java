package com.pitaya.bookingnow.action;

import java.sql.Date;
import java.sql.Timestamp;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.Account;

public class TestAccountAction {
	
	@Test
	public void testRegisterAccount() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		accountAction.setAccount(new Account(2,"zhang","123456","boss","man",new Timestamp(System.currentTimeMillis()),new Date(System.currentTimeMillis())));
		accountAction.registerAccount();
	}

	@Test
	public void testUpdateAccount() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account(1);
		newAccount.setPassword("xxx");
		accountAction.setAccount(newAccount);
		accountAction.updateAccount();
	}
	
	@Test
	public void testRemoveAccount() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account(1);
		accountAction.setAccount(newAccount);
		accountAction.removeAccount();
	}
}
