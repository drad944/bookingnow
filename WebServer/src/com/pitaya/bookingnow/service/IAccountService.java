package com.pitaya.bookingnow.service;

import java.util.List;

import com.pitaya.bookingnow.model.Account;

public interface IAccountService {
	public List<Account> find(Account account);
	public Account findAccountByID(long aid);
	public Account findAccount(String name,String password);
	public boolean addAccount(Account account);
	public boolean modifyAccount(Account account);
	public boolean removeAccount(Account account);
}
