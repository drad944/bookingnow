package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrderTable {
	
	 public static final String TABLE_ORDER = "order_table";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_ORDER_KEY = "key";
	 public static final String COLUMN_TABLE_NUMBER = "table_number";
	 public static final String COLUMN_SUBMITTER = "submitter";
	 public static final String COLUMN_CUSTOMER = "customer";
	 public static final String COLUMN_PHONE = "phone";
	 public static final String COLUMN_PEOPLE_COUNT = "people_count";
	 public static final String COLUMN_LAST_MODIFACTION_DATE = "modification_date";
	 public static final String COLUMN_COMMIT_DATE = "commit_date";
	 public static final String COLUMN_STATUS = "status";
	
	 private static final String TABLE_CREATE = "create table "
	      + TABLE_ORDER
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_ORDER_KEY + " text not null,"
	      + COLUMN_TABLE_NUMBER +  " text," //in case the order is just in booking
	      + COLUMN_SUBMITTER + " text,"
	      + COLUMN_CUSTOMER + " text,"
	      + COLUMN_PHONE + " text,"
	      + COLUMN_PEOPLE_COUNT + " text,"
	      + COLUMN_LAST_MODIFACTION_DATE + " long not null," 
	      + COLUMN_COMMIT_DATE + " long," 
	      + COLUMN_STATUS + " integer not null"
	      + ");";
	
	 public static void onCreate(SQLiteDatabase database) {
 		  database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  Log.w(OrderTable.class.getName(), "Upgrading order database from version "
		    + oldVersion + " to " + newVersion
		    + ", which will destroy all old data");
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
		  onCreate(database);
	 }

}
