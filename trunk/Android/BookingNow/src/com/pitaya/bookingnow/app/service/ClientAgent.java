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
            	String message = null;
            	while((message = in.readLine()) != null){
            		if(message.equals("connected")){
            			Log.d(TAG, "Connected to server");
            			this._service.onConnectServer();
            			break;
            		} else if(message.equals("ready")){
        	        	if(this.msg != null && !this.msg.equals("")){
        	        		this.sendMessage(this.msg);
        	        	} else {
        	        		/*
        	        		 * If the disconnection is not long enough, server maybe return ready because
        	        		 * the client has not been removed, but we know here it is to connect to server
        	        		 * due to message is blank string, so just assume the connection is available
        	        		 */
        	        		this._service.onConnectServer();
        	        	}
            		} else {
            			this._service.onMessage(message, this);
            		}
            	}
	        } catch (IOException e) {
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
	    
	    private void setupConnection(){
	        try {
				socket = new Socket(this.addr, this.port);
		        in =  new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")); 
			} catch (UnknownHostException e) {
				e.printStackTrace();
	        	Log.e(TAG, "Fail to find server");
	        	this.shutdown();
	        	this._service.onDisconnect();
			} catch (IOException e) {
				e.printStackTrace();
	        	Log.e(TAG, "Fail to connect to server");
	        	this.shutdown();
	        	this._service.onDisconnect();
			}
 
	    }
}
