package com.pitaya.bookingnow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread{
	  	
		private Socket socket;
	    private String ip = "127.0.0.1";// 设置成服务器IP
	    private int port = 19191;
	    private String loginmsg = "login:";
	    private String username = "user";
	    private String role;
	    private BufferedReader in;
	    private PrintWriter out;
	    
	    private static int clientnumber = 0;
	    private static String[] roles = {"kitchen","waiter","welcomer","cash"};

	    public Client() {
	    	clientnumber++;
	    	this.username = this.username + clientnumber;
	    	this.role = roles[clientnumber%roles.length];
	    }
	    
	    public void run(){
	        try {
	            if (createConnection()) {
	            	login();
	            	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            	String message = null;
	            	while((message = in.readLine()) != null){
	            		System.out.println("["+this.username + ","+ this.role+"]:" + message);
	            		if(message.equals("bye")){
	            			shutdown();
	            			break;
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
					System.out.println(this.username + " shut down!");
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
	        	out = new PrintWriter(socket.getOutputStream());
	        	out.println(loginmsg + this.username + ":" + this.role + ":123456");
	        	out.flush();
	        } catch (Exception e) {
	            System.out.println("Fail to send login info");
	        }
	    }

}
