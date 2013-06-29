package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pitaya.bookingnow.app.HomeActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderTable;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WaiterOrderListView extends FrameLayout implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static int HEADER = 0;
	private static int ITEM = HEADER +1 ;
	
	private ListView mListView;
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
    
    public void recycle(int position){
    	if(this.mOrderList != null && position < this.mOrderList.size()){
	    	this.mOrderList.get(position).setOnDirtyChangedListener(null);
	    	this.mOrderList.get(position).setOnStatusChangedListener(null);
    	}
    }
    
    public void refresh(){
    	this.mAdapter.notifyDataSetInvalidated();
    }
    
    public void setupViews(int type){
		mListType = type;
    	switch(mListType){
    		case WaiterOrderLeftView.MYORDERS:
    			//TODO get my orders from server
    			mParentView.getActivity().getLoaderManager().initLoader(HomeActivity.ORDER_LIST_LOADER, null, (LoaderCallbacks<Cursor>) this);
    			break;
    		case WaiterOrderLeftView.WAITING_ORDERS:
    			//TODO get my orders from server
    			break;
    	}
    }
    
    @Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return DataService.getOrderListByStatus(mParentView.getActivity(), Order.ALL);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor != null){
			mOrderList = new ArrayList<Order>();
			int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
					OrderTable.COLUMN_ORDER_KEY,
					OrderTable.COLUMN_TABLE_NUMBER,
					OrderTable.COLUMN_SUBMITTER,
					OrderTable.COLUMN_LAST_MODIFACTION_DATE,
					OrderTable.COLUMN_STATUS
			});
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String key = cursor.getString(indexs[0]);
				String tablenumber = cursor.getString(indexs[1]);
				String submitter = cursor.getString(indexs[2]);
				long lastdate = cursor.getLong(indexs[3]);
				int status = cursor.getInt(indexs[4]);
				Order order = new Order();
				order.setKey(key);
				order.setTableNumber(tablenumber);
				order.setSubmitter(submitter);
				order.setStatus(status);
				order.setLastModifyTime(lastdate);
				mOrderList.add(order);
			}
			
	        this.mListView = new ListView(this.getContext());
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
						index = 0;
					}
					mAdapter.setSelectItem(index + 1);
					((WaiterOrderLeftView)mParentView).showOrderDetail(mAdapter.orderlist.get(index), true);
				}
				
				mListView.setAdapter(mAdapter);
				
		        this.mListView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long arg3) {
						if(position > 0){
							((WaiterOrderLeftView)mParentView).showOrderDetail(mAdapter.orderlist.get(position - 1), false);
							((WaiterOrderLeftView)mParentView).setLastItem(mAdapter.orderlist.get(position - 1).getOrderKey());
							mAdapter.setSelectItem(position);
							mAdapter.notifyDataSetInvalidated();
						}
						
					}
		        	
		        });
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();  
	        }
	        addView(mListView);
	        
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
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
		
		@Override
		public int getCount() {
			return orderlist.size() + 1;
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
	        return 2;
	    }
	    
	    @Override  
	    public int getItemViewType(int position) {
	        if(position > 0) {
	            return ITEM;  
	        } else {
	            return HEADER;  
	        }
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
			if(getItemViewType(position) == HEADER){
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.orderlistheader, null);
				}
				((TextView)view.findViewById(R.id.title2)).setVisibility(View.GONE);
				Button newOrderBtn = (Button)view.findViewById(R.id.neworder);
				newOrderBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						//TODO send order to server
						Order order = new Order("A1", "rmzhang");
						DataService.saveNewOrder(mContext, order);
					}
					
				});
				return view;
			} else {
				Order order = orderlist.get(position-1);
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.orderinfo_waiter_mine, null);
				}
				if(this.selectItem == position){
					view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.common_background));
				} else {
					view.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.white));
				}

				((TextView)view.findViewById(R.id.table_number)).setText(order.getTableNum());
				((TextView)view.findViewById(R.id.status)).setText(Order.getOrderStatusString(order.getStatus()));
				SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm:ss"); 
				Date date = new Date();
				if( order.getStatus() == Order.NEW){
					date.setTime(order.getModificationTime());
					((TextView)view.findViewById(R.id.committime)).setText(dateFm.format(date));
				} else if(order.getCommitTime() != null){
					date.setTime(order.getCommitTime());
					((TextView)view.findViewById(R.id.committime)).setText(dateFm.format(date));
				}
				return view;
			}
		}
		
	}
}
