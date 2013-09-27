package com.pitaya.bookingnow.service.socket;

import java.net.InetAddress;
import java.util.List;

public interface IClient extends Runnable{
	
	public void shutdown(String message);
	public InetAddress getInetAddress();
	public String getAddress();
	public void sendMessage(String msg);
	public Integer getRoleType();
	public String getUsername();
	public Long getUserId();
	public void setUsername(String uname);
	public void setRoleType(Integer role);
	public void setUserId(Long id);
	public List<String> getMessages();
	public void addMessages(List<String> messages);
	public void checkConnection();
}
