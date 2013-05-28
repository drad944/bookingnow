package com.pitaya.bookingnow.dao;

import java.util.List;

import com.pitaya.bookingnow.mapper.BaseSqlMapper;
import com.pitaya.bookingnow.model.Account;

public interface AccountMapper<T extends Account> extends BaseSqlMapper<T>{
	public List<T> getAllAccount();
	public Account selectAccountByID(long uid);
}
