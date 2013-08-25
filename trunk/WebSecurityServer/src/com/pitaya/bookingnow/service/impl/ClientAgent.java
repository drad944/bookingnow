package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientAgent extends Thread{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	MessageService service;
	OutputStreamWriter out;
	BufferedWriter bwriter;
	BufferedReader in;
	Long userId;
	Integer role;
	List<String> messages;
	boolean isSending;
	
	ClientAgent(MessageService service, Socket socket){
		this.client_socket = socket;
		this.service = service;
		this.isSending = false;
		this.messages = Collections.synchronizedList(new ArrayList<String>());
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public Integer getRoleType(){
		return this.role;
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(client_socket.getOutputStream(), "UTF-8");  
			bwriter = new BufferedWriter(out);  
			String message = null;
			while((message = in.readLine()) != null){
				logger.debug("Receive message from client: ["+this.userId+"], content:" + message);
				this.service.onMessage(message, this);
			}
		 } catch (Exception e) {
	        e.printStackTrace();
		 } finally {
	         try {
				 in.close();
				 out.close();
				 if(client_socket != null && !client_socket.isClosed()){
					 client_socket.close();
				 }
			 } catch (IOException ex) {
				 ex.printStackTrace();
			 }
	         client_socket = null;
	         logger.debug("Connection to client [" + this.userId +"] is broken");
	         this.service.removeClient(this, true);
		 }
	}
	
	void sendHeartBeat(){
		try {
			this.client_socket.sendUrgentData(0xff);
		} catch (IOException e) {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				bwriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(client_socket != null && !client_socket.isClosed()){
				 try {
					client_socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			 }
			client_socket = null;
			logger.debug("HeartBeat: Connection to client [" + this.userId +"] is broken");
			this.service.removeClient(this, true);
		}
	}
	
	void sendMessage(String message){
		this.messages.add(message);
		synchronized(this){
			if(this.isSending == true){
				logger.debug("client is in sending, add message to queue");
				return;
			} else {
				this.isSending = true;
			}
		}
		while(this.messages.size() > 0){
			String msg = this.messages.get(0);
			try {
				bwriter.write(msg + "\r\n");
				bwriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Fail to send message to client [" + this.userId + "]");
			}
			this.messages.remove(0);
		}

		/*		Notice!!! It's unsafe to add any codes without sync here. Because during the 
		 * 		logic, it's possible that a new thread is asking send message, so it won't be 
		 * 		sent until next time because isSending still true.
		 */
		synchronized(this){
			this.isSending = false;
		}
	}
	
	void shutdown(){
		this.sendMessage("bye");
		try {
			 in.close();
		} catch (IOException e) {
			 e.printStackTrace();
		}
		try {
			 bwriter.close();
		} catch (IOException e) {
			 e.printStackTrace();
		}
		try{
			 if(client_socket != null && !client_socket.isClosed()){
				 client_socket.close();
			 }
		} catch (IOException e) {
			 e.printStackTrace();
		}
		logger.debug("Success to shutdown connection to client [" + this.userId + "]");
		this.service.removeClient(this, true);
	}
}
