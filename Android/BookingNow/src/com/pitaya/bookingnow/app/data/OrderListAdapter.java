package com.pitaya.bookingnow.app.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;

public class OrderListAdapter extends BaseAdapter{

	protected ArrayList<Order> orderlist;
	protected Integer selectItem;
	protected Context mContext;
	protected View mView;
	
	public OrderListAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
        mContext = c;
        mView = view;
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
	
}
