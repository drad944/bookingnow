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
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WaiterOrderListView extends OrderListView{
	
	private static String TAG = "WaiterOrderListView";
	private WaiterOrderLeftView mParentView;

    public WaiterOrderListView(Context context, WaiterOrderLeftView parent){
        super(context);
        mParentView = parent;
    }
      
    public WaiterOrderListView(Context context, AttributeSet attrs, WaiterOrderLeftView parent) {  
        super(context, attrs);
        mParentView = parent;
    }
    
    @Override
    public void setupViews(){
    	super.setupViews();
		((TextView)this.mHeaderView.findViewById(R.id.title1)).setText(R.string.order_list_title_table);
		((TextView)this.mHeaderView.findViewById(R.id.title2)).setText(R.string.order_list_title_status);
		((TextView)this.mHeaderView.findViewById(R.id.title3)).setText(R.string.order_list_title_time);
		((TextView)this.mHeaderView.findViewById(R.id.title4)).setVisibility(View.GONE);
		((TextView)this.mHeaderView.findViewById(R.id.title5)).setVisibility(View.GONE);
        
		final TablesView tablesView = new TablesView(this.getContext(), this.mListView);
		tablesView.setOnConfirmListener(new TablesView.OnConfirmListener() {
			
			@Override
			public void onConfirm(ArrayList<Table> selectedTables) {
					final ArrayList<Table> tables = selectedTables;
					OrderService.submitOrder(tables, new HttpHandler(){
					
					@Override
					public void onSuccess(String action, String response) {
						try {
							JSONObject jresp = new JSONObject(response);
							if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
								//TODO handle fail
								return;
							}
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
							if(mAdapter != null){
								mAdapter.getOrderList().add(order);
								int size = mAdapter.getOrderList().size();
								mParentView.showOrderDetail(order, false, WaiterOrderLeftView.MYORDERS);
								mParentView.setLastItem(order.getOrderKey());
								mAdapter.setSelectItem(size - 1);
								mAdapter.notifyDataSetChanged();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						tablesView.close();
					}

					@Override
					public void onFail(String action, int statuscode) {
						Log.e(TAG, "[OrderService.submitOrder] Network error:" + statuscode);
					}
				});
			}
		});
		
		final Button newOrderBtn = (Button)this.mControlbar.findViewById(R.id.optbtn1);
		newOrderBtn.setText(R.string.neworderbtn);
		
		newOrderBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tablesView.popup(newOrderBtn);
			}
			
		});
		
		this.mControlbar.findViewById(R.id.optbtn2).setVisibility(View.GONE);
    }
    
    @Override
    protected ArrayList<Integer> getOrderStatus(){
		ArrayList<Integer> orderStatus = new ArrayList<Integer>();
		orderStatus.add(Constants.ORDER_NEW);
		orderStatus.add(Constants.ORDER_COMMITED);
		orderStatus.add(Constants.ORDER_PAYING);
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
				Order order = new Order();
				order.setKey(String.valueOf(jorder.getLong("id")));
				JSONArray jtables = jorder.getJSONArray("table_details");
				String tablelabel = "";
				for(int j=0; j < jtables.length(); j++){
					tablelabel += jtables.getJSONObject(j).getJSONObject("table").getString("address") + ",";
				}
				tablelabel = tablelabel.substring(0, tablelabel.length() - 1);
				order.setTableNumber(tablelabel);
				order.setSubmitter(jorder.getJSONObject("user").getString("name"));
				order.setStatus(jorder.getInt("status"));
				order.setLastModifyTime(jorder.getLong("modifyTime"));
				order.setSubmitTime(jorder.getLong("submit_time"));
				DataService.getOrderDirty(this.getContext(), order);
				mOrderList.add(order);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "[onGetOrders] Fail to read the results");
			return;
		}
		
		try {
			 this.mAdapter = new OrderListAdapter(this.getContext(), this.mListView){
		        	
		        	@Override
		        	public View getView(int position, View convertView, ViewGroup parent) {
			    		View view = convertView;
			    		Order order = orderlist.get(position);
			    		if(view == null){
			    			view = View.inflate(parent.getContext(), R.layout.orderinfoview, null);
			    			ViewHolder holder = new ViewHolder();
			    			holder.info1 = (TextView)view.findViewById(R.id.info1);
			    			holder.info2 = (TextView)view.findViewById(R.id.info2);
			    			holder.info3 = (TextView)view.findViewById(R.id.info3);
			    			holder.info4 = (TextView)view.findViewById(R.id.info4);
			    			holder.info5 = (TextView)view.findViewById(R.id.info5);
			    			holder.info4.setVisibility(View.GONE);
			    			holder.info5.setVisibility(View.GONE);
			    			view.setTag(holder);
			    		}
			    		
			    		final ViewHolder viewHolder = (ViewHolder)view.getTag();
			    		if(this.selectItem != null && this.selectItem == position){
			    			view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.common_background));
			    		} else {
			    			view.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.white));
			    		}
			    		viewHolder.info1.setText(order.getTableNum());
			    		viewHolder.info2.setText(Order.getOrderStatusString(order.getStatus()));
			    		if(viewHolder.getOrder() != null && viewHolder.getStatusListener() != null){
			    			viewHolder.getOrder().removeOnStatusChangedListener(viewHolder.getStatusListener());
			    		}
			    		viewHolder.setOrder(order);
			    		OnOrderStatusChangedListener statuslistener = new OnOrderStatusChangedListener(){
			
			    			@Override
			    			public void onOrderStatusChanged(Order order, int status) {
			    				viewHolder.info2.setText(Order.getOrderStatusString(order.getStatus()));
			    			}
			    			
			    		};
			    		viewHolder.setStatusListener(statuslistener);
			    		order.addOnStatusChangedListener(statuslistener);
			    		SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm"); 
			    		Date date = new Date();
			    		date.setTime(order.getModificationTime());
			    		viewHolder.info3.setText(dateFm.format(date));
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
			if(((WaiterOrderLeftView)mParentView).getLastItem() != null){
				String lastSelectKey = ((WaiterOrderLeftView)mParentView).getLastItem();
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
			mParentView.showOrderDetail(mAdapter.getOrderList().get(index), true, WaiterOrderLeftView.MYORDERS);
		}
		
		this.mListView.setAdapter(mAdapter);
		
        this.mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mParentView.showOrderDetail(mAdapter.getOrderList().get(position), false, WaiterOrderLeftView.MYORDERS);
				mParentView.setLastItem(mAdapter.getOrderList().get(position).getOrderKey());
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetChanged();
			}
        	
        });

    }
}
