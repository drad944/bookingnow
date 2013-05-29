package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GoodMenuAdapter extends PagerAdapter {
	
		 private Context mContext;
		 private ArrayList<GoodMenuView> mGoodMenus;
		 private JSONArray mDataSource;
		 private List<String> mTitleList;
	
		 public GoodMenuAdapter(Context context, JSONArray arrays, List<String> titleList) {  
		        this.mContext = context;
		        mGoodMenus = new ArrayList<GoodMenuView>();
		        mDataSource = arrays;
		        mTitleList = titleList;
		 }

	    @Override  
	    public void destroyItem(View container, int position, Object object) {  
	    	GoodMenuView itemView = (GoodMenuView)object;  
	        itemView.recycle();
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
	    }
	    
	    @Override  
	    public Object instantiateItem(View container, int position) {     
	    	GoodMenuView menuView;  
	        if(position < mGoodMenus.size()){
	        	menuView = mGoodMenus.get(position);
	        } else {
	        	menuView = new GoodMenuView(mContext);  
//	            try {  
//	                JSONObject dataObj = (JSONObject) mDataSource.get(position);  
//	                menuView.setData(dataObj);  
//	            } catch (JSONException e) {
//	                e.printStackTrace();
//	            }
	            mGoodMenus.add(menuView);
	            ((ViewPager) container).addView(menuView);  
	        }
	          
	        return menuView;
	    }
      
		@Override
		public int getCount() {
				//return mDataSource.length();
			return mTitleList.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}  
}
