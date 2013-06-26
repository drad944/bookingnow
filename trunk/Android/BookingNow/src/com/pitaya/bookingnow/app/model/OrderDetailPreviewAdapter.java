package com.pitaya.bookingnow.app.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.OrderDetailPreviewActivity;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookinnow.app.util.ToastUtil;

public class OrderDetailPreviewAdapter extends OrderDetailAdapter {

	public OrderDetailPreviewAdapter(Context c, View view, Order order)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, order);
	}
	
	private void setViewByOrderStatus(View view){
		if(view == null || this.mOrder == null){
			return;
		}
		final View itemView = view;
		if(mOrder.getFoods().size() == 0){
			((Button)itemView.findViewById(R.id.action1)).setVisibility(View.GONE);
			((Button)itemView.findViewById(R.id.action2)).setVisibility(View.GONE);
			((TextView)itemView.findViewById(R.id.summary)).setVisibility(View.GONE);
			((TextView)itemView.findViewById(R.id.hint)).setText(R.string.hintinmenu);
			return;
		}
		((TextView)itemView.findViewById(R.id.hint)).setVisibility(View.GONE);
		((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
		final Button confirmBtn = (Button)itemView.findViewById(R.id.action1);
		final Button resetBtn = (Button)itemView.findViewById(R.id.action2);
		switch(mOrder.getStatus()){
			case Order.NEW:
				confirmBtn.setText(R.string.commit);
				resetBtn.setText(R.string.reset);

				confirmBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
						//TODO commit to server
						//remove from local database
						//DataService.removeOrder(mContext, mOrder.getOrderKey());
						mOrder.setStatus(Order.COMMITED);
						mOrder.markDirty(false);
						ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.commitsuccess), Toast.LENGTH_SHORT);
						setViewByOrderStatus(itemView);
					}
					
				});
				
				resetBtn.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View v) {
						DataService.removeFoodsOfOrder(mContext, mOrder.getOrderKey());
						mOrder.removeAllFood();
						OrderDetailPreviewAdapter.this.notifyDataSetChanged();
					}
					
				});
				break;
			case Order.COMMITED:
				confirmBtn.setText(R.string.commitupdate);
				resetBtn.setText(R.string.cancelupdate);
				mOrder.setOnDirtyChangedListener(new Order.OnDirtyChangedListener(){

					@Override
					public void onDirtyChanged(Order order, boolean flag) {
						setViewByOrderStatus(itemView);
					}
					
				});
				if(mOrder.isDirty()){
					confirmBtn.setAlpha(1f);
					resetBtn.setAlpha(1f);
					confirmBtn.setClickable(true);
					resetBtn.setClickable(true);
					confirmBtn.setOnClickListener(new OnClickListener(){
						
						@Override
						public void onClick(View v) {
							//TODO update order to server
							mOrder.markDirty(false);
							ToastUtil.showToast(mContext, mContext.getResources().getString((R.string.commitsuccess)), Toast.LENGTH_SHORT);
							setViewByOrderStatus(itemView);
						}
						
					});
					
					resetBtn.setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							//TODO restore order content from server
							OrderDetailPreviewAdapter.this.notifyDataSetChanged();
						}
						
					});
				} else {
					confirmBtn.setAlpha(0.5f);
					resetBtn.setAlpha(0.5f);
					confirmBtn.setClickable(false);
					resetBtn.setClickable(false);
				}
				break;
		}
	}
    
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	if(getItemViewType(position) == FOOD_ITEM){
    		return super.getView(position, convertView, parent);
    	} else if(getItemViewType(position) == OPERATIONS){
	    	View itemView = View.inflate(mContext, R.layout.orderbottom, null);
	    	((Button)itemView.findViewById(R.id.action3)).setVisibility(View.GONE);
	    	setViewByOrderStatus(itemView);
			return itemView;
    	} else {
    		return null;
    	}
    }

}
