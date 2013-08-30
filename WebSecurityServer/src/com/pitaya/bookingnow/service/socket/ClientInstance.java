package com.pitaya.bookingnow.service.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

	static final long KEEPALIVE_TIMEOUT = 20000L;
	static final long kEEPALIVE_THRESHOLD = 3;
	private static final long KEEP_ALIVE_INTEVAL = 10000L;
	private static final long SOCKET_CLOSE_DELAY = 10000L;
	private static final Log logger =  LogFactory.getLog(ClientInstance.class);
	
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
		this.lastRecvTime = 0L;
		this.init();
	}
	
	private void init(){
		try {
			this.udpSocket = new DatagramSocket();
			mChecker = new Timer();
			mChecker.schedule(new TimerTask(){

				@Override
				public void run() {
					if((System.currentTimeMillis() - lastRecvTime) > ClientInstance.KEEPALIVE_TIMEOUT){
						logger.debug("Timeout on keep alive packet");
						timeout_times ++;
						if(timeout_times > ClientInstance.kEEPALIVE_THRESHOLD){
							disconnect();
							return;
						}
					} else {
						timeout_times = 0;
					}
					DatagramPacket sendPacket = null;
					try {
						byte[] data = new byte[1];
						data[0] = 0x00;
						sendPacket = new DatagramPacket(data, data.length, address, udpport);
						udpSocket.send(sendPacket);
						logger.debug("Send keep alive packet to client");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}, 1000, KEEP_ALIVE_INTEVAL);
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
    	if(this.mConnectionCloser != null){
    		this.mConnectionCloser.cancel();
    		this.mConnectionCloser = null;
    	}
    	if(!this.isReady()){
        	try {
    			this.setupConnection();
    		} catch (UnknownHostException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
		if(!this.isReady()){
			//Can't connect to client, assume it's disconnected
			this.disconnect();
			return false;
		}
		this.messageQueue.add(msg);
    	try {
    		String recvMessage = null;
			while((recvMessage = in.readLine()) != null){
				if(recvMessage.equals("ready")){
					break;
				}
			}
    		while(this.messageQueue.size() > 0){
    			String message = this.messageQueue.get(0);
    			this.bwriter.write(message + "\r\n");
        		this.bwriter.flush();
        		this.messageQueue.remove(0);
        		logger.debug("Send message to client: " + this.userId);
    		}
        	return true;
        } catch (IOException e) {
            logger.error("Fail to send message to client: " + this.userId);
            e.printStackTrace();
            return false;
        } finally {
        	//If this socket is not reused in DELAY time, then it will be closed
    		this.mConnectionCloser = new Timer();
    		this.mConnectionCloser.schedule(new TimerTask(){

				@Override
				public void run() {
					shutdown(null);
				}
    			
    		}, SOCKET_CLOSE_DELAY);
        }
    }
    
    void onCheckFail(){
    	this.disconnect();
    }
    
    void disconnect(){
    	logger.debug("Disconnect to client: " + this.userId);
    	//This will result in shutdown invoking later
    	this._service.removeClient(this, true);
    }
    
    private void setupConnection() throws UnknownHostException, IOException {
        socket = new Socket(this.addr, this.port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
		}
		if(bwriter != null){
			try {
				bwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
