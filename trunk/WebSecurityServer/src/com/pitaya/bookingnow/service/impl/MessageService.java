package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.entity.security.User_Role_Detail;
import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.RegisterMessage;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.service.impl.security.UserService;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.Constants;

class ClientAgent extends Thread{
	
	private static Log logger =  LogFactory.getLog(ClientAgent.class);
	Socket client_socket;
	MessageService service;
	PrintWriter out;
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
	
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));   
			out = new PrintWriter(client_socket.getOutputStream());   
			String message = null;
			while((message = in.readLine()) != null){
				logger.info("Receive message from client: ["+this.userId+"], content:" + message);
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
				 this.service.removeClient(this);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
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
		try {
			while(this.messages.size() > 0){
				String msg = this.messages.get(0);
				out.println(msg);
				out.flush();
				this.messages.remove(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			 this.sendMessage("bye");
			 in.close();
			 out.close();
			 if(client_socket != null && !client_socket.isClosed()){
				 client_socket.close();
			 }
			 this.service.removeClient(this);
			 logger.info("Success to close connection to " + this.userId);
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
	private Map<Integer, Map<Long, ClientAgent>> groups;
	
	private IUserService userService;
	
	public MessageService(){
		this.hasStarted = false;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, ClientAgent>>();
	}
	
	private MessageService(int port){
		this.hasStarted = false;
		this.port = port;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, ClientAgent>>();
		this.start();
	}

	public void start(int port){
		this.port = port;
		this.start();
	}
	
	public boolean shutdown(){
		if(this.hasStarted) {
			try {
        			if(this.serverSocket != null && this.serverSocket.isClosed()){
        				this.serverSocket.close();
        			}
        			this.hasStarted = false;
					for(Entry<Integer, Map<Long, ClientAgent>> entry : this.groups.entrySet()){
						for(Entry<Long, ClientAgent> subentry : ((Map<Long, ClientAgent>)entry.getValue()).entrySet()){
							subentry.getValue().shutdown();
						}
					}
					this.serverThread.shutdown();
					logger.info("Success to shutdown message service.");
					return true;
			} catch (IOException e) {
					e.printStackTrace();
					return false;
			}
		}
		return true;
	}
	
	public void setUserService(IUserService us){
		this.userService = us;
	}
	
	public IUserService getUserService(){
		return this.userService;
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
	
	public boolean updateAllClientsMenuData(){
		Message message = new FoodMessage();
		boolean success = true;
		for(Entry<Integer, Map<Long, ClientAgent>> entry : this.groups.entrySet()){
			for(Entry<Long, ClientAgent> subentry : entry.getValue().entrySet()){
				subentry.getValue().sendMessage(parseMessage(message));
			}
		}
		return success;
	}
	
	public boolean sendMessageToGroup(int groupType, Message message){
		String msgstring = parseMessage(message);
		Map<Long, ClientAgent> group = this.groups.get(groupType);
		if(group != null){
			for(Entry<Long, ClientAgent> entry : group.entrySet()){
				entry.getValue().sendMessage(msgstring);
			}
			logger.info("Send message to group: " + groupType);
			return true;
		}
		return false;
	}
	
	synchronized void onMessage(String msgstring, ClientAgent clientAgent){
		Message message = unparseMessage(msgstring);
		if(message instanceof RegisterMessage){
			Long id = ((RegisterMessage)message).getUserId();
			ResultMessage resultmsg = null;
			User user  = userService.getUserRole(id);
			if(user != null && user.getRole_Details() != null && user.getRole_Details().size() > 0){
				clientAgent.userId = id;
				clientAgent.role = user.getRole_Details().get(0).getRole().getType();
				if(this.addClient(clientAgent)){
					resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
							Constants.SUCCESS, String.valueOf(clientAgent.role));
				} else {
					resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
							Constants.FAIL, "Can't login two clients with same user!");
				}
				clientAgent.sendMessage(parseMessage(resultmsg));
			}
		}
	}
	
	boolean addClient(ClientAgent clientAgent){
		Map<Long, ClientAgent> group = this.groups.get(clientAgent.role);
		if(group == null){
			group = new ConcurrentHashMap<Long, ClientAgent>();
			this.groups.put(clientAgent.role, group);
		}
		if(group.get(clientAgent.userId) == null){
			group.put(clientAgent.userId, clientAgent);
			logger.info("Add client into server thread pool [user id: " + clientAgent.userId + "; role type: " + clientAgent.role + "]");
			return true;
		}else{
			logger.info("Client already registered [user id: " + clientAgent.userId + "]");
			return false;
		}
	}
	
	void removeClient(ClientAgent clientAgent){
		if(clientAgent.userId == null && clientAgent.role == null){
			logger.info("Remove a unautherized clientAgent");
			return;
		}
		if(this.groups.get(clientAgent.role) != null && this.groups.get(clientAgent.role).get(clientAgent.userId) != null){
			this.groups.get(clientAgent.role).remove(clientAgent.userId);
			logger.info("Remove client from server thread pool [user id: " + clientAgent.userId + "; role type: " + clientAgent.role + "]");
			clientAgent = null;
		}
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
					for(Entry<Integer, Map<Long, ClientAgent>> entry : _instance.groups.entrySet()){
						for(Entry<Long, ClientAgent> subentry : ((Map<Long, ClientAgent>)entry.getValue()).entrySet()){
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
				logger.error("Message type is missing: " + message);
			}
		} catch (JSONException e) {
			logger.error("Can't indentify message: " + message);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("Unsupported message type: " + type);
			e.printStackTrace();
		}
		return msg;
	}
	
    public static void main(String [] args){
    	MessageService messageService = MessageService.initService(19191);
		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		IUserService us = aCtx.getBean(IUserService.class);
		messageService.setUserService(us);
//    	for(int i=0 ;i<500; i++){
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			messageService.sendMessageToGroup(Constants.ROLE_WAITER, new Message("test"));
//    	}
    }
	
}
