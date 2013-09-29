package com.pitaya.bookingnow.service.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FlashClientAgent extends ClientAgent{
	
	private static Log logger =  LogFactory.getLog(FlashClientAgent.class);
	
	public FlashClientAgent(MessageService service, Socket socket){
		super(service, socket);
	}

	@Override
	public void run(){
		try{
			in = new BufferedReader(new InputStreamReader(client_socket.getInputStream(), "UTF-8"));
			out = new OutputStreamWriter(client_socket.getOutputStream(), "UTF-8");  
			bwriter = new BufferedWriter(out);
			char[] ch=new char[22];
			in.read(ch, 0, ch.length);
			StringBuffer sb=new StringBuffer();
			for(int i=0;i< ch.length;i++){
				sb.append(ch[i]);
			}
			String message=sb.toString();
			if(message.equals("<policy-file-request/>")){
				this.sendSecurityResponse();
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
			 } catch (IOException ex) {
				 ex.printStackTrace();
			 }
	         client_socket = null;
	         logger.debug("Connection to client [" + this.getUserId() +"] is broken");
	         this.service.removeClient(this, true);
		 }
	}
	
	void sendSecurityResponse(){
		try {
			client_socket.getOutputStream().write(this.service.securityResponse.getBytes("UTF-8"));
			client_socket.getOutputStream().flush();
			//this.shutdown();
			logger.debug("Send cross domain policy to client");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Fail to send cross domain policy to client");
		}
	}
}
