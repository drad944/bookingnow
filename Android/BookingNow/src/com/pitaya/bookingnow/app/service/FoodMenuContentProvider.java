package com.pitaya.bookingnow.app.service;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class FoodMenuContentProvider extends ContentProvider {
	
	private FoodMenuDataBaseHelper dbhelper;
	
	private static final int FOODS = 10;
	private static final int FOOD_ID = 20;
	
	private static final String AUTHORITY = "com.pitaya.bookingnow.food.contentprovider";
	private static final String BASE_PATH = "foods";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE  + "/food";
	
	static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, FOODS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FOOD_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
		    case FOODS:
			      rowsDeleted = sqlDB.delete(FoodMenuTable.TABLE_FOOD, selection, selectionArgs);
			      break;
		    case FOOD_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsDeleted = sqlDB.delete(FoodMenuTable.TABLE_FOOD,  FoodMenuTable.COLUMN_ID + "=" + id, 
			            null);
			      } else {
			        rowsDeleted = sqlDB.delete(FoodMenuTable.TABLE_FOOD, FoodMenuTable.COLUMN_ID + "=" + id 
			            + " and " + selection, selectionArgs);
			      }
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    long id = 0;
	    switch (uriType) {
		    case FOODS:
			      id = sqlDB.insert(FoodMenuTable.TABLE_FOOD, null, values);
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		dbhelper = new FoodMenuDataBaseHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	    
		// Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // Check if the caller has requested a column which does not exists
	    checkColumns(projection);

	    // Set the table
	    queryBuilder.setTables(FoodMenuTable.TABLE_FOOD);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    	case FOODS:
	    		break;
	    	case FOOD_ID:
	    		// Adding the ID to the original query
	    		queryBuilder.appendWhere(FoodMenuTable.COLUMN_ID + "=" + uri.getLastPathSegment());
	    		break;
	    	default:
	    		throw new IllegalArgumentException("Unknown URI: " + uri);
	    }

	    SQLiteDatabase db = dbhelper.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    // Make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
		    case FOODS:
			      rowsUpdated = sqlDB.update(FoodMenuTable.TABLE_FOOD, values, selection, selectionArgs);
			      break;
		    case FOOD_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsUpdated = sqlDB.update(FoodMenuTable.TABLE_FOOD, values, FoodMenuTable.COLUMN_ID + "=" + id,  null);
			      } else {
			    	  rowsUpdated = sqlDB.update(FoodMenuTable.TABLE_FOOD, values, FoodMenuTable.COLUMN_ID + "=" + id  + " and " 
			            + selection, selectionArgs);
			      }
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
		    String[] available = {
		    		FoodMenuTable.COLUMN_ID, 
		    		FoodMenuTable.COLUMN_NAME, 
		    		FoodMenuTable.COLUMN_FOOD_KEY, 
		    		FoodMenuTable.COLUMN_CATEGORY, 
		    		FoodMenuTable.COLUMN_DESCRIPTION,
		    		FoodMenuTable.COLUMN_IMAGE_S,
		    		FoodMenuTable.COLUMN_IMAGE_L,
		    		FoodMenuTable.COLUMN_MATERIAL, 
		    		FoodMenuTable.COLUMN_PRICE,
		    		FoodMenuTable.COLUMN_RECOMMENDATION,
		    		FoodMenuTable.COLUMN_ORDERINDEX,
		    		FoodMenuTable.COLUMN_REVISION, 
		    		FoodMenuTable.COLUMN_STATUS };
		    if (projection != null) {
			      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			      // Check if all columns which are requested are available
			      if (!availableColumns.containsAll(requestedColumns)) {
			    	  throw new IllegalArgumentException("Unknown columns in projection");
			      }
		    }
	 }
	
}
