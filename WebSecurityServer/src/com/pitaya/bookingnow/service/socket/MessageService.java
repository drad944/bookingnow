package com.pitaya.bookingnow.service.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

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

public class MessageService implements IMessageService{
	
	private static Log logger =  LogFactory.getLog(MessageService.class);
	
	public String securityResponse = "<cross-domain-policy>"
		+ "<site-control permitted-cross-domain-policies=\"master-only\"/>"
	    + "<allow-access-from domain=\"*\" to-ports=\"19191\"/>"
		+ "</cross-domain-policy>\0";
	
	protected ServerSocket serverSocket;
	protected MessageServerThread serverThread;
	protected int port;
	protected boolean hasStarted;
	protected IUserService userService;
	
	private Map<Integer, Map<Long, IClient>> groups;
	private List<IClient> clients;
	private Timer mChecker;
	private Class<? extends IClient> clientClz;
	
	public MessageService(){
		this.hasStarted = false;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, IClient>>();
		this.clients = Collections.synchronizedList(new ArrayList<IClient>());
	}
	
	//For flash socket client security check
	public void start(int port){
		this.port = port;
		this.clientClz = FlashClientAgent.class;
		this.securityResponse = this.securityResponse.replace("{port}", String.valueOf(this.port));
		this.start();
	}

	@Override
	public void start(int... args) {
		if(args.length > 0){
			this.port = args[0];
			this.clientClz = ClientAgent.class;
			this.start();
		}
	}
	
	public boolean shutdown(){
		if(this.hasStarted) {
			try {
    			if(this.serverSocket != null && this.serverSocket.isClosed()){
    				this.serverSocket.close();
    			}
    			for(IClient client : this.clients){
    				client.shutdown("bye");
    			}
				this.serverThread.shutdown();
				this.mChecker.cancel();
				this.groups = new ConcurrentHashMap <Integer, Map<Long, IClient>>();
				this.clients = Collections.synchronizedList(new ArrayList<IClient>());
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
	
	public int getPort(){
		return this.port;
	}
	
	public void setUserService(IUserService us){
		this.userService = us;
	}
	
	public IUserService getUserService(){
		return this.userService;
	}
	
	protected boolean start(){
		if(!hasStarted){
			try {
	            serverSocket = new ServerSocket(this.port);
	            serverThread = new MessageServerThread(this, this.clientClz);
	            serverThread.start();
	            logger.info("Success to start message server thread on " + this.port);
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
			for(IClient client : this.clients){
				client.sendMessage(parseMessage(message));
			}
			logger.debug("Send message to all clients.");
			return true;
		}
		return false;
	}
	
	public boolean sendMessageToGroup(int groupType, Message message){
		String msgstring = parseMessage(message);
		Map<Long, IClient> group = this.groups.get(groupType);
		boolean success = false;
		if(group != null){
			for(Entry<Long, IClient> entry : group.entrySet()){
				entry.getValue().sendMessage(msgstring);
				success = true;
			}
			logger.debug("Send message to group: " + groupType);
		}
		return success;
	}
	
	public boolean sendMessageToGroupExcept(int groupType, Long userid, Message message){
		String msgstring = parseMessage(message);
		Map<Long, IClient> group = this.groups.get(groupType);
		boolean success = false;
		if(group != null){
			for(Entry<Long, IClient> entry : group.entrySet()){
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
			for(Entry<Integer, Map<Long, IClient>> entry : this.groups.entrySet()){
				for(Entry<Long, IClient> subentry : entry.getValue().entrySet()){
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
	
	public List<IClient> getClients(){
		return this.clients;
	}
	
	public Map<Integer, Map<Long, IClient>> getClientGroups(){
		return this.groups;
	}
	
	protected synchronized void onMessage(String msgstring, IClient clientAgent){
		Message message = unparseMessage(msgstring);
		if(message == null){
			return;
		}
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
						clientAgent.setUserId(logonuser.getId());
						clientAgent.setUsername(logonuser.getName());
						clientAgent.setRoleType(role.getType());
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
				clientAgent.sendMessage(parseMessage(new ResultMessage(Constants.UNREGISTER_REQUEST, 
						Constants.SUCCESS, "done")));
			}
		}
	}
	
	protected boolean addClient(IClient clientAgent){
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
			logger.debug("Add new client:" + clientAgent.getAddress());
		}
		if(clientAgent.getUserId() != null && clientAgent.getRoleType() != null) {
			Long userId = clientAgent.getUserId();
			Integer role = clientAgent.getRoleType();
			Map<Long, IClient> group = this.groups.get(role);
			if(group == null){
				group = new ConcurrentHashMap<Long, IClient>();
				this.groups.put(role, group);
			}
			if(group.get(userId) != null){
				if(group.get(userId) == clientAgent){
					logger.warn("The same client already registered [user id: " + userId + "]");
					return true;
				} else {
					logger.debug("Client with same user id already registered [user id: " + userId + "], force it logout");
					group.get(userId).sendMessage("relogin");
				}
			}
			group.put(userId, clientAgent);
			logger.info("Register client [user id: " + userId + " and role type: " + role + "]");
			return true;
		} else {
			return false;
		}
	}
	
	protected void removeClient(IClient clientAgent, boolean isRemoved){
		//Remove it from registered clients queue first
		if(clientAgent.getUserId() != null && clientAgent.getRoleType() != null){
			Integer role = clientAgent.getRoleType();
			Long userId = clientAgent.getUserId();
			if(this.groups.get(role) != null && this.groups.get(role).get(userId) != null
					&& this.groups.get(role).get(userId) == clientAgent){
				this.groups.get(role).remove(userId);
				logger.info("Unregister client [user id: " + userId + " and role type: " + role + "]");
				clientAgent.setUserId(null);
				clientAgent.setRoleType(null);
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
	
	protected class MessageServerThread extends Thread{
		
	    boolean flag = true;
	    MessageService service;
	    protected final ExecutorService pool =  Executors.newFixedThreadPool(50);
	    private Class<? extends IClient> clientAgentClz;
	      
	    MessageServerThread(MessageService service, Class<? extends IClient> agentClz){
	    	this.service = service;
	    	this.clientAgentClz = agentClz;
	    }
	    
	      
	    MessageServerThread(MessageService service){
	    	this.service = service;
	    }
	    
	    @Override
	    public void run(){
	    	Socket client_socket = null;
	        try {
	        	while(flag){
	                client_socket = service.serverSocket.accept();
	                Constructor<? extends IClient> con = this.clientAgentClz.getConstructor(MessageService.class, Socket.class);
	                final IClient client = con.newInstance(this.service, client_socket);
	                pool.execute(client);
	                this.service.addClient(client);
	                new Timer().schedule(new TimerTask(){

						@Override
						public void run() {
							 client.sendMessage("ready");
						}
	                	
	                }, 3000);
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
	
	public static String parseMessage(Message message){
		JsonConfig jconfig = new JsonConfig();
		PropertyFilter filter = new PropertyFilter() {
            public boolean apply(Object object, String fieldName, Object fieldValue) {
            	return null == fieldValue;
            }
		};
		jconfig.setJsonPropertyFilter(filter);
		JSONObject jsonMsg = JSONObject.fromObject(message, jconfig);
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
		} catch (ClassNotFoundException e) {
			logger.error("Unsupported message type: " + type);
		}
		return msg;
	}
	
    public static void main(String [] args){
//		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
//		IUserService us = aCtx.getBean(IUserService.class);
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
