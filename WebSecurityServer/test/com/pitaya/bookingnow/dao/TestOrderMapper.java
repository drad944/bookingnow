package com.pitaya.bookingnow.dao;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.pitaya.bookingnow.entity.Order;
import com.pitaya.bookingnow.util.MyBatisUtil;
 
public class TestOrderMapper { 
    static SqlSessionFactory sqlSessionFactory = null; 
    static { 
       sqlSessionFactory = MyBatisUtil.getSqlSessionFactory(); 
    } 
 
    @Test 
    public void testAdd() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
    		Order newOrder = new Order();
    		
    		newOrder.setSubmit_time(new Date());
    		newOrder.setEnabled(true);
    		newOrder.setAllowance(98);
    		newOrder.setStatus(1);
    		newOrder.setModifyTime(new Date());
    		
    		orderMapper.insert(newOrder);
   		
           sqlSession.commit();
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
    @Test 
    public void testGetOrder() {
       SqlSession sqlSession = sqlSessionFactory.openSession(); 
       try { 
           OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class); 
           
           Order newOrder = null;
	   		
           newOrder = orderMapper.selectByPrimaryKey((long)1);
           System.out.println("status: "+newOrder.getStatus());
           System.out.println("allowance: "+newOrder.getAllowance()); 
           System.out.println("id: "+newOrder.getId()); 
           
           
       } finally { 
           sqlSession.close(); 
       } 
    } 
    
    @Test 
    public void testGetOrders() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

			List<Order> resultOrders = null;
			Order templateOrder = new Order();
			templateOrder.setAllowance(95);

			resultOrders = orderMapper.searchSelective(templateOrder);

			if (resultOrders != null) {
				for (int i = 0; i < resultOrders.size(); i++) {
					Order tempOrder = resultOrders.get(i);
					System.out.println("id: " + tempOrder.getId());
					System.out
							.println("status: " + tempOrder.getStatus());
					System.out.println("allowance: "
							+ tempOrder.getAllowance());
					System.out.println("modify time: "
							+ tempOrder.getModifyTime());
					System.out.println("submit time: "
							+ tempOrder.getSubmit_time());
					System.out.println("enabled: " + tempOrder.getEnabled());
				}
			}

		} finally {
			sqlSession.close();
		} 
    } 
 
}