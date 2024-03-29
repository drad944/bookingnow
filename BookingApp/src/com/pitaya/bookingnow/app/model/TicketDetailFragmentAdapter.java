package com.pitaya.bookingnow.app.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pitaya.bookingnow.app.*;
import com.pitaya.bookingnow.app.model.Ticket.OnTicketStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.HttpService;
import com.pitaya.bookingnow.app.views.TicketDetailFragment;
import com.pitaya.bookingnow.message.BaseResultMessage;
import com.pitaya.bookinnow.app.util.ToastUtil;

public class TicketDetailFragmentAdapter extends TicketDetailAdapter{
	
	private static final String TAG = "TicketDetailFragmentAdapter";
	
	public TicketDetailFragmentAdapter(Context c, View view, Ticket ticket)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, ticket);
	}
	
	public void setTicket(Ticket ticket){
		this.mTicket = ticket;
	}
	
	private void setViewByTicketStatus(View view){
		if(view == null || this.mTicket == null){
			return;
		}
		final View itemView = view;
		final Button actBtn1 = (Button)itemView.findViewById(R.id.action1);
		final Button actBtn2 = (Button)itemView.findViewById(R.id.action2);
		final Button actBtn3 = (Button)itemView.findViewById(R.id.action3);
		TextView hinttext = ((TextView)itemView.findViewById(R.id.hint));
		switch(this.mTicket.getStatus()){
			case Ticket.NEW:
				this.mTicket.setOnDirtyChangedListener(null);
				actBtn1.setText(R.string.commit);
				actBtn2.setText(R.string.modification);
				actBtn3.setText(R.string.cancelticket);
				hinttext.setVisibility(View.GONE);
				actBtn1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO commit the ticket and remove it from local database
						JSONObject orderDetail = new JSONObject();
						JSONObject orderJson = new JSONObject();
						try {
							orderDetail.put("order_id", "123123123");
							orderJson.put("order", orderDetail);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						HttpService.setUrl("http://192.168.0.102:18080/Booking/commitOrder.action");
						try {
							HttpService.post(new StringEntity(orderJson.toString()), new Handler(){
							    
								@Override  
							    public void handleMessage(Message msg) {
							        super.handleMessage(msg);
							        Bundle bundle = msg.getData();  
							        String result =bundle.getString("result");
									try {
										Log.i(TAG, result);
									} catch (ParseException e) {
										e.printStackTrace();
									}
									//DataService.removeTicket(mContext, mTicket.getTicketKey());
									ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
									mTicket.markDirty(false);
									mTicket.setStatus(Ticket.COMMITED);
							    }
							});
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					
				});
				actBtn2.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View v) {
						((TicketDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
								.findFragmentById(R.id.ticketdetail)).modifyTicket();
					}
					
				});
				actBtn3.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO send request to cancel a ticket and remove it from local database
						DataService.removeTicket(mContext, mTicket.getTicketKey());
					}
					
				});
				break;
			case Ticket.COMMITED:
				hinttext.setVisibility(View.GONE);
				mTicket.setOnDirtyChangedListener(new Ticket.OnDirtyChangedListener(){

					@Override
					public void onDirtyChanged(Ticket ticket, boolean flag) {
						setViewByTicketStatus(itemView);
					}
					
				});
				if(this.mTicket.isDirty()){
    				actBtn1.setText(R.string.commitupdate);
    				actBtn2.setText(R.string.cancelupdate);
    				actBtn3.setVisibility(View.GONE);
    				actBtn1.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO update the ticket to server
							ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
							mTicket.markDirty(false);
						}
    					
    				});
    				actBtn2.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO send request to get the ticket info again and restore mTicket
							TicketDetailFragmentAdapter.this.notifyDataSetChanged();
						}
    					
    				});
				} else {
    				actBtn1.setText(R.string.pay);
    				actBtn2.setText(R.string.modification);
    				actBtn3.setText(R.string.cancelticket);
    				actBtn3.setVisibility(View.VISIBLE);
    				actBtn1.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO request to pay for the ticket
							mTicket.setStatus(Ticket.PAYING);
						}
    					
    				});
    				actBtn2.setOnClickListener(new OnClickListener(){
    					
						@Override
						public void onClick(View v) {
							((TicketDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
									.findFragmentById(R.id.ticketdetail)).modifyTicket();
						}
    					
    				});
    				actBtn3.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							//TODO send request to cancel a ticket, if any food is in cooking, this will fail
						}
    					
    				});
				}
				break;
			case Ticket.PAYING:
				actBtn1.setVisibility(View.GONE);
				actBtn2.setVisibility(View.GONE);
				actBtn3.setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.VISIBLE);
				((TextView)itemView.findViewById(R.id.hint)).setText(R.string.paying);
				break;
			case Ticket.FINISHED:
				actBtn1.setVisibility(View.GONE);
				actBtn2.setVisibility(View.GONE);
				actBtn3.setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.VISIBLE);
				((TextView)itemView.findViewById(R.id.hint)).setText(R.string.finish);
				break;
		};
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
    		final View itemView = View.inflate(mContext, R.layout.ticketbottom, null);
    		if(mTicket.getFoods().size() != 0){
    			((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mTicket.getTotalPrice()+"元");
    			this.setViewByTicketStatus(itemView);
    			mTicket.setOnStatusChangedListener(new OnTicketStatusChangedListener(){

					@Override
					public void onTicketStatusChanged(Ticket tikcet, int status) {
						setViewByTicketStatus(itemView);
					}
    				
    			});
    		} else {
    			Button act1Btn = ((Button)itemView.findViewById(R.id.action1));
    			act1Btn.setText(R.string.hintinticket);
    			act1Btn.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						((TicketDetailFragment)((HomeActivity)mContext).getSupportFragmentManager()
								.findFragmentById(R.id.ticketdetail)).modifyTicket();
					}
    				
    			});
    			((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
				((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
				((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
    		}
    		return itemView;
    	} else {
    		return null;
    	}
	}
    	
}
