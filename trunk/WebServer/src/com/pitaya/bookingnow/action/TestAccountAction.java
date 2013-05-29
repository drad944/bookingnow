package com.pitaya.bookingnow.action;

import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pitaya.bookingnow.model.Account;

public class TestAccountAction {
	@Test
	public void testAccountAction() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		AccountAction accountAction = (AccountAction) ctx.getBean("AccountAction");
		accountAction.toString();
	}

}
