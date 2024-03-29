package com.pitaya.bookingnow.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class MessageReceiver implements Runnable {

	private static String TAG = "MessageReceiver";
	private Socket socket;
    private BufferedReader in;
    private BufferedWriter bwriter;
    private EnhancedMessageService service;

    public MessageReceiver(Socket s, EnhancedMessageService ms) {
    	this.service = ms;
    	this.socket = s;
    }
    
    @Override
    public void run(){
        try {
        	in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			bwriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			String message = null;
			new Timer().schedule(new TimerTask(){

				@Override
				public void run() {
					sendMessage("ready");
				}
				
			}, 3000);
			this.socket.setSoTimeout(30000);
        	while((message = in.readLine()) != null){
        		this.service.onMessage(message);
        	}
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.shutdown();
        }
    }

	void sendMessage(String message){
		try {
			bwriter.write(message + "\r\n");
			bwriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "Fail to send message.");
		}
	}
    
    public void shutdown(){
    	 if(in != null){
			try {
        		in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	 }
	     if(bwriter != null){
		    try {
		    	bwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	     }
		 if (socket != null && !socket.isClosed()){
			try {
				socket.close();
				Log.d(TAG, "Connection is closed by server.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		 }
		 socket = null;
		 service = null;
    }

    public boolean isReady(){
    	return this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
    }
	
}
