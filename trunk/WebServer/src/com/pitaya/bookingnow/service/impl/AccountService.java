package com.pitaya.bookingnow.service.impl;
import java.util.List;

import com.pitaya.bookingnow.dao.AccountMapper;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.service.IAccountService;

public class AccountService implements IAccountService {

	private AccountMapper accountDao;
	
	public AccountMapper getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountMapper accountDao) {
		this.accountDao = accountDao;
	}

	@Override
	public boolean add(Account account) {
		
		return accountDao.add(account);
	}

	@Override
	public boolean delete(Account account) {
		return accountDao.delete(account);
	}

	@Override
	public boolean modify(Account account) {
		return accountDao.modify(account);
	}

	@Override
	public Account get(Account account) {
		return accountDao.get(account);
	}

	@Override
	public Account getByID(Account account) {
		return accountDao.getByID(account);
	}

	@Override
	public List<Account> getAll(Account account) {
		return accountDao.getAll(account);
	}

}