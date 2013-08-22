package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import com.pitaya.bookingnow.app.util.ContentUtil;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class SlideContent extends RelativeLayout {
	
	private LeftMenuView mMenuView;
	private ContentView mContentView;
	private ArrayList<BaseContentView> contentViews;
	
	public SlideContent(Context context, int menuwidth, ArrayList<BaseContentView> views) {
		super(context);
		init(context, menuwidth);
		this.contentViews = views;
	}

	private void init(Context context, int menuwidth) {
		LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mMenuView = new LeftMenuView(context);
		mMenuView.setMenuWidth(ContentUtil.getPixelsByDP(menuwidth));
		addView(mMenuView, behindParams);
		
		LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mContentView = new ContentView(context);
		mContentView.setMenuWidth(ContentUtil.getPixelsByDP(menuwidth));
		addView(mContentView, contentParams);
	}
	
	public String getCurrentContentViewKey(){
		return this.mContentView.getCurrent();
	}
	
	public void selectItem(String key){
		String currentKey = this.mContentView.getCurrent();
		if(currentKey == null || currentKey.equals(key)){
			mContentView.toggle();
		} else {
			for(int i=0; i < this.contentViews.size(); i++){
				if(key.equals(this.contentViews.get(i).getKey())){
					this.setContent(this.contentViews.get(i));
					if(!mContentView.isMenuOFF()){
						mContentView.closeMenu();
					}
					break;
				}
			}
		}
	}

	public void setMenu(View v) {
		mMenuView.setView(v);
		this.setContent(getContentView("menu"));
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
		return this.contentViews.get(0);
	}
	
	public void showMenu() {
		mContentView.toggle();
	}
	
	private void setContent(BaseContentView v) {
		mContentView.setupView(v);
	}

}
