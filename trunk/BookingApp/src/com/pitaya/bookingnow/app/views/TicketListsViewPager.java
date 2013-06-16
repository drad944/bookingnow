package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class TicketListsViewPager extends ViewPager{

	private int mTouchSlop =  ViewConfiguration.get(getContext()).getScaledTouchSlop();
	private float mLastMotionX;
	private TicketLeftView parentFragment; 
	
	public TicketListsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TicketListsViewPager(Context context) {
		super(context);
	}

	public void setParentFragment(TicketLeftView fragment){
		this.parentFragment = fragment;
	}
	
	@Override
	 public boolean onInterceptTouchEvent(MotionEvent ev) {
			final int action = ev.getAction();
			final float x = ev.getX();
			switch (action) {
		 		case MotionEvent.ACTION_DOWN:
					mLastMotionX = x;
					this.parentFragment.mIsTouched = true;
					break;
		 		case MotionEvent.ACTION_MOVE:
					boolean xMoved = (int) Math.abs(x - mLastMotionX) >= mTouchSlop;
					//boolean isLeft = x < mLastMotionX;
					if(xMoved){ //&& ( (isLeft && this.getCurrentItem() == 0 ) ||  this.getCurrentItem() > 0)
						return true;
					}
					break;
		 		case MotionEvent.ACTION_UP:
		 		case MotionEvent.ACTION_CANCEL:
		 			this.parentFragment.mIsTouched = false;
		 			break;
			}
			return super.onInterceptTouchEvent(ev);
	 }
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
			final int action = ev.getAction();
			switch (action) {
		 		case MotionEvent.ACTION_DOWN:
					this.parentFragment.mIsTouched = true;
					break;
		 		case MotionEvent.ACTION_UP:
		 		case MotionEvent.ACTION_CANCEL:
		 			this.parentFragment.mIsTouched = false;
		 			break;
			}
			return super.onTouchEvent(ev);
	}
	
}
