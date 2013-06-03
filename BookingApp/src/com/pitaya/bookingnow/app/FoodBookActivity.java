package com.pitaya.bookingnow.app;

import java.util.ArrayList;
import com.aphidmobile.flip.FlipViewController;
import com.pitaya.bookingnow.app.domain.Food;
import com.pitaya.bookingnow.app.domain.Ticket;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.FoodMenuTable;
import com.pitaya.bookingnow.app.views.FoodBookAdapter;
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
		private Ticket mTicket;

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
			 bundle.putSerializable("ticket", mTicket);
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
			  mTicket = (Ticket)bundle.getSerializable("ticket");

			  if (cursor != null) {
					ArrayList<Food> foods = new ArrayList<Food>();
					int [] indexs = DataService.getColumnIndexs(cursor, new String[]{
							FoodMenuTable.COLUMN_FOOD_KEY,
							FoodMenuTable.COLUMN_NAME,
							FoodMenuTable.COLUMN_PRICE,
							FoodMenuTable.COLUMN_DESCRIPTION,
							FoodMenuTable.COLUMN_CATEGORY,
							FoodMenuTable.COLUMN_IMAGE_L
					});
					for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
						String key = cursor.getString(indexs[0]);
						String name = cursor.getString(indexs[1]);
						float price = cursor.getFloat(indexs[2]);
						String desc = cursor.getString(indexs[3]);
						String category = cursor.getString(indexs[4]);
						byte[] image = cursor.getBlob(indexs[5]);
						foods.add(new Food(key, name, price, desc, category, null, image));
					}
					flipView.setAdapter(new FoodBookAdapter(this, foods, mTicket), index);
			  }
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			 Log.e("FoodBookActivity", "In loader reset");
		}
		
}
