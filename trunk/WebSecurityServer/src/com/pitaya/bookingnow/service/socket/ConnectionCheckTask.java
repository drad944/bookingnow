package com.pitaya.bookingnow.service.socket;

import java.util.TimerTask;


public class ConnectionCheckTask extends TimerTask {

	private IMessageService service;
	
	public ConnectionCheckTask(IMessageService service){
		this.service = service;
	}
	
	@Override
	public void run() {
		for(IClient client : service.getClients()){
			client.checkConnection();
		}
	}

}
