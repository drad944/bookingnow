package com.pitaya.bookingnow.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.entity.Food;
import com.pitaya.bookingnow.util.MyBatisUtil;

public class TestFoodMapper {
	static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 

    public void showFood(Food food) {
    	if (food != null) {
			System.out.println("Id : " + food.getId());
			System.out.println("Category : " + food.getCategory());
			System.out.println("Description : " + food.getDescription());
			System.out.println("Large_image_absolute_path : " + food.getLarge_image_absolute_path());
			System.out.println("Large_image_relative_path : " + food.getLarge_image_relative_path());
			System.out.println("Name : " + food.getName());
			System.out.println("Small_image_absolute_path : " + food.getSmall_image_absolute_path());
			System.out.println("Small_image_relative_path : " + food.getSmall_image_relative_path());
			System.out.println("Large_image_size : " + food.getLarge_image_size());
			System.out.println("Period : " + food.getPeriod());
			System.out.println("Price : " + food.getPrice());
			System.out.println("Recommendation : " + food.getRecommendation());
			System.out.println("Small_image_size : " + food.getSmall_image_size());
			System.out.println("Status : " + food.getStatus());
			System.out.println("Version : " + food.getVersion());
			System.out.println("Enabled : " + food.getEnabled());
			System.out.println("Large_image : " + food.getLarge_image());
			System.out.println("Small_image : " + food.getSmall_image());
		}
    }
    
    @Test 
    public void testSelectByPrimaryKey() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		FoodMapper foodMapper = sqlSession.getMapper(FoodMapper.class);
    		Food newFood = foodMapper.selectByPrimaryKey((long)2);
    		showFood(newFood);
    		
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
    		tempFood.setCategory("中餐");
    		
    		List<Food> newFoods = foodMapper.selectFoods(tempFood);
    		if(newFoods != null) {
    			
    			for (int i = 0; i < newFoods.size(); i++) {
    				Food newFood = newFoods.get(i);
    				showFood(newFood);
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
    		newFood.setCategory("");
    		newFood.setDescription("laji");
    		newFood.setName("回锅肉");
    		newFood.setPeriod(new Date().getTime());
    		newFood.setPrice(108.00);
    		newFood.setVersion(new Date().getTime());
    		
    		foodMapper.insertSelective(newFood);
    		
    		sqlSession.commit();
    		
    		Food tempFood = foodMapper.selectByPrimaryKey(newFood.getId());
    		showFood(tempFood);
    		
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
    		tempFood.setName("回锅肉2");
    		tempFood.setDescription("回锅肉更新了哈");
    		tempFood.setPrice(98.1);
    		
    		if(tempFood != null && tempFood.getId() != null) {
	    		
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
    		tempFood.setName("卤猪蹄");
    		
    		List<Food> newFoods = foodMapper.selectFoods(tempFood);
    		if(newFoods != null && newFoods.size() > 0) {
    			
    			for (int i = 0; i < newFoods.size(); i++) {
    				Food newFood = newFoods.get(i);
    	    		
    	    		foodMapper.deleteByPrimaryKey(newFood.getId());
    			}
    		}
    		sqlSession.commit();
    	}finally { 
            sqlSession.close(); 
        } 
    }
}
