package com.pitaya.bookingnow.security.dao;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.dao.security.UserMapper;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.util.MyBatisUtil;
 
public class TestUserMapper { 
    static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 
 
    @Test 
    public void testAdd() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		UserMapper userMapper = sqlSession.getMapper(UserMapper.class); 
    		User newUser = new User();
    		newUser.setAccount("shax");
    		newUser.setAddress("lsdjf sldf sdlfkj sldkf");
    		newUser.setBirthday(new Date().getTime());
    		newUser.setDepartment(1);
    		newUser.setDescription("good man");
    		newUser.setEmail("shax@qq.com");
    		newUser.setName("shax");
    		newUser.setPassword("1234");
    		newUser.setPhone("13245678910");
    		newUser.setSex(1);
    		newUser.setSub_system(2);
    		userMapper.insert(newUser);
   		
           sqlSession.commit();
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
    @Test 
    public void testGetUser() { 
       SqlSession sqlSession = sqlSessionFactory.openSession(); 
       try { 
           UserMapper userMapper = sqlSession.getMapper(UserMapper.class); 
           
           User newUser = null;
	   		
           newUser = userMapper.selectByPrimaryKey((long)1);
           System.out.println("account: "+newUser.getAccount());
           System.out.println("password: "+newUser.getPassword()); 
           System.out.println("birthday: "+newUser.getBirthday()); 
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
}