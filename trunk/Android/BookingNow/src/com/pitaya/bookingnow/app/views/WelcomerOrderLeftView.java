package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;
import com.pitaya.bookingnow.app.data.HttpHandler;

public class WelcomerOrderLeftView extends OrderLeftView{
	
	private final static String TAG = "WelcomerOrderLeftView";
	
	private ListView mOrderListView;
	private OrderListAdapter mAdapter;
	private ArrayList<Order> mOrderList;
	private String lastSelectItem = null;
	
	public WelcomerOrderLeftView(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.orderleftview4welcomer, null);
		RelativeLayout viewLayout = (RelativeLayout)view.findViewById(R.id.orderview);
		View headerView =inflater.inflate(R.layout.orderlistheader, null);
		headerView.setId(1);
		((TextView)headerView.findViewById(R.id.title1)).setText(R.string.order_list_title_customer);
		((TextView)headerView.findViewById(R.id.title2)).setText(R.string.order_list_title_phone);
		((TextView)headerView.findViewById(R.id.title3)).setText(R.string.order_list_title_count);
		((TextView)headerView.findViewById(R.id.title4)).setText(R.string.order_list_title_status);
		((TextView)headerView.findViewById(R.id.title5)).setText(R.string.order_list_title_time);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		viewLayout.addView(headerView, lp);
		
		mOrderListView = (ListView)view.findViewById(R.id.myorderlist);
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, headerView.getId());
		mOrderListView.setLayoutParams(lp);
		mOrderList = new ArrayList<Order>();
		try {
			mAdapter = new OrderListAdapter(this.getActivity(), this.mOrderListView);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		mAdapter.setOrderList(this.mOrderList);
		mOrderListView.setAdapter(mAdapter);
        this.mOrderListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				WelcomerOrderLeftView.this.showOrderDetail(mAdapter.orderlist.get(position), false);
				WelcomerOrderLeftView.this.setLastItem(mAdapter.orderlist.get(position).getOrderKey());
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetInvalidated();
			}
        	
        });
		
		ArrayList<Integer> orderStatus = new ArrayList<Integer>();
		orderStatus.add(Constants.ORDER_NEW);
		orderStatus.add(Constants.ORDER_WAITING);
		OrderService.getOrderByStatus(orderStatus, new HttpHandler(){
			@Override
			public void onSuccess(String action, String response) {
				try {
					JSONObject jresp = new JSONObject(response);
					if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
						//TODO handle fail

					} else {
						WelcomerOrderLeftView.this.onGetOrders(jresp);
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
		
		
		final View seatsSearchPopupView = inflater.inflate(R.layout.seatssearchwindow, null);
		final PopupWindow popupWindow =  new PopupWindow(seatsSearchPopupView, 300,  
               200 , true);
		popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.common_background));
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
										.setText(WelcomerOrderLeftView.this.getActivity().getResources().getString(R.string.seatavailable) + tablelabels);
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
    								Long timestamp = jresp.getLong("submit_time");
    								int count = jresp.getInt("customer_count");
    								int status = jresp.getInt("status");
    								Order order = new Order(order_id, user_id, "", phone,
    										customername, count, timestamp);
    								order.setStatus(status);
    								DataService.saveNewOrder(getActivity(), order);
    								if(mAdapter != null){
    									mAdapter.orderlist.add(order);
    									int size = mAdapter.orderlist.size();
    									WelcomerOrderLeftView.this.showOrderDetail(order, false);
    									WelcomerOrderLeftView.this.setLastItem(order.getOrderKey());
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
					ToastUtil.showToast(WelcomerOrderLeftView.this.getActivity(), "请输入正确的预订信息", Toast.LENGTH_SHORT);
				}
			}
			
		});
		
		
		Button openSearchBtn = (Button)view.findViewById(R.id.searchseatsbtn);
		openSearchBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
		          popupWindow.showAtLocation(view, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		          bookingLayout.setVisibility(View.GONE);
		          seatavailableInfo.setVisibility(View.GONE);
		          searchLayout.setVisibility(View.VISIBLE);
			}
			
		});
		return view;
	}
	
	public void setLastItem(String key){
		this.mContentContainer.saveStatus(key);
	}
	
	public String getLastItem(){
		return this.lastSelectItem;
	}
	
	public void showOrderDetail(Order order, boolean isForce){
        // Check what fragment is currently shown, replace if needed.
        String key = order.getOrderKey();
        OrderDetailFragment details = (OrderDetailFragment)getFragmentManager().findFragmentById(R.id.orderdetail);
        if (details == null || isForce || !details.getShownOrder().getOrderKey().equals(key)) {
        	order.enrichFoods(this.getActivity());
        	if(order.getStatus() == Constants.ORDER_WAITING && order.isDirty()){
        		order.enrichUpdateFoods(this.getActivity());
        	}
        	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
    		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    		fragmentTransaction.replace(R.id.orderdetail, OrderDetailFragment.newInstance(order, mContentContainer));
    		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		fragmentTransaction.commit();
        }
	}
	
	private void onGetOrders(JSONObject jresp){
    	
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
				DataService.getOrderDirty(this.getActivity(), order);
				mOrderList.add(order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "[onGetOrders] Fail to read the results");
			return;
		}

		if(mAdapter.orderlist.size() > 0){
			int index = 0;
			boolean hasFound = false;
			if(WelcomerOrderLeftView.this.getLastItem() != null){
				String lastSelectKey =WelcomerOrderLeftView.this.getLastItem();
				for(Order order : mAdapter.orderlist){
					if(order.getOrderKey().equals(lastSelectKey)){
						hasFound = true;
						break;
					}
					index++;
				}
			}
			if(!hasFound){
				index = mAdapter.orderlist.size() - 1;
			}
			mAdapter.setSelectItem(index);
			mAdapter.notifyDataSetChanged();
			WelcomerOrderLeftView.this.showOrderDetail(mAdapter.orderlist.get(index), true);
		}
    }
	
	private class OrderListAdapter extends BaseAdapter{

		private ArrayList<Order> orderlist;
		private int selectItem;
		private Context mContext;
		private View mView;
		
		public OrderListAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
            mContext = c;
            mView = view;
        }
		
		private void updateStatusText(int position, int status){
			View view = ((ListView)mView).getChildAt(position);
			if(view != null && (TextView)view.findViewById(R.id.info4) != null){
				((TextView)view.findViewById(R.id.info4)).setText(Order.getOrderStatusString(status));
			}
		}
		
		@Override
		public int getCount() {
			return orderlist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
	    @Override  
	    public int getViewTypeCount() {
	        return 1;
	    }
	    
	    @Override  
	    public int getItemViewType(int position) {
	    	return 0;
	    }
	    
	    public  void setSelectItem(int selectItem) {  
            this.selectItem = selectItem;  
	    }

	    public  void setOrderList(ArrayList<Order> list) {  
            this.orderlist = list;  
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
					 updateStatusText(index, status);
				}
				
			});
			SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm:ss"); 
			Date date = new Date();
			date.setTime(order.getModificationTime());
			((TextView)view.findViewById(R.id.info5)).setText(dateFm.format(date));
			return view;
		}
		
	}
	
}
