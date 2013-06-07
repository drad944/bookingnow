package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.*;

public class TicketContentView extends BaseContentView implements Ticket.OnDirtyChangedListener{
	
	private Map<String, Ticket> dirtyTickets;
	private String mTicketKey;
	private View mView;
	private TicketListFragment mTicketListFragment;
	private TicketDetailFragment mTicketDetailFragment;
	
	public TicketContentView(String key, Context context, SlideContent home) {
		super(key, context, home);
		dirtyTickets = new HashMap<String, Ticket>();
	}
	
	@Override
	public void setupView(ViewGroup container){
		if(mView == null){
			mView = View.inflate(this.mContext, R.layout.ticketcontentview, null);
			FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			if(mTicketListFragment == null){
				mTicketListFragment = new TicketListFragment();
			}
			if(mTicketDetailFragment == null){
				mTicketDetailFragment = new TicketDetailFragment();
			}
			fragmentTransaction.replace(R.id.ticketlist, mTicketListFragment);
			fragmentTransaction.replace(R.id.ticketdetail, mTicketDetailFragment);
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
		container.addView(mView);
	}
	
	@Override
	public boolean destroyView(ViewGroup container){
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(mTicketListFragment);
		fragmentTransaction.remove(mTicketDetailFragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
		return true;
	}
	
	public void setDisplayTicketKey(String ticket_key){
		this.mTicketKey = ticket_key;
	}
	
	public String getDisplayTicketKey(){
		return this.mTicketKey;
	}
	
	public Ticket getDirtyTicketIfExists(String ticket_key){
		return this.dirtyTickets.get(ticket_key);
	}
	
	@Override
	public void onDirtyChanged(Ticket ticket, boolean flag) {
		if(flag){
			if(dirtyTickets.get(ticket.getTicketKey()) == null){
				dirtyTickets.put(ticket.getTicketKey(), ticket);
			}
		} else {
			if(dirtyTickets.get(ticket.getTicketKey()) != null){
				dirtyTickets.remove(ticket.getTicketKey());
			}
		}
	}
}
