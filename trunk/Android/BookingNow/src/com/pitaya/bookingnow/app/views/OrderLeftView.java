package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.data.WorkerOrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.util.Constants;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OrderLeftView extends Fragment{
	
	protected OrderContentView mContentContainer;
	protected boolean mDualPane;
	protected boolean mIsTouched = false;
	protected String lastSelectItem = null;
	protected MessageHandler mMessageHandler;
	protected MessageService mMessageService;
	protected boolean mIsBound = false;
	protected ServiceConnection mConnection;
	
	public OrderLeftView(){
		mMessageHandler = new MessageHandler();
	}
	
	public void setContainer(OrderContentView v){
		this.mContentContainer = v;
	}
	
	public boolean isTouched(){
		return this.mIsTouched;
	}
	
	public boolean canInterrupt(){
		return true;
	}
	
	public void displayRightPanel(Order order){
	}
	
	public void displayRightPanel(){
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = true;
    }

	@Override
	public void onDestroyView(){
		super.onDestroyView();
		mMessageService.unregisterHandler(mMessageHandler);
		this.doUnbindService();
	}
	
	public void setLastItem(String key){
		this.mContentContainer.saveStatus(key);
	}
	
	public String getLastItem(){
		return this.lastSelectItem;
	}
	
	public void showOrderDetail(Order order, boolean isForce, Class<? extends OrderDetailAdapter> orderDetailAdapterClz){
        String key = order.getOrderKey();
        OrderDetailFragment details = null;
    	Fragment fragment = getFragmentManager().findFragmentById(R.id.orderdetail);
    	if(fragment != null && fragment instanceof OrderDetailFragment){
    		details = (OrderDetailFragment)fragment;
    	}
        if (details == null || isForce || !details.getShownOrder().getOrderKey().equals(key)) {
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.orderdetail, OrderDetailFragment.newInstance(order, mContentContainer, orderDetailAdapterClz));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
	protected Order getCurrentOrder(){
		 OrderDetailFragment details = (OrderDetailFragment)getFragmentManager().findFragmentById(R.id.orderdetail);
		 if(details != null){
			 return details.getShownOrder();
		 } else {
			 return null;
		 }
	}
	
	protected void doBindService() {
		this.getActivity().bindService(new Intent(this.getActivity(), MessageService.class), 
				getServiceConnection(), Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	}
	
	protected void doUnbindService() {
	    if (mIsBound) {
	    	this.getActivity().unbindService(mConnection);
	        mIsBound = false;
	    }
	}
	
	protected ArrayList<String> getMessageCategories(){
		ArrayList<String> categories = new ArrayList<String>();
		categories.add(Constants.ORDER_MESSAGE);
		return categories;
	}
	
	protected ServiceConnection getServiceConnection(){ 
		
		mConnection = new ServiceConnection() {
	
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMessageService = ((MessageService.MessageBinder)service).getService();
				for(String category : getMessageCategories()){
					mMessageService.registerHandler(category, mMessageHandler);
				}
			}
	
			@Override
			public void onServiceDisconnected(ComponentName name) {
				mMessageService = null;
			}

		};
		return mConnection;
	}
	
}
