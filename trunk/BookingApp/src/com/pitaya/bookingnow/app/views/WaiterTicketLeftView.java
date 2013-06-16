package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.TicketDetailActivity;
import com.pitaya.bookingnow.app.model.Ticket;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.TicketTable;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WaiterTicketLeftView extends TicketLeftView{

	public static final int MYTICKETS = 0;
	public static final int WAITING_TICKETS = MYTICKETS + 1;
	
	private static String TAG = "WaiterTicketLeftView";
	
	private TicketListsViewPagerAdapter mAdapter;
	private TicketListsViewPager mTicketsViewPager;
	private String lastSelectItem = null;
	
	public WaiterTicketLeftView(){
		super();
	}
	
	public boolean canInterrupt(){
		if(getCurrentViewPageIndex() == 0){
			return true;
		} else if(isTouched()){
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(this.mContentContainer.getStatus() != null){
			this.lastSelectItem = (String)this.mContentContainer.getStatus();
		}
		View view = inflater.inflate(R.layout.ticketleftview4waiter, null);
		mTicketsViewPager = (TicketListsViewPager)view.findViewById(R.id.waitorticketviewpager);
		mTicketsViewPager.setParentFragment(this);
		ArrayList<Integer> listTypes = new ArrayList<Integer>();
		listTypes.add(MYTICKETS);
		listTypes.add(WAITING_TICKETS);
		mAdapter = new TicketListsViewPagerAdapter(this.getActivity(), listTypes) ;
		mTicketsViewPager.setAdapter(mAdapter);
		return view;
	}
	
	public int getCurrentViewPageIndex(){
		return this.mTicketsViewPager.getCurrentItem();
	}
	
	public void setLastItem(String key){
		this.mContentContainer.saveStatus(key);
	}
	
	public String getLastItem(){
		return this.lastSelectItem;
	}
	
	public void showTicketDetail(Ticket ticket, boolean isForce){
        // Check what fragment is currently shown, replace if needed.
        String key = ticket.getTicketKey();
        TicketDetailFragment details = (TicketDetailFragment)getFragmentManager().findFragmentById(R.id.ticketdetail);
        if (details == null || isForce || !details.getShownTicket().getTicketKey().equals(key)) {
        	//TODO if ticket is new, get it from database, otherwise get it from server
        	DataService.getFoodsOfTicket(this.getActivity(), ticket);
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.ticketdetail, TicketDetailFragment.newInstance(ticket, mContentContainer));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
	private String getTitleByType(int type){
		switch(type){
			case MYTICKETS:
				return "我的订单";
			case WAITING_TICKETS:
				return "等候中的订单";
		}
		return "unknow ticket list type";
	}
	
	private class TicketListsViewPagerAdapter extends PagerAdapter {
		
		private Context mContext;
		private ArrayList<WaiterTicketListView> mTicketListViews;
		private List<String> mTitleList;
		private List<Integer> mListTypes;
	
	    public TicketListsViewPagerAdapter(Context context, ArrayList<Integer> types) {
	        this.mContext = context;
	        mTicketListViews = new ArrayList<WaiterTicketListView>();
	        mTitleList = new ArrayList<String>();
	        mListTypes = types;
	        for(int type : types){
				mTitleList.add(getTitleByType(type));
	        }
		}

	    public void refresh(int index){
	    	if(index < mTicketListViews.size()){
	    		mTicketListViews.get(index).refresh();
	    	}
	    }
	    
	    @Override  
	    public void destroyItem(View container, int position, Object object) {  
	    	WaiterTicketListView itemView = (WaiterTicketListView)object;  
	        itemView.recycle(position);
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
	    }
	    
	    @Override  
	    public Object instantiateItem(View container, int position) {
	    	WaiterTicketListView ticketListView;  
	        if(position < mTicketListViews.size()){
	        	ticketListView = mTicketListViews.get(position);
	        } else {
	        	ticketListView = new WaiterTicketListView(mContext, WaiterTicketLeftView.this);
				ticketListView.setupViews(mListTypes.get(position));
		        mTicketListViews.add(ticketListView);
	            ((ViewPager) container).addView(ticketListView);  
	        }
	        return ticketListView;
	    }
      
		@Override
		public int getCount() {
			return mTitleList.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	} //end of TicketListsViewPagerAdapter
	
}
