package com.pitaya.bookingnow.action;


import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.entity.security.User;



public class TestUserAction {
	ApplicationContext ctx = null;
	
	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
	}
	
	@Test
	public void testRegisterUser() {
		UserAction userAction = (UserAction) ctx.getBean("userAction");
		User newUser = new User();
		newUser.setAccount("shax");
		newUser.setAddress("lsdjf sldf sdlfkj sldkf");
		newUser.setBirthday(new Date());
		newUser.setDepartment(1);
		newUser.setDescription("good man");
		newUser.setEmail("shax@qq.com");
		newUser.setName("shax");
		newUser.setPassword("1234");
		newUser.setPhone("13245678910");
		newUser.setSex(1);
		newUser.setSub_system(2);
		
		userAction.setUser(newUser);
		
		
		String result  = userAction.registerUser();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testFindUser() {
		UserAction userAction = (UserAction) ctx.getBean("userAction");
		User newUser = new User();
		newUser.setSex(1);
		userAction.setUser(newUser);
		
		
		String result  = userAction.findUser();
		System.out.println("result:" + result);
	}

	@Test
	public void testUpdateUser() {
		UserAction userAction = (UserAction) ctx.getBean("userAction");
		User newUser = new User();
		newUser.setId((long)3);
		newUser.setPassword("xxx");
		userAction.setUser(newUser);
		
		
		String result  = userAction.updateUser();
		System.out.println("result:" + result);
	}
	
	@Test
	public void testRemoveUser() {
		UserAction userAction = (UserAction) ctx.getBean("userAction");
		User newUser = new User();
		newUser.setId((long)1);
		userAction.setUser(newUser);
		
		String result  = userAction.removeUser();
		System.out.println("result:" + result);
	}
}
