package com.pitaya.bookingnow.service.impl;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitaya.bookingnow.dao.AccountMapper;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.model.AccountExample;
import com.pitaya.bookingnow.model.AccountExample.Criteria;
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
		return accountDao.insertSelective(record);
	}

	@Override
	public List<Account> selectByExample(Account record) {
		AccountExample example = new AccountExample();
		
		example.or().andNameIsNotNull().andNameLike(record.getName());
		example.or().andRoleIsNotNull().andRoleEqualTo(record.getRole());
		example.or().andSexIsNotNull().andSexEqualTo(record.getSex());
		
		return accountDao.selectByExample(example);
	}

	@Override
	public Account selectByPrimaryKey(Integer aid) {
		return accountDao.selectByPrimaryKey(aid);
	}

	@Override
	public int updateByPrimaryKeySelective(Account record) {
		return accountDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(Account record) {
		
		return accountDao.updateByPrimaryKey(record);
	}

	@Override
	public List<Account> selectByExample(AccountExample example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account selectForLogin(Account record) {
		AccountExample example = new AccountExample();
		
		example.or().andNameIsNotNull().andNameLike(record.getName());
		example.or().andPasswordIsNotNull().andPasswordEqualTo(record.getPassword());
		List<Account> list = accountDao.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		
		return null;
	}

}