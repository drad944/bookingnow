package com.pitaya.bookingnow.service;




import java.util.List;

import com.pitaya.bookingnow.model.Account;


public interface IAccountService extends IBaseService<Account>{
	public boolean add(Account account);
	public boolean delete(Account account);
	public boolean modify(Account account);
	public Account get(Account account);
	public Account getByID(Account account);
	public List<Account> getAll(Account account);

}
