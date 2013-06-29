package com.pitaya.bookingnow.app.service;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodMenuDataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "foodmenu.db";
	private static final int DATABASE_VERSION = 2;
	
	public FoodMenuDataBaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		FoodMenuTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		FoodMenuTable.onUpgrade(database, oldVersion, newVersion);
	}
	
}
