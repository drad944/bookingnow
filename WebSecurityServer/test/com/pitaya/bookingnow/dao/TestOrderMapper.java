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
    public void testSelectFullOrderByPrimaryKey() {
       SqlSession sqlSession = sqlSessionFactory.openSession(); 
       try { 
           OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class); 
           
           Order newOrder = null;
	   		
           newOrder = orderMapper.selectFullOrderByPrimaryKey((long)1);
           
           if (newOrder != null) {
        	   
        	   System.out.println("order id: "+newOrder.getId()); 
	           System.out.println("status: "+newOrder.getStatus());
	           System.out.println("allowance: "+newOrder.getAllowance()); 
	           System.out.println("PrePay: "+newOrder.getPrePay()); 
	           System.out.println("Total_price: "+newOrder.getTotal_price()); 
	           System.out.println("Submit_time: "+newOrder.getSubmit_time()); 
	           System.out.println("Enabled: "+newOrder.getEnabled());
	           System.out.println();
	           
           }else {
			System.out.println("can not find order!");
		}
           
	           
           
           
       } finally { 
           sqlSession.close(); 
       } 
    } 
 
    @Test 
    public void testAdd() { 
    	SqlSession sqlSession = sqlSessionFactory.openSession(); 
    	try { 
    		OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
    		Order newOrder = new Order();
    		
    		newOrder.setSubmit_time(new Date().getTime());
    		newOrder.setEnabled(true);
    		newOrder.setAllowance(0.98);
    		newOrder.setStatus(1);
    		newOrder.setModifyTime(new Date().getTime());
    		
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
           System.out.println("id: "+newOrder.getId()); 
           System.out.println("status: "+newOrder.getStatus());
           System.out.println("allowance: "+newOrder.getAllowance()); 
           System.out.println("PrePay: "+newOrder.getPrePay()); 
           System.out.println("Total_price: "+newOrder.getTotal_price()); 
           System.out.println("Submit_time: "+newOrder.getSubmit_time()); 
           System.out.println("Enabled: "+newOrder.getEnabled()); 
           
           
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
			templateOrder.setAllowance(0.95);

			resultOrders = orderMapper.searchOrders(templateOrder);

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