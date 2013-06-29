package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FoodMenuTable {

	  public static final String TABLE_FOOD = "food";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_FOOD_KEY = "key";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_CATEGORY = "category";
	  public static final String COLUMN_DESCRIPTION = "description";
	  public static final String COLUMN_MATERIAL = "material";
	  public static final String COLUMN_PRICE = "price";
	  public static final String COLUMN_STATUS = "status";
	  public static final String COLUMN_RECOMMENDATION = "recommendation";
	  public static final String COLUMN_ORDERINDEX = "orderidx";
//	  public static final String COLUMN_IMAGE_S = "largeimage";
//	  public static final String COLUMN_IMAGE_L = "smallimage";
	  public static final String COLUMN_REVISION = "revision";
	  public static final String COLUMN_IAMGE_REVISION = "image_revision";
	  
	  private static final String TABLE_CREATE = "create table " 
		      + TABLE_FOOD
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, "
		      + COLUMN_FOOD_KEY + " text not null,"
		      + COLUMN_NAME +  " text not null,"
		      + COLUMN_CATEGORY + " text not null,"
		      + COLUMN_DESCRIPTION + " text," 
		      + COLUMN_MATERIAL + " text," 
		      + COLUMN_PRICE + " real not null," 
		      + COLUMN_STATUS + " text not null," 
		      + COLUMN_RECOMMENDATION + " text," 
		      + COLUMN_ORDERINDEX + " integer,"
//		      + COLUMN_IMAGE_S + " text not null," 
//		      + COLUMN_IMAGE_L + " text not null," 
		      + COLUMN_REVISION + " text not null," 
		      + COLUMN_IAMGE_REVISION + " text not null"
		      + ");";

	 public static void onCreate(SQLiteDatabase database) {
		 	database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
			Log.w(FoodMenuTable.class.getName(), "Upgrading database from version "
			    + oldVersion + " to " + newVersion
			    + ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
			onCreate(database);
	 }
	  
}
