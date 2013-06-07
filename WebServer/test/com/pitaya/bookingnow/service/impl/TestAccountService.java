package com.pitaya.bookingnow.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.model.Account;

public class TestAccountService {
	Logger logger = Logger.getLogger("TestAccountService");
	 
	 AccountService service = null;

	 @Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src:spring.xml");
	  AccountService service = (AccountService) aCtx.getBean("accountService");
	  assertNotNull(service);
	  this.service = service;
	 }
	 
	 @Test
	 public void testAddAccount() {

	  Account account = new Account();
	  // account.setAccountId(1);
	  account.setName("li");
	  account.setPassword("123456");
	  
	  service.insert(account);
	  logger.debug("account id: " + account.getAid());
	  
	  Account accountFromDb =  service.selectByPrimaryKey(account.getAid());
	  assertNotNull(accountFromDb);
	  assertEquals(account.getAid(), accountFromDb.getAid());
	 }
}
