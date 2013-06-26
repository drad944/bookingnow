package com.pitaya.bookingnow.app.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderDetailDataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "orderdetail.db";
	private static final int DATABASE_VERSION = 1;
	
	public OrderDetailDataBaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		OrderDetailTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		OrderDetailTable.onUpgrade(database, oldVersion, newVersion);
	}
}
