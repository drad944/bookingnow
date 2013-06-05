package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;
import com.pitaya.bookingnow.app.R;

public class ContentView extends ViewGroup {

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	private final static String TAG = "ContentView";
	
	protected FrameLayout mContainer;
	protected Context context;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int SNAP_VELOCITY = 1000;
	private int menuWidth = 200;
	private BaseContentView mCurrentContentView;
	public int mTouchState = TOUCH_STATE_REST;

	public ContentView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public ContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContainer.layout(0, 0, width, height);
	}

	private void init() {
		mContainer = (FrameLayout) View.inflate(this.getContext(), R.layout.contentlayout, null);
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		super.addView(mContainer);
	}
	
	public String getCurrent(){
		if(this.mCurrentContentView != null){
			return this.mCurrentContentView.getKey();
		} else {
			return null;
		}
	}

	public void setupView(BaseContentView v) {
		FragmentManager fragmentManager = ((FragmentActivity)this.context).getSupportFragmentManager(); 
		FragmentManager.enableDebugLogging(true);
		if(v.getRendererType() == BaseContentView.VIEW){
			if(mCurrentContentView != null){
				if(mCurrentContentView.getRendererType() == BaseContentView.FRAGMENT){
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.remove(mCurrentContentView.getFragment());
					fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					fragmentTransaction.commit();
				} else {
					mContainer.removeAllViews();
				}
			}
			mContainer.addView(v.getView());
		} else if(v.getRendererType() == BaseContentView.FRAGMENT){
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			if(mCurrentContentView != null){
				if(mCurrentContentView.getRendererType() == BaseContentView.FRAGMENT){
					fragmentTransaction.remove(mCurrentContentView.getFragment());
				}else{
					mContainer.removeAllViews();
				}
			}
			fragmentTransaction.add(mContainer.getId(), v.getFragment());
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		} else if(mContainer.getChildCount() > 0){
			mContainer.removeAllViews();
		}
		this.mCurrentContentView = v;
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				invalidate();
				return;
			}
		}
	}

	
	@Override
	 public boolean onInterceptTouchEvent(MotionEvent ev) {
			final int action = ev.getAction();
			final float x = ev.getX();
			final float y = ev.getY();
			switch (action) {
		 		case MotionEvent.ACTION_DOWN:
						mLastMotionX = x;
						mLastMotionY = y;
						if(this.mCurrentContentView.canIntercept() && !this.isMenuOFF()){
							Log.e(TAG, "Catch the down action:" + this.mCurrentContentView.canIntercept() + " " +!this.isMenuOFF());
							return true;
						}
						break;
		 		case MotionEvent.ACTION_MOVE:
						boolean xMoved = (int) Math.abs(x - mLastMotionX) > mTouchSlop;
						boolean isRight = x > mLastMotionX;
						mLastMotionX = x;
						mLastMotionY = y;
						if(xMoved && this.mCurrentContentView.canIntercept() && isRight){
							Log.e(TAG, "Catch the move action:" + this.mCurrentContentView.canIntercept() + " " +isRight);

							return true;
						}
						break;
			}
			return false;
	 }

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//This is unexpected, because if the action can not be intercepted now, there should be no more action will be sent to here
		if(!this.mCurrentContentView.canIntercept())
			return false;
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				mLastMotionY = y;
				if (getScrollX() == -200 && mLastMotionX < 200) {
					return false;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionX);
				//final int yDiff = (int) Math.abs(y - mLastMotionY);
				final int touchSlop = mTouchSlop;
				boolean xMoved = xDiff > touchSlop;
				if (xMoved) {
					mTouchState = TOUCH_STATE_SCROLLING;
					enableChildrenCache();
				}
				if (mTouchState == TOUCH_STATE_SCROLLING) {
					final float deltaX = mLastMotionX - x;
					mLastMotionX = x;
					float oldScrollX = getScrollX();
					float scrollX = oldScrollX + deltaX;
					final float leftBound = 0;
					final float rightBound = -menuWidth;
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
					scrollTo((int) scrollX, getScrollY());
				}
				break;
			case MotionEvent.ACTION_UP:
				//if (mTouchState == TOUCH_STATE_SCROLLING) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				//int velocityX = (int) velocityTracker.getXVelocity();
				int oldScrollX = getScrollX();
				int dx = 0;
				if (oldScrollX < -100) {
					dx = -menuWidth - oldScrollX;
				} else {
					dx = -oldScrollX;
				}
				smoothScrollTo(dx);
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				mTouchState = TOUCH_STATE_REST;
				break;
			case MotionEvent.ACTION_CANCEL:
				mTouchState = TOUCH_STATE_REST;
		}

		return true;
	}

	private boolean isMenuOFF(){
		int oldScrollX = getScrollX();
		return oldScrollX == 0;
	}
	
	private boolean isMenuON(){
		int oldScrollX = getScrollX();
		return oldScrollX == -menuWidth;
	}
	
	public void toggle() {
		if (isMenuOFF()) {
			smoothScrollTo(-menuWidth);
		} else if (isMenuON()) {
			smoothScrollTo(menuWidth);
		}
	}

	void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = getScrollX();
		mScroller.startScroll(oldScrollX, getScrollY(), dx, getScrollY(),
				duration);
		invalidate();
	}

	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

}
