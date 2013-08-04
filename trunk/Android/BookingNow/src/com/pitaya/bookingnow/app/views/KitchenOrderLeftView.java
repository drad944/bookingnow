package com.pitaya.bookingnow.app.views;

import java.lang.ref.SoftReference;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.OrderDetailMessage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KitchenOrderLeftView extends OrderLeftView{
	
	private static final String TAG = "KitchenOrderLeftView";
	private SoftReference<MessageService> msRef;
	private MessageHandler mMessageHandler;

	public KitchenOrderLeftView(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.orderleftview4kitchen, null);
		this.showCookingItemsList();
		mMessageHandler = new MessageHandler();
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof OrderDetailMessage){
					OrderDetailMessage orderDetailMsg = (OrderDetailMessage)message;
				}
			}
			
		});
		getMessageService().registerHandler(Constants.ORDER_MESSAGE, mMessageHandler);
		return view;
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		getMessageService().unregisterHandler(Constants.ORDER_MESSAGE, mMessageHandler);
	}
	
	public void showCookingItemsList(){
		FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.orderdetail, CookingItemsListFragment.newInstance());
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	private MessageService getMessageService(){
		if(this.msRef == null){
			msRef = new SoftReference<MessageService>(MessageService.getService());
		}
		return (MessageService)msRef.get();
	}
	
}
