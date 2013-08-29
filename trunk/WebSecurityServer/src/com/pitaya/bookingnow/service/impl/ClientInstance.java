package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientInstance {
	
	private static Log logger =  LogFactory.getLog(Client.class);
	private static final long INTEVAL = 10000L;
	private static final long DELAY = 10000L;
	static final long TIMEOUT = 10000L;
	static final long TIMEOUT_TIMES = 3;
	InetAddress address;
	int port;
	int udpport;
	DatagramSocket udpSocket;
	Long lastRecvTime;
	int timeout_times;
	private Long userId;
	private String username;
	private Integer roleType;
	private Timer mChecker;
	private Timer mConnectionCloser;
    private BufferedReader in;
    private BufferedWriter bwriter;
    private String addr;
    private Socket socket;
	private ArrayList<String> messageQueue;
	private EnhancedMessageService _service;
	
	public ClientInstance(EnhancedMessageService _service, InetAddress addr, int port, int udpport){
		this._service = _service;
		this.address = addr;
		this.port = port;
		this.udpport = udpport;
		this.timeout_times = 0;
		this.messageQueue = new ArrayList<String>();
		this.init();
	}
	
	private void init(){
		try {
			this.udpSocket = new DatagramSocket();
			mChecker = new Timer();
			mChecker.schedule(new UDPCheckTask(this), 1000, INTEVAL);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserId(Long id){
		this.userId = id;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setUsername(String uname){
		this.username = uname;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setRoleType(Integer role){
		this.roleType = role;
	}
	
	public Integer getRoleType(){
		return this.roleType;
	}
	
	public ArrayList<String> getMessages(){
		return this.messageQueue;
	}
	
	public void addMessages(ArrayList<String> messages){
		this.messageQueue.addAll(messages);
	}
	
    public boolean isReady(){
    	return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
    }
	
    public synchronized boolean sendMessage(String msg) {
    	if(!this.isReady()){
        	try {
    			this.setupConnection();
    		} catch (UnknownHostException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    	if(this.mConnectionCloser != null){
    		this.mConnectionCloser.cancel();
    		this.mConnectionCloser = null;
    	}
		if(!this.isReady()){
			//Can't connect to client, assume it's disconnected
			this.disconnect();
			return false;
		}
		this.messageQueue.add(msg);
    	try {
    		while(this.messageQueue.size() > 0){
    			String message = this.messageQueue.get(0);
    			this.bwriter.write(message + "\r\n");
        		this.bwriter.flush();
        		this.messageQueue.remove(0);
        		logger.error("Send message to client: " + this.userId);
    		}
        	return true;
        } catch (IOException e) {
            logger.error("Fail to send message to client: " + this.userId);
            return false;
        } finally {
        	//If this socket is not reused in DELAY time, it will be closed
    		this.mConnectionCloser = new Timer();
    		this.mConnectionCloser.schedule(new TimerTask(){

				@Override
				public void run() {
					shutdown(null);
				}
    			
    		}, DELAY);
        }
    }
    
    void onCheckFail(){
    	this.disconnect();
    }
    
    void disconnect(){
    	logger.error("Disconnect to client: " + this.userId);
    	this._service.removeClient(this, true);
    }
    
    private void setupConnection() throws UnknownHostException, IOException {
        socket = new Socket(this.addr, this.port);
		bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));  
    }
    
	void shutdown(String message){
		if(message != null){
			this.sendMessage(message);
		}
    	if(this.mConnectionCloser != null){
    		this.mConnectionCloser.cancel();
    		this.mConnectionCloser = null;
    	}
    	if(this.mChecker != null){
        	this.mChecker.cancel();
        	this.mChecker = null;
    	}
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
		}
		if(bwriter != null){
			try {
				bwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bwriter = null;
		}
		try{
			if(this.socket != null && !this.socket.isClosed()){
				this.socket.close();
			}
		} catch (IOException e) {
			 e.printStackTrace();
		}
		socket = null;
		logger.debug("Success to shutdown connection to client [" + this.userId + "]");
	}
    
}
