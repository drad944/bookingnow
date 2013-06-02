package com.pitaya.bookingnow.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.pitaya.bookingnow.message.BaseResultMessage;


import android.util.Log;

public class Client extends Thread{
	  	
		private static String LOGTAG = "ClientThread";
		private Socket socket;
	    private String ip;
	    private int port;
	    private BufferedReader in;
	    private PrintWriter out;
	    private MessageService service;
	    private volatile boolean isConnecting = false;

	    public Client(String ip, int port, MessageService ms) {
	    	this.ip = ip;
	    	this.port = port;
	    	this.service = ms;
	    }
	    
	    public void run(){
	        try {
		    	isConnecting = true;
				setupConnection();
				isConnecting = false;
				Log.i(LOGTAG, "Success to connect to web server");
            	String message = null;
	        	if(this.service != null){
    				this.service.onMessage(new BaseResultMessage("systemservice", "connection", "success", "连接服务器成功"));
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
    				this.service.onMessage(new BaseResultMessage("systemservice", "connection", "fail", "无法连接到服务器"));
    			}
	        	Log.e(LOGTAG, "Fail to connect to web server");
	            e.printStackTrace();
	        } catch (IOException e) {
	        	if(this.service != null){
    				this.service.onMessage(new BaseResultMessage("systemservice", "connection", "fail", "无法连接到服务器或连接断开"));
    			}
	        	Log.e(LOGTAG, "Fail to connect to web server");
				e.printStackTrace();
			} finally {
	        	try {
	        		 if(in != null){
					    in.close();
					    in = null;
	        		 }
	        		 if(out != null){
					    out.close();
					    out = null;
	        		 } 
					 if (socket != null && !socket.isClosed()){
						 socket.close();
						 socket = null;
					 }
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	isConnecting = false;
	        }
	    }

	    public void shutdown(){
				try {
	        		 if(in != null){
					    in.close();
					    in = null;
	        		 }
	        		 if(out != null){
					    out.close();
					    out = null;
	        		 } 
					 if (socket != null && !socket.isClosed()){
						 try {
							socket.close();
							socket = null;
							Log.i(LOGTAG, "Success to shutdown the connection to server");
						} catch (IOException e) {
							e.printStackTrace();
						}
					 }
					 this.interrupt();
				} catch (IOException e) {
					 e.printStackTrace();
				}
	    }

	    public synchronized boolean sendMessage(String msg) {
	        try {
	        	out.println(msg);
	        	out.flush();
	        	return true;
	        } catch (Exception e) {
	            Log.e(LOGTAG, "Fail to send message:" + msg);
	            return false;
	        }
	    }
	    
	    public boolean isReady(){
	    	return this.socket != null && this.socket.isConnected();
	    }
	    
	    public boolean isConnecting(){
	    	return this.isConnecting;
	    }
  	    
	    private void setupConnection() throws UnknownHostException, IOException {
	        socket = new Socket(ip, port);
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream());
	    }

}
