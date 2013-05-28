package com.pitaya.bookingnow.service.impl;
import java.util.List; 

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException; 
import org.springframework.stereotype.Component; 
 

import com.pitaya.bookingnow.dao.AccountDao;
import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.service.IAccountService;

//@Component @Service 
public class AccountService<T extends Account> implements IAccountService<T> {

	private AccountDao<T> dao;

	public boolean addAccount(T entity) throws DataAccessException {
		if (entity == null) {

			try {
				throw new Exception(Account.class.getName() + "对象参数信息为Empty！");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dao.addAccount(entity);
	}

	public T getAccount(Integer id) throws DataAccessException {
		return dao.getAccount(id);
	}

	public List<T> getList() throws DataAccessException {
		return dao.getList();
	}

	public boolean execute(T entity) throws DataAccessException {
		if (entity == null) {

			try {
				throw new Exception(Account.class.getName() + "对象参数信息为Empty！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dao.addAccount(entity);
	}
}