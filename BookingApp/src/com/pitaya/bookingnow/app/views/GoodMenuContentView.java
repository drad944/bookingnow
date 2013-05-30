package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;

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
public class GoodMenuContentView extends BaseContentView{

	private ArrayList<Fragment> mFragmentList;
	
	public GoodMenuContentView(String key, Context context) {
		super(key, context);
		this.mFragmentList = new ArrayList<Fragment>();
	}

	@Override
	public boolean canIntercept(){
		return ((GoodMenuContentFragment)mFragmentList.get(0)).getCurrentViewIndex() == 0;
	}
	
	@Override
	public ArrayList<Fragment> getFragments(){
		if(mFragmentList.size() == 0){
			mFragmentList.add(new GoodMenuContentFragment());
		}
		return this.mFragmentList;
	}
	
}
