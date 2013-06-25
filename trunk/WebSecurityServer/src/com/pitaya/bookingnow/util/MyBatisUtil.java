package com.pitaya.bookingnow.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 
public class MyBatisUtil  { 
    private  final static SqlSessionFactory sqlSessionFactory; 
    static { 
       String resource = "mybatis-config.xml"; 
       
       
       
       InputStream inputStream = null; 
       try { 
    	   inputStream = Resources.getResourceAsStream(resource);
       } catch (IOException e) { 
           System.out.println(e.getMessage()); 
 
       } 
       
       sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    } 
 
    public static SqlSessionFactory getSqlSessionFactory() { 
       return sqlSessionFactory; 
    } 
}

