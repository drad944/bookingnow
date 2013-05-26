package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.R;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class LeftMenuView extends ViewGroup {

	private FrameLayout mContainer;
	
	public LeftMenuView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mContainer = new FrameLayout(getContext());
		super.addView(mContainer);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);
		final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0,
				height);
		final int menuWidth = getChildMeasureSpec(widthMeasureSpec, 0,
				mContainer.getWidth());
		Log.e("ad", "menuWidth----------------" + menuWidth);
		Log.e("ad", "contentHeight----------------" + contentHeight);
		mContainer.measure(menuWidth, contentHeight);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContainer.layout(0, 0, 200, height);		
	}

	public void setView(View v) {
		if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		mContainer.addView(v);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View child;
		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);
			child.setFocusable(true);
			child.setClickable(true);
		}
	}
	
}
