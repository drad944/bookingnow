package com.pitaya.bookingnow.app.service;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.message.Message;

public class MessageService {
	
	private static String LOGTAG = "MessageService";
	private static MessageService _instance;
	private Client clientAgent;
	private Map<String, Handler> handlers;
	
	private MessageService(String ip, int port){
		this.handlers = new HashMap<String, Handler>();
		this.start(ip, port);
	}
	
	public static MessageService initService(String ip, int port){
		if(_instance == null){
			_instance = new MessageService(ip, port);
		} else if(!_instance.isReady() && !_instance.isConnecting()){
			_instance.start(ip, port);
		}
		return _instance;
	}
	
	public static MessageService getService(){
		return _instance;
	}
	
	public static void shutdownService(){
		_instance.clientAgent.shutdown();
		_instance = null;
	}
	
	public void registerHandler(String key, Handler handler){
		if(this.handlers.get(key) != null){
			Log.e(LOGTAG, "Fail to register handler with same key");
		} else {
			this.handlers.put(key, handler);
		}
	}
	
	public void unregisterHandler(String key){
		this.handlers.remove(key);
	}
	
	public boolean isConnecting(){
		return this.clientAgent.isConnecting();
	}
	
	private void start(String ip, int port){
		if(this.clientAgent == null || !clientAgent.isReady()){
			if(this.clientAgent != null){
				this.clientAgent.interrupt();
				try {
					this.clientAgent.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.clientAgent = null;
			}
			clientAgent = new Client(ip, port, this);
			clientAgent.start();
		}
	}
	
	public boolean sendMessage(Message message){
		return this.clientAgent.sendMessage(this.parseMessage(message));
	}
	
	public boolean isReady(){
		return this.clientAgent.isReady();
	}
	
	//For only system service message, broadcast to all handlers
	void onMessage(Message message){
		for(Entry<String, Handler> entry : handlers.entrySet()){
			android.os.Message amsg = new android.os.Message();
	        Bundle bundle = new Bundle();  
	        bundle.putSerializable("message", message);
	        amsg.setData(bundle);
			entry.getValue().sendMessage(amsg);
		}
	}
	
	void onMessage(String message){
		Message msg = this.unparseMessage(message);
		if(msg != null){
			Handler handler = this.handlers.get(msg.getKey());
			if(handler != null){
				android.os.Message amsg = new android.os.Message();  
		        Bundle bundle = new Bundle();  
		        bundle.putSerializable("message", msg);
		        amsg.setData(bundle);
		        this.handlers.get(msg.getKey()).sendMessage(amsg);
			} else {
				Log.e(LOGTAG, "The key of this message has not been registered: " + message);
			}
		}
	}
	
	public String parseMessage(Message message){
		JSONObject jsonObj = new JSONObject();
		message.toJSONObject(jsonObj);
		return jsonObj.toString();
	}
	
	public Message unparseMessage(String message){
		Message msg = null;
		String type = null;
		try {
			JSONObject jsonMsg = new JSONObject(message);
			type = jsonMsg.getString("type");
			if(type != null){
				Class<?> msgcls = Class.forName(type);
				msg = (Message) msgcls.newInstance();
				msg.fromJSONObject(jsonMsg);
			} else {
				Log.e(LOGTAG, "Message type is null: " + message);
			}
		} catch (JSONException e) {
			Log.e(LOGTAG, "Fail to parse from message to json" + message);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(LOGTAG, "Unsupported message type: " + type);
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e(LOGTAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(LOGTAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		}
		return msg;
	}
	
}
