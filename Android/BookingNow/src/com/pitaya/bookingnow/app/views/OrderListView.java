package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.OrderListAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ContentUtil;

public class OrderListView extends RelativeLayout{
	
	protected static String TAG = "OrderListView";
	
	protected View mHeaderView;
	protected ListView mListView;
	protected View mControlbar;
	protected ArrayList<Order> mOrderList;
	protected OrderListAdapter mAdapter;

    public OrderListView(Context context){
        super(context);
        mOrderList = new ArrayList<Order>();
    }
      
    public OrderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mOrderList = new ArrayList<Order>();
    }
    
    public void recycle(){}
    
    public void refresh(){
    	this.mAdapter.notifyDataSetChanged();
    }
    
    public void setupViews(){
    	this.mHeaderView = View.inflate(this.getContext(), R.layout.orderlistheader, null);
    	this.mHeaderView.setId(1);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ContentUtil.getPixelsByDP(40));
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(this.mHeaderView, lp);
		
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.mControlbar = View.inflate(this.getContext(), R.layout.ordercontrolbar, null);
        this.mControlbar.setId(2);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(this.mControlbar, lp);

        this.mListView = new ListView(this.getContext());
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, this.mHeaderView.getId());
        lp.addRule(RelativeLayout.ABOVE, this.mControlbar.getId());
        addView(mListView, lp);
        
        
        OrderService.getOrderByStatus(getContext(), this.isGetOrderByUser(), this.getOrderStatus(), new HttpHandler(){
			@Override
			public void onSuccess(String action, String response) {
				try {
					JSONObject jresp = new JSONObject(response);
					if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
						//TODO handle fail

					} else {
						OrderListView.this.onGetOrders(jresp);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFail(String action, int statuscode) {
				Log.e(TAG, "[OrderService.getOrderByStatus] Network error:" + statuscode);
			}
		});
    }
    
    public ArrayList<Order> getOrderList(){
    	return this.mOrderList;
    }
    
    protected void onGetOrders(JSONObject jresp){
    }

    protected ArrayList<Integer> getOrderStatus(){
		return null;
    }
    
    protected boolean isGetOrderByUser(){
    	return true;
    }
}
