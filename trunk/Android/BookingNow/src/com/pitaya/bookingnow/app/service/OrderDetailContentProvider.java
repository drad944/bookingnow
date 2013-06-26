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

public class OrderDetailContentProvider  extends ContentProvider {
	private OrderDetailDataBaseHelper dbhelper;
	
	private static final int ORDER_DETAILS = 10;
	private static final int ORDER_DETAIL_ID = 20;
	
	private static final String AUTHORITY = "com.pitaya.bookingnow.orderdetail.contentprovider";
	private static final String BASE_PATH = "orderdetails";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE  + "/orderdetail";
	
	static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, ORDER_DETAILS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ORDER_DETAIL_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
		    case ORDER_DETAILS:
			      rowsDeleted = sqlDB.delete(OrderDetailTable.TABLE_ORDER_DETIALS, selection, selectionArgs);
			      break;
		    case ORDER_DETAIL_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsDeleted = sqlDB.delete(OrderDetailTable.TABLE_ORDER_DETIALS,  OrderTable.COLUMN_ID + "=" + id, 
			            null);
			      } else {
			    	  rowsDeleted = sqlDB.delete(OrderDetailTable.TABLE_ORDER_DETIALS, OrderTable.COLUMN_ID + "=" + id 
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
		    case ORDER_DETAILS:
			      id = sqlDB.insert(OrderDetailTable.TABLE_ORDER_DETIALS, null, values);
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		dbhelper = new OrderDetailDataBaseHelper(this.getContext());
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
	    queryBuilder.setTables(OrderDetailTable.TABLE_ORDER_DETIALS);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    	case ORDER_DETAILS:
	    		break;
	    	case ORDER_DETAIL_ID:
	    		// Adding the ID to the original query
	    		queryBuilder.appendWhere(OrderDetailTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
		    case ORDER_DETAILS:
			      rowsUpdated = sqlDB.update(OrderDetailTable.TABLE_ORDER_DETIALS, values, selection, selectionArgs);
			      break;
		    case ORDER_DETAIL_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsUpdated = sqlDB.update(OrderDetailTable.TABLE_ORDER_DETIALS, values, OrderDetailTable.COLUMN_ID + "=" + id,  null);
			      } else {
			    	  rowsUpdated = sqlDB.update(OrderDetailTable.TABLE_ORDER_DETIALS, values, OrderDetailTable.COLUMN_ID + "=" + id  + " and " 
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
	    		OrderDetailTable.COLUMN_ID, 
	    		OrderDetailTable.COLUMN_ORDER_KEY,
	    		OrderDetailTable.COLUMN_FOOD_KEY,
	    		OrderDetailTable.COLUMN_QUANTITY,
	    		OrderDetailTable.COLUMN_FREE};
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
