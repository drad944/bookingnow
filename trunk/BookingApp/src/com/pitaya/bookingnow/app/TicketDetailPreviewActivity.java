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

import com.pitaya.bookingnow.app.model.Ticket;
import com.pitaya.bookingnow.app.model.TicketDetailPreviewAdapter;
import com.pitaya.bookingnow.app.model.Ticket.Food;
import com.pitaya.bookingnow.app.service.DataService;
import java.util.Map.Entry;

public class TicketDetailPreviewActivity extends ListActivity  {

	private Ticket mTicket;
	private TicketDetailPreviewAdapter mTicketAdapter;
	
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
		if(bundle != null && bundle.getSerializable("ticket") != null){
			mTicket = (Ticket)bundle.getSerializable("ticket");
	          
	        try {
				mTicketAdapter = new TicketDetailPreviewAdapter(this, this.getListView(), mTicket);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        setListAdapter(mTicketAdapter);
		} else {
			this.finish();
		}
	}
	
	@Override
	protected void onPause() {
		 super.onPause();
		 Intent intent = new Intent(this, HomeActivity.class);
		 Bundle bundle = new Bundle();
		 bundle.putSerializable("ticket", mTicket);
		 intent.putExtras(bundle);
		 startActivity(intent);
		 this.finish();
	}
	
	@Override
	protected void onDestroy(){
		Log.i("TicketDetailPreviewActivity", "in TicketDetailPreviewActivity destroy" + this);
		super.onDestroy();
		if(this.mTicket != null){
			this.mTicket.setOnDirtyChangedListener(null);
			this.mTicket.setOnStatusChangedListener(null);
			for(Entry<Food, Integer> entry : mTicket.getFoods().entrySet()){
				entry.getKey().setOnFoodStatusChangedListener(null);
			}
		}
	}
}
