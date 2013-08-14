package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.OrderListAdapter;
import com.pitaya.bookingnow.app.data.TablesAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WaitingOrderListView extends OrderListView{

	private WaiterOrderLeftView mParentView;
	
	public WaitingOrderListView(Context context, WaiterOrderLeftView parent) {
		super(context);
		this.mParentView = parent;
	}
	
	public WaitingOrderListView(Context context, AttributeSet attrs,
			WaiterOrderLeftView parent) {
		super(context, attrs);
		this.mParentView = parent;
	}

    @Override
    public void setupViews(){
    	super.setupViews();
		((TextView)this.mHeaderView.findViewById(R.id.title1)).setText(R.string.order_list_title_customer);
		((TextView)this.mHeaderView.findViewById(R.id.title2)).setText(R.string.order_list_title_phone);
		((TextView)this.mHeaderView.findViewById(R.id.title3)).setText(R.string.order_list_title_count);
		((TextView)this.mHeaderView.findViewById(R.id.title4)).setText(R.string.order_list_title_status);
		((TextView)this.mHeaderView.findViewById(R.id.title5)).setText(R.string.order_list_title_submittime);
		
		final TablesView tablesView = new TablesView(this.getContext(), this.mListView);
		tablesView.setOnConfirmListener(new TablesView.OnConfirmListener() {
			
			@Override
			public void onConfirm(ArrayList<Table> selectedTables) {
				if(WaitingOrderListView.this.mAdapter == null){
					return;
				}
				final ArrayList<Table> tables = selectedTables;
				Integer currentItem = WaitingOrderListView.this.mAdapter.getSelectItem();
				if(currentItem == null){
					return;
				}
				Order selectedOrder = WaitingOrderListView.this.mAdapter.getOrderList().get(currentItem);
				if(selectedOrder != null){
					selectedOrder.setUserId(UserManager.getUserId());
					selectedOrder.setTables(tables);
					OrderService.commitWaitingOrder(selectedOrder, new HttpHandler(){
						
						@Override
						public void onSuccess(String action, String response) {
							try {
								JSONObject jresp = new JSONObject(response);
								if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
									//TODO handle fail
									return;
								} else {
									tablesView.close();
									Long user_id = jresp.getJSONObject("user").getLong("id");
									String username = jresp.getJSONObject("user").getString("name");
									Long order_id = jresp.getLong("id");
									Long timestamp =  jresp.getLong("modifyTime");
									Long submit_ts = jresp.getLong("submit_time");
									int status = jresp.getInt("status");
									Order order = new Order(tables, order_id, user_id, username, timestamp);
									order.setStatus(status);
									order.setSubmitTime(submit_ts);
									DataService.saveNewOrder(getContext(), order);
									if(status == Constants.ORDER_COMMITED){
										JSONArray jorder_details = jresp.getJSONArray("food_details");
										for(int i=0; i < jorder_details.length(); i++){
											JSONObject jorder_detail = jorder_details.getJSONObject(i);
											JSONObject jfood = jorder_detail.getJSONObject("food");
											Order.Food food = order.new Food(String.valueOf(jfood.getLong("id")), null, 0f);
											food.setId(jorder_detail.getLong("id"));
											food.setFree(jorder_detail.getBoolean("isFree"));
											order.addFood(food, jorder_detail.getInt("count"));
										}
										DataService.saveOrderDetails(getContext(), order);
									}
									mParentView.moveOrderToMineList(order);
									mParentView.showOrderDetail(order, true, WaiterOrderLeftView.MYORDERS);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFail(String action, int statuscode) {
							Log.e(TAG, "[OrderService.submitOrder] Network error:" + statuscode);
						}
					});
				}
			}
			
		});
		
		final Button addToMineListBtn = (Button)this.mControlbar.findViewById(R.id.optbtn1);
		addToMineListBtn.setText(R.string.commitwaitingorderbtn);
		addToMineListBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				tablesView.popup(addToMineListBtn);
			}
			
		});
		
		Button orderSearchBtn = (Button)this.mControlbar.findViewById(R.id.optbtn2);
		orderSearchBtn.setText(R.string.orderssearchbtn);
    }
    
    @Override
    protected ArrayList<Integer> getOrderStatus(){
		ArrayList<Integer> orderStatus = new ArrayList<Integer>();
		orderStatus.add(Constants.ORDER_WELCOMER_NEW);
		orderStatus.add(Constants.ORDER_WAITING);
		return orderStatus;
    }
    
    @Override
    protected boolean isGetOrderByUser(){
    	return false;
    }
    
    @Override
    protected void onGetOrders(JSONObject jresp){
		JSONArray jorders;
		try {
			jorders = jresp.getJSONArray("result");
			for(int i=0; i < jorders.length(); i++){
				JSONObject jorder = jorders.getJSONObject(i);
				
				Long order_id = jorder.getLong("id");
				Long user_id = jorder.getJSONObject("user").getLong("id");
				String username = jorder.getJSONObject("user").getString("name");
				Long modifyTime = jorder.getLong("modifyTime");
				String customer = jorder.getJSONObject("customer").getString("name");
				String phone = jorder.getJSONObject("customer").getString("phone");
				int count = jorder.getInt("customer_count");

				Order order = new Order(order_id, user_id, username, phone, customer, count, modifyTime);
				order.setStatus(jorder.getInt("status"));
				order.setSubmitTime(jorder.getLong("submit_time"));
				int j = 0;
				while(j < mOrderList.size() && mOrderList.get(j).getSubmitTime() <= order.getSubmitTime()){
					j++;
				}
				mOrderList.add(j, order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "[onGetOrders] Fail to read the results");
			return;
		}

		try {
			mAdapter = new OrderListAdapter(this.getContext(), this.mListView){
					
				private void updateOrderStatus(int position, int status){
					View view = ((ListView)mView).getChildAt(position);
					if(view != null && (TextView)view.findViewById(R.id.info4) != null){
						((TextView)view.findViewById(R.id.info4)).setText(Order.getOrderStatusString(status));
					}
				}
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = convertView;
					final int index = position;
					Order order = orderlist.get(position);
					if(view == null){
						view = View.inflate(parent.getContext(), R.layout.orderinfoview, null);
					}
					if(this.selectItem != null && this.selectItem == position){
						view.setBackgroundColor(mContext.getResources().getColor(R.color.common_background));
					} else {
						view.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
					}
	
					((TextView)view.findViewById(R.id.info1)).setText(order.getCustomerName());
					((TextView)view.findViewById(R.id.info2)).setText(order.getPhoneNumber());
					((TextView)view.findViewById(R.id.info3)).setText(String.valueOf(order.getPeopleCount()));
					((TextView)view.findViewById(R.id.info4)).setText(Order.getOrderStatusString(order.getStatus()));
					order.addOnStatusChangedListener(new OnOrderStatusChangedListener(){
	
						@Override
						public void onOrderStatusChanged(Order order, int status) {
							 updateOrderStatus(index, status);
						}
						
					});
					SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm:ss"); 
					Date date = new Date();
					date.setTime(order.getSubmitTime());
					((TextView)view.findViewById(R.id.info5)).setText(dateFm.format(date));
					return view;
				}
				
			};
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		mAdapter.setOrderList(this.mOrderList);
		this.mListView.setAdapter(mAdapter);
        this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mParentView.showOrderDetail(mAdapter.getOrderList().get(position), false, WaiterOrderLeftView.WAITING_ORDERS);
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetInvalidated();
			}
        	
        });
		

	}
	
}