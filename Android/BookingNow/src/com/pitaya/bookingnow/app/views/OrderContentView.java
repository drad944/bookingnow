package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.*;
import com.pitaya.bookingnow.app.service.RoleManager;

public class OrderContentView extends BaseContentView{
	
	private View mView;
	private OrderLeftView mLeftView;
	
	public OrderContentView(String key, Context context, SlideContent home) {
		super(key, context, home);
	}
	
	@Override
	public boolean canIntercept(){
		return mLeftView.canInterrupt();
	}
	
	@Override
	public void setupView(ViewGroup container){
		mView = View.inflate(this.mContext, R.layout.ordercontentview, null);
		FragmentManager fragmentManager = ((FragmentActivity)this.mContext).getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(mLeftView == null){
			switch(RoleManager.getRole()){
				case RoleManager.WAITER:
					mLeftView = new WaiterOrderLeftView();
					break;
				case RoleManager.WELCOMER:
					mLeftView = new WelcomerOrderLeftView();
			}
			mLeftView.setContainer(this);
		}
		fragmentTransaction.replace(R.id.orderlist, mLeftView);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
		container.addView(mView);
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
