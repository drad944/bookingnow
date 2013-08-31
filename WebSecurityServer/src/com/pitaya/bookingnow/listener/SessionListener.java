package com.pitaya.bookingnow.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pitaya.bookingnow.source.EventManager;

public class SessionListener implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		 HttpSession session = event.getSession();
		 WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		 EventManager em = (EventManager)ctx.getBean("eventManager");
		 em.unSubscribeBySessionId(session.getId());
	}

}
