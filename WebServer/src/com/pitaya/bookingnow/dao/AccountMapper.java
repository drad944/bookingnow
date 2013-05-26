package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.model.Account;

public interface AccountMapper {
	public List<Account> selectAccountByName(String name);
	public Account selectAccount(String name,String password);
	public Account selectAccountByID(long uid);
	public boolean insertAccount(Account account);
	public boolean updateAccount(Account account);
	public boolean deleteAccount(Account account);
}
