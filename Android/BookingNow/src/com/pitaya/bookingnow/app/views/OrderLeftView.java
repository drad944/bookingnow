package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.model.Order;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class OrderLeftView extends Fragment{
	
	protected OrderContentView mContentContainer;
	protected boolean mDualPane;
	protected boolean mIsTouched = false;
	
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
	
}
