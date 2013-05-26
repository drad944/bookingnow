package com.pitaya.bookingnow.app.views;

import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class SlideContent extends RelativeLayout {
	
	private LeftMenuView mMenuView;
	private ContentView mContentView;
	private Map<Integer, ContentView> contentViews;
	
	public SlideContent(Context context, Map<Integer, ContentView> views) {
		super(context);
		this.contentViews = views;
		init(context);
	}

	private void init(Context context) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mMenuView = new LeftMenuView(context);
		addView(mMenuView, behindParams);

//		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT);
//		mContentView = new ContentView(context);
//		addView(mContentView, aboveParams);
	}
	
	public void selectItem(int index){
		this.setContent(this.contentViews.get(index));
	}

	public void setMenu(View v) {
		mMenuView.setView(v);
		mMenuView.invalidate();
		this.setContent(this.contentViews.get(0));
	}

	private void setContent(ContentView v) {
		this.removeView(mContentView);
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
		LayoutParams.MATCH_PARENT);
		mContentView = v;
		mContentView.setupView();
		addView(mContentView, aboveParams);
	}
	
	public void showMenu() {
		mContentView.toggle();
	}
	
}
