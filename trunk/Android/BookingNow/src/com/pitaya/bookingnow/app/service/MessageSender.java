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

public class MessageSender extends Thread{
	  	
		private static String TAG = "MessageSender";
	    private BufferedReader in;
	    private BufferedWriter bwriter;
	    private String msg;
	    private String addr;
	    private int port;
	    private Socket socket;
	    private EnhancedMessageService _service;
	    
	    /*
	     * addr: server address, port: server tcp port
	     */
	    public MessageSender(String addr, int port, String message, EnhancedMessageService ms) {
	    	this._service = ms;
	    	this.addr = addr;
	    	this.port = port;
	    	this.msg = message;
	    }
	    
	    @Override
	    public void run(){
	    	this.setupConnection();
	    	if(!this.isReady()){
	        	this.shutdown();
	        	this._service.onDisconnect("无法连接至服务器，请检查网络");
	    		return;
	    	}
	    	try {
            	String message = null;
            	while((message = in.readLine()) != null){
            		if(message.equals("ready")){
    	        		/*
    	        		 * In a short time after disconnect, server maybe return ready because the client has not 
    	        		 * been removed, but the message is blank string means here is to re-connect to server
    	        		 * , so it's safe to assume that the connection is established
    	        		 */
            			if(this.msg != null && !this.msg.equals("")){
            				this.sendMessage(this.msg);
            			} else {
            				this._service.onConnectServer();
            				Log.d(TAG, "Connected to server");
                			break;
            			}
            		} else {
            			//Handler server reply message
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
					Log.d(TAG, "Close connection to server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
			 socket = null;
			 this._service = null;
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
			} catch (IOException e) {
				e.printStackTrace();
	        	Log.e(TAG, "Fail to connect to server");
			}
	    }
}
