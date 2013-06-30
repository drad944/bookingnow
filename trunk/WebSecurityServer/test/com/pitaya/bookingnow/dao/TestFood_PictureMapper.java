package com.pitaya.bookingnow.dao;

import java.util.Date;

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
    		Food_Picture newFood_Picture = food_pictureMapper.selectByPrimaryKey((long)5);
    		System.out.println("id : " + newFood_Picture.getId());
    		System.out.println("name : " + newFood_Picture.getName());
    		System.out.println("Last_modify_time : " + newFood_Picture.getVersion());
    		System.out.println("Small_image : " + newFood_Picture.getSmall_image());
    		System.out.println("big image : " + newFood_Picture.getBig_image());
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testSelectByFoodId() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		Food_PictureMapper food_pictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
    		Food_Picture newFood_Picture = food_pictureMapper.selectByFoodId((long)5);
    		System.out.println("id : " + newFood_Picture.getId());
    		System.out.println("name : " + newFood_Picture.getName());
    		System.out.println("Last_modify_time : " + newFood_Picture.getVersion());
    		System.out.println("Small_image : " + newFood_Picture.getSmall_image());
    		System.out.println("big image : " + newFood_Picture.getBig_image());
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testInsertSelective() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	try { 
    		
    		Food_PictureMapper food_PictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
    		Food_Picture picture = new Food_Picture();
    		picture.setEnabled(true);
    		picture.setVersion(new Date().getTime());
    		picture.setName("回锅炒肉");
    		picture.setSmall_image(new byte[]{1,2,3});
    		food_PictureMapper.insertSelective(picture);
    		sqlSession.commit();
    		
    		Food_Picture tempPicture = food_PictureMapper.selectByPrimaryKey(picture.getId());
    		
    		if(picture != null) {
    			System.out.println("id : " + tempPicture.getId());
        		System.out.println("name : " + tempPicture.getName());
        		System.out.println("Last_modify_time : " + tempPicture.getVersion());
    		}
    		
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
}
