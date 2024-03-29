package com.pitaya.bookingnow.service.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

import com.pitaya.bookingnow.entity.security.Role;
import com.pitaya.bookingnow.entity.security.User;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.RegisterMessage;
import com.pitaya.bookingnow.message.ResultMessage;
import com.pitaya.bookingnow.service.security.IUserService;
import com.pitaya.bookingnow.util.Constants;
import com.pitaya.bookingnow.util.MyResult;

public class EnhancedMessageService implements IMessageService{
	
	private static Log logger =  LogFactory.getLog(EnhancedMessageService.class);
	//UDP check packet timeout value
	private static final int TIMEOUT = 60000;
	private Map<Integer, Map<Long, IClient>> groups;
	private List<IClient> clients;
	final ExecutorService senderPool =  Executors.newFixedThreadPool(50);
	
	DatagramSocket udpSocket;
	int udpport;
	
	protected ServerSocket serverSocket;
	private MessageServerThread serverThread;
	private boolean hasStarted;
	private int port;
	private int clientport;
	private boolean udpCheck;
	
	private IUserService userService;
	
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
		} catch (ClassNotFoundException e) {
			logger.error("Unsupported message type: " + type);
		}
		return msg;
	}
	
	public EnhancedMessageService(){
	}
	
	public void setUserService(IUserService us){
		this.userService = us;
	}
	
	public IUserService getUserService(){
		return this.userService;
	}
	

	@Override
	public void start(int... args) {
		if(args.length == 3){
			this.doStart(args[0], args[1], args[2]);
		} else {
			logger.error("Fail to start message service with args: " + args);
		}
	}
	
	private void doStart(int port, int clientport, int udpport){
		this.port = port;
		this.udpport = udpport;
		this.clientport = clientport;
		this.groups = new ConcurrentHashMap <Integer, Map<Long, IClient>>();
		this.clients = Collections.synchronizedList(new ArrayList<IClient>());
		this.hasStarted = false;
		this.udpCheck = true;
		this.start();
	}
	
	public boolean shutdown(){
		if(this.hasStarted) {
			try {
    			if(this.serverSocket != null && this.serverSocket.isClosed()){
    				this.serverSocket.close();
    			}
    			if(this.udpSocket != null){
    				this.udpSocket.close();
    			}
    			for(IClient instance : this.clients){
    				instance.shutdown("bye");
    			}
				this.serverThread.shutdown();
    			this.clients = null;
    			this.groups = null;
				this.hasStarted = false;
				this.udpCheck = false;
				logger.info("Success to shutdown enhanced message service.");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public List<IClient> getClients(){
		return this.clients;
	}
	
	public Map<Integer, Map<Long, IClient>> getClientGroups(){
		return this.groups;
	}
	
	protected boolean start(){
		if(!hasStarted){
			try {
				//new Thread(this).start();
	            serverSocket = new ServerSocket(this.port);
	            serverThread = new MessageServerThread();
	            serverThread.start();
	            logger.info("Success to start enhanced message server on " + this.port);
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
	
	private boolean hasClient(Socket client_socket){
		for(int i = 0; i < this.clients.size(); i++){
			IClient instance = this.clients.get(i);
			if(instance.getInetAddress().equals(client_socket.getInetAddress())){
				return true;
			}
		}
		return false;
	}
	
	private IClient getClient(InetAddress addr){
		for(IClient instance : this.clients){
			if(instance.getInetAddress().equals(addr)){
				return instance;
			}
		}
		return null;
	}
	
	protected void addClient(IClient client){
		Long userid = client.getUserId();
		Integer role = client.getRoleType();
		if(userid != null && role != null){
			Map<Long, IClient> instances = this.groups.get(role);
			if(instances == null){
				instances = new ConcurrentHashMap<Long, IClient>();
				this.groups.put(role, instances);
			}
			IClient instance = instances.get(userid);
			if(instance != null && instance != client){
				this.removeClient(instance, false);
				instance.sendMessage("relogin");
				client.addMessages(instance.getMessages());
				logger.info("Client already registered: " + client.getUserId() + ", force it logout first.");
			}
			instances.put(userid, client);
			logger.info("Client registered: " + client.getUserId());
		} else {
			this.clients.add(client);
		}
	}
	
	protected void removeClient(IClient client, boolean isClosed){
		if(client == null){
			return;
		} else {
			boolean isRegistered = client.getUserId() != null;
			if(isRegistered){
				if(this.groups.get(client.getRoleType()) != null){
					this.groups.get(client.getRoleType()).remove(client.getUserId());
					client.setUserId(null);
					client.setRoleType(null);
					client.setUsername(null);
					logger.info("Unregistered client");
				}
			}
			if(isClosed){
				for(int i=0; i < this.clients.size(); i++){
					if(this.clients.get(i) == client){
						this.clients.remove(i);
						client.shutdown(null);
						logger.info("Remove client: " + client.getUserId());
						client = null;
						break;
					}
				}
			}
		}
	}
	
	public boolean sendMessageToAll(Message message){
		if(this.clients.size() > 0){
			for(IClient instance : this.clients){
				instance.sendMessage(MessageService.parseMessage(message));
			}
			logger.debug("Send message to all clients.");
			return true;
		}
		return false;
	}
	
	public boolean sendMessageToGroup(int groupType, Message message){
		String msgstring = MessageService.parseMessage(message);
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
		String msgstring = MessageService.parseMessage(message);
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
						subentry.getValue().sendMessage(MessageService.parseMessage(message));
						logger.debug("Send message to user: " + userid);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	void onMessage(String msgstring, ClientThread clientThread){
		Message message = MessageService.unparseMessage(msgstring);
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
						IClient instance = this.getClient(clientThread.getInetAddress());
						if(instance != null){
							if(instance.getUserId() != null){
								this.removeClient(instance, false);
							}
							Role role = logonuser.getRole_Details().get(0).getRole();
							instance.setRoleType(role.getType());
							instance.setUserId(logonuser.getId());
							instance.setUsername(logonuser.getName());
							String detail = String.valueOf(logonuser.getId()) + "###";
							detail += logonuser.getName() + "###";
							detail += String.valueOf(role.getType());
							this.addClient(instance);
							resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
									Constants.SUCCESS, detail);
						} else {
							resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
									Constants.FAIL, "无效用户");
						}
					} else {
						resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
								Constants.FAIL, loginresult.getErrorDetails().get("Error"));
					}
				} else {
					resultmsg = new ResultMessage(Constants.REGISTER_REQUEST, 
							Constants.FAIL, "请输入用户名和密码");
				}
				clientThread.sendMessage(parseMessage(resultmsg));
			} else {
				//unregister
				this.removeClient(this.getClient(clientThread.getInetAddress()), false);
				clientThread.sendMessage(parseMessage(new ResultMessage(Constants.UNREGISTER_REQUEST, 
							Constants.SUCCESS, "done")));
			}
		}
	}

//	@Override
//	public void run() {
//    	if(this.udpSocket == null){
//    		try {
//				this.udpSocket = new DatagramSocket(this.udpport);
//			} catch (SocketException e) {
//				e.printStackTrace();
//			}
//    	}
//    	if(this.udpSocket != null && this.udpSocket.isBound()){
//    		while(udpCheck){
//    		    if(this.clients.size() > 0){
//	    			try {
//	    		    	DatagramPacket packet = new DatagramPacket(new byte[1], 1);
//	    		    	this.udpSocket.setSoTimeout(TIMEOUT);
//	    				this.udpSocket.receive(packet);
//	    				IClient instance = this.getClient(packet.getAddress());
//	    				if(instance != null){
//	    					instance.lastRecvTime = System.currentTimeMillis();
//	    					logger.debug("Received udp check package: " + packet.getData());
//	    				} else {
//	    					logger.debug("Ignore packet");
//	    				}
//	    			} catch (IOException e) {
//	    				if(e instanceof SocketTimeoutException){
//	    					logger.warn("Timeout on receiving keep-alive packet");
//	    				} else {
//	    					e.printStackTrace();
//	    				}
//	    			}
//    		    } else {
//    		    	logger.debug("No clients connected");
//    		    	try {
//						Thread.sleep(TIMEOUT);
//					} catch (InterruptedException e){
//						e.printStackTrace();
//					}
//    		    }
//    		}
//    	} else {
//    		logger.error("Fail to start udp check service");
//    		this.shutdown();
//    	}
//	}
	
	protected class MessageServerThread extends Thread{
		
	    private boolean flag = true;
	    private final ExecutorService pool =  Executors.newFixedThreadPool(50);
	      
	    MessageServerThread(){
	    }
	    
	    @Override
	    public void run(){
	    	Socket client_socket = null;
	        try {
	        	while(flag){
	        		client_socket = serverSocket.accept();
	                if(!hasClient(client_socket)){
	                	//A new client connected
	                	ClientInstance client = new ClientInstance(EnhancedMessageService.this, 
    	                		client_socket.getInetAddress(), clientport, udpport);
	                	addClient(client);
	                }
	                //Prepare to receive client message
	                this.pool.execute(new ClientThread(EnhancedMessageService.this, client_socket));
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
	
    public static void main(String [] args){
		ApplicationContext aCtx = new FileSystemXmlApplicationContext("src/applicationContext.xml");
		EnhancedMessageService ms = (EnhancedMessageService)aCtx.getBean("messageService");
		ms.start(19191, 25252, 19192);
    }
	
}
