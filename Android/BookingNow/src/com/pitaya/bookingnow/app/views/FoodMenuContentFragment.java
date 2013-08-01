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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.pitaya.bookingnow.app.HomeActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.OrderDetailPreviewActivity;
import com.pitaya.bookingnow.app.data.CustomerOrderDetailAdapter;
import com.pitaya.bookingnow.app.data.GetOrderFoodsStatusHandler;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;

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
			if(this.mFoodMenuViewPager != null && this.mFoodMenuAdapter != null
					&& this.mFoodMenuAdapter.getCount() > 0){
				return this.mFoodMenuViewPager.getCurrentItem();
			} else {
				return -1;
			}
		}
		
		public void selectPage(int index){
			int currentIdx = getCurrentViewIndex();
			if( currentIdx != -1 && currentIdx != index){
				this.mFoodMenuViewPager.setCurrentItem(index, true);
			}
		}
		
		public void refreshCurrentPage(){
			if(getCurrentViewIndex() != -1){
				mFoodMenuAdapter.refresh(getCurrentViewIndex());
			}
		}
		
		public void refreshAllPages(){
			if(getCurrentViewIndex() != -1){
				for(int i = 0; i < mFoodMenuAdapter.getCount(); i++){
					mFoodMenuAdapter.refresh(i);
				}
			}
		}
		
		private int getPopupWindowSize(int items){
			return (items + 1) * 40 + 8;
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Log.i(TAG, "onCreateView in FoodMenuContentFragment" + this.hashCode());
			mFoodMenuContentView = inflater.inflate(R.layout.foodmenucontentview, container, false);
			mFoodMenuViewPager = (ViewPager)mFoodMenuContentView.findViewById(R.id.foodmenuviewpager);
			
			this.getActivity().getLoaderManager().initLoader(HomeActivity.MENU_LOADER, null, (LoaderCallbacks<Cursor>) this);
			
			TextView showOrderBtn = (TextView)mFoodMenuContentView.findViewById(R.id.showOrder);
			
			if(this.mContentContainer != null && this.mContentContainer.getOrder() != null){
				
				final ListView orderPreview = new ListView(getActivity());
				final PopupWindow popupWindow =  new PopupWindow(orderPreview, 0,  
						0, true);
				popupWindow.setFocusable(true);
		        popupWindow.setOutsideTouchable(false);
		        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		        popupWindow.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.common_background));
		        popupWindow.setAnimationStyle(R.style.AnimBottom);
		        popupWindow.setOnDismissListener(new OnDismissListener(){

					@Override
					public void onDismiss() {
						refreshAllPages();
					}
		        	
		        });
				
				showOrderBtn.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View arg0) {
						//get current good items						
				        popupWindow.showAtLocation(mFoodMenuContentView, 
				        		Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
						try {
							final CustomerOrderDetailAdapter orderAdapter = new CustomerOrderDetailAdapter(getActivity(), orderPreview, mContentContainer.getOrder());
							orderAdapter.setDataSetChangedListener(new OrderDetailAdapter.DataSetChangedListener(){

								@Override
								public void OnDataSetChanged() {
									popupWindow.update(700, getPopupWindowSize(mContentContainer.getOrder().getFoods().size()));
								}
								
							});
							if(mContentContainer.getOrder().getStatus() == Constants.ORDER_COMMITED){
			            		GetOrderFoodsStatusHandler handler = new GetOrderFoodsStatusHandler(getActivity(), mContentContainer.getOrder());
			            		handler.setAfterGetFoodsStatusListener(new GetOrderFoodsStatusHandler.AfterGetFoodsStatusListener(){
			
									@Override
									public void afterGetFoodsStatus() {
										orderPreview.setAdapter(orderAdapter);
										popupWindow.update(700,getPopupWindowSize(mContentContainer.getOrder().getFoods().size()));
									}
			            			
			            		});
			            		OrderService.getFoodsOfOrder(Long.parseLong(mContentContainer.getOrder().getOrderKey()), handler);
							} else {
								orderPreview.setAdapter(orderAdapter);
								popupWindow.update(700,getPopupWindowSize(mContentContainer.getOrder().getFoods().size()));
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					
					
				});
			} else {
				showOrderBtn.setVisibility(View.GONE);
			}
			TextView goBackBtn = (TextView)mFoodMenuContentView.findViewById(R.id.backHome);
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
		}
		
		@Override
		public void onResume(){
			super.onResume();
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
			if (cursor != null) {
				Map<String, ArrayList<Food>> foods = new LinkedHashMap<String, ArrayList<Food>>();
				foods.put("全部", new ArrayList<Food>());
				int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
						FoodMenuTable.COLUMN_FOOD_KEY,
						FoodMenuTable.COLUMN_NAME,
						FoodMenuTable.COLUMN_PRICE,
						FoodMenuTable.COLUMN_RECOMMENDATION,
						FoodMenuTable.COLUMN_DESCRIPTION,
						FoodMenuTable.COLUMN_CATEGORY,
						FoodMenuTable.COLUMN_REVISION
				});
				for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
					String key = cursor.getString(indexs[0]);
					String name = cursor.getString(indexs[1]);
					float price = cursor.getFloat(indexs[2]);
					boolean isRecmd = Boolean.parseBoolean(cursor.getString(indexs[3]));
					String desc = cursor.getString(indexs[4]);
					String category = cursor.getString(indexs[5]);
					Long version = cursor.getLong(indexs[6]);
					ArrayList<Food> foodsByCategory = foods.get(category);
					if(foods.get(category) == null){
						foodsByCategory = new ArrayList<Food>();
						foods.put(category, foodsByCategory);
					}
					Food food = new Food(key, name, price, desc, category, isRecmd);
					food.setVersion(version);
					foodsByCategory.add(food);
				}
				ArrayList<Food> allCategory = null;
				for(Entry<String, ArrayList<Food>> entry : foods.entrySet()){
					if(entry.getKey().equals("全部")){
						allCategory = entry.getValue();
					} else {
						//This is for category view
						Food firstFood = entry.getValue().get(0);
						allCategory.add(new Food(firstFood.getKey(), entry.getKey(), 0f, "", null, false));
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
				private Map<String, ArrayList<Food>> mFoodPages;
				private List<String> mTitleList;
			
			    public FoodMenuAdapter(Context context, Map<String, ArrayList<Food>> foods) {
			        this.mContext = context;
			        mFoodMenus = new ArrayList<FoodMenuView>();
			        mTitleList = new ArrayList<String>();
			        mFoodPages = foods;
			        for(Entry<String, ArrayList<Food>> entry : foods.entrySet()){
						mTitleList.add(entry.getKey());
			        }
				}

			    public void refresh(int index){
			    	if(index < mFoodMenus.size()){
			    		mFoodMenus.get(index).needUpdateImage(false);
			    		mFoodMenus.get(index).refresh();
			    	}
			    }
			    
			    @Override
			    public void destroyItem(View container, int position, Object object) {  
			    	FoodMenuView itemView = (FoodMenuView)object;
			    	itemView.recycle();
//			    	((ViewPager)container).removeView(itemView);
//			    	for(int i=this.mFoodMenus.size() - 1; i >= 0 ; i--){
//			    		if(this.mFoodMenus.get(i) == itemView){
//			    			this.mFoodMenus.remove(i);
//			    		}
//			    	}
//			    	itemView = null;
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
				        for(Entry<String, ArrayList<Food>> entry : this.mFoodPages.entrySet()){
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
					return mFoodPages.size();
				}
				
				@Override
				public boolean isViewFromObject(View arg0, Object arg1) {
					return arg0 == arg1;
				}

		}


}
