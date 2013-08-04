package com.pitaya.bookingnow.app.service;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.message.Message;

public class MessageService {
	
	private static String TAG = "MessageService";
	private static MessageService _instance;
	private Client clientAgent;
	private Map<String, List<Handler>> handlers;
	
	private MessageService(String ip, int port){
		this.handlers = new HashMap<String, List<Handler>>();
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
		List<Handler> handlerList = this.handlers.get(key);
		if(handlerList == null){
			handlerList = new ArrayList<Handler>();
			this.handlers.put(key, handlerList);
		}
		handlerList.add(handler);
		Log.i(TAG, "Register message handler with key: " + key);
	}
	
	public void unregisterHandler(String key, Handler handler){
		List<Handler> handlerList = this.handlers.get(key);
		if(handlerList != null){
			for(int i=0; i < handlerList.size(); i++){
				if(handlerList.get(i) == handler){
					handlerList.remove(i);
					Log.i(TAG, "Register message handler with key: " + key);
					break;
				}
			}
		}
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
		return this.clientAgent.sendMessage(parseMessage(message));
	}
	
	public boolean isReady(){
		return this.clientAgent.isReady();
	}
	
	void onMessage(Message message){
		if(message == null){
			return;
		}
		List<Handler> handlerList = this.handlers.get(message.getCategory());
		if(handlerList != null && handlerList.size() > 0){
	        for(Handler handler : handlerList){
				android.os.Message amsg = new android.os.Message();  
		        Bundle bundle = new Bundle();  
		        bundle.putSerializable("message", message);
		        amsg.setData(bundle);
	        	handler.sendMessage(amsg);
	        }
		} else {
			Log.i(TAG, "This category message has not been registered: " + message.getCategory());
		}
	}
	
	void onMessage(String message){
		Message msg = unparseMessage(message);
		onMessage(msg);
	}
	
	public static String parseMessage(Message message){
		JSONObject jsonObj = new JSONObject();
		message.toJSONObject(jsonObj);
		return jsonObj.toString();
	}
	
	public static Message unparseMessage(String message){
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
				Log.e(TAG, "Message type is null: " + message);
			}
		} catch (JSONException e) {
			Log.e(TAG, "Fail to parse from message to json" + message);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Unsupported message type: " + type);
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e(TAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Fail to parse message type:" + type);
			e.printStackTrace();
		}
		return msg;
	}
	
}
