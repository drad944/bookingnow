package com.pitaya.bookingnow.app.data;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.util.ContentUtil;

public class OrderListAdapter extends BaseAdapter{

	protected ArrayList<Order> orderlist;
	protected Integer selectItem;
	protected Context mContext;
	protected View mView;
	
	public OrderListAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
        mContext = c;
        mView = view;
    }
	
	protected void setViewLayoutParams(View view){
		view.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, ContentUtil.getPixelsByDP(30)));
	}
	
	@Override
	public int getCount() {
		return orderlist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
    @Override  
    public int getViewTypeCount() {
        return 1;
    }
    
    @Override  
    public int getItemViewType(int position) {
    	return 0;
    }
    
    public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
    }
    
    public Integer getSelectItem(){
    	return this.selectItem;
    }

    public void setOrderList(ArrayList<Order> list) {  
        this.orderlist = list;  
    }
    
    public ArrayList<Order> getOrderList(){
    	return this.orderlist;
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
	public static class ViewHolder{
		public TextView info1;
		public TextView info2;
		public TextView info3;
		public TextView info4;
		public TextView info5;
		public WeakReference<Order> currentDisplayOrderRef;
		public WeakReference<Order.OnOrderStatusChangedListener> listenerRef;
		public ViewHolder(){};
		
		public Order getOrder(){
			if(currentDisplayOrderRef != null){
				return currentDisplayOrderRef.get();
			} else {
				return null;
			}
		}
		
		public void setOrder(Order order){
			if(currentDisplayOrderRef != null){
				currentDisplayOrderRef.clear();
			}
			currentDisplayOrderRef = new WeakReference<Order>(order);
		}
		
		public Order.OnOrderStatusChangedListener getStatusListener(){
			if(listenerRef != null){
				return listenerRef.get();
			} else {
				return null;
			}
		}
		
		public void setStatusListener(Order.OnOrderStatusChangedListener l){
			if(listenerRef != null){
				listenerRef.clear();
			}
			listenerRef = new WeakReference<Order.OnOrderStatusChangedListener>(l);
		}
	}
	
}
