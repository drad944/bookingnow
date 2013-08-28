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

public class ClientAgent extends Thread{
	  	
		private static String TAG = "ClientAgentThread";
	    private BufferedReader in;
	    private BufferedWriter bwriter;
	    private String msg;
	    private String addr;
	    private int port;
	    private Socket socket;
	    private EnhancedMessageService _service;

	    public ClientAgent(String addr, int port, String message, EnhancedMessageService ms) {
	    	this._service = ms;
	    	this.addr = addr;
	    	this.port = port;
	    	this.msg = message;
	    }
	    
	    public void run(){
	        try {
	        	this.setupConnection();
	        	if(this.msg != null && this.msg.equals("")){
	        		this.sendMessage(this.msg);
	        	}
            	String message = null;
            	while((message = in.readLine()) != null){
            		if(message.equals("connected")){
            			Log.d(TAG, "Connected to server");
            			this._service.onConnectServer();
            			break;
            		} else {
            			this._service.onMessage(message, this);
            		}
            	}
	        } catch (UnknownHostException e) {
	        	Log.e(TAG, "Fail to find server");
	        	this._service.onDisconnect();
	            e.printStackTrace();
	        } catch (IOException e) {
	        	Log.e(TAG, "Fail to connect to server");
	        	this._service.onDisconnect();
				e.printStackTrace();
			} finally {
				this.shutdown();
	        }
	    }

	    public void shutdown(){
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
			 if (socket != null && !socket.isClosed()){
				try {
					socket.close();
					Log.d(TAG, "Success to shutdown the connection to server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
			 socket = null;
	    }

	    public boolean isReady(){
	    	return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
	    }
	    
	    public boolean sendMessage(String msg) {
	    	try {
	        	this.bwriter.write(msg + "\r\n");
	        	this.bwriter.flush();
	        	return true;
	        } catch (IOException e) {
	            Log.e(TAG, "Fail to send message to server");
	            this._service.onSendMsgFail();
	            return false;
	        }
	    }
	    
	    private void setupConnection() throws UnknownHostException, IOException {
	        socket = new Socket(this.addr, this.port);
			bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));  
	    }
}
