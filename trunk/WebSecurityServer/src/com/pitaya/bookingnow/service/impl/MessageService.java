package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
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

import com.pitaya.bookingnow.entity.security.Role;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.entity.security.User_Role_Detail;
import com.pitaya.bookingnow.message.FoodMessage;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.RegisterMessage;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.service.impl.security.UserService;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

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
                ClientAgent client = new ClientAgent(this.service, client_socket);
                pool.execute(client);
                this.service.addClient(client);
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
	private List<ClientAgent> clients;
	private Timer mChecker;
	
	private IUserService userService;
	
	public MessageService(){
		this.hasStarted = false;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, ClientAgent>>();
		this.clients = Collections.synchronizedList(new ArrayList<ClientAgent>());
	}
	
	private MessageService(int port){
		this.hasStarted = false;
		this.port = port;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, ClientAgent>>();
		this.clients = Collections.synchronizedList(new ArrayList<ClientAgent>());
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
    			for(ClientAgent client : this.clients){
    				client.shutdown();
    			}
				this.serverThread.shutdown();
				this.mChecker.cancel();
				this.groups = new ConcurrentHashMap <Integer, Map<Long, ClientAgent>>();
				this.clients = Collections.synchronizedList(new ArrayList<ClientAgent>());
				this.hasStarted = false;
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
	            mChecker = new Timer();
	            mChecker.schedule(new ConnectionCheckTask(this), 60000, 60000);
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
	
	public boolean sendMessageToAll(Message message){
		if(this.clients.size() > 0){
			for(ClientAgent client : this.clients){
				client.sendMessage(parseMessage(message));
			}
			logger.debug("Send message to all clients.");
			return true;
		}
		return false;
	}
	
	public boolean sendMessageToGroup(int groupType, Message message){
		String msgstring = parseMessage(message);
		Map<Long, ClientAgent> group = this.groups.get(groupType);
		boolean success = false;
		if(group != null){
			for(Entry<Long, ClientAgent> entry : group.entrySet()){
				entry.getValue().sendMessage(msgstring);
				success = true;
			}
			logger.debug("Send message to group: " + groupType);
		}
		return success;
	}
	
	public boolean sendMessageToGroupExcept(int groupType, Long userid, Message message){
		String msgstring = parseMessage(message);
		Map<Long, ClientAgent> group = this.groups.get(groupType);
		boolean success = false;
		if(group != null){
			for(Entry<Long, ClientAgent> entry : group.entrySet()){
				if(!entry.getKey().equals(userid)){
					entry.getValue().sendMessage(msgstring);
					success = true;
				}
			}
			logger.debug("Send message to group: " + groupType + " except this one: " + userid);
		}
		return success;
	}
	
	public boolean sendMessageToOne(Long userid, Message message){
		if(userid != null){
			for(Entry<Integer, Map<Long, ClientAgent>> entry : this.groups.entrySet()){
				for(Entry<Long, ClientAgent> subentry : entry.getValue().entrySet()){
					if(subentry.getKey().equals(userid)){
						subentry.getValue().sendMessage(parseMessage(message));
						logger.debug("Send message to user: " + userid);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public List<ClientAgent> getClients(){
		return this.clients;
	}
	
	public Map<Integer, Map<Long, ClientAgent>> getClientGroups(){
		return this.groups;
	}
	
	synchronized void onMessage(String msgstring, ClientAgent clientAgent){
		Message message = unparseMessage(msgstring);
		if(message instanceof RegisterMessage){
			RegisterMessage msg = (RegisterMessage)message;
			if(msg.getAction().equals("register")){
				String username = msg.getUsername();
				String password = msg.getPassword();
				ResultMessage resultmsg = null;
				if(username != null && password != null){
					User loginuser = new User();
					loginuser.setAccount(username);
					loginuser.setPassword(password);
					MyResult loginresult = userService.login(loginuser);
					if(loginresult.isExecuteResult()){
						User logonuser = loginresult.getUser();
						Role role = logonuser.getRole_Details().get(0).getRole();
						clientAgent.userId = logonuser.getId();
						clientAgent.role = role.getType();
						if(this.addClient(clientAgent)){
							String detail = String.valueOf(logonuser.getId()) + "###";
							detail += logonuser.getName() + "###";
							detail += String.valueOf(role.getType());
							resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
									Constants.SUCCESS, detail);
						} else {
							resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
									Constants.SUCCESS, "无效用户");
						}
					} else {
						resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
								Constants.FAIL, loginresult.getErrorDetails().get("Error"));
					}
				} else {
					resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
							Constants.FAIL, "请输入用户名和密码");
				}
				clientAgent.sendMessage(parseMessage(resultmsg));
			} else {
				this.removeClient(clientAgent, false);
			}
		}
	}
	
	boolean addClient(ClientAgent clientAgent){
		boolean hasAdded = false;
		for(int i=0; i < this.clients.size(); i++){
			if(this.clients.get(i) == clientAgent){
				logger.debug("Client already added");
				hasAdded = true;
				break;
			}
		}
		if(!hasAdded){
			this.clients.add(clientAgent);
			logger.info("Add new client");
		}
		if(clientAgent.userId != null && clientAgent.role != null) {
			Map<Long, ClientAgent> group = this.groups.get(clientAgent.role);
			if(group == null){
				group = new ConcurrentHashMap<Long, ClientAgent>();
				this.groups.put(clientAgent.role, group);
			}
			if(group.get(clientAgent.userId) != null){
				if(group.get(clientAgent.userId) == clientAgent){
					logger.warn("The same client already registered [user id: " + clientAgent.userId + "]");
					return true;
				} else {
					logger.debug("Client with same user id already registered [user id: " + clientAgent.userId + "], shutdown it first");
					group.get(clientAgent.userId).shutdown();
				}
			}
			group.put(clientAgent.userId, clientAgent);
			logger.info("Register client [user id: " + clientAgent.userId + " and role type: " + clientAgent.role + "]");
			return true;
		} else {
			return false;
		}
	}
	
	void removeClient(ClientAgent clientAgent, boolean isRemoved){
		//Remove it from registered clients queue first
		if(clientAgent.userId != null && clientAgent.role != null){
			if(this.groups.get(clientAgent.role) != null && this.groups.get(clientAgent.role).get(clientAgent.userId) != null
					&& this.groups.get(clientAgent.role).get(clientAgent.userId) == clientAgent){
				this.groups.get(clientAgent.role).remove(clientAgent.userId);
				logger.info("Unregister client [user id: " + clientAgent.userId + " and role type: " + clientAgent.role + "]");
				clientAgent.userId = null;
				clientAgent.role = null;
			}
		}
		if(isRemoved){
			//Remove it from all clients queue
			for(int i=0; i < this.clients.size(); i++){
				if(this.clients.get(i) == clientAgent){
					this.clients.remove(i);
					logger.info("Remove client");
					break;
				}
			}
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
