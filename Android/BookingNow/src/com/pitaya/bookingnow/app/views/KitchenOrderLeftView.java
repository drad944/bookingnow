package com.pitaya.bookingnow.app.views;

import java.lang.ref.SoftReference;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.OrderDetailMessage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KitchenOrderLeftView extends OrderLeftView{
	
	private static final String TAG = "KitchenOrderLeftView";
	private CookingItemsListFragment mContent;
	
	public KitchenOrderLeftView(OrderContentView v){
		super(v);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.orderleftview4kitchen, null);
		this.showCookingItemsList();
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof OrderDetailMessage){
					OrderDetailMessage orderDetailMsg = (OrderDetailMessage)message;
					mContent.updateView(orderDetailMsg.getHasNew(), orderDetailMsg.getUpdateItems(), orderDetailMsg.getRemoveItems());
				}
			}
			
		});
		//this.doBindService();
		return view;
	}
	
	public void showCookingItemsList(){
		FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mContent = CookingItemsListFragment.newInstance();
		fragmentTransaction.replace(R.id.orderdetail, mContent);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
}
