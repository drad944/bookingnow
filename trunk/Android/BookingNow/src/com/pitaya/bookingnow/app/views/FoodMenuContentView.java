package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
/*
 * Use a view pager which contains all of the menu view, and put the view pager in a fragment
 */
public class FoodMenuContentView extends BaseContentView{

	private FoodMenuContentFragment mFragment;
	private Order mOrder;
	
	public FoodMenuContentView(String key, Context context, SlideContent home) {
		super(key, context, home);
	}

	@Override
	public boolean canIntercept(){
		if(mFragment != null){
			return mFragment.getCurrentViewIndex() == 0;
		} else {
			return super.canIntercept();
		}
	}
	
	@Override
	public void setupView(ViewGroup container){
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager(); 
		//FragmentManager.enableDebugLogging(false);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(mFragment == null){
			mFragment = new FoodMenuContentFragment();
			mFragment.setContainer(this);
		}
		fragmentTransaction.add(container.getId(), mFragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	@Override
	public boolean destroyView(ViewGroup container){
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(mFragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
		return true;
	}
	
	public void selectPage(int index){
		if(this.mFragment != null) {
			mFragment.selectPage(index);
		}
	}
	
	public void setOrder(Order order){
		mOrder = order;
	}
	
	public void setOrderAndRefresh(Order order){
		mOrder = order;
		if(mFragment != null) {
			mFragment.refreshAllPages();
		}
	}
	
	
	public Order getOrder(){
		return this.mOrder;
	}
}
