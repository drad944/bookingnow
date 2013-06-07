package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

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
	
	public boolean canIntercept(){
		return true;
	}
	
	public void setupView(ViewGroup container){
		//need to override in sub class
	}
	
	public boolean destroyView(ViewGroup container){
		//need to override in sub class
		return true;
	}
	
	public String getKey(){
		return this.key;
	}
	
}
