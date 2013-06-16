package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Ticket;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.TicketTable;

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

public class WaiterTicketListView extends FrameLayout implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static int HEADER = 0;
	private static int ITEM = HEADER +1 ;
	
	private ListView mListView;
	private ArrayList<Ticket> mTicketList;
	private TicketListAdapter mAdapter;
	private TicketLeftView mParentView;
	private int mListType;

    public WaiterTicketListView(Context context, TicketLeftView parent){
        super(context);
        this.mParentView = parent;
    }
      
    public WaiterTicketListView(Context context, AttributeSet attrs) {  
        super(context, attrs);
    }
    
    public void recycle(int position){
    	if(this.mTicketList != null && position < this.mTicketList.size()){
	    	this.mTicketList.get(position).setOnDirtyChangedListener(null);
	    	this.mTicketList.get(position).setOnStatusChangedListener(null);
    	}
    }
    
    public void refresh(){
    	this.mAdapter.notifyDataSetInvalidated();
    }
    
    public void setupViews(int type){
		mListType = type;
    	switch(mListType){
    		case WaiterTicketLeftView.MYTICKETS:
    			//TODO get my tickets from server
    			mParentView.getActivity().getLoaderManager().initLoader(1, null, (LoaderCallbacks<Cursor>) this);
    			break;
    		case WaiterTicketLeftView.WAITING_TICKETS:
    			//TODO get my tickets from server
    			break;
    	}
    }
    
    @Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return DataService.getTicketListByStatus(mParentView.getActivity(), Ticket.ALL);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor != null){
			mTicketList = new ArrayList<Ticket>();
			int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
					TicketTable.COLUMN_TICKET_KEY,
					TicketTable.COLUMN_TABLE_NUMBER,
					TicketTable.COLUMN_SUBMITTER,
					TicketTable.COLUMN_LAST_MODIFACTION_DATE,
					TicketTable.COLUMN_STATUS
			});
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String key = cursor.getString(indexs[0]);
				String tablenumber = cursor.getString(indexs[1]);
				String submitter = cursor.getString(indexs[2]);
				long lastdate = cursor.getLong(indexs[3]);
				int status = cursor.getInt(indexs[4]);
				Ticket ticket = new Ticket();
				ticket.setKey(key);
				ticket.setTableNumber(tablenumber);
				ticket.setSubmitter(submitter);
				ticket.setStatus(status);
				ticket.setLastModifyTime(lastdate);
				mTicketList.add(ticket);
			}
			
	        this.mListView = new ListView(this.getContext());
			try {
				mAdapter = new TicketListAdapter(getContext(), mListView);
				mAdapter.setTicketList(this.mTicketList);
				if(mAdapter.ticketlist.size() > 0){
					int index = 0;
					boolean hasFound = false;
					if(((WaiterTicketLeftView)mParentView).getLastItem() != null){
						String lastSelectKey = ((WaiterTicketLeftView)mParentView).getLastItem();
						for(Ticket ticket : mAdapter.ticketlist){
							if(ticket.getTicketKey().equals(lastSelectKey)){
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
					((WaiterTicketLeftView)mParentView).showTicketDetail(mAdapter.ticketlist.get(index), true);
				}
				
				mListView.setAdapter(mAdapter);
				
		        this.mListView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long arg3) {
						if(position > 0){
							((WaiterTicketLeftView)mParentView).showTicketDetail(mAdapter.ticketlist.get(position - 1), false);
							((WaiterTicketLeftView)mParentView).setLastItem(mAdapter.ticketlist.get(position - 1).getTicketKey());
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
    
	private class TicketListAdapter extends BaseAdapter{

		private ArrayList<Ticket> ticketlist;
		private int selectItem;
		private Context mContext;
		private View mView;
		
		public TicketListAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
            mContext = c;
            mView = view;
        }
		
		@Override
		public int getCount() {
			return ticketlist.size() + 1;
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

	    public  void setTicketList(ArrayList<Ticket> list) {  
            this.ticketlist = list;  
	    }
	    
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(getItemViewType(position) == HEADER){
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.ticketlistheader, null);
				}
				((TextView)view.findViewById(R.id.title2)).setVisibility(View.GONE);
				Button newTicketBtn = (Button)view.findViewById(R.id.newticket);
				newTicketBtn.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						//TODO send ticket to server
						Ticket ticket = new Ticket("A1", "rmzhang");
						DataService.saveNewTicket(mContext, ticket);
					}
					
				});
				return view;
			} else {
				Ticket ticket = ticketlist.get(position-1);
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.ticketinfo_waiter_mine, null);
				}
				if(this.selectItem == position){
					view.setBackgroundColor(parent.getContext().getResources().getColor(R.color.common_background));
				} else {
					view.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.white));
				}

				((TextView)view.findViewById(R.id.table_number)).setText(ticket.getTableNum());
				((TextView)view.findViewById(R.id.status)).setText(Ticket.getTicketStatusString(ticket.getStatus()));
				SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 HH:mm:ss"); 
				Date date = new Date();
				if( ticket.getStatus() == Ticket.NEW){
					date.setTime(ticket.getModificationTime());
					((TextView)view.findViewById(R.id.committime)).setText(dateFm.format(date));
				} else if(ticket.getCommitTime() != null){
					date.setTime(ticket.getCommitTime());
					((TextView)view.findViewById(R.id.committime)).setText(dateFm.format(date));
				}
				return view;
			}
		}
		
	}
}
