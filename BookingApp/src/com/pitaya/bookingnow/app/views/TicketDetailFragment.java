package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.domain.Ticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class TicketDetailFragment extends Fragment {
	
	private Ticket mTicket;
	private ScrollView mView;

    public void setTicket(Ticket ticket){
    	this.mTicket = ticket;
    	updateView();
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
        mView = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        text.setId(1);
        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        mView.addView(text);
        if(this.mTicket != null){
        	text.setText("TextTextTextTextTextText" + this.mTicket.getTicketKey());
        }
        return mView;
    }
    
    private void updateView(){
    	if(this.mTicket != null){
    		((TextView)mView.findViewById(1))
    			.setText("TextTextTextTextTextText" + this.mTicket.getTicketKey());
    	}
    }
}
