package com.pitaya.bookingnow.app.views;

import java.util.Map.Entry;

import com.pitaya.bookingnow.app.model.Ticket;
import com.pitaya.bookingnow.app.model.TicketDetailFragmentAdapter;
import com.pitaya.bookingnow.app.model.Ticket.Food;

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
    		TicketDetailFragmentAdapter ticketAdapter = new TicketDetailFragmentAdapter(this.getActivity(), mView, this.mTicket);
    		mView.setAdapter(ticketAdapter);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return mView;
    }
    
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(this.mTicket != null){
			this.mTicket.setOnDirtyChangedListener(null);
			this.mTicket.setOnStatusChangedListener(null);
			for(Entry<Food, Integer> entry : mTicket.getFoods().entrySet()){
				entry.getKey().setOnFoodStatusChangedListener(null);
			}
		}
	}
    
    public void modifyTicket(){
    	mContentContainer.openMenu(this.mTicket);
    }
}
