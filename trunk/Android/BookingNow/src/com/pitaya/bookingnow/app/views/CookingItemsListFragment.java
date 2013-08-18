package com.pitaya.bookingnow.app.views;

import java.util.List;

import com.pitaya.bookingnow.app.model.CookingItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CookingItemsListFragment extends Fragment{
	
	private static final String TAG = "CookingItemsListFragment";
	private CookingItemsListView mView;
	
	public static CookingItemsListFragment newInstance(){
		CookingItemsListFragment instance = new CookingItemsListFragment();
		return instance;
	}
	
	public CookingItemsListFragment(){
		super();
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		this.mView = new CookingItemsListView(this.getActivity());
        return mView;
    }
    
    public void updateView(boolean hasNew, List<CookingItem> updatedItems, List<CookingItem> removedItems){
    	boolean isChanged = false;
    	if(updatedItems != null && updatedItems.size() > 0){
    		this.mView.updateItems(updatedItems);
    		isChanged = true;
    	}
    	if(removedItems != null && removedItems.size() > 0){
    		this.mView.removeItems(removedItems);
    		isChanged = true;
    	}
    	if(isChanged){
    		this.mView.refresh();
    	}
    	if(hasNew){
    		this.mView.getNextCookingItems(false, 5000L);
    	}
    }
}
