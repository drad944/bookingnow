package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.HomeActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.OrderTable;
import com.pitaya.bookingnow.app.util.Constants;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WaiterOrderListView extends RelativeLayout{
	
	private static String TAG = "Order";
	
	private ListView mListView;
	private View mControlbar;
	private ArrayList<Order> mOrderList;
	private OrderListAdapter mAdapter;
	private OrderLeftView mParentView;
	private int mListType;

    public WaiterOrderListView(Context context, OrderLeftView parent){
        super(context);
        this.mParentView = parent;
    }
      
    public WaiterOrderListView(Context context, AttributeSet attrs) {  
        super(context, attrs);
    }
    
    public void recycle(){}
    
    public void refresh(){
    	this.mAdapter.notifyDataSetInvalidated();
    }
    
    public void setupViews(int type){
		mListType = type;
        
    	switch(mListType){
    		case WaiterOrderLeftView.MYORDERS:
    			View headerView =View.inflate(this.getContext(), R.layout.orderlistheader, null);
    			headerView.setId(1);
    			((TextView)headerView.findViewById(R.id.title1)).setText(R.string.order_list_title_table);
    			((TextView)headerView.findViewById(R.id.title2)).setText(R.string.order_list_title_status);
    			((TextView)headerView.findViewById(R.id.title3)).setText(R.string.order_list_title_time);
    			((TextView)headerView.findViewById(R.id.title4)).setVisibility(View.GONE);
    			((TextView)headerView.findViewById(R.id.title5)).setVisibility(View.GONE);
    			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    			addView(headerView, lp);
    	        this.mListView = new ListView(this.getContext());
    	        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	        lp.addRule(RelativeLayout.BELOW, headerView.getId());
    	        addView(mListView, lp);
    	        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	        this.mControlbar = View.inflate(this.getContext(), R.layout.ordercontrolbar, null);
    	        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	        addView(this.mControlbar, lp);
    	        
    			//TODO get my orders from server
    			ArrayList<Integer> orderStatus = new ArrayList<Integer>();
    			orderStatus.add(Constants.ORDER_NEW);
    			orderStatus.add(Constants.ORDER_COMMITED);
    			orderStatus.add(Constants.ORDER_PAYING);
    			OrderService.getOrderByStatus(orderStatus, new HttpHandler(){
    				@Override
					public void onSuccess(String action, String response) {
						try {
							JSONObject jresp = new JSONObject(response);
							if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
								//TODO handle fail

							} else {
								WaiterOrderListView.this.onGetOrders(jresp);
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
    			
    	        View tablesview = View.inflate(this.getContext(), R.layout.tablesview, null);
    			final GridView tablegridview = (GridView)tablesview.findViewById(R.id.tablegridview);
    			Button confirmBtn =  (Button)tablesview.findViewById(R.id.confirm);
    			confirmBtn.setOnClickListener(new OnClickListener(){

    				@Override
    				public void onClick(View v) {
    					final ArrayList<Table> tableids = ((TablesAdapter)tablegridview.getAdapter()).getSelectedTables();
    					OrderService.submitOrder(tableids, new HttpHandler(){
    						
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
    								Long timestamp = jresp.getLong("submit_time");
    								int status = jresp.getInt("status");
    								Order order = new Order(tableids, order_id, user_id, username, timestamp);
    								order.setStatus(status);
    								DataService.saveNewOrder(getContext(), order);
    								if(mAdapter != null){
    									mAdapter.orderlist.add(order);
    									int size = mAdapter.orderlist.size();
    									((WaiterOrderLeftView)mParentView).showOrderDetail(order, false);
    									((WaiterOrderLeftView)mParentView).setLastItem(order.getOrderKey());
    									mAdapter.setSelectItem(size - 1);
    									mAdapter.notifyDataSetChanged();
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
    				
    			});
    			
    			final PopupWindow popupWindow =  new PopupWindow(tablesview, 400, 400, true);
    			popupWindow.setFocusable(true);
    	        popupWindow.setOutsideTouchable(false);
    	        popupWindow.setBackgroundDrawable(this.getContext().getResources().getDrawable(R.drawable.common_background));
    	        popupWindow.setAnimationStyle(R.style.AnimBottom);
    			final Button newOrderBtn = (Button)this.mControlbar.findViewById(R.id.optbtn1);
    			newOrderBtn.setText(R.string.neworderbtn);
    			
    			newOrderBtn.setOnClickListener(new OnClickListener(){
    				@Override
    				public void onClick(View v) {
    					OrderService.getAvailableTables(Constants.TABLE_EMPTY, new HttpHandler(){
    						
    						public void onSuccess(String action, String response){
    							try {
    								JSONObject jresp = new JSONObject(response);
    								if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
    									//TODO handle fail
    								} else {
    									JSONArray jtables = jresp.getJSONArray("result");
	    								ArrayList<Table> tables = new ArrayList<Table>();
	    								for(int i=0; i < jtables.length(); i++){
	    									tables.add(new Table(jtables.getJSONObject(i).getLong("id"), jtables.getJSONObject(i).getString("address")));
	    								}
	    								try {
	    									tablegridview.setAdapter(new TablesAdapter(getContext(), tables));
	    								} catch (IllegalArgumentException e) {
	    									e.printStackTrace();
	    								} catch (IllegalAccessException e) {
	    									e.printStackTrace();
	    								}
	    								popupWindow.showAsDropDown(newOrderBtn);
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
    			
    			this.mControlbar.findViewById(R.id.optbtn2).setVisibility(View.GONE);
    			break;
    		case WaiterOrderLeftView.WAITING_ORDERS:
    			//TODO get my orders from server
    			break;
    	}
    }
    
    private void onGetOrders(JSONObject jresp){
    	mOrderList = new ArrayList<Order>();
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
			mAdapter = new OrderListAdapter(getContext(), mListView);
			mAdapter.setOrderList(this.mOrderList);
			if(mAdapter.orderlist.size() > 0){
				int index = 0;
				boolean hasFound = false;
				if(((WaiterOrderLeftView)mParentView).getLastItem() != null){
					String lastSelectKey = ((WaiterOrderLeftView)mParentView).getLastItem();
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
				((WaiterOrderLeftView)mParentView).showOrderDetail(mAdapter.orderlist.get(index), true);
			}
			
			mListView.setAdapter(mAdapter);
			
	        this.mListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					((WaiterOrderLeftView)mParentView).showOrderDetail(mAdapter.orderlist.get(position), false);
					((WaiterOrderLeftView)mParentView).setLastItem(mAdapter.orderlist.get(position).getOrderKey());
					mAdapter.setSelectItem(position);
					mAdapter.notifyDataSetInvalidated();
				}
	        	
	        });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        }
    }
    
	private class TablesAdapter extends BaseAdapter{

		private ArrayList<Table> tables;
		private ArrayList<Table> selectedTables;
		private Context mContext;
		
		public TablesAdapter(Context context, ArrayList<Table> tableids) throws IllegalArgumentException, IllegalAccessException{  
			tables = tableids;
			mContext = context;
			selectedTables = new ArrayList<Table>();
        }
		
		public ArrayList<Table> getSelectedTables(){
			return this.selectedTables;
		}
		
		@Override
		public int getCount() {
			return tables.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final int index = position;
			if(view == null){
				view = new CheckBox(mContext);
				((CheckBox)view).setText(this.tables.get(position).getLabel());
				((CheckBox)view).setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if(isChecked){
							selectedTables.add(tables.get(index));
						} else {
							int i = 0;
							while(!selectedTables.get(i).equals(tables.get(index).getId())){
								i++;
							}
							if(i < selectedTables.size()){
								selectedTables.remove(i);
							}
						}
					}
					
				});
			}
			return view;
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
			if(view != null && (TextView)view.findViewById(R.id.info2) != null){
				((TextView)view.findViewById(R.id.info2)).setText(Order.getOrderStatusString(status));
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
				view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.common_background));
			} else {
				view.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.white));
			}

			((TextView)view.findViewById(R.id.info1)).setText(order.getTableNum());
			((TextView)view.findViewById(R.id.info2)).setText(Order.getOrderStatusString(order.getStatus()));
			order.addOnStatusChangedListener(new OnOrderStatusChangedListener(){

				@Override
				public void onOrderStatusChanged(Order order, int status) {
					 updateStatusText(index, status);
				}
				
			});
			SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm:ss"); 
			Date date = new Date();
			date.setTime(order.getModificationTime());
			((TextView)view.findViewById(R.id.info3)).setText(dateFm.format(date));
			
			view.findViewById(R.id.info4).setVisibility(View.GONE);
			view.findViewById(R.id.info5).setVisibility(View.GONE);
			return view;
		}
		
	}
}
