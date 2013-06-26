package com.pitaya.bookingnow.app.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;

public class WelcomerOrderLeftView extends OrderLeftView{
	
	public WelcomerOrderLeftView(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.orderleftview4welcomer, null);
		
		final View seatsSearchPopupView = inflater.inflate(R.layout.seatssearchwindow, null);
		final PopupWindow popupWindow =  new PopupWindow(seatsSearchPopupView, 300,  
               200 , true);
		popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.common_background));
		final View bookingLayout = seatsSearchPopupView.findViewById(R.id.bookinglayout);
		final View searchLayout = seatsSearchPopupView.findViewById(R.id.searchlayout);
		final View seatavailableInfo = seatsSearchPopupView.findViewById(R.id.seatavailable);
		Button searchBtn = (Button)seatsSearchPopupView.findViewById(R.id.search);
		searchBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//send request to server;
				boolean seatAvailable = false;
				if(seatAvailable){
					seatavailableInfo.setVisibility(View.VISIBLE);
				} else {
					searchLayout.setVisibility(View.GONE);
					bookingLayout.setVisibility(View.VISIBLE);
				}
			}
			
		});
		
		((Button)bookingLayout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
		          bookingLayout.setVisibility(View.GONE);
		          seatavailableInfo.setVisibility(View.GONE);
		          searchLayout.setVisibility(View.VISIBLE);
			}
			
		});
		
		((Button)bookingLayout.findViewById(R.id.bookingbtn)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
			}
			
		});
		
		
		Button openSearchBtn = (Button)view.findViewById(R.id.searchseatsbtn);
		openSearchBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
		          popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		          bookingLayout.setVisibility(View.GONE);
		          seatavailableInfo.setVisibility(View.GONE);
		          searchLayout.setVisibility(View.VISIBLE);
			}
			
		});
		return view;
	}
	
	public void showOrderDetail(Order order, boolean isForce){
        // Check what fragment is currently shown, replace if needed.
        String key = order.getOrderKey();
        OrderDetailFragment details = (OrderDetailFragment)getFragmentManager().findFragmentById(R.id.orderdetail);
        if (details == null || isForce || !details.getShownOrder().getOrderKey().equals(key)) {
        	//TODO if order is new, get it from database, otherwise get it from server
        	DataService.getFoodsOfOrder(this.getActivity(), order);
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.orderdetail, OrderDetailFragment.newInstance(order, mContentContainer));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
}
