package com.pitaya.bookingnow.app.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class UDPService implements Runnable{

	private static final String TAG = "UDPService";
	private static final long kEEPALIVE_THRESHOLD = 3;
	private static final int KEEPALIVE_TIMEOUT = 20000;
	private static final long KEEPALIVE_INTEVAL = 10000L;
	private static final int TIMEOUT = 30000;
	private DatagramSocket dSocket;
	private Long lastRecvTime;
	private Timer mChecker;
	private int timeout_times;
	private boolean flag;
	private boolean isRunning;
	private EnhancedMessageService _service;
	
	public UDPService(EnhancedMessageService ms){
		this._service = ms;
		this.timeout_times = 0;
		this.lastRecvTime = 0L;
		this.flag = true;
	}
	
	@Override
	public void run() {
		isRunning = true;
		try {
			this.dSocket = new DatagramSocket(HttpService.UDPPORT);
			this.dSocket.setSoTimeout(TIMEOUT);
			InetAddress remoteAddr = null;
			try {
				remoteAddr = InetAddress.getByName(HttpService.IP);
			} catch (UnknownHostException e) {
				Log.e(TAG, "Fail to get server address for udp service");
				e.printStackTrace();
				this.isRunning = false;
				return;
			}
			final InetAddress remoteAddrRef = remoteAddr;
			mChecker = new Timer();
			mChecker.schedule(new TimerTask(){

				@Override
				public void run() {
						try {
							DatagramPacket packet = new DatagramPacket(new byte[1], 1, remoteAddrRef, HttpService.UDPPORT);
							dSocket.send(packet);
							if(System.currentTimeMillis() - lastRecvTime > KEEPALIVE_TIMEOUT){
								 timeout_times ++;
								 if(timeout_times > kEEPALIVE_THRESHOLD){
									 mChecker.cancel();
									 _service.onDisconnect();
									 return;
								 }
							} else {
								timeout_times = 0;
							}
						} catch (IOException e) {
							e.printStackTrace();
							mChecker.cancel();
							_service.onDisconnect();
						}
				}
				
			}, 1000, KEEPALIVE_INTEVAL);
			
			_service.onUDPServiceStart();
			while(flag){
				byte[] result = new byte[1];
				DatagramPacket recvPacket = new DatagramPacket(result, 1);
				try {
					dSocket.setSoTimeout(TIMEOUT);
					dSocket.receive(recvPacket);
					if(recvPacket.getAddress().equals(remoteAddr)
							&& recvPacket.getLength() == 1 && result[0] == 0x00){
						this.lastRecvTime = System.currentTimeMillis();
						Log.d(TAG, "Receive keep alive udp packet");
					}
				} catch (IOException e) {
					e.printStackTrace();
					if(! (e instanceof SocketTimeoutException)){
						_service.onDisconnect();
					}
				}
			}
		} catch (SocketException e) {
			Log.e(TAG, "Fail to start udp service");
			e.printStackTrace();
		} finally {
			isRunning = false;
		}
	}
	
	public boolean isRunnning(){
		return this.isRunning;
	}
	
	public void shutdown(){
		this.isRunning = false;
		this.flag = false;
		if(this.dSocket != null){
			this.dSocket.close();
		}
	}
	
}
