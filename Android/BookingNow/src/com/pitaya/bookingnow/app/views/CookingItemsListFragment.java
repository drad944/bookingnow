package com.pitaya.bookingnow.app.views;

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
}
