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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.domain.Ticket.Food;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.views.TicketDetailsPreviewAdapter;
import java.util.Map.Entry;

public class TicketDetailPopUpActivity extends ListActivity  {

	private Ticket mTicket;
	private TicketDetailsPreviewAdapter mTicketAdapter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setView();
	}
	
	private void setView(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle != null && bundle.getSerializable("ticket") != null){
			mTicket = (Ticket)bundle.getSerializable("ticket");
	          
	        try {
				mTicketAdapter = new TicketDetailsPreviewAdapter(this, this.getListView(), mTicket);
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
	}
		

}
