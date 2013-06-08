package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pitaya.bookingnow.app.*;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.domain.Ticket.OnDirtyChangedListener;
import com.pitaya.bookingnow.app.service.DataService;

public class TicketDetailFragmentAdapter extends TicketDetailAdapter{
	
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
				actBtn1.setText(R.string.commit);
				actBtn2.setText(R.string.modification);
				actBtn3.setText(R.string.cancelticket);
				hinttext.setVisibility(View.GONE);
				actBtn1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						//TODO commit the ticket and remove it from local database
						DataService.removeTicket(mContext, mTicket.getTicketKey());
						mTicket.setStatus(Ticket.COMMITED);
						mTicket.markDirty(false);
						setViewByTicketStatus(itemView);
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
						
					}
					
				});
				break;
			case Ticket.COMMITED:
				hinttext.setVisibility(View.GONE);
				mTicket.setOnDirtyChangedListener(new OnDirtyChangedListener(){

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
							setViewByTicketStatus(itemView);
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
    		View itemView = View.inflate(mContext, R.layout.ticketbottom, null);
    		if(mTicket.getFoods().size() != 0){
    			((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mTicket.getTotalPrice()+"元");
    			this.setViewByTicketStatus(itemView);
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
