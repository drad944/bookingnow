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

public class TicketContentProvider extends ContentProvider {

	private TicketDataBaseHelper dbhelper;
	
	private static final int TICKETS = 10;
	private static final int TICKET_ID = 20;
	
	private static final String AUTHORITY = "com.pitaya.bookingnow.ticket.contentprovider";
	private static final String BASE_PATH = "tickets";
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE  + "/" + BASE_PATH;
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE  + "/ticket";
	
	static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, TICKETS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TICKET_ID);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsDeleted = 0;
	    switch (uriType) {
		    case TICKETS:
			      rowsDeleted = sqlDB.delete(TicketTable.TABLE_TICKET, selection, selectionArgs);
			      break;
		    case TICKET_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsDeleted = sqlDB.delete(TicketTable.TABLE_TICKET,  TicketTable.COLUMN_ID + "=" + id, 
			            null);
			      } else {
			    	  rowsDeleted = sqlDB.delete(TicketTable.TABLE_TICKET, TicketTable.COLUMN_ID + "=" + id 
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    long id = 0;
	    switch (uriType) {
		    case TICKETS:
			      id = sqlDB.insert(TicketTable.TABLE_TICKET, null, values);
			      break;
		    default:
		    	  throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		dbhelper = new TicketDataBaseHelper(this.getContext());
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
	    queryBuilder.setTables(TicketTable.TABLE_TICKET);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
	    	case TICKETS:
	    		break;
	    	case TICKET_ID:
	    		// Adding the ID to the original query
	    		queryBuilder.appendWhere(TicketTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
	public int update(Uri uri, ContentValues values, String selection,String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = dbhelper.getWritableDatabase();
	    int rowsUpdated = 0;
	    switch (uriType) {
		    case TICKETS:
			      rowsUpdated = sqlDB.update(TicketTable.TABLE_TICKET, values, selection, selectionArgs);
			      break;
		    case TICKET_ID:
			      String id = uri.getLastPathSegment();
			      if (TextUtils.isEmpty(selection)) {
			    	  rowsUpdated = sqlDB.update(TicketTable.TABLE_TICKET, values, TicketTable.COLUMN_ID + "=" + id,  null);
			      } else {
			    	  rowsUpdated = sqlDB.update(TicketTable.TABLE_TICKET, values, TicketTable.COLUMN_ID + "=" + id  + " and " 
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
	    		TicketTable.COLUMN_ID, 
	    		TicketTable.COLUMN_TICKET_KEY,
	    		TicketTable.COLUMN_TABLE_NUMBER,
	    		TicketTable.COLUMN_SUBMITTER,
	    		TicketTable.COLUMN_LAST_MODIFACTION_DATE,
	    		TicketTable.COLUMN_COMMIT_DATE,
	    		TicketTable.COLUMN_STATUS};
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
