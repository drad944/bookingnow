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
	private MessageHandler mMessageHandler;
	private CookingItemsListFragment mContent;
	private MessageService mMessageService;
	private boolean mIsBound = false;
	
	public KitchenOrderLeftView(){
		super();
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMessageService = ((MessageService.MessageBinder)service).getService();
			mMessageService.registerHandler(Constants.ORDER_MESSAGE, mMessageHandler);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMessageService = null;
		}

	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		doBindService();
		View view = inflater.inflate(R.layout.orderleftview4kitchen, null);
		this.showCookingItemsList();
		mMessageHandler = new MessageHandler();
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof OrderDetailMessage){
					OrderDetailMessage orderDetailMsg = (OrderDetailMessage)message;
					mContent.updateView(orderDetailMsg.getHasNew(), orderDetailMsg.getUpdateItems(), orderDetailMsg.getRemoveItems());
				}
			}
			
		});
		return view;
	}
	
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		mMessageService.unregisterHandler(mMessageHandler);
		this.doUnbindService();
	}
	
	public void showCookingItemsList(){
		FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mContent = CookingItemsListFragment.newInstance();
		fragmentTransaction.replace(R.id.orderdetail, mContent);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	void doBindService() {
		this.getActivity().bindService(new Intent(this.getActivity(), MessageService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}
	
	void doUnbindService() {
	    if (mIsBound) {
	        // Detach our existing connection.
	    	this.getActivity().unbindService(mConnection);
	        mIsBound = false;
	    }
	}
}
