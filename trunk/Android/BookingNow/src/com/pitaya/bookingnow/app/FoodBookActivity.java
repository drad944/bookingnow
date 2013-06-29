package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import com.aphidmobile.flip.FlipViewController;
import com.pitaya.bookingnow.app.data.FoodBookAdapter;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;

public class FoodBookActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
	
		private FlipViewController flipView;
		private Order mOrder;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
			  super.onCreate(savedInstanceState);

			  setContentView(R.layout.foodbooklayout);
		      //flipView = new FlipViewController(this, FlipViewController.HORIZONTAL);
			  flipView = (FlipViewController) findViewById(R.id.flipView);
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
					ArrayList<Food> foods = new ArrayList<Food>();
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
						boolean isRecommended = Boolean.parseBoolean(cursor.getString(indexs[3]));
						String desc = cursor.getString(indexs[4]);
						String category = cursor.getString(indexs[5]);
						String image = cursor.getString(indexs[6]);
//						foods.add(new Food(key, name, price, desc, category, null, image));
					}
					flipView.setAdapter(new FoodBookAdapter(this, foods, mOrder), index);
			  }
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			 Log.e("FoodBookActivity", "In loader reset");
		}
		
}
