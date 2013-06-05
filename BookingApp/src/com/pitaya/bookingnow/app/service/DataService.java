package com.pitaya.bookingnow.app.service;

import java.util.Map.Entry;

import com.pitaya.bookingnow.app.domain.Ticket;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class DataService {

	public static CursorLoader getAllFoodData(Context context){
		String[] projection = { 		    		
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY, 
	    		FoodMenuTable.COLUMN_ID, 
	    		FoodMenuTable.COLUMN_IMAGE_S,
	    		FoodMenuTable.COLUMN_IMAGE_L,
	    		FoodMenuTable.COLUMN_MATERIAL, 
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_RECOMMENDATION,
	    		FoodMenuTable.COLUMN_ORDERINDEX,
	    		FoodMenuTable.COLUMN_STATUS };
		CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, null, null, FoodMenuTable.COLUMN_ORDERINDEX);
		return cursorLoader;
	}
	
	public static CursorLoader getFoodDataByCategory(Context context, String category){
		  String[] projection = { 		    		
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_IMAGE_L,
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_ORDERINDEX,
	    		FoodMenuTable.COLUMN_STATUS };
		  android.content.CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, FoodMenuTable.COLUMN_CATEGORY + "=?", 
				new String[]{category}, FoodMenuTable.COLUMN_ORDERINDEX);
		  return cursorLoader;
	}
	
	public static void saveNewTicket(Context context, Ticket ticket){
		ContentValues values = new ContentValues();
		String key = ticket.getTicketKey();
		String tablenum = ticket.getTableNum();
		String submitter = ticket.getSubmitter();
		Long createts = ticket.getModificationTime();
		int status = ticket.getStatus();
		values.put(TicketTable.COLUMN_TICKET_KEY, key);
		values.put(TicketTable.COLUMN_TABLE_NUMBER, tablenum);
		values.put(TicketTable.COLUMN_SUBMITTER, submitter);
		values.put(TicketTable.COLUMN_LAST_MODIFACTION_DATE, createts);
		values.put(TicketTable.COLUMN_STATUS, status);
		context.getContentResolver().insert(TicketContentProvider.CONTENT_URI, values);
	}
	
	public static void saveTicketDetails(Context context, Ticket ticket){
		String ticket_key = ticket.getTicketKey();
		ContentValues values = null;
		for(Entry<Ticket.Food, Integer> entry : ticket.getFoods().entrySet()){
			values = new ContentValues();
			String food_key = entry.getKey().getKey();
			int quantity = entry.getValue();
			values.put(TicketDetailTable.COLUMN_TICKET_KEY, ticket_key);
			values.put(TicketDetailTable.COLUMN_FOOD_KEY, food_key);
			values.put(TicketDetailTable.COLUMN_QUANTITY, quantity);
			context.getContentResolver().insert(TicketDetailContentProvider.CONTENT_URI, values);
		}
	}
	
	public static int[] getColumnIndexs(Cursor cursor, String[] columns){
		int [] indexs = new int[columns.length];
		for(int i=0; i < columns.length; i++){
			indexs[i] = cursor.getColumnIndexOrThrow(columns[i]);
		}
		return indexs;
	}
	
}
