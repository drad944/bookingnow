package com.pitaya.bookingnow.app.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TicketDataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "ticket.db";
	private static final int DATABASE_VERSION = 3;
	
	public TicketDataBaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		TicketTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		TicketTable.onUpgrade(database, oldVersion, newVersion);
	}
}
