package com.pitaya.bookingnow.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import com.pitaya.bookingnow.model.Account;

@Repository
public class AccountDao implements AccountMapper{
	
	public AccountDao() {
		
	}
	
	@Resource
	private SqlSessionFactory sessionFactory;

	public SqlSessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SqlSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<Account> selectAccountByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Account selectAccount(String name, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	public Account selectAccountByID(long aid) {
		  SqlSession session = sessionFactory.openSession();
		  Account AccountFromDB = (Account)session.selectOne("com.pitaya.bookingnow.dao.AccountMapper.selectAccountByID", aid);
		  return AccountFromDB;
	}

	public boolean insertAccount(Account account) {
		SqlSession session = sessionFactory.openSession();
		session.insert("com.pitaya.bookingnow.dao.AccountMapper.insertAccount", account);
		return true;
	}

	public boolean updateAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteAccount(Account account) {
		// TODO Auto-generated method stub
		return false;
	}

}
