package com.pitaya.bookingnow.app.views;

import java.util.Map.Entry;

import com.pitaya.bookingnow.app.data.OrderDetailFragmentAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class OrderDetailFragment extends Fragment {
	
	private Order mOrder;
	private ListView mView;
	private OrderContentView mContentContainer;

	public static OrderDetailFragment newInstance(Order order, OrderContentView container){
		OrderDetailFragment instance = new OrderDetailFragment();
		instance.setContainer(container);
		instance.setOrder(order);
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
    	try {
    		OrderDetailFragmentAdapter orderAdapter = new OrderDetailFragmentAdapter(this.getActivity(), mView, this.mOrder);
    		mView.setAdapter(orderAdapter);
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
			this.mOrder.setOnDirtyChangedListener(null);
			this.mOrder.setOnStatusChangedListener(null);
			for(Entry<Food, Integer> entry : mOrder.getFoods().entrySet()){
				entry.getKey().setOnFoodStatusChangedListener(null);
			}
		}
	}
    
    public void modifyOrder(){
    	mContentContainer.openMenu(this.mOrder);
    }
}
