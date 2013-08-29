package com.pitaya.bookingnow.service.socket;

import java.util.TimerTask;


public class ConnectionCheckTask extends TimerTask {

	private MessageService service;
	
	public ConnectionCheckTask(MessageService service){
		this.service = service;
	}
	
	@Override
	public void run() {
		for(ClientAgent client : service.getClients()){
			client.sendHeartBeat();
		}
	}

}
