package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.TicketActivity;
import com.pitaya.bookingnow.app.domain.Food;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class FoodMenuContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
		private static String TAG = "FoodMenuContentFragment";
		private View mFoodMenuContentView;
		private ViewPager mFoodMenuViewPager;
		private FoodMenuAdapter mFoodMenuAdapter;
		private FoodMenuContentView mContentContainer;
				 
		public FoodMenuContentFragment(){
			super();
		}
		
		public void setContainer(FoodMenuContentView v){
			this.mContentContainer = v;
		}
			
		public int getCurrentViewIndex(){
			return this.mFoodMenuViewPager.getCurrentItem();
		}
		
		public void selectPage(int index){
			this.mFoodMenuViewPager.setCurrentItem(index, true);
		}
		
		public void refreshCurrentPage(){
			mFoodMenuAdapter.refresh(getCurrentViewIndex());
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				Log.i(TAG, "onCreateView in FoodMenuContentFragment" + this.hashCode());
				mFoodMenuContentView = inflater.inflate(R.layout.foodmenucontentview, container, false);
				mFoodMenuViewPager = (ViewPager)mFoodMenuContentView.findViewById(R.id.foodmenuviewpager);
				
				this.getActivity().getLoaderManager().initLoader(0, null, (LoaderCallbacks<Cursor>) this);
				
				View showTicketBtn = mFoodMenuContentView.findViewById(R.id.showTicket);
				showTicketBtn.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View arg0) {
						//get current good items
						Bundle bundle = new Bundle();
						bundle.putSerializable("ticket", mContentContainer.getTicket());
						Intent intent = new Intent(FoodMenuContentFragment.this.getActivity(), TicketActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
					
				});
				View goBackBtn = mFoodMenuContentView.findViewById(R.id.backHome);
				goBackBtn.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View arg0) {
						mContentContainer.selectPage(0);
					}
					
				});
				
				return mFoodMenuContentView;
	    }
		
		@Override
		public void onDetach(){
			super.onDetach();
			Log.i(TAG, "onDetach in FoodMenuContentFragment" + this.hashCode());
		}
		
		@Override
		public void onResume(){
			super.onResume();
			Log.i(TAG, "onResume in FoodMenuContentFragment" + this.hashCode());
		}
		
		@Override
		public void onPause(){
			super.onPause();
			Log.i(TAG, "onPause in FoodMenuContentFragment" +this.hashCode());
		}


		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			return DataService.getAllFoodData(this.getActivity());
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			Log.e(TAG, "Get food menu from database " + (cursor == null));
			if (cursor != null) {
				Map<String, ArrayList<Food>> foods = new LinkedHashMap<String, ArrayList<Food>>();
				foods.put("全部", new ArrayList<Food>());
				int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
						FoodMenuTable.COLUMN_FOOD_KEY,
						FoodMenuTable.COLUMN_NAME,
						FoodMenuTable.COLUMN_PRICE,
						FoodMenuTable.COLUMN_DESCRIPTION,
						FoodMenuTable.COLUMN_CATEGORY,
						FoodMenuTable.COLUMN_IMAGE_S
				});
				for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
					String key = cursor.getString(indexs[0]);
					String name = cursor.getString(indexs[1]);
					float price = cursor.getFloat(indexs[2]);
					String desc = cursor.getString(indexs[3]);
					String category = cursor.getString(indexs[4]);
					byte[] image = cursor.getBlob(indexs[5]);
					ArrayList<Food> foodsByCategory = foods.get(category);
					if(foods.get(category) == null){
						foodsByCategory = new ArrayList<Food>();
						foods.put(category, foodsByCategory);
					}
					foodsByCategory.add(new Food(key, name, price, desc, category, image));
				}
				ArrayList<Food> allCategory = null;
				for(Entry<String, ArrayList<Food>> entry : foods.entrySet()){
					if(entry.getKey().equals("全部")){
						allCategory = entry.getValue();
					} else {
						//This is for category view
						allCategory.add(new Food(null, entry.getKey(), 0f, "", "", entry.getValue().get(0).getSmallImage()));
					}
				}
				mFoodMenuAdapter = new FoodMenuAdapter(mFoodMenuContentView.getContext(),  foods);
				mFoodMenuViewPager.setAdapter(mFoodMenuAdapter);
			}
		}
		
		@Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        Log.i(TAG, "Rotation");
	        mFoodMenuViewPager.invalidate();
	    }

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			Log.e(TAG, "In loader reset");
		}

		/*
		 * Generate each good menu view in the menu view pager
		 */
		private class FoodMenuAdapter extends PagerAdapter {
			
				private Context mContext;
				private ArrayList<FoodMenuView> mFoodMenus;
				private Map<String, ArrayList<Food>> mFoodList;
				private List<String> mTitleList;
			
			    public FoodMenuAdapter(Context context, Map<String, ArrayList<Food>> foods) {
			        this.mContext = context;
			        mFoodMenus = new ArrayList<FoodMenuView>();
			        mTitleList = new ArrayList<String>();
			        mFoodList = foods;
			        for(Entry<String, ArrayList<Food>> entry : foods.entrySet()){
						mTitleList.add(entry.getKey());
			        }
				}

			    public void refresh(int index){
			    	mFoodMenus.get(index).refresh();
			    }
			    
			    @Override  
			    public void destroyItem(View container, int position, Object object) {  
			    	FoodMenuView itemView = (FoodMenuView)object;  
			        itemView.recycle();
			    }
			    
			    @Override
			    public CharSequence getPageTitle(int position) {
			        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
			    }
			    
			    @Override  
			    public Object instantiateItem(View container, int position) {     
			    	FoodMenuView menuView;  
			        if(position < mFoodMenus.size()){
			        	menuView = mFoodMenus.get(position);
			        } else {
			        	menuView = new FoodMenuView(mContext);
			        	menuView.setContentContainer(mContentContainer);
		            	int index = 0;
				        for(Entry<String, ArrayList<Food>> entry : this.mFoodList.entrySet()){
							if((index++) == position){
								menuView.setupViews(entry.getValue());
								break;
							}
				        }
			            mFoodMenus.add(menuView);
			            ((ViewPager) container).addView(menuView);  
			        }
			        return menuView;
			    }
		      
				@Override
				public int getCount() {
					return mFoodList.size();
				}
				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}
				
				@Override  
				public int getItemPosition(Object object) {  
				    return POSITION_NONE;  
				} 
		}


}
