package com.pitaya.bookingnow.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.pitaya.bookingnow.message.RegisterMessage;
import com.pitaya.bookingnow.service.socket.MessageService;

public class Client extends Thread{
	  	
		private Socket socket;
	    private String ip = "127.0.0.1";// 设置成服务器IP
	    private int port = 19191;
	    private Long userId;
	    private BufferedReader in;
	    private PrintWriter out;

	    public Client(Long uid) {
	    	this.userId = uid;
	    }
	    
	    public void run(){
	        try {
	            if (createConnection()) {
	            	login();
	            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            	String message = null;
	            	while((message = in.readLine()) != null){
	            		if(message.equals("bye")){
	            			shutdown();
	            			break;
	            		} else {
	            			System.out.println("["+this.userId+"] receive message: " + message);
	            		}
	            	}
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    private void shutdown(){
				try {
					in.close();
					out.close();
					 if (socket != null){
						 socket.close();
					 }
					System.out.println("[" + this.userId + "] shut down!");
				} catch (IOException e) {
					e.printStackTrace();
				}
	    }
	    
	    private boolean createConnection() {
	    	 try {
	             socket = new Socket(ip, port);
	             return true;
	         } catch (Exception e) {
	             e.printStackTrace();
	             if (socket != null){
						try {
							socket.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
	             }
	             return false;
	         }

	    }

	    private void login() {
	        try {
	        	RegisterMessage registermsg = new RegisterMessage(this.userId);
	        	out = new PrintWriter(socket.getOutputStream());
	        	out.println(MessageService.parseMessage(registermsg));
	        	out.flush();
	        } catch (Exception e) {
	            System.out.println("Fail to send register info");
	        }
	    }
	    
	    public static void main(String [] args){
	    }

}
