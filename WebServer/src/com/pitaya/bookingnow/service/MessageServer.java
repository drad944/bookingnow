package com.pitaya.bookingnow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ClientAgent extends Thread{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	MessageServer server;
	PrintWriter out;
	BufferedReader in;
	String username;
	String role;
	
	ClientAgent(MessageServer server, Socket socket){
		this.client_socket = socket;
		this.server = server;
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));   
			out = new PrintWriter(client_socket.getOutputStream());   
			String message = null;
			while((message = in.readLine()) != null){
				if(message.startsWith("login:")){
					String[] logininfos = message.substring("login:".length()).split(":");
					if(logininfos.length == 3 && logininfos[2].equals("123456")){
						this.username = logininfos[0];
						this.role = logininfos[1];
						if(this.server.addClient(this)){
							this.sendMessage("Login successfully!");
							logger.info("Success to connect to "+ this.username);
						} else {
							this.sendMessage("Can't login two clients with same user!");
						}
					} else {
						this.sendMessage("Fail to login!");
					}
				} else {
					if(this.username == null){
						this.sendMessage("Login first!");
					} else {
						logger.info("["+this.username+"]" + message);
					}
				}
			}
		 }catch (Exception e){
	        e.printStackTrace();
		 }finally{
	        try {
				 in.close();
				 out.close();
				 if(client_socket != null){
					 client_socket.close();
				 }
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		 }
	}
	
	boolean sendMessage(String message){
		try {
			out.println(message);
			out.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	void shutdown(){
		 try {
			 this.sendMessage("bye");
			 in.close();
			 out.close();
			 if(client_socket != null){
				 client_socket.close();
			 }
			 logger.info("Success to shutdown connect to " + this.username);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class MessageServerThread extends Thread{
	private static Log logger =  LogFactory.getLog(MessageServerThread.class);
    boolean flag = true;
    MessageServer server;
      
    MessageServerThread(MessageServer server){
    	this.server = server;
    }  
      
    public void run(){
    	Socket client_socket = null;
        try {
        	while(flag){
                client_socket = server.serverSocket.accept( );  
                ClientAgent clientAgent = new ClientAgent(this.server, client_socket);  
                clientAgent.start( );
        	}
        	logger.info("Success to shutdown message server thread");
        } catch( Exception e ) {  
            e.printStackTrace( );
        } finally{
        	if(client_socket != null){
        		try {
					client_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    
    void shutdown(){
    	this.flag = false;
    }
}

public class MessageServer {
	
	private static MessageServer _instance;
	private static Log logger =  LogFactory.getLog(MessageServer.class);
	
	ServerSocket serverSocket;
	
	private MessageServerThread serverThread;
	private int port;
	private boolean hasStarted;
	private Map<String, Map<String, ClientAgent>> groups;
	
	private MessageServer(){
		this.hasStarted = false;
		groups = new ConcurrentHashMap <String, Map<String, ClientAgent>>();
	}
	
	public static MessageServer getInstance(){
		if(_instance == null){
			_instance = new MessageServer();
		}
		return _instance;
	}
			
	public boolean start(int port){
		if(!hasStarted){
			try {
				this.port = port;
	            serverSocket = new ServerSocket(this.port);
	            serverThread = new MessageServerThread(this);  
	            serverThread.start();
	            logger.info("Success to start message server thread");
	            this.hasStarted = true;
	            return true;
	        } catch(IOException e) {
	        	e.printStackTrace();
	        	if(serverSocket != null){
	        		try {
						serverSocket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
	        	}
	            return false;
	        }
		}
		return true;
	}
	
	public boolean shutdown(){
		if(hasStarted) {
			this.serverThread.shutdown();
        	if(serverSocket != null){
        		try {
					serverSocket.close();
					this.hasStarted = false;
					for(Entry<String, Map<String, ClientAgent>> entry : groups.entrySet()){
						for(Entry<String, ClientAgent> subentry : ((Map<String, ClientAgent>)entry.getValue()).entrySet()){
							subentry.getValue().shutdown();
						}
					}
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
        	}
		}
		return true;
	}
	
	public boolean sendMessageToGroup(String groupType, String message){
		Map<String, ClientAgent> group = this.groups.get(groupType);
		if(group != null){
			boolean success = true;
			for(Entry<String, ClientAgent> entry : group.entrySet()){
				if(!entry.getValue().sendMessage(message)){
					logger.error("Fail to send message to" + entry.getKey() + ":" + message);
					success = false;
				}
			}
			return success;
		}
		return false;
	}
	
	boolean addClient(ClientAgent clientAgent){
		Map<String, ClientAgent> group = this.groups.get(clientAgent.role);
		if(group == null){
			group = new ConcurrentHashMap<String, ClientAgent>();
		}
		if(group.get(clientAgent.username) == null){
			group.put(clientAgent.username, clientAgent);
			return true;
		}else{
			return false;
		}
	}
	
}
