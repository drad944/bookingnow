package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;

import com.aphidmobile.flip.FlipViewController;
import com.pitaya.bookingnow.app.data.AsyncDrawable;
import com.pitaya.bookingnow.app.data.AsyncImageTask;
import com.pitaya.bookingnow.app.data.FlipAsyncImageTask;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.views.FoodMenuView;

public class FoodGalleryActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	public static final int MENU_LOADER =  0;
	
	private ViewPager mFoodMenuViewPager; 
	private FoodViewAdapter mAdapter;
	private Order mOrder;
	private TextWatcher mTextWatcher;
	private TextView mPriceText;
	private TextView mFoodNameText;
	private EditText mQuantityText;
	private View mFoodStepper;
	private Food mCurrentFood;
	private ArrayList<Food> mFoodsList;
	private Bitmap placeholderBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);

		  this.setHomeContent();
		  mFoodsList = new ArrayList<Food>();
		  mFoodMenuViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int index) {
				if(index < mFoodsList.size()){
					mCurrentFood = mFoodsList.get(index);
					updateCurrentFoodInfo();
				}
			}
			  
		  });
		  placeholderBitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.dark_header);
		  this.getLoaderManager().initLoader(MENU_LOADER, null, (LoaderCallbacks<Cursor>) this);
    }
    
    private void updateCurrentFoodInfo(){
    	if(mCurrentFood == null){
    		return;
    	}
    	mFoodNameText.setText(mCurrentFood.getName());
    	mPriceText.setText(String.valueOf(mCurrentFood.getPrice())+"元/份");
    	if(mOrder == null){
    		mQuantityText.setVisibility(View.GONE);
     		((Button)mFoodStepper.findViewById(R.id.addbtn)).setVisibility(View.GONE);
     		((Button)mFoodStepper.findViewById(R.id.minusbtn)).setVisibility(View.GONE);
    	} else {
            boolean hasFound = false;
            for(Entry<com.pitaya.bookingnow.app.model.Order.Food, Integer> entry : mOrder.getFoods().entrySet()){
            	if(entry.getKey().getKey().equals(mCurrentFood.getKey())){
            		 mQuantityText.setText(String.valueOf(entry.getValue()));
            		 hasFound = true;
            		 break;
            	}
            }
            if(!hasFound){
            	mQuantityText.setText("0");
            }
    	}
    	
    }

    private void setHomeContent(){
    	setContentView(R.layout.foodgallerylayout);
    	RelativeLayout fooditemRL = (RelativeLayout)findViewById(R.id.fooditem);
    	mFoodMenuViewPager = (ViewPager) fooditemRL.findViewById(R.id.foodimagepager);
        mFoodNameText = (TextView) fooditemRL.findViewById(R.id.foodname);
        mFoodStepper = View.inflate(this, R.layout.foodstepper, null);
	    RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
       		 	ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        mFoodStepper.setLayoutParams(fsRL_LP);
        fooditemRL.addView(mFoodStepper);
        
        RelativeLayout fsRL  = (RelativeLayout) mFoodStepper.findViewById(R.id.food_stepper);
	    mPriceText = (TextView) fsRL.findViewById(R.id.price);
	    mQuantityText = (EditText)fsRL.findViewById(R.id.quantity);
	    
	    mTextWatcher = new TextWatcher(){
        	
			@Override
			public void afterTextChanged(Editable text) {
				int quantity = 0;
				try{
					quantity = Integer.parseInt(text.toString());
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 0;
				}
				if(mCurrentFood != null){
					Order.Food bookingfood = mOrder.new Food(mCurrentFood.getKey(), mCurrentFood.getName(), mCurrentFood.getPrice());
					bookingfood.setVersion(mCurrentFood.getVersion());
					
					
					if(mOrder.getStatus() != Constants.ORDER_PAYING && mOrder.getStatus() != Constants.ORDER_FINISHED){
						int result = DataService.updateOrderDetails(FoodGalleryActivity.this, mOrder, bookingfood, quantity);
						if(result != Order.IGNORED && (mOrder.getStatus() == Constants.ORDER_COMMITED
								|| mOrder.getStatus() == Constants.ORDER_WAITING)){
							mOrder.addUpdateFoods(FoodGalleryActivity.this, result, bookingfood, quantity);
							mOrder.markDirty(FoodGalleryActivity.this, true);
						}
					}

				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int arg1,
					int arg2, int arg3) {
				mQuantityText.setSelection(text.length());
			}

			@Override
			public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
			}
        	
        };
        
        mQuantityText.addTextChangedListener(mTextWatcher);
	    
        ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				String current = mQuantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) - 1;
				} catch(Exception e){
					Log.e("FoodBookActivity", "Fail to parse food quantity");
				}
				if(quantity < 0){
					return;
				}
				mQuantityText.setText(String.valueOf(quantity));
			}
        	
        });
        
        ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String current = mQuantityText.getText().toString();
				int quantity = 0;
				try{
					quantity = Integer.parseInt(current) + 1;
				} catch(Exception e){
					Log.e("FoodMenuView", "Fail to parse food quantity");
					quantity = 1;
				}
				mQuantityText.setText(String.valueOf(quantity));
			}
        	
        });

    }
    
	@Override
	protected void onResume() {
		 super.onResume();
	}
	
	@Override
	protected void onPause() {
		 super.onPause();
	}
  
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("FoodBook", "On Destroy");
		if(this.mAdapter != null){
			this.mAdapter.recycle();
		}
		//unbindDrawables(this.mFoodMenuViewPager);
		System.gc();
		Intent intent = new Intent(this, HomeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("order", mOrder);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        	view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            	unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            if(! (view instanceof AdapterView)){
            	((ViewGroup) view).removeAllViews();
            }
        }
    }
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		  Bundle bundle = this.getIntent().getExtras();
		  String category = bundle.getString("category");
		  return DataService.getFoodDataByCategory(this, category);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		  Bundle bundle = this.getIntent().getExtras();
		  String title = bundle.getString("category");
		  setTitle(title);
		  String foodid = bundle.getString("id");
		  mOrder = (Order)bundle.getSerializable("order");

		  if (cursor != null) {
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
					Food food = new Food(key, name, price, desc, category, isRecmd);
					food.setVersion(version);
					mFoodsList.add(food);
				}
				
				int index = 0;
				for(Food food : mFoodsList){
					if(food.getKey().equals(foodid)){
						break;
					}
					index++;
				}
				if(index >= mFoodsList.size()){
					index = 0;
				}
				if(this.mAdapter == null){
					this.mAdapter = new FoodViewAdapter();
				}
				this.mFoodMenuViewPager.setAdapter(mAdapter);
				this.mFoodMenuViewPager.setCurrentItem(index, true);
				//Update current food
				if(mFoodsList.size() > index){
					this.mCurrentFood = mFoodsList.get(index);
					this.updateCurrentFoodInfo();
				}
		  }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		 Log.e("FoodBookActivity", "In loader reset");
	}
	
	private class FoodViewAdapter extends PagerAdapter {
		
		private ArrayList<ImageView> foodImageViews = new ArrayList<ImageView>();
		private static final int SIZE = 3;
		
		@Override
		public int getCount() {
			return mFoodsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
	    @Override
	    public void destroyItem(View container, int position, Object object) {  
	    	//leave the work in recycle, we only use SIZE imageview to display
	    }
	    
	    public void recycle(){
	    	for(ImageView foodImageView : this.foodImageViews){
		    	if(foodImageView.getDrawable() != null){
		    		foodImageView.getDrawable().setCallback(null);
		    	}
	    	}
	    }
	    
	    @Override  
	    public Object instantiateItem(View container, int position) {
	         Food food = mFoodsList.get(position);
	         ImageView foodImageView = null;
	         int index = position%SIZE;
	         if(index < foodImageViews.size() && foodImageViews.get(index) != null){
	        	 foodImageView = foodImageViews.get(index);
	         } else {
	        	 foodImageView = new ImageView(FoodGalleryActivity.this);
	        	 foodImageView.setScaleType(ScaleType.FIT_CENTER);
	        	 foodImageViews.add(foodImageView);
	        	 ((ViewPager) container).addView(foodImageView);  
	         }
	         boolean needReload = true;
	         AsyncImageTask previousTask = AsyncDrawable.getTask(foodImageView);
	         if (previousTask != null) {
		           if (previousTask.getPageIndex() == position && previousTask.getImageName()
		               .equals(food.getLargeImageName()))  {
		        	   needReload = false;
		           } else {
		        	   previousTask.cancel(true);
		           }
	         }
	         if (needReload) {
		           AsyncImageTask task = new AsyncImageTask(FoodGalleryActivity.this, foodImageView, 
		        		   position,  food.getLargeImageName());
		           foodImageView.setImageDrawable(new AsyncDrawable(FoodGalleryActivity.this.getResources(), 
		        		   placeholderBitmap, task));
		           task.execute();
	         }
	        return foodImageView;
	    }
		
	}
	
}
