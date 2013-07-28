package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.OrderListAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WelcomerOrderListView extends OrderListView{
	
	private static String TAG = "WelcomerOrderListView";
	private WelcomerOrderLeftView mParentView;

    public WelcomerOrderListView(Context context, WelcomerOrderLeftView parent){
        super(context);
        this.mParentView = parent;
    }
	
	public WelcomerOrderListView(Context context, AttributeSet attrs,
			WelcomerOrderLeftView parent) {
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
		
		final View seatsSearchPopupView = View.inflate(this.getContext(), R.layout.seatssearchwindow, null);
		final PopupWindow popupWindow =  new PopupWindow(seatsSearchPopupView, 300,  
               200 , true);
		popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.common_background));
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        
		final View bookingLayout = seatsSearchPopupView.findViewById(R.id.bookinglayout);
		final View searchLayout = seatsSearchPopupView.findViewById(R.id.searchlayout);
		final View seatavailableInfo = seatsSearchPopupView.findViewById(R.id.seatavailable);
		Button searchBtn = (Button)seatsSearchPopupView.findViewById(R.id.search);
		searchBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//send request to server;
				EditText peoplecountview = (EditText)seatsSearchPopupView.findViewById(R.id.peoplecount);
				final int customercount = Integer.parseInt(peoplecountview.getText().toString());
				OrderService.getAvailableTables(Constants.TABLE_EMPTY, new HttpHandler(){
					
					public void onSuccess(String action, String response){
						try {
							JSONObject jresp = new JSONObject(response);
							if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
								//TODO handle fail
							} else {
								JSONArray jtables = jresp.getJSONArray("result");
								String tablelabels = "";
								int count = 0;
								for(int i=0; i < jtables.length(); i++){
									JSONObject jtable = jtables.getJSONObject(i);
									if(jtable.getInt("minCustomerCount") <= customercount && customercount <= jtable.getInt("maxCustomerCount")){
										count ++;
										if(count <=5){
											tablelabels += jtable.getString("address") + ",";
										} else {
											break;
										}
									}
								}
								if(count > 0){
									seatavailableInfo.setVisibility(View.VISIBLE);
									tablelabels = tablelabels.substring(0, tablelabels.length() - 1);
									if(count > 5){
										tablelabels += "...";	
									}
									((TextView)seatsSearchPopupView.findViewById(R.id.seatavailable))
										.setText(getContext().getResources().getString(R.string.seatavailable) + tablelabels);
								} else {
									searchLayout.setVisibility(View.GONE);
									bookingLayout.setVisibility(View.VISIBLE);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					public void onFail(String action, int statuscode){
						Log.e(TAG, "[OrderService.getAvailableTables] Network error:" + statuscode);
					}
				});
				
			}
			
		});
		
		((Button)bookingLayout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
		          bookingLayout.setVisibility(View.GONE);
		          seatavailableInfo.setVisibility(View.GONE);
		          searchLayout.setVisibility(View.VISIBLE);
			}
			
		});
		
		((Button)bookingLayout.findViewById(R.id.bookingbtn)).setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				final String customername = ((EditText)bookingLayout.findViewById(R.id.customername)).getText().toString();
				final String phone = ((EditText)bookingLayout.findViewById(R.id.phonenumber)).getText().toString();
				final int count = Integer.parseInt(((EditText)bookingLayout.findViewById(R.id.bookingpeoplecount)).getText().toString());
				if(customername != null && ! customername.equals("") 
						&& phone != null && phone.length() > 7
						&& count > 0){
					OrderService.submitWaitingOrder(customername, phone, count, new HttpHandler(){
						
						public void onSuccess(String action, String response){
							try {
								JSONObject jresp = new JSONObject(response);
								if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
									//TODO handle fail
								} else {
    								Long order_id = jresp.getLong("id");
    								Long user_id = UserManager.getUserId();
    								Long timestamp =  jresp.getLong("modifyTime");
    								Long submit_ts = jresp.getLong("submit_time");
    								int count = jresp.getInt("customer_count");
    								int status = jresp.getInt("status");
    								Order order = new Order(order_id, user_id, "", phone,
    										customername, count, timestamp);
    								order.setStatus(status);
    								order.setSubmitTime(submit_ts);
    								DataService.saveNewOrder(getContext(), order);
    								if(mAdapter != null){
    									mAdapter.getOrderList().add(order);
    									int size = mAdapter.getOrderList().size();
    									WelcomerOrderListView.this.mParentView.showOrderDetail(order, false);
    									WelcomerOrderListView.this.mParentView.setLastItem(order.getOrderKey());
    									mAdapter.setSelectItem(size - 1);
    									mAdapter.notifyDataSetChanged();
    								}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						
						public void onFail(String action, int statuscode){
							Log.e(TAG, "[OrderService.getAvailableTables] Network error:" + statuscode);
						}
					});
				} else {
					ToastUtil.showToast(WelcomerOrderListView.this.getContext(), "请输入正确的预订信息", Toast.LENGTH_SHORT);
				}
			}
			
		});
		
		
		final Button openSearchBtn = (Button)this.mControlbar.findViewById(R.id.optbtn1);
		openSearchBtn.setText(R.string.seatssearchbtn);
		openSearchBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
		          popupWindow.showAtLocation(mListView, Gravity.CENTER, 0, 0);
		          bookingLayout.setVisibility(View.GONE);
		          seatavailableInfo.setVisibility(View.GONE);
		          searchLayout.setVisibility(View.VISIBLE);
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
    	return true;
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
				DataService.getOrderDirty(this.getContext(), order);
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
					View view = mListView.getChildAt(position);
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
					if(this.selectItem == position){
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
		if(mAdapter.getOrderList().size() > 0){
			int index = 0;
			boolean hasFound = false;
			if(mParentView.getLastItem() != null){
				String lastSelectKey = mParentView.getLastItem();
				for(Order order : mAdapter.getOrderList()){
					if(order.getOrderKey().equals(lastSelectKey)){
						hasFound = true;
						break;
					}
					index++;
				}
			}
			if(!hasFound){
				index = mAdapter.getOrderList().size() - 1;
			}
			mAdapter.setSelectItem(index);
			mParentView.showOrderDetail(mAdapter.getOrderList().get(index), true);
		}
		this.mListView.setAdapter(mAdapter);
        this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mParentView.showOrderDetail(mAdapter.getOrderList().get(position), false);
				mParentView.setLastItem(mAdapter.getOrderList().get(position).getOrderKey());
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetInvalidated();
			}
        	
        });
	}
	
}
