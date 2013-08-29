package com.pitaya.bookingnow.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pitaya.bookingnow.entity.ClientInfo;
import com.pitaya.bookingnow.service.socket.EnhancedMessageService;
import com.pitaya.bookingnow.service.socket.ClientInstance;

public class AdminAction extends BaseAction {
	
	private EnhancedMessageService messageService;
	
	private List<ClientInfo> clientInfos;
	
	public void setMessageService(EnhancedMessageService ms){
		this.messageService = ms;
	}
	
	public EnhancedMessageService getMessageService(){
		return this.messageService;
	}
	
	public void setClientInfos(List<ClientInfo> infos){
		this.clientInfos = infos;
	}
	
	public List<ClientInfo> getClientInfos(){
		return this.clientInfos;
	}
	
	public String getConnectionInfo(){
		int total = this.messageService.getClients().size();
		int unauthcount = 0;
		int authcount = 0;
		this.clientInfos = new ArrayList<ClientInfo>();
		for(ClientInstance instance : this.messageService.getClients()){
			if(instance.getUserId() == null && instance.getRoleType() == null){
				ClientInfo client = new ClientInfo();
				client.setIsAuth(false);
				clientInfos.add(client);
				unauthcount ++;
			}
		}
		for(Entry<Integer, Map<Long, ClientInstance>> entry : this.messageService.getClientGroups().entrySet()){
			for(Entry<Long, ClientInstance> subentry : entry.getValue().entrySet()){
				ClientInfo client = new ClientInfo();
				client.setIsAuth(true);
				client.setRoleType(subentry.getValue().getRoleType());
				client.setUserid(subentry.getValue().getUserId());
				clientInfos.add(client);
				authcount ++;
			}
		}
		logger.debug("Total" + total + ", Auth:" + authcount + ", Unauth: " + unauthcount);
		return "Success";
	}
	
}
