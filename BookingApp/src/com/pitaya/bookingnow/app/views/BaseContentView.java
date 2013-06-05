package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public class BaseContentView {
	
	public static final int VIEW = 0;
	public static final int FRAGMENT = 1;
	protected View mView;
	protected Context mContext;
	protected SlideContent home;
	private int rendererType;
	private String key;
	
	public BaseContentView(int type, String key, Context context, SlideContent home){
		this.mContext = context;
		this.key = key;
		this.home = home;
		this.rendererType = type;
	}
	
	public View getView(){
		return null;
	}
	
	public Fragment getFragment(){
		return null;
	}
	
	public int getRendererType(){
		return this.rendererType;
	}
	
	public boolean canIntercept(){
		return true;
	}
	
	public String getKey(){
		return this.key;
	}
	
}
