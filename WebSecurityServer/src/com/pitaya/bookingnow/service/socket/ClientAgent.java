package com.pitaya.bookingnow.service.socket;

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

public class ClientAgent implements IClient{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	MessageService service;
	OutputStreamWriter out;
	BufferedWriter bwriter;
	BufferedReader in;
	List<String> messages;
	boolean isSending;
	
	private Long userId;
	private String username;
	private Integer roleType;
	
	public ClientAgent(MessageService service, Socket socket){
		this.client_socket = socket;
		this.service = service;
		this.isSending = false;
		this.messages = Collections.synchronizedList(new ArrayList<String>());
	}
	
	@Override
	public Long getUserId(){
		return this.userId;
	}
	
	@Override
	public Integer getRoleType(){
		return this.roleType;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public void setUsername(String uname) {
		this.username = uname;
	}

	@Override
	public void setRoleType(Integer role) {
		this.roleType = role;
	}

	@Override
	public void setUserId(Long id) {
		this.userId = id;
	}
	
	@Override
	public InetAddress getInetAddress() {
		if(this.client_socket != null){
			if(this.client_socket.getInetAddress() != null){
				return this.client_socket.getInetAddress();
			}
		}
		return null;
	}
	
	@Override
	public String getAddress(){
		if(this.client_socket != null){
			if(this.client_socket.getInetAddress() != null){
				return this.client_socket.getInetAddress().getHostAddress();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}
	
	@Override
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(client_socket.getOutputStream(), "UTF-8");  
			bwriter = new BufferedWriter(out);  
			String message = null;
			while((message = in.readLine()) != null){
				logger.debug(this.service.getPort() + ": Receive message from client: ["+this.userId+"], content:" + message);
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
	
	private void sendHeartBeat(){
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
	
	@Override
	public void sendMessage(String message){
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
				logger.debug("Send message to client [" + this.userId + "]: " + msg);
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
	
	@Override
	public void shutdown(String message){
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
		logger.debug("Success to shutdown connection to client [" + this.userId + "]");
		this.service.removeClient(this, true);
	}

	@Override
	public List<String> getMessages() {
		return this.messages;
	}

	@Override
	public void addMessages(List<String> messages) {
		this.messages.addAll(messages);
	}

	@Override
	public void checkConnection() {
		this.sendHeartBeat();
	}
}
