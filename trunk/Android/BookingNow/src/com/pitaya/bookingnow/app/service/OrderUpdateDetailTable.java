package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderUpdateDetailTable {
	
	 public static final String TABLE_ORDER_UPDATE_DETIALS = "order_update_details";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_ORDER_FOOD_REFID = "order_food_ref_id";
	 public static final String COLUMN_ORDER_KEY = "order_key";
	 public static final String COLUMN_FOOD_KEY = "food_key";
	 public static final String COLUMN_UPDATE_TYPE = "type";
	 public static final String COLUMN_QUANTITY = "quantity";
	 public static final String COLUMN_FREE = "free";
	 public static final String COLUMN_PREFERENCE = "preference";
	 public static final String COLUMN_VERSION = "version";
	
	 private static final String TABLE_CREATE = "create table " 
	      + TABLE_ORDER_UPDATE_DETIALS
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_ORDER_FOOD_REFID + " text,"
	      + COLUMN_ORDER_KEY + " text not null,"
	      + COLUMN_FOOD_KEY +  " text not null," 
	      + COLUMN_UPDATE_TYPE + " integer not null,"
	      + COLUMN_QUANTITY + " integer,"
	      + COLUMN_FREE + " text,"
	      + COLUMN_PREFERENCE + " text,"
	      + COLUMN_VERSION + " text"
	      + ");";
	 
	 public static void onCreate(SQLiteDatabase database) {
		  database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  Log.w(FoodMenuTable.class.getName(), "Upgrading order update details database from version "
		    + oldVersion + " to " + newVersion
		    + ", which will destroy all old data");
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_UPDATE_DETIALS);
		  onCreate(database);
	 }
}
