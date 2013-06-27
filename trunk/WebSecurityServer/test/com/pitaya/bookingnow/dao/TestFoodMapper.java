package com.pitaya.bookingnow.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.util.MyBatisUtil;

public class TestFoodMapper {
	static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 
 
    @Test 
    public void testAdd() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food newFood = new Food();
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testSelectByPrimaryKey() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food newFood = foodMapper.selectByPrimaryKey((long)2);
    		System.out.println("name : " + newFood.getName());
    		System.out.println("price : " + newFood.getPrice());
    		
    		Food_Picture picture = newFood.getPicture();
    		if(picture != null) {
    			System.out.println("id : " + picture.getId());
        		System.out.println("name : " + picture.getName());
        		System.out.println("Last_modify_time : " + picture.getLast_modify_time());
    		}
    		
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
}
