package com.pitaya.bookingnow.dao;
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
           Account account = new Account("zhang2","123456","boss"); 
           accountMapper.add(account);
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
           Account account = accountMapper.getByID(new Account(1));
           System.out.println("name: "+account.getName()+" | role: "+account.getRole()); 
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
}