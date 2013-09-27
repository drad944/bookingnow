package com.pitaya.bookingnow.service.socket;

import java.util.List;
import java.util.Map;

import com.pitaya.bookingnow.message.Message;

public interface IMessageService {
	
	public void start(int ... args);
	public boolean shutdown();
	public boolean sendMessageToAll(Message message);
	public boolean sendMessageToGroup(int groupType, Message message);
	public boolean sendMessageToGroupExcept(int groupType, Long userid, Message message);
	public boolean sendMessageToOne(Long userid, Message message);
	public List<IClient> getClients();
	public Map<Integer, Map<Long, IClient>> getClientGroups();

}
