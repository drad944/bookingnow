package com.pitaya.bookingnow.dao;

import java.util.List; 
import org.springframework.dao.DataAccessException; 
import org.springframework.stereotype.Repository; 

import com.pitaya.bookingnow.model.Account;

@SuppressWarnings("unchecked")
@Repository
public class AccountDaoImpl<T extends Account> implements AccountDao<T> {
	private BaseMapperDao<Account> dao;

	public boolean addAccount(T entity) throws DataAccessException {
		dao.setMapperClass(AccountMapper.class);
		boolean flag = false;
		try {
			dao.add(entity);
			flag = true;
		} catch (DataAccessException e) {
			flag = false;
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return flag;
	}

	public T getAccount(Long id) throws DataAccessException {
		dao.setMapperClass(AccountMapper.class);
		Account acc = new Account();
		T entity = null;
		try {
			acc.setAid(id);
			entity = (T) dao.get(acc);
		} catch (DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}

	public List<T> getList() throws DataAccessException {
		dao.setMapperClass(AccountMapper.class);
		List<T> list = null;
		try {
			list = (List<T>) ((AccountMapper) dao.getMapper()).getAllAccount();
		} catch (DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return list;
	}

}