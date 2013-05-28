package com.pitaya.bookingnow.dao.impl;

import org.junit.Before; 
import org.springframework.test.context.ContextConfiguration; 
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests; 

import com.pitaya.bookingnow.dao.AccountMapper;
import com.pitaya.bookingnow.dao.BaseMapperDao;
import com.pitaya.bookingnow.model.Account;
 
@ContextConfiguration("classpath:applicationContext-*.xml")
public class BaseMapperDaoImplTest extends AbstractJUnit38SpringContextTests {

	private BaseMapperDao<Account> dao;

	@Before
	public void init() {
		System.out.println(dao);
		dao.setMapperClass(AccountMapper.class);
	}

	public void testGet() throws Exception {
		init();
		Account acc = new Account();
		acc.setAid((long) 28);
		System.out.println(dao.get(acc));
	}

	public void testAdd() throws Exception {
		init();
		Account account = new Account();
		account.setName("temp@156.com");
		account.setPassword("abc");
		System.out.println(dao.add(account));
	}
}