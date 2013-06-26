package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderDetailTable {
	
	 public static final String TABLE_ORDER_DETIALS = "order_details";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_ORDER_KEY = "order_key";
	 public static final String COLUMN_FOOD_KEY = "food_key";
	 public static final String COLUMN_QUANTITY = "quantity";
	 public static final String COLUMN_FREE = "free";
	
	 private static final String TABLE_CREATE = "create table " 
	      + TABLE_ORDER_DETIALS
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_ORDER_KEY + " text not null,"
	      + COLUMN_FOOD_KEY +  " text not null," 
	      + COLUMN_QUANTITY + " integer not null,"
	      + COLUMN_FREE + " text not null"
	      + ");";
	 
	 public static void onCreate(SQLiteDatabase database) {
		  database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  Log.w(FoodMenuTable.class.getName(), "Upgrading order detail database from version "
		    + oldVersion + " to " + newVersion
		    + ", which will destroy all old data");
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETIALS);
		  onCreate(database);
	 }
}
