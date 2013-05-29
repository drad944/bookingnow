package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.view.View;

public class BaseContentView {
	
	protected View mView;
	protected Context mContext;
	
	public BaseContentView(Context context){
		this.mContext = context;
	}
	
	public View getView(){
		return null;
	}
	
	public boolean canIntercept(){
		return true;
	}
	
}
