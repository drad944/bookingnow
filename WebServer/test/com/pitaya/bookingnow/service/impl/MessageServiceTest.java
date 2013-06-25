package com.pitaya.bookingnow.service.impl;

import junit.framework.TestCase;

public class MessageServiceTest extends TestCase {

		MessageService messageServer;
	
		@Override 
		public void setUp(){
			messageServer = MessageService.initService(19191);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i=0; i < 10; i++){
				new Client().start();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		
		public void testSendToGroup(){
//			messageServer.sendMessageToGroup(Constants.CASH_GP, "有新的订单需要结帐");
//			messageServer.sendMessageToGroup(Constants.KITCHEN_GP, "新的菜品加工条目");
//			messageServer.sendMessageToGroup(Constants.WAITER_GP, "菜品加工好了");
		}
		
		@Override 
		public void tearDown(){
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MessageService.shutdownService();
		}
		
}
