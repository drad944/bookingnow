package com.pitaya.bookingnow.app.views;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map.Entry;

import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.data.WorkerOrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.util.ContentUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class OrderDetailFragment extends Fragment {
	
	private static final String TAG = "OrderDetailFragment";
	private Order mOrder;
	private ListView mView;
	private OrderDetailAdapter mOrderDetailAdapter;
	private OrderContentView mContentContainer;
	private Class<? extends OrderDetailAdapter> mAdapterClazz;

	public static OrderDetailFragment newInstance(Order order, OrderContentView container, 
			Class<? extends OrderDetailAdapter> adapterclz){
		OrderDetailFragment instance = new OrderDetailFragment();
		instance.setContainer(container);
		instance.setOrder(order);
		instance.setAdapterClass(adapterclz);
		return instance;
	}
	
	public OrderDetailFragment(){
		super();
	}
	
    public void setOrder(Order order){
    	this.mOrder = order;
    }
    
	public void setContainer(OrderContentView v){
		this.mContentContainer = v;
	}
	
	public void setAdapterClass(Class<? extends OrderDetailAdapter> clz){
		this.mAdapterClazz = clz;
	}
	
    public Order getShownOrder(){
    	return this.mOrder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null || this.mOrder == null) {
            return null;
        }
        mView = new ListView(getActivity());
    	mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	try {
    		try {
				Constructor<? extends OrderDetailAdapter> con = this.mAdapterClazz.getConstructor(Context.class, View.class, Order.class);
				try {
					this.mOrderDetailAdapter = (OrderDetailAdapter)con.newInstance(this.getActivity(), mView, this.mOrder);
				} catch (java.lang.InstantiationException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
            mView.setAdapter(mOrderDetailAdapter);
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
		if(this.mOrder != null){
			this.mOrder.removeOnStatusChangedListeners();
			this.mOrder.removeOnDirtyChangedListener();
			if(mOrder.getFoods() != null){
				for(Entry<Food, Integer> entry : mOrder.getFoods().entrySet()){
					entry.getKey().setOnFoodStatusChangedListener(null);
				}
			}
		}
	}
    
    public void modifyOrder(){
    	mContentContainer.openMenu(this.mOrder);
    }
}
