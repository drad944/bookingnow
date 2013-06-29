package com.pitaya.bookingnow.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        		System.out.println("version : " + picture.getVersion());
    		}
    		
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testSelectFoods() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food tempFood = new Food();
    		tempFood.setCategory(501);
    		
    		List<Food> newFoods = foodMapper.selectFoods(tempFood);
    		if(newFoods != null) {
    			
    			for (int i = 0; i < newFoods.size(); i++) {
    				Food newFood = newFoods.get(i);
    				System.out.println("id : " + newFood.getId());
    				System.out.println("name : " + newFood.getName());
    	    		System.out.println("price : " + newFood.getPrice());
    	    		
    	    		Food_Picture picture = newFood.getPicture();
    	    		if(picture != null) {
    	    			System.out.println("id : " + picture.getId());
    	        		System.out.println("name : " + picture.getName());
    	        		System.out.println("version : " + picture.getVersion());
    	    		}
    			}
    		}
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testInsertSelective() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food newFood = new Food();
    		newFood.setCategory(505);
    		newFood.setDescription("laji");
    		newFood.setName("回锅肉");
    		newFood.setPeriod(new Date());
    		newFood.setPrice(new BigDecimal(108));
    		newFood.setVersion(2);
    		
    		Food_PictureMapper food_PictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
    		Food_Picture picture = new Food_Picture();
    		picture.setEnabled(true);
    		picture.setVersion(new Date());
    		picture.setName("回锅炒肉");
    		picture.setSmall_image(new byte[]{1,2,3});
    		
    		food_PictureMapper.insertSelective(picture);
    		
    		newFood.setPicture_id(picture.getId());
    		foodMapper.insertSelective(newFood);
    		
    		sqlSession.commit();
    		
    		Food tempFood = foodMapper.selectByPrimaryKey(newFood.getId());
    		System.out.println("name : " + tempFood.getName());
    		System.out.println("price : " + tempFood.getPrice());
    		
    		Food_Picture tempPicture = tempFood.getPicture();
    		if(picture != null) {
    			System.out.println("id : " + tempPicture.getId());
        		System.out.println("name : " + tempPicture.getName());
        		System.out.println("version : " + tempPicture.getVersion());
    		}
    		
    		
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testUpdateByPrimaryKeySelective() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food tempFood = new Food();
    		tempFood.setId((long)21);
    		tempFood.setPicture_id((long)22);
    		tempFood.setName("回锅肉2");
    		tempFood.setDescription("回锅肉更新了哈");
    		tempFood.setPrice(new BigDecimal(98.12));
    		
    		Food_Picture picture = new Food_Picture();
    		picture.setEnabled(true);
    		picture.setVersion(new Date());
    		picture.setName("回锅炒肉 图片2");
    		picture.setSmall_image(new byte[]{1,2,3,4,5,6});
    		picture.setBig_image(new byte[]{7,8,9});
    		
    		tempFood.setPicture(picture);
    		
    		
    		if(tempFood != null && tempFood.getId() != null) {
    			
				Food_PictureMapper food_PictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
				
	    		if(tempFood.getPicture_id() != null && tempFood.getPicture() != null) {
	    			Food_Picture tempPicture = tempFood.getPicture();
	    			tempPicture.setId(tempFood.getPicture_id());
	    			food_PictureMapper.updateByPrimaryKeySelective(tempPicture);
	    		}else if(tempFood.getPicture() != null && tempFood.getPicture().getId() != null) {
	    			food_PictureMapper.updateByPrimaryKeySelective(tempFood.getPicture());
	    			
				}
	    		
	    		foodMapper.updateByPrimaryKeySelective(tempFood);
    		}
    		sqlSession.commit();
    	}finally { 
            sqlSession.close(); 
        } 
    }
    
    @Test 
    public void testDeleteByPrimaryKey() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food tempFood = new Food();
    		tempFood.setName("回锅肉2");
    		
    		List<Food> newFoods = foodMapper.selectFoods(tempFood);
    		if(newFoods != null && newFoods.size() > 0) {
    			
    			for (int i = 0; i < newFoods.size(); i++) {
    				Food newFood = newFoods.get(i);
    				
    				
    				Food_PictureMapper food_PictureMapper = sqlSession.getMapper(Food_PictureMapper.class);
    	    		if(newFood.getPicture_id() != null && newFood.getPicture_id() != 0) {
    	    			food_PictureMapper.deleteByPrimaryKey(newFood.getPicture_id());
    	    		}
    	    		
    	    		foodMapper.deleteByPrimaryKey(newFood.getId());
    			}
    		}
    		sqlSession.commit();
    	}finally { 
            sqlSession.close(); 
        } 
    }
}
