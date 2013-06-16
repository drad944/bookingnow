package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.model.Ticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TicketLeftView extends Fragment{
	
	protected TicketContentView mContentContainer;
	protected boolean mDualPane;
	protected boolean mIsTouched = false;
	
	public void setContainer(TicketContentView v){
		this.mContentContainer = v;
	}
	
	public boolean isTouched(){
		return this.mIsTouched;
	}
	
	public boolean canInterrupt(){
		return true;
	}
	
	public void displayRightPanel(Ticket ticket){
	}
	
	public void displayRightPanel(){
		
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = true;
    }
	
}
