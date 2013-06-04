package com.pitaya.bookingnow.dao;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.pitaya.bookingnow.model.Account;

public interface AccountMapper extends BaseMapper<Account>{
	
	public boolean add(Account account) throws DataAccessException;
	public boolean delete(Account account) throws DataAccessException;
	public boolean modify(Account account) throws DataAccessException;
	public Account getByID(Account account) throws DataAccessException;
	public Account get(Account account) throws DataAccessException;
	public List<Account> getAll(Account account) throws DataAccessException;
	
}