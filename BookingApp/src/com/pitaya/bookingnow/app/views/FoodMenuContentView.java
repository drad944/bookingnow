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

	private ArrayList<Fragment> mFragmentList;
	private Ticket mTicket;
	
	public FoodMenuContentView(String key, Context context, SlideContent home, Ticket ticket) {
		super(key, context, home);
		mFragmentList = new ArrayList<Fragment>();
		this.mTicket = ticket;
	}

	@Override
	public boolean canIntercept(){
		return ((FoodMenuContentFragment)mFragmentList.get(0)).getCurrentViewIndex() == 0;
	}
	
	@Override
	public ArrayList<Fragment> getFragments(){
		if(mFragmentList.size() == 0){
			FoodMenuContentFragment fragment = new FoodMenuContentFragment();
			fragment.setContainer(this);
			mFragmentList.add(fragment);
		}
		return mFragmentList;
	}
	
	public void setTicket(Ticket ticket){
		mTicket = ticket;
	}
	
	public Ticket getTicket(){
		return this.mTicket;
	}
}
