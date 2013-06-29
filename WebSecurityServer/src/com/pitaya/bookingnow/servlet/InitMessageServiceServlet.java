package com.pitaya.bookingnow.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.pitaya.bookingnow.service.impl.MessageService;

@SuppressWarnings("serial")
public class InitMessageServiceServlet extends HttpServlet {

    public void init() throws ServletException {   
    		MessageService.initService(Integer.parseInt(getInitParameter("port")));
    }

}
