package com.pitaya.bookingnow.app.service;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.pitaya.bookingnow.app.model.Ticket;
import com.pitaya.bookingnow.app.model.TicketDetailAdapter;

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
				TicketTable.COLUMN_CUSTOMER,
				TicketTable.COLUMN_PHONE,
				TicketTable.COLUMN_PEOPLE_COUNT,
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
				TicketDetailTable.COLUMN_FREE
		};
		String[] foodprojection = {
				FoodMenuTable.COLUMN_NAME,
				FoodMenuTable.COLUMN_PRICE
		};
		Cursor cursor = context.getContentResolver().query(TicketDetailContentProvider.CONTENT_URI, projection, 
				TicketDetailTable.COLUMN_TICKET_KEY +"=?", new String[]{ticket.getTicketKey()}, null);
		if(cursor != null){
			int indexes[] = getColumnIndexs(cursor, projection);
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String food_key = cursor.getString(indexes[0]);
				int quantity = cursor.getInt(indexes[1]);
				boolean isFree = Boolean.parseBoolean(cursor.getString(indexes[2]));
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
				Ticket.Food food = ticket.new Food(food_key, food_name, price);
				food.setFree(isFree);
				ticket.addFood(food, quantity);
			}
			cursor.close();
		}
	}

	public static void saveNewTicket(Context context, Ticket ticket){
		ContentValues values = new ContentValues();
		String key = ticket.getTicketKey();
		Long createts = ticket.getModificationTime();
		int status = ticket.getStatus();
		values.put(TicketTable.COLUMN_TICKET_KEY, key);
		values.put(TicketTable.COLUMN_LAST_MODIFACTION_DATE, createts);
		values.put(TicketTable.COLUMN_STATUS, status);
		String tablenum = ticket.getTableNum();
		String submitter = ticket.getSubmitter();
		if(tablenum != null && submitter != null){
			values.put(TicketTable.COLUMN_TABLE_NUMBER, tablenum);
			values.put(TicketTable.COLUMN_SUBMITTER, submitter);
		} else {
			String customer = ticket.getCustomerName();
			String phone = ticket.getPhoneNumber();
			int count = ticket.getPeopleCount();
			values.put(TicketTable.COLUMN_CUSTOMER, customer);
			values.put(TicketTable.COLUMN_PHONE, phone);
			values.put(TicketTable.COLUMN_PEOPLE_COUNT, count);
		}
		context.getContentResolver().insert(TicketContentProvider.CONTENT_URI, values);
	}
	
	public static void saveTicketDetails(Context context, Ticket ticket){
		String ticket_key = ticket.getTicketKey();
		ContentValues values = null;
		for(Entry<Ticket.Food, Integer> entry : ticket.getFoods().entrySet()){
			values = new ContentValues();
			String food_key = entry.getKey().getKey();
			int quantity = entry.getValue();
			boolean isFree = entry.getKey().isFree();
			values.put(TicketDetailTable.COLUMN_TICKET_KEY, ticket_key);
			values.put(TicketDetailTable.COLUMN_FOOD_KEY, food_key);
			values.put(TicketDetailTable.COLUMN_QUANTITY, quantity);
			values.put(TicketDetailTable.COLUMN_FREE, isFree);
			context.getContentResolver().insert(TicketDetailContentProvider.CONTENT_URI, values);
		}
	}
	
	public static int updateTicketDetails(Context context, Ticket ticket, Ticket.Food food, int quantity){
		int result = ticket.addFood(food, quantity);
		switch(result){
			case Ticket.REMOVED:
				removeFoodOfTicket(context, ticket, food);
				break;
			case Ticket.ADDED:
				addFoodOfTicket(context, ticket, food, quantity);
				break;
			case Ticket.UPDATED:
				updateFoodQuantity(context, ticket, food, quantity);
				break;
		};
		return result;
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
	
	public static void addFoodOfTicket(Context context,  Ticket ticket, Ticket.Food food, int quantity){
		ContentValues values = new ContentValues();
		values.put(TicketDetailTable.COLUMN_TICKET_KEY, ticket.getTicketKey());
		values.put(TicketDetailTable.COLUMN_FOOD_KEY, food.getKey());
		values.put(TicketDetailTable.COLUMN_QUANTITY, quantity);
		values.put(TicketDetailTable.COLUMN_FREE, food.isFree());
		context.getContentResolver().insert(TicketDetailContentProvider.CONTENT_URI, values);
		ticket.markDirty(false);
	}
	
	public static void removeFoodOfTicket(Context context, Ticket ticket, Ticket.Food food){
		context.getContentResolver().delete(TicketDetailContentProvider.CONTENT_URI,
				TicketDetailTable.COLUMN_TICKET_KEY + "=? and " + TicketDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ ticket.getTicketKey(), food.getKey()});
		ticket.markDirty(false);
	}
	
	//update the quantity of the food
	public static void updateFoodQuantity(Context context, Ticket ticket, Ticket.Food food, int newquantity){
		ContentValues values = new ContentValues();
		values.put(TicketDetailTable.COLUMN_QUANTITY, newquantity);
		context.getContentResolver().update(TicketDetailContentProvider.CONTENT_URI, values, 
				TicketDetailTable.COLUMN_TICKET_KEY + "=? and " + TicketDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ ticket.getTicketKey(), food.getKey()});
		ticket.markDirty(false);
	}
	
	//update the free status of the food
	public static void updateFoodFreeStatus(Context context, Ticket ticket, Ticket.Food food){
		ContentValues values = new ContentValues();
		values.put(TicketDetailTable.COLUMN_FREE, String.valueOf(food.isFree()));
		context.getContentResolver().update(TicketDetailContentProvider.CONTENT_URI, values, 
				TicketDetailTable.COLUMN_TICKET_KEY + "=? and " + TicketDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ticket.getTicketKey(), food.getKey()});
		ticket.markDirty(false);
	}
	
	public static int[] getColumnIndexs(Cursor cursor, String[] columns){
		int [] indexs = new int[columns.length];
		for(int i=0; i < columns.length; i++){
			indexs[i] = cursor.getColumnIndexOrThrow(columns[i]);
		}
		return indexs;
	}
	
}
