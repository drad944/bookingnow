package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.Ticket;

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
/*
 * Use a view pager which contains all of the menu view, and put the view pager in a fragment
 */
public class FoodMenuContentView extends BaseContentView{

	private FoodMenuContentFragment mFragment;
	private Ticket mTicket;
	
	public FoodMenuContentView(int type, String key, Context context, SlideContent home, Ticket ticket) {
		super(type, key, context, home);
		this.mTicket = ticket;
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
	public Fragment getFragment(){
		if(mFragment == null){
			mFragment = new FoodMenuContentFragment();
			mFragment.setContainer(this);
		}
		return mFragment;
	}
	
	public void selectPage(int index){
		if(this.mFragment != null) {
			mFragment.selectPage(index);
		}
	}
	
	public void setTicket(Ticket ticket){
		mTicket = ticket;
		if(mFragment != null) {
			mFragment.refreshCurrentPage();
		}
	}
	
	public Ticket getTicket(){
		return this.mTicket;
	}
}
