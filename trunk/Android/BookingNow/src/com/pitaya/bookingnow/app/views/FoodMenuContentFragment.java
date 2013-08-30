package com.pitaya.bookingnow.app.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
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
import android.widget.BaseAdapter;
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
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.EnhancedMessageService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ContentUtil;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.OrderDetailMessage;
import com.pitaya.bookingnow.message.OrderMessage;

import java.util.ArrayList;
import java.util.HashMap;
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
		private CustomerOrderDetailAdapter mOrderPreviewAdapter;
		
		private MessageHandler mMessageHandler;
		private EnhancedMessageService mMessageService;
		private PopupWindow mPopupWindow;
		private ListView mOrderPreviewView;
		protected boolean mIsBound = false;
		protected ServiceConnection mConnection;
				 
		public FoodMenuContentFragment(){
			super();
			mMessageHandler = new MessageHandler();
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
			if(mFoodMenuAdapter != null){
				mFoodMenuAdapter.refreshAllPages();
			}
		}
		
		public void onServiceConnected(EnhancedMessageService service){
			mMessageService = service;
			mIsBound = true;
			for(String category : getMessageCategories()){
				mMessageService.registerHandler(category, mMessageHandler);
			}
		}
		
		public void onServiceDisconnected(){
			mMessageService.unregisterHandler(mMessageHandler);
			mMessageService = null;
			mIsBound = false;
		}
		
		protected void doBindService() {
			this.getActivity().bindService(new Intent(this.getActivity(), EnhancedMessageService.class), 
					getServiceConnection(), Context.BIND_AUTO_CREATE);
		}
		
		protected void doUnbindService() {
		    if (mIsBound) {
		    	mMessageService.unregisterHandler(mMessageHandler);
		    	this.getActivity().unbindService(mConnection);
		        mIsBound = false;
		    }
		}

		protected ArrayList<String> getMessageCategories(){
			ArrayList<String> categories = new ArrayList<String>();
			categories.add(Constants.ORDER_MESSAGE);
			return categories;
		}
		
		protected ServiceConnection getServiceConnection(){ 
			
			mConnection = new ServiceConnection() {
		
				@Override
				public void onServiceConnected(ComponentName name, IBinder service) {
					mMessageService = ((EnhancedMessageService.MessageBinder)service).getService();
					mIsBound = true;
					for(String category : getMessageCategories()){
						mMessageService.registerHandler(category, mMessageHandler);
					}
				}
		
				@Override
				public void onServiceDisconnected(ComponentName name) {
					mMessageService = null;
					mIsBound = false;
				}

			};
			return mConnection;
		}
		
		private int getPopupWindowSize(int items){
			int height = (items + 1) * ContentUtil.getPixelsByDP(40) + ContentUtil.getPixelsByDP(10);
			return height > ContentUtil.getPixelsByDP(600) ? ContentUtil.getPixelsByDP(600) : height;
		}
		
		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			mFoodMenuContentView = inflater.inflate(R.layout.foodmenucontentview, container, false);
			mFoodMenuViewPager = (ViewPager)mFoodMenuContentView.findViewById(R.id.foodmenuviewpager);
			
			this.getActivity().getLoaderManager().initLoader(HomeActivity.MENU_LOADER, null, (LoaderCallbacks<Cursor>) this);
			
			TextView showOrderBtn = (TextView)mFoodMenuContentView.findViewById(R.id.showOrder);
			
			if(this.mContentContainer != null && this.mContentContainer.getOrder() != null){
				
				mOrderPreviewView = new ListView(getActivity());
				mPopupWindow =  new PopupWindow(mOrderPreviewView, 0,  0, true);
				mPopupWindow.setFocusable(true);
		        mPopupWindow.setOutsideTouchable(false);
		        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		        mPopupWindow.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.common_background));
		        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		        mPopupWindow.setOnDismissListener(new OnDismissListener(){
		        
					@Override
					public void onDismiss() {
						refreshAllPages();
					}
		        	
		        });
				
		        mOrderPreviewAdapter = null;
				try {
					mOrderPreviewAdapter = new CustomerOrderDetailAdapter(getActivity(), 
							mOrderPreviewView, mContentContainer.getOrder());
					mOrderPreviewAdapter.setDataSetChangedListener(new OrderDetailAdapter.DataSetChangedListener(){

						@Override
						public void OnDataSetChanged() {
							if(mPopupWindow.isShowing()){
								mPopupWindow.update(ContentUtil.getPixelsByDP(700), 
										getPopupWindowSize(mContentContainer.getOrder().getFoods().size()));
							}
						}
						
					});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				if(mOrderPreviewAdapter != null){
					mOrderPreviewView.setAdapter(mOrderPreviewAdapter);
				}
		        
				showOrderBtn.setOnClickListener(new OnClickListener(){
					
					@Override
					public void onClick(View arg0) {
				        mPopupWindow.showAtLocation(mFoodMenuContentView, 
				        		Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0);
						if(mContentContainer.getOrder().getStatus() == Constants.ORDER_COMMITED){
		            		GetOrderFoodsStatusHandler handler = new GetOrderFoodsStatusHandler(getActivity(), mContentContainer.getOrder());
		            		handler.setAfterGetFoodsStatusListener(new GetOrderFoodsStatusHandler.AfterGetFoodsStatusListener(){
		
								@Override
								public void afterGetFoodsStatus() {
									if(mOrderPreviewAdapter != null){
										if(mContentContainer.getOrder() != mOrderPreviewAdapter.getOrder()){
											mOrderPreviewAdapter.setOrder(mContentContainer.getOrder());
										}
										mOrderPreviewAdapter.refresh();
									}
								}
		            			
		            		});
		            		OrderService.getFoodStatusOfOrder(Long.parseLong(mContentContainer.getOrder().getOrderKey()), handler);
						} else {
							if(mOrderPreviewAdapter != null){
								if(mContentContainer.getOrder() != mOrderPreviewAdapter.getOrder()){
									mOrderPreviewAdapter.setOrder(mContentContainer.getOrder());
								}
								mOrderPreviewAdapter.refresh();
							}
						}
					}
					
				});
				
				mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

					@Override
					public void onMessage(Message message) {
						if(message instanceof OrderDetailMessage){
							OrderDetailMessage msg = (OrderDetailMessage)message;
							if(msg.getUpdateItems() != null && msg.getUpdateItems().size() > 0){
								CookingItem item = msg.getUpdateItems().get(0);
								if(mContentContainer != null && mContentContainer.getOrder() != null){
									Order currentOrder = mContentContainer.getOrder();
									if(currentOrder.getOrderKey().equals(String.valueOf(item.getOrderId()))){
										for(Entry<Order.Food, Integer> entry : currentOrder.getFoods().entrySet()){
											if(item.getId().equals(entry.getKey().getId())){
												entry.getKey().setStatus(item.getStatus());
												break;
											}
										}
									}
								}
							}
						}
					}
					
				});
				//this.doBindService();
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
		}
		
		@Override
		public void onDestroyView(){
			//this.doUnbindService();
			super.onDestroyView();
			this.mFoodMenuAdapter.recycle();
			System.gc();
			Log.i(TAG, "onDestroyView in FoodMenuContentFragment");
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
				if(mFoodMenuAdapter != null){
					mFoodMenuAdapter.recycle();
					mFoodMenuAdapter = null;
					System.gc();
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
				private Map<Integer, FoodMenuView> mFoodMenus;
				private Map<String, ArrayList<Food>> mFoodPages;
				private List<String> mTitleList;
			
			    public FoodMenuAdapter(Context context, Map<String, ArrayList<Food>> foods) {
			        this.mContext = context;
			        mFoodMenus = new HashMap<Integer, FoodMenuView>();
			        mTitleList = new ArrayList<String>();
			        mFoodPages = foods;
			        for(Entry<String, ArrayList<Food>> entry : foods.entrySet()){
						mTitleList.add(entry.getKey());
			        }
				}
			    
			    public void refresh(int i){
			    	if(mFoodMenus.get(i) != null){
			    		mFoodMenus.get(i).needUpdateImage(false);
			    		mFoodMenus.get(i).refresh();
			    	}
			    }
			    
			    public void refreshAllPages(){
			    	for(Entry<Integer, FoodMenuView> entry : mFoodMenus.entrySet()){
			    		entry.getValue().needUpdateImage(false);
			    		entry.getValue().refresh();
			    	}
			    }
			    
			    public void recycle(){
			    	for(Entry<Integer, FoodMenuView> entry : mFoodMenus.entrySet()){
				    	entry.getValue().recycle();
			    	}
			    	this.mFoodMenus = new HashMap<Integer, FoodMenuView>();
			    }
			    
			    @Override
			    public void destroyItem(View container, int position, Object object) {  
//			    	Log.i(TAG, "In food menu fragment destroy item");
//			    	FoodMenuView itemView = (FoodMenuView)object;
//			    	itemView.recycle();
//			    	if(mFoodMenus.get(position) != null){
//			    		mFoodMenus.remove(position);
//			    	}
			    }
			    
			    @Override
			    public CharSequence getPageTitle(int position) {
			        return (mTitleList.size() > position) ? mTitleList.get(position) : "";
			    }
			    
			    @Override  
			    public Object instantiateItem(View container, int position) {     
			    	FoodMenuView menuView;
			        if(mFoodMenus.get(position) != null){
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
			            mFoodMenus.put(position, menuView);
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
