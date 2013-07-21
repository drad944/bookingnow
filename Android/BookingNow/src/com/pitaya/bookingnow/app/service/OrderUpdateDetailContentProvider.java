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

public class OrderUpdateDetailContentProvider  extends ContentProvider {
	private OrderUpdateDetailDataBaseHelper dbhelper;
	
	private static final int ORDER_UPDATE_DETAILS = 10;
	private static final int ORDER_UPDATE_DETAIL_ID = 20;
	
	private static final String AUTHORITY = "com.pitaya.bookingnow.orderupdatedetail.contentprovider";
	private static final String BASE_PATH = "orderupdatedetails";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE  + "/orderupdatedetail";
	
	static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, ORDER_UPDATE_DETAILS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ORDER_UPDATE_DETAIL_ID);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
		    case ORDER_UPDATE_DETAILS:
			      rowsDeleted = sqlDB.delete(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, selection, selectionArgs);
			      break;
		    case ORDER_UPDATE_DETAIL_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsDeleted = sqlDB.delete(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS,  OrderTable.COLUMN_ID + "=" + id, 
			            null);
			      } else {
			    	  rowsDeleted = sqlDB.delete(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, OrderTable.COLUMN_ID + "=" + id 
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
		    case ORDER_UPDATE_DETAILS:
			      id = sqlDB.insert(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, null, values);
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		dbhelper = new OrderUpdateDetailDataBaseHelper(this.getContext());
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
	    queryBuilder.setTables(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    	case ORDER_UPDATE_DETAILS:
	    		break;
	    	case ORDER_UPDATE_DETAIL_ID:
	    		// Adding the ID to the original query
	    		queryBuilder.appendWhere(OrderUpdateDetailTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
		    case ORDER_UPDATE_DETAILS:
			      rowsUpdated = sqlDB.update(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, values, selection, selectionArgs);
			      break;
		    case ORDER_UPDATE_DETAIL_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsUpdated = sqlDB.update(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, values, OrderUpdateDetailTable.COLUMN_ID + "=" + id,  null);
			      } else {
			    	  rowsUpdated = sqlDB.update(OrderUpdateDetailTable.TABLE_ORDER_UPDATE_DETIALS, values, OrderUpdateDetailTable.COLUMN_ID + "=" + id  + " and " 
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
	    		OrderUpdateDetailTable.COLUMN_ID, 
	    		OrderUpdateDetailTable.COLUMN_ORDER_FOOD_REFID,
	    		OrderUpdateDetailTable.COLUMN_ORDER_KEY,
	    		OrderUpdateDetailTable.COLUMN_FOOD_KEY,
	    		OrderUpdateDetailTable.COLUMN_UPDATE_TYPE,
	    		OrderUpdateDetailTable.COLUMN_QUANTITY,
	    		OrderUpdateDetailTable.COLUMN_FREE,
	    		OrderUpdateDetailTable.COLUMN_PREFERENCE,
	    		OrderUpdateDetailTable.COLUMN_VERSION};
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
