package com.pitaya.bookingnow.service;

import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookingnow.message.LoginMessage;
import com.pitaya.bookingnow.message.Message;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class MessageServiceTest {
	
	private static String logTag = "MessageService";
	private static MessageServiceTest _instance;
	private Client clientAgent;
	
	public MessageServiceTest(){
		
	}
	
	private MessageServiceTest(String ip, int port){
		this.start(ip, port);
	}
	
	public static void newInstance(String ip, int port){
		_instance = new MessageServiceTest(ip, port);
	}
	
	public void start(String ip, int port){
		clientAgent.start();
	}
	
	public boolean sendMessage(Message message){
		onMessage(this.parseMessage(message));
		return true;
		//return this.clientAgent.sendMessage(this.parseMessage(message));
	}
	
	void onMessage(String message){
		LoginMessage lm = (LoginMessage)this.unparseMessage(message);
		System.out.println(lm.getType());
		System.out.println(lm.getKey());
		System.out.println(lm.getUsername());
		System.out.println(lm.getPassword());
	}
	
	private String parseMessage(Message message){
		JSONObject jsonMsg = JSONObject.fromObject(message);
		return jsonMsg.toString();
	}
	
	private Object unparseMessage(String message){
		Object msg = null;
		String type = null;
		try {
			JSONObject jsonMsg = JSONObject.fromObject(message);
			type = jsonMsg.getString("type");
			if(type != null){
				Class<?> msgcls = Class.forName(type);
				msg = JSONObject.toBean(jsonMsg, msgcls);
			} else {
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public static void main(String args[]){
		LoginMessage lm = new LoginMessage("key","zrm","123");
		new MessageServiceTest().sendMessage(lm);
	}
	
}
