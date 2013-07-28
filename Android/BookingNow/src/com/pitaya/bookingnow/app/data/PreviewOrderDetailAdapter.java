package com.pitaya.bookingnow.app.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;

public class PreviewOrderDetailAdapter extends OrderDetailAdapter {

	public PreviewOrderDetailAdapter(Context c, View view, Order order)
			throws IllegalArgumentException, IllegalAccessException {
		super(c, view, order);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = convertView;
		if(getItemViewType(position) == FOOD_ITEM){
			if(itemView == null){
				itemView = View.inflate(mContext, R.layout.fooditem_preview, null);
			}
			final Item item = (Item)this.getItem(position);
			Order.Food food = item.food;
			int quantity = item.quantity;
			
			LinearLayout fooditemRL = (LinearLayout) itemView.findViewById(R.id.fooditemdetail);
	        TextView nameText = (TextView) fooditemRL.findViewById(R.id.name);
	        nameText.setText(food.getName());
	        TextView priceText = (TextView) fooditemRL.findViewById(R.id.price);
	        priceText.setText(food.getPrice() + "元/份");
	        TextView quantityText = (TextView) fooditemRL.findViewById(R.id.quantity);
	        quantityText.setText(quantity + "份");
	        final TextView totalPriceText = (TextView) fooditemRL.findViewById(R.id.totalprice);
	        if(food.isFree()){
	        	totalPriceText.setText("0元");
	        } else {
	        	totalPriceText.setText(String.valueOf(food.getPrice() * quantity) + "元");
	        }
	        
    	} else if(getItemViewType(position) == OPERATIONS){
    		if(itemView == null){
    			itemView = View.inflate(mContext, R.layout.orderbottom, null);
    		}
    		itemView.findViewById(R.id.action1).setVisibility(View.GONE);
    		itemView.findViewById(R.id.action2).setVisibility(View.GONE);
    		itemView.findViewById(R.id.action3).setVisibility(View.GONE);
    		if(mOrder.getFoods().size() != 0){
    			((TextView)itemView.findViewById(R.id.summary)).setText("合计"+mOrder.getTotalPrice()+"元");
    		} else {
    			((TextView)itemView.findViewById(R.id.hint)).setText("该订单尚未点餐");
    			itemView.findViewById(R.id.summary).setVisibility(View.GONE);
    		}
    		return itemView;
    	}
		return itemView;
	}
}
