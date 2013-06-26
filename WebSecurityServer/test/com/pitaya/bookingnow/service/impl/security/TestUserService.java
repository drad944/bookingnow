package com.pitaya.bookingnow.service.impl.security;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.service.impl.security.UserService;


public class TestUserService {
	Logger logger = Logger.getLogger("TestUserService");
	 
	 UserService service = null;

	 @Before
	 public void init() {
	  
	  ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
	  UserService service = (UserService) aCtx.getBean("userService");
	  assertNotNull(service);
	  this.service = service;
	 }
	 
	 @Test
	 public void testAddUser() {

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
	  
	  service.add(newUser);
	  
	  User userFromDb =  service.login(newUser);
	  assertNotNull(userFromDb);
	  System.out.println("id: " + userFromDb.getId());
	 }
}
