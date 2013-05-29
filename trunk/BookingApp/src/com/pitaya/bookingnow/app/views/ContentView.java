package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class ContentView extends ViewGroup {

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;
	
	protected FrameLayout mContainer;
	protected Context context;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int SNAP_VELOCITY = 1000;
	private int menuWidth = 200;
	private BaseContentView mContentView;
	private boolean isMenuOFFWhenTouch = true;
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
		mContainer = new FrameLayout(getContext());
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		super.addView(mContainer);
	}

	public void setupView(BaseContentView v) {
		if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		this.mContentView = v;
		mContainer.addView(v.getView());
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
						break;
		 		case MotionEvent.ACTION_MOVE:
						final int xDiff = (int) Math.abs(x - mLastMotionX);
						final int touchSlop = mTouchSlop;
						boolean xMoved = xDiff > touchSlop;
						if(xMoved && this.mContentView.canIntercept()){
							if(x - mLastMotionX > 0){
								return true;
							} else if(!this.isMenuOFF()){
								return true;
							}
						}
						break;
			}
			return false;
	 }

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

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
				Log.e("ad", "onTouchEvent ACTION_DOWN");
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastMotionX);
				final int yDiff = (int) Math.abs(y - mLastMotionY);
				final int touchSlop = mTouchSlop;
				boolean xMoved = xDiff > touchSlop;
				if (xMoved) {
					mTouchState = TOUCH_STATE_SCROLLING;
					//Log.e("ad", "onTouchEvent  ACTION_MOVE");
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
					Log.e("ad", "onTouchEvent ACTION_MOVE");
				}
				
				break;
			case MotionEvent.ACTION_UP:
				if (mTouchState == TOUCH_STATE_SCROLLING) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					velocityTracker.computeCurrentVelocity(1000);
					int velocityX = (int) velocityTracker.getXVelocity();
					int oldScrollX = getScrollX();
					Log.e("ad", "oldScrollX  ==  " + oldScrollX);
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
				}
				Log.e("ad", "onTouchEvent ACTION_UP");
				mTouchState = TOUCH_STATE_REST;
				break;
			case MotionEvent.ACTION_CANCEL:
				Log.e("ad", "ACTION_CANCEL");
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
