package com.pitaya.bookingnow.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ServerAgent extends Thread {

	private static String TAG = "ServerAgentThread";
	private Socket socket;
    private BufferedReader in;
    private BufferedWriter bwriter;
    private EnhancedMessageService service;

    public ServerAgent(Socket s, EnhancedMessageService ms) {
    	this.service = ms;
    	this.socket = s;
    }
    
    @Override
    public void run(){
        try {
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));  
        	String message = null;
        	while((message = in.readLine()) != null){
        		if(message.equals("bye")){
        			break;
        		} else {
        			this.service.onMessage(message, this);
        		}
        	}
        } catch (IOException e) {
        	Log.e(TAG, "Fail to receive message from server");
			e.printStackTrace();
		} finally {
			this.shutdown();
        }
    }

    public void shutdown(){
    	 if(in != null){
			try {
        		 in.close();
			} catch (IOException e) {
				 e.printStackTrace();
			}
			in = null;
    	 }
		     if(bwriter != null){
		    try {
		    	bwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    bwriter = null;
		     }
		 if (socket != null && !socket.isClosed()){
			try {
				socket.close();
				Log.d(TAG, "Success to shutdown the connection to server");
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 socket = null;
    }

    public boolean isReady(){
    	return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
    }
	
}
