package com.pitaya.bookingnow.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.message.ResultMessage;


import android.util.Log;

public class Client extends Thread{
	  	
		private static String LOGTAG = "ClientThread";
		private Socket socket;
	    private String ip;
	    private int port;
	    private BufferedReader in;
	    private OutputStreamWriter out;
	    private BufferedWriter bwriter;
	    private MessageService service;
	    private Long userid;
	    private volatile boolean isConnecting = false;

	    public Client(String ip, int port, MessageService ms) {
	    	this.ip = ip;
	    	this.port = port;
	    	this.service = ms;
	    }
	    
	    public void run(){
	    	boolean error = false;
	        try {
		    	isConnecting = true;
				setupConnection();
				isConnecting = false;
				Log.i(LOGTAG, "Success to connect to web server");
            	String message = null;
	        	if(this.service != null){
    				this.service.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.SUCCESS, "连接服务器成功"));
    			}
            	while((message = in.readLine()) != null){
        			if(this.service != null){
        				this.service.onMessage(message);
        			}
            		if(message.equals("bye")){
            			shutdown();
            			break;
            		}
            	}
	        } catch (UnknownHostException e) {
	        	if(this.service != null){
	        		UserManager.setLoginUser(this.service, null);
    				this.service.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "无法识别的服务器"));
    			}
	        	error = true;
	        	Log.e(LOGTAG, "Fail to connect to web server");
	            e.printStackTrace();
	        } catch (IOException e) {
	        	if(this.service != null){
	        		UserManager.setLoginUser(this.service, null);
    				this.service.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "无法连接到服务器或连接中断"));
    			}
	        	error = true;
	        	Log.e(LOGTAG, "Fail to connect to web server");
				e.printStackTrace();
			} finally {
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
	       		if (socket != null && !socket.isClosed()){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	       		}
	       		socket = null;
		    	isConnecting = false;
	        	if(this.service != null && error == false){
	        		UserManager.setLoginUser(this.service, null);
    				this.service.onMessage(new ResultMessage(Constants.SOCKET_CONNECTION, Constants.FAIL, "与服务器连接中断"));
    			}
	        }
	    }

	    public void setUserId(Long id){
	    	this.userid = id;
	    }
	    
	    public Long getUserId(){
	    	return this.userid;
	    }
	    
	    public void shutdown(){
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
			 if (socket != null && !socket.isClosed()){
				try {
					socket.close();
					Log.i(LOGTAG, "Success to shutdown the connection to server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
			 socket = null;
	    }

	    public synchronized boolean sendMessage(String msg) {
	        try {
	        	this.bwriter.write(msg + "\r\n");
	        	this.bwriter.flush();
	        	return true;
	        } catch (Exception e) {
	            Log.e(LOGTAG, "Fail to send message:" + msg);
	            return false;
	        }
	    }
	    
	    public boolean isReady(){
	    	return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
	    }
	    
	    public boolean isConnecting(){
	    	return this.isConnecting;
	    }
  	    
	    private void setupConnection() throws UnknownHostException, IOException {
	        socket = new Socket(ip, port);
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        	out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");  
			bwriter = new BufferedWriter(out);  
	    }

}
