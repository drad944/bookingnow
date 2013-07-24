package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.*;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;

public class OrderContentView extends BaseContentView{
	
	private static final String TAG = "OrderContentView";
	private View mView;
	private OrderLeftView mLeftView;
	private int currentUserRole;
	
	public OrderContentView(String key, Context context, SlideContent home) {
		super(key, context, home);
	}
	
	@Override
	public boolean canIntercept(){
		return mLeftView.canInterrupt();
	}
	
	@Override
	public void setupView(ViewGroup container){
		if(mView == null){
			mView = View.inflate(this.mContext, R.layout.ordercontentview, null);
		}
		container.addView(mView);
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(mLeftView == null || currentUserRole != UserManager.getUserRole()){
			currentUserRole = UserManager.getUserRole();
			switch(UserManager.getUserRole()){
				case Constants.ROLE_WAITER:
					mView.findViewById(R.id.orderlist).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 0.6f));
					mView.findViewById(R.id.orderdetail).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 0.4f));
					mLeftView = new WaiterOrderLeftView();
					break;
				case Constants.ROLE_WELCOME:
					mView.findViewById(R.id.orderlist).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 0.55f));
					mView.findViewById(R.id.orderdetail).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 0.45f));
					mLeftView = new WelcomerOrderLeftView();
					break;
			}
		}
		if(mLeftView != null){
			mLeftView.setContainer(this);
		} else {
			Log.w(TAG, "Unsupported user role:" + UserManager.getUserRole());
			return;
		}
		fragmentTransaction.replace(R.id.orderlist, mLeftView);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	@Override
	public boolean destroyView(ViewGroup container){
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(mLeftView);
		if(((FragmentActivity)this.mContext).getSupportFragmentManager().findFragmentById(R.id.orderdetail) != null){
			fragmentTransaction.remove(((FragmentActivity)this.mContext).getSupportFragmentManager().findFragmentById(R.id.orderdetail));
		}
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
		return true;
	}
		
	public void openMenu(Order order){
		((FoodMenuContentView)this.home.getContentView("menu")).setOrder(order);
		this.home.selectItem("menu");
	}
}
