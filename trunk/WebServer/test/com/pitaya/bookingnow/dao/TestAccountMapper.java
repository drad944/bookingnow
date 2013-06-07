package com.pitaya.bookingnow.dao;
import java.util.Date;

import org.apache.ibatis.session.SqlSession; 
import org.apache.ibatis.session.SqlSessionFactory; 
import org.junit.Test; 

import com.pitaya.bookingnow.model.Account;
import com.pitaya.bookingnow.util.MyBatisUtil;
 
public class TestAccountMapper { 
    static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 
 
    @Test 
    public void testAdd() { 
       SqlSession sqlSession = sqlSessionFactory.openSession(); 
       try { 
           AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class); 
           Account newAccount = new Account();
	   		newAccount.setAid(2);
	   		newAccount.setBirthday(new Date());
	   		newAccount.setCreateDateTime(new Date());
	   		newAccount.setImage(new byte[]{});
	   		newAccount.setLevel(3);
	   		newAccount.setName("li");
	   		newAccount.setPassword("123456");
	   		newAccount.setRole("boss");
	   		newAccount.setSex("m");
   		
           accountMapper.insert(newAccount);
           sqlSession.commit();
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
    @Test 
    public void testGetAccount() { 
       SqlSession sqlSession = sqlSessionFactory.openSession(); 
       try { 
           AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class); 
           
           Account newAccount = new Account();
	   		
           Account account = accountMapper.selectByPrimaryKey(1);
           System.out.println("name: "+account.getName()+" | role: "+account.getRole()); 
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
}