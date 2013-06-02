package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class SlideContent extends RelativeLayout {
	
	private LeftMenuView mMenuView;
	private ContentView mContentView;
	private ArrayList<BaseContentView> contentViews;
	
	public SlideContent(Context context, ArrayList<BaseContentView> views) {
		super(context);
		this.contentViews = views;
		init(context);
	}

	private void init(Context context) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mMenuView = new LeftMenuView(context);
		addView(mMenuView, behindParams);
		
		LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mContentView = new ContentView(context);
		addView(mContentView, contentParams);
	}
	
	public void selectItem(String key){
		String currentKey = this.mContentView.getCurrent();
		if(currentKey == null || currentKey.equals(key)){
			mContentView.toggle();
			mContentView.invalidate();
			return;
		}
		for(int i=0; i < this.contentViews.size(); i++){
			if(key.equals(this.contentViews.get(i).getKey())){
				this.setContent(this.contentViews.get(i));
				mContentView.toggle();
				mContentView.invalidate();
				break;
			}
		}
	}

	public void setMenu(View v) {
		mMenuView.setView(v);
		mMenuView.invalidate();
		this.setContent(this.contentViews.get(0));
	}
	
	public void updateContentViews(ArrayList<BaseContentView> views){
		this.contentViews = views;
	}
	
	public BaseContentView getContentView(String key){
		for(int i=0; i < this.contentViews.size(); i++){
			if(key.equals(this.contentViews.get(i).getKey())){
				return this.contentViews.get(i);
			}
		}
		return null;
	}
	
	private void setContent(BaseContentView v) {
		mContentView.setupView(v);
		mContentView.invalidate();
	}
	
	public void showMenu() {
		mContentView.toggle();
	}
	
}
