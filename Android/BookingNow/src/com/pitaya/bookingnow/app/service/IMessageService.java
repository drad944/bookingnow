package com.pitaya.bookingnow.app.service;

import com.pitaya.bookingnow.message.Message;

import android.os.Binder;
import android.os.Handler;

public interface IMessageService {
	
	public void start();
	public void registerHandler(String key, Handler handler);
	public void unregisterHandler(Handler handler);
	public boolean isConnecting();
	public boolean isReady();
	public boolean sendMessage(Message message);
	public boolean sendMessage(String msg);
}
