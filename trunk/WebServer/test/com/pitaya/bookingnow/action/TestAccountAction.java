package com.pitaya.bookingnow.action;


import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.Account;

public class TestAccountAction {
	ApplicationContext ctx = null;
	
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
	}
	
	@Test
	public void testRegisterAccount() {
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account();
		newAccount.setAid(2);
		newAccount.setBirthday(new Date());
		newAccount.setCreateDateTime(new Date());
		newAccount.setImage(new byte[]{});
		newAccount.setLevel(3);
		newAccount.setName("li");
		newAccount.setPassword("123456");
		newAccount.setRole("boss");
		newAccount.setSex("m");
		
		accountAction.setAccount(newAccount);
		
		
		String result  = accountAction.registerAccount();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testFindAccount() {
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account();
		newAccount.setAid(1);
		accountAction.setAccount(newAccount);
		
		
		String result  = accountAction.findAccount();
		System.out.println("result:" + result);
	}

	@Test
	public void testUpdateAccount() {
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account();
		newAccount.setAid(1);
		newAccount.setPassword("xxx");
		accountAction.setAccount(newAccount);
		
		
		String result  = accountAction.updateAccount();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testRemoveAccount() {
		AccountAction accountAction = (AccountAction) ctx.getBean("accountAction");
		Account newAccount = new Account();
		newAccount.setAid(1);
		accountAction.setAccount(newAccount);
		
		
		String result  = accountAction.removeAccount();
		System.out.println("result:" + result);
	}
}
