package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TicketDetailTable {
	
	 public static final String TABLE_TICKET_DETIALS = "ticket_details";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_TICKET_KEY = "ticket";
	 public static final String COLUMN_FOOD_KEY = "food";
	 public static final String COLUMN_QUANTITY = "quantity";
	 public static final String COLUMN_FREE = "free";
	
	 private static final String TABLE_CREATE = "create table " 
	      + TABLE_TICKET_DETIALS
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_TICKET_KEY + " text not null,"
	      + COLUMN_FOOD_KEY +  " text not null," 
	      + COLUMN_QUANTITY + " integer not null,"
	      + COLUMN_FREE + " text not null"
	      + ");";
	 
	 public static void onCreate(SQLiteDatabase database) {
		  database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  Log.w(FoodMenuTable.class.getName(), "Upgrading ticket detail database from version "
		    + oldVersion + " to " + newVersion
		    + ", which will destroy all old data");
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_DETIALS);
		  onCreate(database);
	 }
}
