package com.pitaya.bookingnow.app.service;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TicketTable {
	
	 public static final String TABLE_TICKET = "ticket";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_TICKET_KEY = "key";
	 public static final String COLUMN_TABLE_NUMBER = "table_num";
	 public static final String COLUMN_SUBMITTER = "submitter";
	 public static final String COLUMN_LAST_MODIFACTION_DATE = "modification_date";
	 public static final String COLUMN_COMMIT_DATE = "commit_date";
	 public static final String COLUMN_STATUS = "status";
	
	 private static final String TABLE_CREATE = "create table " 
	      + TABLE_TICKET
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_TICKET_KEY + " text not null,"
	      + COLUMN_TABLE_NUMBER +  " text," //in case the ticket is just in booking
	      + COLUMN_SUBMITTER + " text not null,"
	      + COLUMN_LAST_MODIFACTION_DATE + " long not null," 
	      + COLUMN_COMMIT_DATE + " long," 
	      + COLUMN_STATUS + " integer not null"
	      + ");";
	
	 public static void onCreate(SQLiteDatabase database) {
 		  database.execSQL(TABLE_CREATE);
	 }
	 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		  Log.w(FoodMenuTable.class.getName(), "Upgrading ticket database from version "
		    + oldVersion + " to " + newVersion
		    + ", which will destroy all old data");
		  database.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
		  onCreate(database);
	 }

}
