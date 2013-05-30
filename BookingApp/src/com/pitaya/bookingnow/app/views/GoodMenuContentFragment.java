package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pitaya.bookingnow.app.R;

public class GoodMenuContentFragment extends Fragment{
	
		private static String TAG = "GoodMenuContentFragment";
		private View mGoodMenuContentView;
		private ViewPager mGoodMenuViewPager;
				 
		public GoodMenuContentFragment(){
			super();
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				Log.i(TAG, "onCreateView in GoodMenuContentFragment" + this.hashCode());
				mGoodMenuContentView = inflater.inflate(R.layout.goodmenucontentview, container, false);
				mGoodMenuViewPager = (ViewPager)mGoodMenuContentView.findViewById(R.id.goodmenuviewpager);
				ArrayList<String> titleList = new ArrayList<String>();
				for (int i = 0; i < 5; i++) {
					titleList.add("title " + i);
				}
				GoodMenuAdapter adapter= new GoodMenuAdapter(inflater.getContext(),  new JSONArray(), titleList);
				mGoodMenuViewPager.setAdapter(adapter);
				return mGoodMenuContentView;
	    }
		
		@Override
		public void onResume(){
			super.onResume();
			Log.i(TAG, "onResume in GoodMenuContentFragment" + this.hashCode());
		}
		
		@Override
		public void onPause(){
			super.onPause();
			Log.i(TAG, "onPause in GoodMenuContentFragment" +this.hashCode());
		}
		
		public int getCurrentViewIndex(){
			return this.mGoodMenuViewPager.getCurrentItem();
		}

}
