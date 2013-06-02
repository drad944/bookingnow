package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.aphidmobile.flip.FlipViewController;
import com.pitaya.bookingnow.app.domain.Food;
import com.pitaya.bookingnow.app.service.FoodMenuContentProvider;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.views.FoodBookAdapter;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.util.Log;

public class FoodBookActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private FlipViewController flipView;
	
	 /**
	   * Called when the activity is first created.
	   */
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    
		  super.onCreate(savedInstanceState);
		  
	      flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
		  this.getLoaderManager().initLoader(0, null, (LoaderCallbacks<Cursor>) this);

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
	  }
	  
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			  Bundle bundle = this.getIntent().getExtras();
			  String category = bundle.getString("category");
			  String[] projection = { 		    		
					FoodMenuTable.COLUMN_CATEGORY, 
		    		FoodMenuTable.COLUMN_DESCRIPTION,
		    		FoodMenuTable.COLUMN_FOOD_KEY,
		    		FoodMenuTable.COLUMN_IMAGE_L,
		    		FoodMenuTable.COLUMN_NAME,
		    		FoodMenuTable.COLUMN_PRICE,
		    		FoodMenuTable.COLUMN_ORDERINDEX,
		    		FoodMenuTable.COLUMN_STATUS };
			  CursorLoader cursorLoader = new CursorLoader(this, FoodMenuContentProvider.CONTENT_URI, 
					projection, FoodMenuTable.COLUMN_CATEGORY + "=?", 
					new String[]{category}, FoodMenuTable.COLUMN_ORDERINDEX);
			  return cursorLoader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			  Bundle bundle = this.getIntent().getExtras();
			  String title = bundle.getString("category");
			  setTitle(title);
			  int index = bundle.getInt("index");

			  if (cursor != null) {
					ArrayList<Food> foods = new ArrayList<Food>();
					int keyIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_FOOD_KEY);
					int nameIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_NAME);
					int priceIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_PRICE);
					int descIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_DESCRIPTION);
					int categoryIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_CATEGORY);
					int largeImageIdx = cursor.getColumnIndexOrThrow(FoodMenuTable.COLUMN_IMAGE_L);
					for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
						String key = cursor.getString(keyIdx);
						String name = cursor.getString(nameIdx);
						String desc = cursor.getString(descIdx);
						String category = cursor.getString(categoryIdx);
						float price = cursor.getFloat(priceIdx);
						byte[] image = cursor.getBlob(largeImageIdx);
						foods.add(new Food(key, name, price, desc, category, null, image));
					}
					flipView.setAdapter(new FoodBookAdapter(this, foods), index);
					setContentView(flipView);
					cursor.close();
			  }
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			Log.e("FoodBookActivity", "In loader reset");
		}
}
