package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.TicketDetailActivity;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.TicketTable;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TicketListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

	private static int HEADER = 0;
	private static int ITEM = HEADER +1 ;
	private static String TAG = "TicketContentFragment";
	private TicketContentView mContentContainer;
	private boolean mDualPane;
	private ArrayList<Ticket> mTicketList;
	private TicketListAdapter mAdapter;
	
	public TicketListFragment(){
		super();
	}
	
	public void setContainer(TicketContentView v){
		this.mContentContainer = v;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.getActivity().getLoaderManager().initLoader(1, null, (LoaderCallbacks<Cursor>) this);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView in TicketListFragment" + this.hashCode());
		
		View view = super.onCreateView(inflater, container, savedInstanceState);
//        View detailsFrame = this.mContentContainer.getView().findViewById(R.id.ticketdetail);
//        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
//        if (mDualPane) {
//            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        }
		
        mDualPane = true;
		return view;
	}
	
	private void showDetails(int index) {

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            Ticket selectedTicket = mTicketList.get(index);
            String key = selectedTicket.getTicketKey();
            TicketDetailFragment details = (TicketDetailFragment)getFragmentManager().findFragmentById(R.id.ticketdetail);
            if (details != null 
            		&& (details.getShownTicket() == null || !details.getShownTicket().getTicketKey().equals(key))) {
            	DataService.getFoodsOfTicket(this.getActivity(), selectedTicket);
            	FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        		fragmentTransaction.replace(R.id.ticketdetail, TicketDetailFragment.newInstance(selectedTicket, mContentContainer));
        		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        		fragmentTransaction.commit();
            }
        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), TicketDetailActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
	}
		
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return DataService.getTicketListByStatus(this.getActivity(), Ticket.ALL);
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
			this.mAdapter = new TicketListAdapter();
			this.mAdapter.ticketlist = this.mTicketList;
			this.setListAdapter(this.mAdapter);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(position > 0){
			this.showDetails(position - 1);
		}
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
	
	private class TicketListAdapter extends BaseAdapter {
		
		ArrayList<Ticket> ticketlist = new ArrayList<Ticket>();
		
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if(getItemViewType(position) == HEADER){
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.ticketlistheader, null);
				}
				Button newTicketBtn = (Button)view.findViewById(R.id.newticket);
				newTicketBtn.setOnClickListener(new OnClickListener(){
		
					@Override
					public void onClick(View v) {
						//TODO send ticket to service
						Ticket ticket = new Ticket("A1", "rmzhang");
						DataService.saveNewTicket(TicketListFragment.this.getActivity(), ticket);
					}
					
				});
				return view;
			} else {
				Ticket ticket = ticketlist.get(position-1);
				if(view == null){
					view = View.inflate(parent.getContext(), R.layout.ticketinfo, null);
				}
	
				((TextView)view.findViewById(R.id.table_number)).setText(ticket.getTableNum());
				((TextView)view.findViewById(R.id.submitter)).setText(ticket.getSubmitter());
				((TextView)view.findViewById(R.id.status)).setText(String.valueOf(ticket.getStatus()));
				Log.i(TAG, position+"' ticket key is" + ticket.getTicketKey());
				SimpleDateFormat dateFm = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss"); 
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
