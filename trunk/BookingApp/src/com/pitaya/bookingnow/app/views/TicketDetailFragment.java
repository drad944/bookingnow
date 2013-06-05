package com.pitaya.bookingnow.app.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class TicketDetailFragment extends Fragment {
	
	private String ticket_key;
	
	public static TicketDetailFragment newInstance(String ticketKey) {
		TicketDetailFragment f = new TicketDetailFragment();
		f.ticket_key = ticketKey;
        return f;
    }

    public String getShownIndex() {
        return ticket_key;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        text.setText("TextTextTextTextTextText" + this.getShownIndex());
        return scroller;
    }
}
