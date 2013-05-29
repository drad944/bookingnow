package com.pitaya.bookingnow.mapper;

import java.util.List; 
import org.springframework.test.context.ContextConfiguration; import 
org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests; 

import com.pitaya.bookingnow.dao.AccountMapper;
import com.pitaya.bookingnow.model.Account;

 
@ContextConfiguration("classpath:applicationContext-*.xml")
public class AccountMapperTest extends AbstractJUnit38SpringContextTests {

	private AccountMapper<Account> mapper;

	public void testGetAccount() {
		Account acc = new Account();
		acc.setAid((long) 28);
		System.out.println(mapper.get(acc));
	}

	public void testAdd() {
		Account account = new Account();
		account.setName("lisi@155.com");
		account.setPassword("abc");
		mapper.add(account);
	}

	public void testEditAccount() {
		Account acc = new Account();
		acc.setAid((long) 28);
		acc = mapper.get(acc);
		System.out.println(acc);
		acc.setName("Zhangsan22");
		acc.setPassword("123123");
		mapper.edit(acc);
		System.out.println(mapper.get(acc));
	}

	public void testRemoveAccount() {
		Account acc = new Account();
		acc.setAid((long) 28);
		mapper.remove(acc);
		System.out.println(mapper.get(acc));
	}

	public void testAccountList() {
		List<Account> acc = mapper.getAllAccount();
		System.out.println(acc.size());
		System.out.println(acc);
	}

	public void testList() {

		Account acc = new Account();

		acc.setName("@qq.com");
		List<Account> list = mapper.getList(acc);
		System.out.println(list.size());
		System.out.println(list);
	}
}