package com.pitaya.bookingnow.app.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TicketDetailDataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ticketdetail.db";
	private static final int DATABASE_VERSION = 2;
	
	public TicketDetailDataBaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		TicketDetailTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		TicketDetailTable.onUpgrade(database, oldVersion, newVersion);
	}
}
