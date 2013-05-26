package com.pitaya.bookingnow.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pitaya.bookingnow.dao.AccountDao;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.service.IAccountService;

@Service
public class AccountService implements IAccountService {
	
	@Resource
	private AccountDao accountDao;

	public List<Account> find(Account account) {
		
		return null;
	}

	public Account findAccount(String name, String password) {
		return accountDao.selectAccount(name, password);
	}

	public boolean addAccount(Account account) {
		return accountDao.insertAccount(account);
	}

	public boolean modifyAccount(Account account) {
		return accountDao.updateAccount(account);
	}

	public boolean removeAccount(Account account) {
		return accountDao.deleteAccount(account);
	}

	public Account findAccountByID(long aid) {
		
		return accountDao.selectAccountByID(aid);
	}

}
