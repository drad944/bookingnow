package com.pitaya.bookingnow.app.views;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.OrderDetailActivity;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.data.GetOrderFoodsHandler;
import com.pitaya.bookingnow.app.data.GetOrderFoodsStatusHandler;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.data.WorkerOrderDetailAdapter;
import com.pitaya.bookingnow.app.data.PreviewOrderDetailAdapter;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.OrderTable;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.message.*;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WaiterOrderLeftView extends OrderLeftView{

	public static final int MYORDERS = 0;
	public static final int WAITING_ORDERS = MYORDERS + 1;
	
	private static String TAG = "WaiterOrderLeftView";
	
	private OrderListsViewPagerAdapter mAdapter;
	private OrderListsViewPager mOrdersViewPager;
	
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
		super.onCreateView(inflater, container, savedInstanceState);
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
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof OrderMessage){
					//Can only be new welcomer_new or waiting order
					OrderMessage msg = (OrderMessage)message;
					if(msg.getAction().equals(Constants.ACTION_ADD)){
						Order order =  new Order(msg.getOrderId(), null, null, 
								msg.getPhone(), msg.getCustomer(), msg.getPeopleCount(), msg.getTimestamp());
						order.setSubmitTime(msg.getTimestamp());
						order.setStatus(Constants.ORDER_WELCOMER_NEW);
						mAdapter.addNewWaitingOrder(order);
					} else if(msg.getAction().equals(Constants.ACTION_REMOVE)){
						Order order =  new Order(msg.getOrderId(), null, null, 
								null, null, 0, null);
						mAdapter.removeWaitingOrder(order);
					}
				} else if(message instanceof OrderDetailMessage){
					OrderDetailMessage msg = (OrderDetailMessage)message;
					if(msg.getUpdateItems() != null && msg.getUpdateItems().size() > 0){
						updateOrderDetailStatus(msg.getUpdateItems().get(0));
					}
				}
			}
			
		});
		this.doBindService();
		return view;
	}
	
	public void moveOrderToMineList(Order order){
		if(this.mAdapter.mOrderListViews.size() == 2){
			OrderListView waiterOrderListV = this.mAdapter.mOrderListViews.get(0);
			OrderListView waitingOrderListV = this.mAdapter.mOrderListViews.get(1);
			ArrayList<Order> waitingOrderList = waitingOrderListV.mAdapter.getOrderList();
			for(int i=0; i < waitingOrderList.size(); i++){
				Order searchorder = waitingOrderList.get(i);
				if(searchorder.getOrderKey().equals(order.getOrderKey())){
					waitingOrderList.remove(i);
					break;
				}
			}
			waiterOrderListV.mAdapter.getOrderList().add(order);
			waiterOrderListV.mAdapter.setSelectItem(waiterOrderListV.mAdapter.getOrderList().size() - 1);
			this.setLastItem(order.getOrderKey());
			waiterOrderListV.refresh();
			this.mOrdersViewPager.setCurrentItem(0, true);
		}
	}
	
	public void showOrderDetail(final Order order, final boolean isForce, int type){
    	switch(type){
    		case MYORDERS:
    			order.enrichFoods(this.getActivity());
            	if(order.getStatus() == Constants.ORDER_COMMITED){
            		if(order.isDirty()){
            			order.enrichUpdateFoods(this.getActivity());
            		}
            		GetOrderFoodsStatusHandler handler = new GetOrderFoodsStatusHandler(this.getActivity(), order);
            		handler.setAfterGetFoodsStatusListener(new GetOrderFoodsStatusHandler.AfterGetFoodsStatusListener(){

						@Override
						public void afterGetFoodsStatus() {
							showOrderDetail(order, isForce, WorkerOrderDetailAdapter.class);
						}
            			
            		});
            		OrderService.getFoodsOfOrder(Long.parseLong(order.getOrderKey()), handler);
            	} else {
            		showOrderDetail(order, isForce, WorkerOrderDetailAdapter.class);
            	}
    			break;
    		case WAITING_ORDERS:
    			GetOrderFoodsHandler handler = new GetOrderFoodsHandler(getActivity(), order);
    			handler.setAfterGetFoodsListener(new GetOrderFoodsHandler.AfterGetFoodsListener(){
					@Override
					public void afterGetFoods() {
						showOrderDetail(order, isForce, PreviewOrderDetailAdapter.class);
					}
    			});
    			OrderService.getFoodsOfOrder(Long.parseLong(order.getOrderKey()), handler);
    			break;
    	}
	}
	
	public int getCurrentViewPageIndex(){
		return this.mOrdersViewPager.getCurrentItem();
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
	
	private void updateOrderDetailStatus(CookingItem item){
		Order currentOrder = this.getCurrentOrder();
		if(currentOrder != null && String.valueOf(item.getOrderId()).equals(currentOrder.getOrderKey())){
			for(Order.Food food : currentOrder.getFoods().keySet()){
				if(food.getId().equals(item.getId())){
					food.setStatus(item.getStatus());
					return;
				}
			}
		}
	}
	
	class OrderListsViewPagerAdapter extends PagerAdapter {
		
		private Context mContext;
		private ArrayList<OrderListView> mOrderListViews;
		private List<String> mTitleList;
	
	    public OrderListsViewPagerAdapter(Context context, ArrayList<Integer> types) {
	        this.mContext = context;
	        mOrderListViews = new ArrayList<OrderListView>();
	        mTitleList = new ArrayList<String>();
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
	        itemView.recycle();
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
	    }
	    
	    @Override  
	    public Object instantiateItem(View container, int position) {
	    	OrderListView orderListView = null;  
	        if(position < mOrderListViews.size()){
	        	orderListView = mOrderListViews.get(position);
	        } else {
	        	if(position == 0){
	        		orderListView = new WaiterOrderListView(mContext, WaiterOrderLeftView.this);
	        	} else if(position == 1){
	        		orderListView = new WaitingOrderListView(mContext, WaiterOrderLeftView.this);
	        	}
	        	orderListView.setupViews();
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
		
		private void addNewWaitingOrder(Order order){
			if(this.getCount() > 1){
				mOrderListViews.get(1).getOrderList().add(order);
				mOrderListViews.get(1).refresh();
			}
		}
		
		private void removeWaitingOrder(Order removedorder){
			if(this.getCount() > 1){
				int i = 0;
				boolean isRemoved = false;
				for(Order order : mOrderListViews.get(1).getOrderList()){
					if(order.getOrderKey().equals(removedorder.getOrderKey())){
						mOrderListViews.get(1).getOrderList().remove(i);
						isRemoved = true;
						break;
					}
					i++;
				}
				if(isRemoved){
					mOrderListViews.get(1).refresh();
				}
			}
		}
		
	} //end of OrderListsViewPagerAdapter
	
}
