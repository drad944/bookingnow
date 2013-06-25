package com.pitaya.bookingnow.servlet;

import javax.servlet.ServletException;   
import javax.servlet.http.HttpServlet;   
import org.apache.log4j.xml.DOMConfigurator;
   
@SuppressWarnings("serial")
public class Log4jInitServlet  extends HttpServlet {

    public void init() throws ServletException {   
    	 String appFilePath = getServletContext().getRealPath("/");
         System.setProperty("webappRoot", appFilePath);
         DOMConfigurator.configure(appFilePath + getInitParameter("configfile"));   
     }
}
