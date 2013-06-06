package com.pitaya.bookingnow.service;




import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.model.AccountExample;


public interface IAccountService{

    int deleteByPrimaryKey(Integer aid);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Integer aid);
    
    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

	List<Account> selectByExample(Account record);
	
	Account selectForLogin(Account record);
}
