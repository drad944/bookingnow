package com.pitaya.bookingnow.app.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.util.Log;

public class UDPService implements Runnable{

	private static final String TAG = "UDPService";
	private static final int RETRY_TIMES = 3;
	private static final int TIMEOUT = 10;
	private static final long INTEVAL = 60000L;
	private DatagramSocket dSocket;
	private boolean flag;
	private boolean isRunning;
	private EnhancedMessageService _service;
	
	public UDPService(EnhancedMessageService ms){
		this._service = ms;
	}
	
	@Override
	public void run() {
		isRunning = true;
		this.flag = true;
		try {
			this.dSocket = new DatagramSocket(HttpService.UDPPORT);
			this.dSocket.setSoTimeout(TIMEOUT);
			InetAddress remoteAddr = null;
			try {
				remoteAddr = InetAddress.getByAddress(HttpService.IP.getBytes());
			} catch (UnknownHostException e) {
				Log.e(TAG, "Fail to get server address for udp service");
				e.printStackTrace();
				this.isRunning = false;
				return;
			}
			
			final DatagramPacket packet = new DatagramPacket(new byte[1], 1, remoteAddr, HttpService.UDPPORT);
			final Handler handler = new Handler();
			final InetAddress remoteAddrRef = remoteAddr;
			handler.post(new Runnable(){

				@Override
				public void run() {
					int i = 0;
					boolean ok = false;
					do{
						try {
							dSocket.send(packet);
							byte[] result = new byte[1];
							DatagramPacket recvPacket = new DatagramPacket(result, 1);
							dSocket.receive(recvPacket);
							if(recvPacket.getAddress().equals(remoteAddrRef) 
									&& recvPacket.getLength() == 1 && result[0] == 0x00){
								ok = true;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} while (!ok && i++ < RETRY_TIMES);
					
					if(ok && flag == true){
						handler.postDelayed(this, INTEVAL);
						Log.d(TAG, "UDP check success");
					} else {
						handler.removeCallbacks(this);
						onDisconnect();
					}
				}
				
			});

		} catch (SocketException e) {
			Log.e(TAG, "Fail to start udp service");
			e.printStackTrace();
			isRunning = false;
		}
	}
	
	public boolean isRunnning(){
		return this.isRunning;
	}
	
	public void shutdown(){
		this.isRunning = false;
		this.flag = false;
		this.dSocket.close();
	}
	
	private void onDisconnect(){
		Log.d(TAG, "UDP check fail, assume server is disconnected");
		this.shutdown();
		this._service.onDisconnect();
	}
	
}
