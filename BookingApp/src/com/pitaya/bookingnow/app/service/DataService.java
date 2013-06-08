package com.pitaya.bookingnow.app.service;

import java.util.ArrayList;
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
		  CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, FoodMenuTable.COLUMN_CATEGORY + "=?", 
				new String[]{category}, FoodMenuTable.COLUMN_ORDERINDEX);
		  return cursorLoader;
	}
	
	public static CursorLoader getTicketListByStatus(Context context, int status){
		String[] projection = {
				TicketTable.COLUMN_TICKET_KEY,
				TicketTable.COLUMN_TABLE_NUMBER,
				TicketTable.COLUMN_SUBMITTER,
				TicketTable.COLUMN_LAST_MODIFACTION_DATE,
				TicketTable.COLUMN_COMMIT_DATE,
				TicketTable.COLUMN_STATUS
		};
		if(status == Ticket.ALL){
			return new CursorLoader(context, TicketContentProvider.CONTENT_URI, 
					projection, null, null, TicketTable.COLUMN_LAST_MODIFACTION_DATE);
		} else {
			return new CursorLoader(context, TicketContentProvider.CONTENT_URI, 
					projection, TicketTable.COLUMN_STATUS + "=?", 
					new String[]{String.valueOf(status)},TicketTable.COLUMN_LAST_MODIFACTION_DATE);
		}
	}
	
	public static void getFoodsOfTicket(Context context, Ticket ticket){
		String[] projection = {
				TicketDetailTable.COLUMN_FOOD_KEY,
				TicketDetailTable.COLUMN_QUANTITY,
		};
		Cursor cursor = context.getContentResolver().query(TicketDetailContentProvider.CONTENT_URI, projection, 
				TicketDetailTable.COLUMN_TICKET_KEY +"=?", new String[]{ticket.getTicketKey()}, null);
		if(cursor != null){
			int indexes[] = getColumnIndexs(cursor, projection);
			String[] foodprojection = {
					FoodMenuTable.COLUMN_NAME,
					FoodMenuTable.COLUMN_PRICE
			};
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String food_key = cursor.getString(indexes[0]);
				int quantity = cursor.getInt(indexes[1]);
				String food_name = null;
				float price = 0f;
				Cursor subcursor = context.getContentResolver().query(FoodMenuContentProvider.CONTENT_URI, foodprojection,
						FoodMenuTable.COLUMN_FOOD_KEY + "=?", new String[]{food_key},null);
				if(subcursor != null && subcursor.moveToFirst()){
					int [] subindexes = getColumnIndexs(subcursor, foodprojection);
					food_name = subcursor.getString(subindexes[0]);
					price = subcursor.getFloat(subindexes[1]);
					subcursor.close();
				}
				ticket.addFood(food_key, food_name, price, quantity);
			}
			cursor.close();
		}
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
	
	public static void removeTicket(Context context, String ticket_key){
		context.getContentResolver().delete(TicketDetailContentProvider.CONTENT_URI,
				TicketDetailTable.COLUMN_TICKET_KEY +"=?",
				new String[]{ticket_key});
		context.getContentResolver().delete(TicketContentProvider.CONTENT_URI,
				TicketTable.COLUMN_TICKET_KEY +"=?",
				new String[]{ticket_key});
	}
	
	public static void removeFoodsOfTicket(Context context, String ticket_key){
		context.getContentResolver().delete(TicketDetailContentProvider.CONTENT_URI,
				TicketDetailTable.COLUMN_TICKET_KEY +"=?",
				new String[]{ticket_key});
	}
	
	public static int[] getColumnIndexs(Cursor cursor, String[] columns){
		int [] indexs = new int[columns.length];
		for(int i=0; i < columns.length; i++){
			indexs[i] = cursor.getColumnIndexOrThrow(columns[i]);
		}
		return indexs;
	}
	
}
