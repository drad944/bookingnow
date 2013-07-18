package com.pitaya.bookingnow.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitaya.bookingnow.app.data.OrderDetailPreviewAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.Food;
import com.pitaya.bookingnow.app.service.DataService;
import java.util.Map.Entry;

public class OrderDetailPreviewActivity extends ListActivity  {

	private Order mOrder;
	private OrderDetailPreviewAdapter mOrderAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewGroup.LayoutParams lp =  this.getListView().getLayoutParams();
		lp.width = 700;
		this.getListView().setLayoutParams(lp);
		this.setView();
	}
	
	private void setView(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null && bundle.getSerializable("order") != null){
			mOrder = (Order)bundle.getSerializable("order");
	          
	        try {
				mOrderAdapter = new OrderDetailPreviewAdapter(this, this.getListView(), mOrder);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        setListAdapter(mOrderAdapter);
		} else {
			this.finish();
		}
	}
	
	@Override
	protected void onPause() {
		 super.onPause();
		 Intent intent = new Intent(this, HomeActivity.class);
		 Bundle bundle = new Bundle();
		 bundle.putSerializable("order", mOrder);
		 intent.putExtras(bundle);
		 startActivity(intent);
		 this.finish();
	}
	
	@Override
	protected void onDestroy(){
		Log.i("OrderDetailPreviewActivity", "in OrderDetailPreviewActivity destroy" + this);
		super.onDestroy();
		if(this.mOrder != null){
			this.mOrder.setOnDirtyChangedListener(null);
			this.mOrder.removeOnStatusChangedListeners();
			for(Entry<Food, Integer> entry : mOrder.getFoods().entrySet()){
				entry.getKey().setOnFoodStatusChangedListener(null);
			}
		}
	}
}
