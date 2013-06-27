package com.pitaya.bookingnow.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.entity.Food_Picture;
import com.pitaya.bookingnow.util.MyBatisUtil;

public class TestFood_PictureMapper {
	static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 
 
    
    @Test 
    public void testSelectByPrimaryKey() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		Food_PictureMapper food_pictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
    		Food_Picture newFood_Picture = food_pictureMapper.selectByPrimaryKey((long)2);
    		System.out.println("id : " + newFood_Picture.getId());
    		System.out.println("id : " + newFood_Picture.getName());
    		System.out.println("food_id : " + newFood_Picture.getFood_id());
    		System.out.println("Last_modify_time() : " + newFood_Picture.getLast_modify_time());
    		System.out.println("Small_image : " + newFood_Picture.getSmall_image());
    		
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
}
