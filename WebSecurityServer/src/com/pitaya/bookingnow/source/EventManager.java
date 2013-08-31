package com.pitaya.bookingnow.source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pitaya.bookingnow.servlet.TopicServlet;

import net.sf.json.JSONObject;
import nl.justobjects.pushlet.core.Event;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

public class EventManager {
	
	public static final String ORDER_EVENT = "order";
	private static Log logger =  LogFactory.getLog(EventManager.class);
	
	private Map<String, String> allEvents;
	private Map<String, Map<String, Thread>> allSubscribers;
	
	public EventManager(){
		this.allEvents = new HashMap<String, String>();
		this.allSubscribers = new HashMap<String, Map<String, Thread>>();
	}
	
	public void subscribe(String topic, String sid, HttpServletResponse resp){
		Map<String, Thread> subscribers = this.allSubscribers.get(topic);
		if(subscribers == null){
			subscribers = new HashMap<String, Thread>();
			this.allSubscribers.put(topic, subscribers);
		}
		if(subscribers.get(sid) != null){
			subscribers.get(sid).interrupt();
		}
		subscribers.put(sid, Thread.currentThread());
		try {
			synchronized(Thread.currentThread()){
				Thread.currentThread().wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			try {
				resp.getOutputStream().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		resp.setContentType("text/html");  
        PrintWriter out = null;
		try {
			out = resp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
        out.print(this.allEvents.get(topic));  
        out.flush();  
        out.close();
	}
	
	public void unSubscribeBySessionId(String sid){
		for(Entry<String, Map<String, Thread>> entry : this.allSubscribers.entrySet()){
			if(entry.getValue().get(sid) != null){
				entry.getValue().get(sid).interrupt();
			}
		}
	}
	
	public void publish(String topic, Object obj){
		JSONObject jobj = JSONObject.fromObject(obj);
		this.allEvents.put(topic, jobj.toString());
		if(this.allSubscribers.get(topic) != null){
			for(Entry<String, Thread> entry : this.allSubscribers.get(topic).entrySet()){
				 synchronized(entry.getValue()){
				 	 entry.getValue().notify();
				 }
			}
		}
	}
}
