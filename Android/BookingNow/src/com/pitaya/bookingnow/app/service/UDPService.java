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

import android.util.Log;

public class UDPService implements Runnable{

	private static final String TAG = "UDPService";
	//Max afford fail times
	private static final long kEEPALIVE_THRESHOLD = 3;
	//Log a fail if not received UDP check packet in this time
	private static final int KEEPALIVE_TIMEOUT = 60000;
	//UDP check packet sending rate, it must be shorter than KEEPALIVE_TIMEOUT
	private static final long KEEPALIVE_INTEVAL = 15000L;
	
	private static final int TIMEOUT = 30000;
	private DatagramSocket mReceiverSocket;
	private DatagramSocket mSenderSocket;
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
			this.mReceiverSocket = new DatagramSocket(HttpService.UDPPORT);
			this.mSenderSocket = new DatagramSocket();
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
							if(System.currentTimeMillis() - lastRecvTime > KEEPALIVE_TIMEOUT){
								Log.w(TAG, "Timeout on receiving keep alive packet");
								 timeout_times ++;
								 if(timeout_times > kEEPALIVE_THRESHOLD){
									 mChecker.cancel();
									 _service.onDisconnect("与服务器的连接中断，请检查网络");
									 return;
								 }
							} else {
								timeout_times = 0;
							}
							DatagramPacket packet = new DatagramPacket(new byte[1], 1, remoteAddrRef, HttpService.UDPPORT);
							mSenderSocket.send(packet);
						} catch (IOException e) {
							e.printStackTrace();
							Log.d(TAG, "Fail to send udp packet");
						}
				}
				
			}, 1000, KEEPALIVE_INTEVAL);
			
			_service.onUDPServiceStart();
			while(flag){
				byte[] result = new byte[1];
				DatagramPacket recvPacket = new DatagramPacket(result, 1);
				try {
					this.mReceiverSocket.setSoTimeout(TIMEOUT);
					this.mReceiverSocket.receive(recvPacket);
					if(recvPacket.getAddress().equals(remoteAddr)
							&& recvPacket.getLength() == 1 && result[0] == 0x00){
						this.lastRecvTime = System.currentTimeMillis();
						Log.d(TAG, "Receive keep alive udp packet");
					}
				} catch (IOException e) {
					e.printStackTrace();
					Log.e(TAG, "Fail to receive udp packet");
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
		this.flag = false;
		if(this.mSenderSocket != null){
			this.mSenderSocket.close();
		}
		if(this.mReceiverSocket != null){
			this.mReceiverSocket.close();
		}
		this.isRunning = false;
	}
	
}
