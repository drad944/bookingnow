package com.pitaya.bookingnow.action;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.pitaya.bookingnow.source.EventManager;

public class TopicAction {
	
	private EventManager eventManager;
	private String topic;
	
	public void setTopic(String t){
		this.topic = t;
	}
	
	public String getTopic(){
		return this.topic;
	}
	
	public void setEventManager(EventManager em){
		this.eventManager = em;
	}
	
	public EventManager getEventManager(){
		return this.eventManager;
	}
	
	public String subscribeTopic(){
		if(this.topic != null && !this.topic.equals("")){
			ActionContext ac = ActionContext.getContext();
			this.eventManager.subscribe(topic, ServletActionContext.getRequest(),
					(HttpServletResponse)ac.get(ServletActionContext.HTTP_RESPONSE));
		}
		return null;
	}
	
	public String unsubscribeTopic(){
		this.eventManager.unSubscribeTopic(topic, ServletActionContext.getRequest());
		return null;
	}
	
}
