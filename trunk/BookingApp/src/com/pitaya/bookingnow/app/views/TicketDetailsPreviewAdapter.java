package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.TicketDetailPopUpActivity;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.service.DataService;

public class TicketDetailsPreviewAdapter extends TicketDetailsAdapter {

	public TicketDetailsPreviewAdapter(Context c, View view, Ticket ticket)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, ticket);
	}
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
	    	View itemView = View.inflate(mContext, R.layout.ticketbottom, null);
	    	((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
			if(mTicket.getFoods().size() != 0){
					Button confirmBtn = (Button)itemView.findViewById(R.id.action1);
					confirmBtn.setText("完成");
					Button resetBtn = (Button)itemView.findViewById(R.id.action2);
					resetBtn.setText("清空");
					((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mTicket.getTotalPrice()+"元");
					((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
					confirmBtn.setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							//save to local database
							if(mTicket.getStatus() == Ticket.NEW){
								DataService.saveNewTicket(mContext, mTicket);
								DataService.saveTicketDetails(mContext, mTicket);
							}
						}
						
					});
					
					resetBtn.setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							mTicket.removeAllFood();
							((TicketDetailPopUpActivity)mContext).finish();
						}
						
					});
			} else {
					((Button)itemView.findViewById(R.id.action1)).setVisibility(View.GONE);
					((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
					((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
					((TextView)itemView.findViewById(R.id.hint)).setText(R.string.hintinmenu);
			}
			return itemView;
    	} else {
    		return null;
    	}
    }

}
