package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.TicketDetailPreviewActivity;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.domain.Ticket.OnDirtyChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookinnow.app.util.ToastUtil;

public class TicketDetailPreviewAdapter extends TicketDetailAdapter {

	public TicketDetailPreviewAdapter(Context c, View view, Ticket ticket)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, ticket);
	}
	
	private void setViewByTicketStatus(View view){
		if(view == null || this.mTicket == null){
			return;
		}
		final View itemView = view;
		if(mTicket.getFoods().size() == 0){
			((Button)itemView.findViewById(R.id.action1)).setVisibility(View.GONE);
			((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
			((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
			((TextView)itemView.findViewById(R.id.hint)).setText(R.string.hintinmenu);
			return;
		}
		((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mTicket.getTotalPrice()+"元");
		((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
		final Button confirmBtn = (Button)itemView.findViewById(R.id.action1);
		final Button resetBtn = (Button)itemView.findViewById(R.id.action2);
		switch(mTicket.getStatus()){
			case Ticket.NEW:
				confirmBtn.setText(R.string.commit);
				resetBtn.setText(R.string.reset);

				confirmBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
						//TODO commit to server
						//remove from local database
						DataService.removeTicket(mContext, mTicket.getTicketKey());
						mTicket.setStatus(Ticket.COMMITED);
						mTicket.markDirty(false);
						ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
						setViewByTicketStatus(itemView);
					}
					
				});
				
				resetBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
						mTicket.removeAllFood();
						DataService.removeFoodsOfTicket(mContext, mTicket.getTicketKey());
						((TicketDetailPreviewActivity)mContext).finish();
					}
					
				});
				break;
			case Ticket.COMMITED:
				confirmBtn.setText(R.string.commitupdate);
				resetBtn.setText(R.string.cancelupdate);
				mTicket.setOnDirtyChangedListener(new OnDirtyChangedListener(){

					@Override
					public void onDirtyChanged(Ticket ticket, boolean flag) {
						setViewByTicketStatus(itemView);
					}
					
				});
				if(mTicket.isDirty()){
					confirmBtn.setAlpha(1f);
					confirmBtn.setClickable(true);
					resetBtn.setAlpha(1f);
					resetBtn.setClickable(true);
					confirmBtn.setOnClickListener(new OnClickListener(){
						
						@Override
						public void onClick(View v) {
							//TODO update ticket to server
							mTicket.markDirty(false);
							ToastUtil.showToast(mContext, mContext.getResources().getString((R.string.commitsuccess)), Toast.LENGTH_SHORT);
							setViewByTicketStatus(itemView);
						}
						
					});
					
					resetBtn.setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							//TODO restore ticket content from server
							((TicketDetailPreviewActivity)mContext).finish();
						}
						
					});
				} else {
					confirmBtn.setClickable(false);
					confirmBtn.setAlpha(0.5f);
					resetBtn.setClickable(false);
					resetBtn.setAlpha(0.5f);
				}
				break;
		}
	}
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
	    	View itemView = View.inflate(mContext, R.layout.ticketbottom, null);
	    	((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
	    	setViewByTicketStatus(itemView);
			return itemView;
    	} else {
    		return null;
    	}
    }

}
