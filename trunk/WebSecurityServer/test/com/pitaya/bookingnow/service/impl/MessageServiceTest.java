package com.pitaya.bookingnow.service.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.message.OrderDetailMessage;
import com.pitaya.bookingnow.util.Constants;

import junit.framework.TestCase;

public class MessageServiceTest extends TestCase {

		MessageService messageServer;
	
		@Override 
		public void setUp(){
			ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
			messageServer = (MessageService)aCtx.getBean("messageService");
			messageServer.start(19191);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			for(int i=0; i < 10; i++){
//				new Client().start();
//			}
			new Client(3L).start();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		
		public void testSendToGroup(){
			for(int i = 0; i < 10; i++){
				new Thread(){
					
					@Override
					public void run(){
						OrderDetailMessage msg = new OrderDetailMessage();
						msg.setHasNew(true);
						messageServer.sendMessageToGroup(Constants.ROLE_CHEF, msg);
					}
					
				}.start();
			}
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		@Override 
		public void tearDown(){
			messageServer.shutdown();
		}
		
}
