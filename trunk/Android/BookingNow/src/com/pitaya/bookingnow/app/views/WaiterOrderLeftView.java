package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.OrderDetailActivity;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderTable;
import com.pitaya.bookinnow.app.util.Constants;

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

public class WaiterOrderLeftView extends OrderLeftView{

	public static final int MYORDERS = 0;
	public static final int WAITING_ORDERS = MYORDERS + 1;
	
	private static String TAG = "WaiterOrderLeftView";
	
	private OrderListsViewPagerAdapter mAdapter;
	private OrderListsViewPager mOrdersViewPager;
	private String lastSelectItem = null;
	
	public WaiterOrderLeftView(){
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
		View view = inflater.inflate(R.layout.orderleftview4waiter, null);
		mOrdersViewPager = (OrderListsViewPager)view.findViewById(R.id.waitororderviewpager);
		mOrdersViewPager.setParentFragment(this);
		ArrayList<Integer> listTypes = new ArrayList<Integer>();
		listTypes.add(MYORDERS);
		listTypes.add(WAITING_ORDERS);
		mAdapter = new OrderListsViewPagerAdapter(this.getActivity(), listTypes) ;
		mOrdersViewPager.setAdapter(mAdapter);
		return view;
	}
	
	public int getCurrentViewPageIndex(){
		return this.mOrdersViewPager.getCurrentItem();
	}
	
	public void setLastItem(String key){
		this.mContentContainer.saveStatus(key);
	}
	
	public String getLastItem(){
		return this.lastSelectItem;
	}
	
	public void showOrderDetail(Order order, boolean isForce){
        // Check what fragment is currently shown, replace if needed.
        String key = order.getOrderKey();
        OrderDetailFragment details = (OrderDetailFragment)getFragmentManager().findFragmentById(R.id.orderdetail);
        if (details == null || isForce || !details.getShownOrder().getOrderKey().equals(key)) {
        	order.enrichFoods(this.getActivity());
        	if(order.getStatus() == Constants.ORDER_COMMITED && order.isDirty()){
        		order.enrichUpdateFoods(this.getActivity());
        	}
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.orderdetail, OrderDetailFragment.newInstance(order, mContentContainer));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
	private String getTitleByType(int type){
		switch(type){
			case MYORDERS:
				return "我的订单";
			case WAITING_ORDERS:
				return "等候中的订单";
		}
		return "unknow order list type";
	}
	
	private class OrderListsViewPagerAdapter extends PagerAdapter {
		
		private Context mContext;
		private ArrayList<WaiterOrderListView> mOrderListViews;
		private List<String> mTitleList;
		private List<Integer> mListTypes;
	
	    public OrderListsViewPagerAdapter(Context context, ArrayList<Integer> types) {
	        this.mContext = context;
	        mOrderListViews = new ArrayList<WaiterOrderListView>();
	        mTitleList = new ArrayList<String>();
	        mListTypes = types;
	        for(int type : types){
				mTitleList.add(getTitleByType(type));
	        }
		}

	    public void refresh(int index){
	    	if(index < mOrderListViews.size()){
	    		mOrderListViews.get(index).refresh();
	    	}
	    }
	    
	    @Override  
	    public void destroyItem(View container, int position, Object object) {  
	    	WaiterOrderListView itemView = (WaiterOrderListView)object;  
	        itemView.recycle(position);
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
	    }
	    
	    @Override  
	    public Object instantiateItem(View container, int position) {
	    	WaiterOrderListView orderListView;  
	        if(position < mOrderListViews.size()){
	        	orderListView = mOrderListViews.get(position);
	        } else {
	        	orderListView = new WaiterOrderListView(mContext, WaiterOrderLeftView.this);
				orderListView.setupViews(mListTypes.get(position));
		        mOrderListViews.add(orderListView);
	            ((ViewPager) container).addView(orderListView);  
	        }
	        return orderListView;
	    }
      
		@Override
		public int getCount() {
			return mTitleList.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	} //end of OrderListsViewPagerAdapter
	
}
