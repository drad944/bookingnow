package com.pitaya.bookingnow.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.pitaya.bookingnow.service.socket.EnhancedMessageService;
import com.pitaya.bookingnow.service.socket.FlashClientAgent;
import com.pitaya.bookingnow.service.socket.MessageService;

@SuppressWarnings("serial")
public class InitMessageServiceServlet extends HttpServlet {

    public void init() throws ServletException {
    	ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
    	EnhancedMessageService ms = (EnhancedMessageService) ac.getBean("messageService");
    	if(ms != null){
    		ms.start(Integer.parseInt(getInitParameter("port")), 25252, 19192);
    	}
    	MessageService secms = (MessageService) ac.getBean("securitySocketService");
    	if(secms != null){
    		secms.start(843, FlashClientAgent.class);
    	}
    }

}
