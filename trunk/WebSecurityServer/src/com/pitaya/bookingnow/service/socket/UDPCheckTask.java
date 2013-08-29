package com.pitaya.bookingnow.service.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.TimerTask;

public class UDPCheckTask extends TimerTask{

	private ClientInstance mClient;
	
	public UDPCheckTask(ClientInstance client){
		this.mClient = client;
	}
	
	@Override
	public void run(){
		if((System.currentTimeMillis() - this.mClient.lastRecvTime) > ClientInstance.KEEPALIVE_TIMEOUT){
			this.mClient.timeout_times ++;
			if(this.mClient.timeout_times > ClientInstance.kEEPALIVE_THRESHOLD){
				this.mClient.disconnect();
				return;
			}
		} else {
			this.mClient.timeout_times = 0;
		}
		DatagramPacket sendPacket = null;
		try {
			byte[] data = new byte[1];
			data[0] = 0x00;
			sendPacket = new DatagramPacket(data, data.length, mClient.address, mClient.udpport);
			mClient.udpSocket.send(sendPacket);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
