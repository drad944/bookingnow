package com.pitaya.bookingnow.service.impl;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitaya.bookingnow.dao.AccountMapper;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.model.AccountExample;
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
	public int deleteByPrimaryKey(Integer aid) {
		
		return accountDao.deleteByPrimaryKey(aid);
	}

	@Override
	public int insert(Account record) {
		// TODO Auto-generated method stub
		return accountDao.insert(record);
	}

	@Override
	public int insertSelective(Account record) {
		// TODO Auto-generated method stub
		return accountDao.insertSelective(record);
	}

	@Override
	public List<Account> selectByExample(AccountExample example) {
		

		return null;
	}

	@Override
	public Account selectByPrimaryKey(Integer aid) {
		// TODO Auto-generated method stub
		return accountDao.selectByPrimaryKey(aid);
	}

	@Override
	public int updateByPrimaryKeySelective(Account record) {
		// TODO Auto-generated method stub
		return accountDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Account record) {
		// TODO Auto-generated method stub
		return accountDao.updateByPrimaryKey(record);
	}

	
	
}