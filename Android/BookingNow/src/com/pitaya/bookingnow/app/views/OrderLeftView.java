package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.data.WorkerOrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.util.Constants;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class OrderLeftView extends Fragment{
	
	protected OrderContentView mContentContainer;
	protected boolean mDualPane;
	protected boolean mIsTouched = false;
	protected String lastSelectItem = null;
	
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
	
	public void setLastItem(String key){
		this.mContentContainer.saveStatus(key);
	}
	
	public String getLastItem(){
		return this.lastSelectItem;
	}
	
	public void showOrderDetail(Order order, boolean isForce, Class<? extends OrderDetailAdapter> orderDetailAdapterClz){
        String key = order.getOrderKey();
        OrderDetailFragment details = (OrderDetailFragment)getFragmentManager().findFragmentById(R.id.orderdetail);
        if (details == null || isForce || !details.getShownOrder().getOrderKey().equals(key)) {
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.orderdetail, OrderDetailFragment.newInstance(order, mContentContainer, orderDetailAdapterClz));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
}
