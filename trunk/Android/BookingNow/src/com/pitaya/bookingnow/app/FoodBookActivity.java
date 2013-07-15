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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
import com.pitaya.bookinnow.app.util.Constants;

public class FoodBookActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	public static final int MENU_LOADER =  0;
	
	private FlipViewController flipView;
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
		  flipView = (FlipViewController) findViewById(R.id.flipView);
		  flipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {

		      @Override
		      public void onViewFlipped(View view, int position) {
		    	  if(mFoodsList.size() > position){
		    		  mCurrentFood = mFoodsList.get(position);
		    	  	  updateCurrentFoodInfo();
		    	  }
		      }
		    });
		  flipView.setOnTouchListener(new View.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
			    InputMethodManager imm = (InputMethodManager)FoodBookActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE); 
        	    if(imm.isActive()){
        	    	imm.hideSoftInputFromWindow(flipView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        	    }
				return false;
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
    	setContentView(R.layout.foodbooklayout);
    	RelativeLayout fooditemRL = (RelativeLayout)findViewById(R.id.foodbookitem);
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
					if(mOrder.getStatus() == Constants.ORDER_NEW){
						DataService.updateOrderDetails(FoodBookActivity.this, mOrder, bookingfood, quantity);
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
		 flipView.onResume();
	}
	
	@Override
	protected void onPause() {
		 super.onPause();
		 flipView.onPause();
		 Intent intent = new Intent(this, HomeActivity.class);
		 Bundle bundle = new Bundle();
		 bundle.putSerializable("order", mOrder);
		 intent.putExtras(bundle);
		 startActivity(intent);
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
		  int index = bundle.getInt("index");
		  mOrder = (Order)bundle.getSerializable("order");

		  if (cursor != null) {
				int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
						FoodMenuTable.COLUMN_FOOD_KEY,
						FoodMenuTable.COLUMN_NAME,
						FoodMenuTable.COLUMN_PRICE,
						FoodMenuTable.COLUMN_RECOMMENDATION,
						FoodMenuTable.COLUMN_DESCRIPTION,
						FoodMenuTable.COLUMN_CATEGORY
				});
				for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
					String key = cursor.getString(indexs[0]);
					String name = cursor.getString(indexs[1]);
					float price = cursor.getFloat(indexs[2]);
					boolean isRecmd = Boolean.parseBoolean(cursor.getString(indexs[3]));
					String desc = cursor.getString(indexs[4]);
					String category = cursor.getString(indexs[5]);
					mFoodsList.add(new Food(key, name, price, desc, category, isRecmd));
				}
				
				//Update flip view
				flipView.setAdapter(new BaseAdapter() {
					
				      @Override
				      public int getCount() {
				        return mFoodsList.size();
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
				      public View getView(int position, View convertView, ViewGroup parent) {
				        View view = convertView;
				        Food food = mFoodsList.get(position);
				        if (view == null) {
				          final Context context = parent.getContext();
				          view = new ImageView(context);
				          ((ImageView)view).setScaleType(ScaleType.FIT_CENTER);
				        }

				         boolean needReload = true;
				         AsyncImageTask previousTask = AsyncDrawable.getTask(((ImageView)view));
				         if (previousTask != null) {
					           if (previousTask.getPageIndex() == position && previousTask.getImageName()
					               .equals(food.getLargeImageName()))  {
					        	   needReload = false;
					           } else {
					        	   previousTask.cancel(true);
					           }
				         }

				         if (needReload) {
					           AsyncImageTask task = new FlipAsyncImageTask(flipView, FoodBookActivity.this, ((ImageView)view),
					        		   position, food.getLargeImageName());
					           ((ImageView)view).setImageDrawable(new AsyncDrawable(FoodBookActivity.this.getResources(), 
					        		   placeholderBitmap, task));
					           task.execute();
				         }
				         return view;
				      }
				      
				}, index);
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
	
}
