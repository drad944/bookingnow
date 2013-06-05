package com.pitaya.bookingnow.app.views;

import java.util.HashMap;
import java.util.Map;

import com.pitaya.bookingnow.app.R;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TicketListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

	private static String TAG = "TicketContentFragment";
	private TicketContentView mContentContainer;
	private boolean mDualPane;
	private int mCurCheckPosition = 0;
	
	public TicketListFragment(){
		super();
	}
	
	public void setContainer(TicketContentView v){
		this.mContentContainer = v;
	}
	
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.i(TAG, "onCreateView in TicketContentFragment" + this.hashCode());
//	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.ticketdetail);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
	
	 void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            TicketDetailFragment details = (TicketDetailFragment)
                    getFragmentManager().findFragmentById(R.id.ticketdetail);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = TicketDetailFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.ticketdetail, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), TicketDetailFragment.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
	    }
		
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
	
}
