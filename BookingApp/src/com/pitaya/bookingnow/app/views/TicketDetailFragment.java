package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.domain.Ticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class TicketDetailFragment extends Fragment {
	
	private static TicketDetailFragmentAdapter sTicketAdapter;
	private Ticket mTicket;
	private ListView mView;
	private TicketContentView mContentContainer;

	public static TicketDetailFragment newInstance(Ticket ticket, TicketContentView container){
		TicketDetailFragment instance = new TicketDetailFragment();
		instance.setContainer(container);
		instance.setTicket(ticket);
		return instance;
	}
	
	public TicketDetailFragment(){
		super();
	}
	
    public void setTicket(Ticket ticket){
    	this.mTicket = ticket;
    }
    
	public void setContainer(TicketContentView v){
		this.mContentContainer = v;
	}
	
    public Ticket getShownTicket(){
    	return this.mTicket;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null || this.mTicket == null) {
            return null;
        }
        mView = new ListView(getActivity());
    	try {
    		if(sTicketAdapter == null){
    			sTicketAdapter = new TicketDetailFragmentAdapter(this.getActivity(), mView, this.mTicket);
    		} else {
    			sTicketAdapter.setTicket(mTicket);
    		}
    		mView.setAdapter(sTicketAdapter);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return mView;
    }
    
    
    public void modifyTicket(){
    	mContentContainer.openMenu(this.mTicket);
    }
}
