package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.R;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KitchenOrderLeftView extends OrderLeftView{
	
	private static final String TAG = "KitchenOrderLeftView";

	public KitchenOrderLeftView(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.orderleftview4kitchen, null);
		this.showCookingItemsList();
		return view;
	}
	
	public void showCookingItemsList(){
		FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.orderdetail, CookingItemsListFragment.newInstance());
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
}
