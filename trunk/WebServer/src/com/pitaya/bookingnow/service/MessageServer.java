package com.pitaya.bookingnow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class ClientAgent extends Thread{
	
	Socket client_socket;
	MessageServer server;
	PrintWriter out;
	String userid;
	String role;
	
	ClientAgent(MessageServer server, Socket socket){
		this.client_socket = socket;
		this.server = server;
	}
	
	public void run(){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));   
			out = new PrintWriter(client_socket.getOutputStream());   
			String message = null;
			while((message = in.readLine()) != null){
				System.out.println(message);
			}
		 }catch (Exception e){
	        e.printStackTrace();
	        if(client_socket != null){
				try {
					client_socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        }
		 }
	}
	
	void sendMessage(String message){
		out.write(message);
	}
}

class MessageServerThread extends Thread{
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
        } catch( Exception e ) {  
            e.printStackTrace( );  
        } finally {
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
	
	ServerSocket serverSocket;
	
	private MessageServerThread serverThread;
	private int port;
	private boolean hasStarted;
	private Map<String, Map<String, ClientAgent>> groups;
	
	private MessageServer(){
		this.hasStarted = false;
		groups = new HashMap<String, Map<String, ClientAgent>>();
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
	            this.hasStarted = true;
	            return true;
	        } catch(IOException e) {
	            return false;
	        } finally {
	        	if(serverSocket != null){
	        		try {
						serverSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	}
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
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
        	}
		}
		return true;
	}
	
	void addClient(ClientAgent clientAgent){
		
	}
	
}
