package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseContentView {
	
	protected View mView;
	protected Context mContext;
	protected SlideContent home;
	private String key;
	
	public BaseContentView(String key, Context context, SlideContent home){
		this.mContext = context;
		this.key = key;
		this.home = home;
	}
	
	public ArrayList<Fragment> getFragments(){
		return null;
	}
	
	public boolean canIntercept(){
		return true;
	}
	
	public String getKey(){
		return this.key;
	}
	
}
