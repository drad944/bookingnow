package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pitaya.bookingnow.message.LoginMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.util.Constants;

class ClientAgent extends Thread{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	MessageService service;
	PrintWriter out;
	BufferedReader in;
	String username;
	Integer role;
	
	ClientAgent(MessageService service, Socket socket){
		this.client_socket = socket;
		this.service = service;
	}
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));   
			out = new PrintWriter(client_socket.getOutputStream());   
			String message = null;
			while((message = in.readLine()) != null){
				this.service.onMessage(message, this);
				logger.info("["+this.username+"]" + message);
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
				 this.service.removeChild(this);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		 }
	}
	
	synchronized boolean sendMessage(String message){
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
			 if(client_socket != null && !client_socket.isClosed()){
				 client_socket.close();
			 }
			 this.service.removeChild(this);
			 logger.info("Success to close connection to " + this.username);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class MessageServerThread extends Thread{
	private static Log logger =  LogFactory.getLog(MessageServerThread.class);
	
    boolean flag = true;
    MessageService service;
    private final ExecutorService pool =  Executors.newFixedThreadPool(50);
      
    MessageServerThread(MessageService service){
    	this.service = service;
    }  
      
    public void run(){
    	Socket client_socket = null;
        try {
        	while(flag){
                client_socket = service.serverSocket.accept( );  
                pool.execute(new ClientAgent(this.service, client_socket));
        	}
        } catch(Exception e) {
        	logger.error("Message server thread is crashed");
            e.printStackTrace( );
            if(client_socket != null && !client_socket.isClosed()){
        		try {
					client_socket.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
        	}
        }
    }
    
    void shutdown(){
    	this.flag = false;
    	pool.shutdown();
    	try { 
    	    if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
    	    	 pool.shutdownNow();
    	    	 if (!pool.awaitTermination(60, TimeUnit.SECONDS)){
    	    		 logger.error("Pool did not terminate");  
    	    	 }
    	    }
    	 } catch (InterruptedException ie) {
    	    pool.shutdownNow();
    	 }
    	 this.interrupt();
    }
}

public class MessageService {
	
	private static MessageService _instance;
	private static Log logger =  LogFactory.getLog(MessageService.class);
	
	ServerSocket serverSocket;
	
	private MessageServerThread serverThread;
	private int port;
	private boolean hasStarted;
	private Map<Integer, Map<String, ClientAgent>> groups;
	
	private MessageService(int port){
		this.hasStarted = false;
		this.port = port;
		this.groups = new ConcurrentHashMap <Integer, Map<String, ClientAgent>>();
		this.start();
	}
	
	public static MessageService initService(int port){
		if(_instance == null){
			_instance = new MessageService(port);
		}
		return _instance;
	}
	
	public static MessageService getService(){
		return _instance;
	}
	
	public static boolean shutdownService(){
		if(_instance.hasStarted) {
			_instance.serverThread.shutdown();
			try {
        			if(_instance.serverSocket != null && _instance.serverSocket.isClosed()){
        				_instance.serverSocket.close();
        			}
        			_instance.hasStarted = false;
					for(Entry<Integer, Map<String, ClientAgent>> entry : _instance.groups.entrySet()){
						for(Entry<String, ClientAgent> subentry : ((Map<String, ClientAgent>)entry.getValue()).entrySet()){
							subentry.getValue().shutdown();
						}
					}
					_instance = null;
					logger.info("Success to shutdown message service.");
					return true;
			} catch (IOException e) {
					e.printStackTrace();
					_instance = null;
					return false;
			}
		}
		return true;
	}
	
	private boolean start(){
		if(!hasStarted){
			try {
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
	
	public boolean sendMessageToGroup(int groupType, Message message){
		logger.info("Send message to group");
		Map<String, ClientAgent> group = this.groups.get(groupType);
		String msgstring = parseMessage(message);
		if(group != null){
			boolean success = true;
			for(Entry<String, ClientAgent> entry : group.entrySet()){
				if(!entry.getValue().sendMessage(msgstring)){
					logger.error("Fail to send message to" + entry.getKey() + ":" + msgstring);
					success = false;
				}
			}
			return success;
		}
		return false;
	}
	
	synchronized void onMessage(String msgstring, ClientAgent clientAgent){
		Message message = unparseMessage(msgstring);
		if(message instanceof LoginMessage){
			String username = ((LoginMessage)message).getUsername();
			String password = ((LoginMessage)message).getPassword();
			String key = ((LoginMessage)message).getKey();
			ResultMessage resultmsg = null;
			//Get password from database service
			if(password.equals("123456")){
				clientAgent.username = username;
				//Get role from database service
				clientAgent.role = Constants.ROLE_WAITER;
				if(this.addClient(clientAgent)){
					resultmsg = new ResultMessage(key, Constants.LOGIN_REQUEST, 
							Constants.SUCCESS, String.valueOf(Constants.ROLE_WAITER));
				} else {
					resultmsg = new ResultMessage(key, Constants.LOGIN_REQUEST, 
							Constants.FAIL, "Can't login two clients with same user!");
				}
			} else {
				resultmsg = new ResultMessage(key, Constants.LOGIN_REQUEST, 
						Constants.FAIL, "Wrong username or password!");
			}
			clientAgent.sendMessage(parseMessage(resultmsg));
		}
	}
	
	boolean addClient(ClientAgent clientAgent){
		Map<String, ClientAgent> group = this.groups.get(clientAgent.role);
		if(group == null){
			group = new ConcurrentHashMap<String, ClientAgent>();
			this.groups.put(clientAgent.role, group);
		}
		if(group.get(clientAgent.username) == null){
			group.put(clientAgent.username, clientAgent);
			logger.info("Add client into server thread pool: [" + clientAgent.username + ":" + clientAgent.role + "]");
			return true;
		}else{
			return false;
		}
	}
	
	void removeChild(ClientAgent clientAgent){
		
		if(clientAgent.username == null && clientAgent.role == null){
			logger.info("Remove a unautherized clientAgent");
			return;
		}
		if(this.groups.get(clientAgent.role) != null && this.groups.get(clientAgent.role).get(clientAgent.username) != null){
			this.groups.get(clientAgent.role).remove(clientAgent.username);
			logger.info("Remove client from server thread pool: [" + clientAgent.username + ":" + clientAgent.role + "]");
			clientAgent = null;
		}
	}
	
	
	public static String parseMessage(Message message){
		JSONObject jsonMsg = JSONObject.fromObject(message);
		return jsonMsg.toString();
	}
	
	public static Message unparseMessage(String message){
		Message msg = null;
		String type = null;
		try {
			JSONObject jsonMsg = JSONObject.fromObject(message);
			type = jsonMsg.getString("type");
			if(type != null){
				Class<?> msgcls = Class.forName(type);
				msg = (Message) JSONObject.toBean(jsonMsg, msgcls);
			} else {
				logger.error("Message type is null: " + message);
			}
		} catch (JSONException e) {
			logger.error("Fail to parse message to json object: " + message);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("Unsupported message type: " + type);
			e.printStackTrace();
		}
		return msg;
	}
	
    public static void main(String [] args){
    	MessageService messageService = MessageService.initService(19191);
	    	for(int i=0 ;i<500; i++){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			messageService.sendMessageToGroup(Constants.ROLE_WAITER, new Message("test"));
    	}
    }
	
}
