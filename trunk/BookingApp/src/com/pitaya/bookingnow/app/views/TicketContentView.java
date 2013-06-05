package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.*;

public class TicketContentView extends BaseContentView implements Ticket.OnDirtyChangedListener{
	
	private Map<String, Ticket> dirtyTickets;
	private String mTicketKey;
	private View mView;

	public TicketContentView(int type, String key, Context context, SlideContent home, String ticket_key) {
		super(type ,key, context, home);
		dirtyTickets = new HashMap<String, Ticket>();
		this.mTicketKey = ticket_key;
	}
	
	@Override
	public View getView(){
		if(mView == null){
			mView = View.inflate(this.mContext, R.layout.ticketcontentview, null);
		}
		return mView;
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
