package com.pitaya.bookingnow.app.service;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class DataService {

	public static CursorLoader getAllFoodData(Context context){
		String[] projection = { 		    		
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY, 
	    		FoodMenuTable.COLUMN_ID, 
	    		FoodMenuTable.COLUMN_IMAGE_S,
	    		FoodMenuTable.COLUMN_IMAGE_L,
	    		FoodMenuTable.COLUMN_MATERIAL, 
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_RECOMMENDATION,
	    		FoodMenuTable.COLUMN_ORDERINDEX,
	    		FoodMenuTable.COLUMN_STATUS };
		CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, null, null, FoodMenuTable.COLUMN_ORDERINDEX);
		return cursorLoader;
	}
	
	public static android.content.CursorLoader getFoodDataByCategory(Context context, String category){
		  String[] projection = { 		    		
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_IMAGE_L,
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_ORDERINDEX,
	    		FoodMenuTable.COLUMN_STATUS };
		  android.content.CursorLoader cursorLoader = new android.content.CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, FoodMenuTable.COLUMN_CATEGORY + "=?", 
				new String[]{category}, FoodMenuTable.COLUMN_ORDERINDEX);
		  return cursorLoader;
	}
	
	public static int[] getColumnIndexs(Cursor cursor, String[] columns){
		int [] indexs = new int[columns.length];
		for(int i=0; i < columns.length; i++){
			indexs[i] = cursor.getColumnIndexOrThrow(columns[i]);
		}
		return indexs;
	}
	
}
