package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClientThread extends Thread{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	EnhancedMessageService _service;
	OutputStreamWriter out;
	BufferedWriter bwriter;
	BufferedReader in;
	List<String> messages;
	private boolean isSending = false;
	
	public ClientThread(EnhancedMessageService service, Socket socket){
		this.client_socket = socket;
		this._service = service;
		this.messages = Collections.synchronizedList(new ArrayList<String>());
	}

	InetAddress getInetAddress(){
		if(this.client_socket != null){
			return this.client_socket.getInetAddress();
		} else {
			return null;
		}
	}
	
	int getPort(){
		if(this.client_socket != null){
			return this.client_socket.getPort();
		} else {
			return -1;
		}
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(client_socket.getOutputStream(), "UTF-8");  
			bwriter = new BufferedWriter(out);  
			String message = null;
			while((message = in.readLine()) != null){
				logger.debug("Receive message:" + message);
				this._service.onMessage(message, this);
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
	         logger.debug("Finished");
		 }
	}

	void sendMessage(String message){
		this.messages.add(message);
		synchronized(this){
			if(this.isSending == true){
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
				this.messages.remove(0);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Fail to send message.");
				break;
			}
		}

		/*		Notice!!! It's unsafe to add any codes without sync here. Because during the 
		 * 		logic, it's possible that a new thread is asking send message, so it won't be 
		 * 		sent until next time because isSending still true.
		 */
		synchronized(this){
			this.isSending = false;
		}
	}
	
	void shutdown(String message){
		if(message != null){
			this.sendMessage(message);
		}
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
	}
}

