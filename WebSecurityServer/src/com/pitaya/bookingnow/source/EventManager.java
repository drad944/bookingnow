package com.pitaya.bookingnow.source;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pitaya.bookingnow.util.CommonUtil;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EventManager {
	
	public static final String ORDER_EVENT = "order";
	private static Log logger =  LogFactory.getLog(EventManager.class);
	
	private Map<String, Map<String, Subscription>> allSubscriptions;
	
	public EventManager(){
		this.allSubscriptions = new HashMap<String, Map<String, Subscription>>();
	}
	
	/*
	 * synchronize subscriptions to prevent from new subscription interrupt the old one
	 * which is in executing
	 */
	public void subscribe(String topic, HttpServletRequest req, HttpServletResponse resp){
		String ip = CommonUtil.getRemoteUrl(req);
		Map<String, Subscription> subscriptions = this.allSubscriptions.get(ip);
		if(subscriptions == null){
			subscriptions = new HashMap<String, Subscription>();
			this.allSubscriptions.put(ip, subscriptions);
		}
		if(subscriptions.get(topic) != null){
			subscriptions.get(topic).t.interrupt();
		}
		Subscription subscription = new Subscription(Thread.currentThread(), req.getSession().getId());
		subscriptions.put(topic, subscription);
		logger.debug("Subscribe topic: " + topic + " on ip:" + ip);
		try {
			synchronized(Thread.currentThread()) {
				Thread.currentThread().wait();
			}
		} catch (InterruptedException e) {
			logger.debug("Topic subscription is removed");
			try {
				resp.getOutputStream().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		synchronized(subscriptions){
			resp.setContentType("text/html");
	        PrintWriter out = null;
			try {
				out = resp.getWriter();
		        out.print(subscription.event);  
		        out.flush();  
		        out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void unSubscribeBySessionId(String sid){
		Iterator<Map.Entry<String, Map<String, Subscription>>> iter = this.allSubscriptions.entrySet().iterator();
		while(iter.hasNext()){
			Map<String, Subscription> subscriptions = iter.next().getValue();
			Iterator<Map.Entry<String, Subscription>> subiter = subscriptions.entrySet().iterator();
			while(subiter.hasNext()){
				Subscription subcription = subiter.next().getValue();
				if(subcription.sid.equals(sid)){
					subiter.remove();
				}
			}
		}
	}
	
	public void unSubscribeTopic(String topic, HttpServletRequest req){
		String ip = CommonUtil.getRemoteUrl(req);
		Map<String, Subscription> subscriptions = this.allSubscriptions.get(ip);
		if(subscriptions != null){
			if(topic == null || topic.equals("")){
				for(Entry<String, Subscription> entry : subscriptions.entrySet()){
					entry.getValue().t.interrupt();
				}
				this.allSubscriptions.remove(ip);
			} else {
				Subscription subscription = subscriptions.get(topic);
				if(subscription != null){
					subscription.t.interrupt();
				}
				subscriptions.remove(topic);
			}
		}
	}
	
	public void publish(String topic, Object obj){
		JSONObject jobj = JSONObject.fromObject(obj);
		for(Entry<String, Map<String, Subscription>> entry : this.allSubscriptions.entrySet()){
			for(Entry<String, Subscription> subentry : entry.getValue().entrySet()){
				if(subentry.getKey().equals(topic)){
					subentry.getValue().event = jobj.toString();
					synchronized(subentry.getValue().t) {
						subentry.getValue().t.notify();
					}
				}
			}
		}
	}
	
	private static class Subscription{
		Thread t;
		String sid;
		String event;
		
		Subscription(Thread t, String sid){
			this.t = t;
			this.sid = sid;
		}
	}
}
