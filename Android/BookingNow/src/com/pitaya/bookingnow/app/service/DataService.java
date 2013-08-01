package com.pitaya.bookingnow.app.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.UpdateFood;
import com.pitaya.bookingnow.app.util.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

public class DataService {

	public static CursorLoader getFoodCheckingData(Context context){
		String[] projection = {
	    		FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_REVISION, 
	    		FoodMenuTable.COLUMN_IAMGE_REVISION};
		CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, null, null, null);
		return cursorLoader;
	}
	
	public static CursorLoader getAllFoodData(Context context){
		String[] projection = { 		    		
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY, 
	    		FoodMenuTable.COLUMN_ID,
	    		FoodMenuTable.COLUMN_MATERIAL, 
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_RECOMMENDATION,
	    		FoodMenuTable.COLUMN_REVISION,
	    		FoodMenuTable.COLUMN_STATUS };
		CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, null, null, null);
		return cursorLoader;
	}
	
	public static CursorLoader getFoodDataByCategory(Context context, String category){
		  String[] projection = {
				FoodMenuTable.COLUMN_CATEGORY, 
	    		FoodMenuTable.COLUMN_DESCRIPTION,
	    		FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_NAME,
	    		FoodMenuTable.COLUMN_PRICE,
	    		FoodMenuTable.COLUMN_RECOMMENDATION,
	    		FoodMenuTable.COLUMN_ORDERINDEX,
	    		FoodMenuTable.COLUMN_REVISION,
	    		FoodMenuTable.COLUMN_STATUS };
		  CursorLoader cursorLoader = new CursorLoader(context, FoodMenuContentProvider.CONTENT_URI, 
				projection, FoodMenuTable.COLUMN_CATEGORY + "=?",  new String[]{category}, null);
		  return cursorLoader;
	}
	
	public static CursorLoader getOrderListByStatus(Context context, int status){
		String[] projection = {
				OrderTable.COLUMN_ORDER_KEY,
				OrderTable.COLUMN_TABLE_NUMBER,
				OrderTable.COLUMN_SUBMITTER,
				OrderTable.COLUMN_CUSTOMER,
				OrderTable.COLUMN_PHONE,
				OrderTable.COLUMN_PEOPLE_COUNT,
				OrderTable.COLUMN_LAST_MODIFACTION_DATE,
				OrderTable.COLUMN_COMMIT_DATE,
				OrderTable.COLUMN_STATUS
		};
		if(status == Constants.ALL){
			return new CursorLoader(context, OrderContentProvider.CONTENT_URI, 
					projection, null, null, OrderTable.COLUMN_LAST_MODIFACTION_DATE);
		} else {
			return new CursorLoader(context, OrderContentProvider.CONTENT_URI, 
					projection, OrderTable.COLUMN_STATUS + "=?", 
					new String[]{String.valueOf(status)},OrderTable.COLUMN_LAST_MODIFACTION_DATE);
		}
	}
	
	public static void getFoodVersions(Context context, Map<String, Long> foodVersions){
		String[] projection = {
				FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_REVISION
	    };
		String where = "";
		String[] values = new String[foodVersions.size()];
		int i=0;
		for(Entry<String, Long> entry : foodVersions.entrySet()){
			where += FoodMenuTable.COLUMN_FOOD_KEY + "=? or ";
			values[i++] = entry.getKey();
		}
		where = where.substring(0, where.lastIndexOf(" or"));
		Cursor cursor = context.getContentResolver().query(FoodMenuContentProvider.CONTENT_URI, projection, 
				where, values, null);
		if(cursor != null){
			int indexes[] = getColumnIndexs(cursor, projection);
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String food_key = cursor.getString(indexes[0]);
				Long version = Long.parseLong(cursor.getString(indexes[1]));
				foodVersions.put(food_key, version);
			}
			cursor.close();
		}
	}
	
	public static ArrayList<Food> getFoodsForUpdate(Context context){
		String[] projection = {
	    		FoodMenuTable.COLUMN_FOOD_KEY,
	    		FoodMenuTable.COLUMN_REVISION, 
	    		FoodMenuTable.COLUMN_IAMGE_REVISION};
		Cursor cursor = context.getContentResolver().query(FoodMenuContentProvider.CONTENT_URI, projection, 
				null, null, null);
		if(cursor != null) {
			int indexes[] = getColumnIndexs(cursor, projection);
			ArrayList<Food> check_foods = new ArrayList<Food>();
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String id = cursor.getString(indexes[0]);
				Long food_v = Long.parseLong(cursor.getString(indexes[1]));
				Long image_v = Long.parseLong(cursor.getString(indexes[2]));
				check_foods.add(new Food(id, food_v, image_v));
			}
			cursor.close();
			return check_foods;
		}
		return null;
	}
	
	public static void addNewFood(Context context, Food food){
		ContentValues values = new ContentValues();
		values.put(FoodMenuTable.COLUMN_FOOD_KEY, food.getKey());
		values.put(FoodMenuTable.COLUMN_NAME, food.getName());
		values.put(FoodMenuTable.COLUMN_CATEGORY, food.getCategory());
		values.put(FoodMenuTable.COLUMN_PRICE, food.getPrice());
		values.put(FoodMenuTable.COLUMN_DESCRIPTION, food.getDescription());
		values.put(FoodMenuTable.COLUMN_RECOMMENDATION, food.isRecommended());
		values.put(FoodMenuTable.COLUMN_STATUS, food.getStatus());
		values.put(FoodMenuTable.COLUMN_REVISION, food.getVersion());
		values.put(FoodMenuTable.COLUMN_IAMGE_REVISION, food.getImageVersion());
		context.getContentResolver().insert(FoodMenuContentProvider.CONTENT_URI, values);
	}
	
	public static void removeFood(Context context, String foodkey){
		context.getContentResolver().delete(FoodMenuContentProvider.CONTENT_URI,
				FoodMenuTable.COLUMN_FOOD_KEY +"=?",
				new String[]{foodkey});
	}
	
	public static void updateFood(Context context, Food food){
		ContentValues values = new ContentValues();
		values.put(FoodMenuTable.COLUMN_NAME, food.getName());
		values.put(FoodMenuTable.COLUMN_CATEGORY, food.getCategory());
		values.put(FoodMenuTable.COLUMN_PRICE, food.getPrice());
		values.put(FoodMenuTable.COLUMN_DESCRIPTION, food.getDescription());
		values.put(FoodMenuTable.COLUMN_RECOMMENDATION, food.isRecommended());
		values.put(FoodMenuTable.COLUMN_STATUS, food.getStatus());
		values.put(FoodMenuTable.COLUMN_REVISION, food.getVersion());
		if(food.getImageVersion() != null && food.getImageVersion() > 0L){
			values.put(FoodMenuTable.COLUMN_IAMGE_REVISION, food.getImageVersion());
		}
		context.getContentResolver().update(FoodMenuContentProvider.CONTENT_URI, values, 
				FoodMenuTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{food.getKey()});
	}
	
	public static void updateFoodImage(Context context, String foodkey, String imageversion){
		ContentValues values = new ContentValues();
		values.put(FoodMenuTable.COLUMN_IAMGE_REVISION, imageversion);
		context.getContentResolver().update(FoodMenuContentProvider.CONTENT_URI, values, 
				FoodMenuTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ foodkey});
	}
	
	public static void saveFoodId(Context context, Order order, Order.Food food){
		ContentValues values = new ContentValues();
		values.put(OrderDetailTable.COLUMN_ORDER_FOOD_REFID, food.getId());
		context.getContentResolver().update(OrderDetailContentProvider.CONTENT_URI, values,
				OrderDetailTable.COLUMN_ORDER_KEY + "=? and " + OrderDetailTable.COLUMN_FOOD_KEY + "=?",
				new String[]{order.getOrderKey(), food.getKey()});
	}
	
	
	public static void getFoodDetails(Context context, Order.Food food){
		String[] projection = {
				FoodMenuTable.COLUMN_NAME,
				FoodMenuTable.COLUMN_PRICE,
				FoodMenuTable.COLUMN_REVISION
		};
		Cursor cursor = context.getContentResolver().query(FoodMenuContentProvider.CONTENT_URI, projection,
				FoodMenuTable.COLUMN_FOOD_KEY + "=?", new String[]{food.getKey()},null);
		if(cursor != null && cursor.moveToFirst()){
			int indexes[] = getColumnIndexs(cursor, projection);
			food.setName(cursor.getString(indexes[0]));
			food.setPrice(cursor.getFloat(indexes[1]));
			food.setVersion(cursor.getLong(indexes[2]));
			cursor.close();
		}
	}
	
	public static void getFoodsOfOrder(Context context, Order order){
		String[] projection = {
				OrderDetailTable.COLUMN_FOOD_KEY,
				OrderDetailTable.COLUMN_QUANTITY,
				OrderDetailTable.COLUMN_FREE,
				OrderDetailTable.COLUMN_ORDER_FOOD_REFID
		};
		String[] foodprojection = {
				FoodMenuTable.COLUMN_NAME,
				FoodMenuTable.COLUMN_PRICE,
				FoodMenuTable.COLUMN_REVISION
		};
		Cursor cursor = context.getContentResolver().query(OrderDetailContentProvider.CONTENT_URI, projection, 
				OrderDetailTable.COLUMN_ORDER_KEY +"=?", new String[]{order.getOrderKey()}, null);
		if(cursor != null){
			int indexes[] = getColumnIndexs(cursor, projection);
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				String food_key = cursor.getString(indexes[0]);
				int quantity = cursor.getInt(indexes[1]);
				boolean isFree = Boolean.parseBoolean(cursor.getString(indexes[2]));
				Long order_food_refid = cursor.getLong(indexes[3]);
				String food_name = null;
				float price = 0f;
				Long version = null;
				Cursor subcursor = context.getContentResolver().query(FoodMenuContentProvider.CONTENT_URI, foodprojection,
						FoodMenuTable.COLUMN_FOOD_KEY + "=?", new String[]{food_key},null);
				if(subcursor != null && subcursor.moveToFirst()){
					int [] subindexes = getColumnIndexs(subcursor, foodprojection);
					food_name = subcursor.getString(subindexes[0]);
					price = subcursor.getFloat(subindexes[1]);
					version = subcursor.getLong(subindexes[2]);
					subcursor.close();
				}
				Order.Food food = order.new Food(food_key, food_name, price);
				if(order_food_refid == -1){
					food.setId(null);
				} else {
					food.setId(order_food_refid);
				}
				food.setFree(isFree);
				food.setVersion(version);
				order.addFood(food, quantity);
			}
			cursor.close();
		}
	}

	public static void saveNewOrder(Context context, Order order){
		ContentValues values = new ContentValues();
		String key = order.getOrderKey();
		Long modifyts = order.getModificationTime();
		int status = order.getStatus();
		values.put(OrderTable.COLUMN_ORDER_KEY, key);
		values.put(OrderTable.COLUMN_LAST_MODIFACTION_DATE, modifyts);
		values.put(OrderTable.COLUMN_COMMIT_DATE, order.getSubmitTime());
		values.put(OrderTable.COLUMN_STATUS, status);
		values.put(OrderTable.COLUMN_DIRTY, order.isDirty());
		String tablenum = order.getTableNum();
		String submitter = order.getSubmitter();
		if(tablenum != null){
			values.put(OrderTable.COLUMN_TABLE_NUMBER, tablenum);
			values.put(OrderTable.COLUMN_SUBMITTER, submitter);
		} else {
			String customer = order.getCustomerName();
			String phone = order.getPhoneNumber();
			int count = order.getPeopleCount();
			values.put(OrderTable.COLUMN_SUBMITTER, submitter);
			values.put(OrderTable.COLUMN_CUSTOMER, customer);
			values.put(OrderTable.COLUMN_PHONE, phone);
			values.put(OrderTable.COLUMN_PEOPLE_COUNT, count);
		}
		context.getContentResolver().insert(OrderContentProvider.CONTENT_URI, values);
	}
	
	public static void resetOrderUpdateDetails(Context context, Order order){
		context.getContentResolver().delete(OrderUpdateDetailContentProvider.CONTENT_URI, 
				OrderUpdateDetailTable.COLUMN_ORDER_KEY + "=?", new String[]{order.getOrderKey()});
	}
	
	public static void enrichOrderUpdateDetails(Context context, Order order){
		String[] projection = {
				OrderUpdateDetailTable.COLUMN_UPDATE_TYPE,
				OrderUpdateDetailTable.COLUMN_ORDER_FOOD_REFID,
				OrderUpdateDetailTable.COLUMN_FOOD_KEY,
				OrderUpdateDetailTable.COLUMN_QUANTITY,
				OrderUpdateDetailTable.COLUMN_FREE,
				OrderUpdateDetailTable.COLUMN_VERSION,
		};
		Cursor cursor = context.getContentResolver().query(OrderUpdateDetailContentProvider.CONTENT_URI, projection, 
				OrderUpdateDetailTable.COLUMN_ORDER_KEY +"=?", new String[]{order.getOrderKey()}, null);
		if(cursor != null){
			int indexes[] = getColumnIndexs(cursor, projection);
			for(cursor.moveToFirst(); ! cursor.isAfterLast(); cursor.moveToNext()){
				int type = cursor.getInt(indexes[0]);
				Long refid = cursor.getLong(indexes[1]);
				String foodkey = cursor.getString(indexes[2]);
				int count = cursor.getInt(indexes[3]);
				boolean isfree = Boolean.parseBoolean(cursor.getString(indexes[4]));
				Long version = cursor.getLong(indexes[5]);
				if(refid == -1L){
					refid = null;
				}
				UpdateFood updateFood = new UpdateFood(foodkey, refid, version, isfree, count);
				order.getUpdateFoods().get(Order.getUpdateType(type)).add(updateFood);
			}
			cursor.close();
		}
	}
	
	public static void removeOrderUpdateDetail(Context context, String order_key, String food_key){
		context.getContentResolver().delete(OrderUpdateDetailContentProvider.CONTENT_URI,
				OrderUpdateDetailTable.COLUMN_ORDER_KEY + "=? and " + OrderUpdateDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ order_key, food_key});
	}
	
	public static void saveOrderUpdateDetail(Context context, int type, String order_key, UpdateFood updateFood){
		ContentValues values = new ContentValues();
		values.put(OrderUpdateDetailTable.COLUMN_ORDER_KEY, order_key);
		values.put(OrderUpdateDetailTable.COLUMN_FOOD_KEY, updateFood.getFoodKey());
		String refid = "-1";
		if(updateFood.getRefId() != null){
			refid = String.valueOf(updateFood.getRefId());
		}
		values.put(OrderUpdateDetailTable.COLUMN_ORDER_FOOD_REFID, refid);
		values.put(OrderUpdateDetailTable.COLUMN_UPDATE_TYPE, type);
		values.put(OrderUpdateDetailTable.COLUMN_QUANTITY, updateFood.getQuantity());
		values.put(OrderUpdateDetailTable.COLUMN_FREE, String.valueOf(updateFood.isFree()));
		values.put(OrderUpdateDetailTable.COLUMN_VERSION, String.valueOf(updateFood.getVersion()));
		context.getContentResolver().insert(OrderUpdateDetailContentProvider.CONTENT_URI, values);
	}
	
	public static void saveOrderDetails(Context context, Order order){
		String order_key = order.getOrderKey();
		ContentValues values = null;
		for(Entry<Order.Food, Integer> entry : order.getFoods().entrySet()){
			values = new ContentValues();
			String food_key = entry.getKey().getKey();
			int quantity = entry.getValue();
			boolean isFree = entry.getKey().isFree();
			String refid = "-1";
			if(entry.getKey().getId() != null){
				refid = String.valueOf(entry.getKey().getId());
			}
			values.put(OrderDetailTable.COLUMN_ORDER_KEY, order_key);
			values.put(OrderDetailTable.COLUMN_FOOD_KEY, food_key);
			values.put(OrderDetailTable.COLUMN_ORDER_FOOD_REFID, refid);
			values.put(OrderDetailTable.COLUMN_QUANTITY, quantity);
			values.put(OrderDetailTable.COLUMN_FREE, isFree);
			context.getContentResolver().insert(OrderDetailContentProvider.CONTENT_URI, values);
		}
	}
	
	public static int updateOrderDetails(Context context, Order order, Order.Food food, int quantity){
		int result = order.addFood(food, quantity);
		switch(result){
			case Order.REMOVED:
				removeFoodOfOrder(context, order, food);
				break;
			case Order.ADDED:
				addFoodOfOrder(context, order, food, quantity);
				break;
			case Order.UPDATED:
				updateFoodQuantity(context, order, food, quantity);
				break;
		};
		return result;
	}
	
	public static void removeOrder(Context context, String order_key){
		context.getContentResolver().delete(OrderDetailContentProvider.CONTENT_URI,
				OrderDetailTable.COLUMN_ORDER_KEY +"=?",
				new String[]{order_key});
		context.getContentResolver().delete(OrderContentProvider.CONTENT_URI,
				OrderTable.COLUMN_ORDER_KEY +"=?",
				new String[]{order_key});
	}
	
	public static void removeFoodsOfOrder(Context context, String order_key){
		context.getContentResolver().delete(OrderDetailContentProvider.CONTENT_URI,
				OrderDetailTable.COLUMN_ORDER_KEY +"=?",
				new String[]{order_key});
	}
	
	public static void addFoodOfOrder(Context context,  Order order, Order.Food food, int quantity){
		ContentValues values = new ContentValues();
		String refid = "-1";
		if(food.getId() != null){
			refid = String.valueOf(food.getId());
		}
		values.put(OrderDetailTable.COLUMN_ORDER_FOOD_REFID, refid);
		values.put(OrderDetailTable.COLUMN_ORDER_KEY, order.getOrderKey());
		values.put(OrderDetailTable.COLUMN_FOOD_KEY, food.getKey());
		values.put(OrderDetailTable.COLUMN_QUANTITY, quantity);
		values.put(OrderDetailTable.COLUMN_FREE, food.isFree());
		context.getContentResolver().insert(OrderDetailContentProvider.CONTENT_URI, values);
	}
	
	public static void removeFoodOfOrder(Context context, Order order, Order.Food food){
		context.getContentResolver().delete(OrderDetailContentProvider.CONTENT_URI,
				OrderDetailTable.COLUMN_ORDER_KEY + "=? and " + OrderDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ order.getOrderKey(), food.getKey()});
	}
	
	public static void setOrderDirty(Context context, Order order){
		ContentValues values = new ContentValues();
		values.put(OrderTable.COLUMN_DIRTY, String.valueOf(order.isDirty()));
		context.getContentResolver().update(OrderContentProvider.CONTENT_URI, values, 
				OrderTable.COLUMN_ORDER_KEY + "=?", 
				new String[]{order.getOrderKey()});
	}
	
	public static void getOrderDirty(Context context, Order order){
		String[] projection = {
				OrderTable.COLUMN_DIRTY,
	    };
		Cursor cursor = context.getContentResolver().query(OrderContentProvider.CONTENT_URI, projection, 
				OrderTable.COLUMN_ORDER_KEY + "=?", new String[]{order.getOrderKey()}, null);
		
		if(cursor != null && cursor.moveToFirst()){
			int [] indexes =  getColumnIndexs(cursor, projection);
			order.setDirty(Boolean.parseBoolean(cursor.getString(indexes[0])));
			cursor.close();
		} else {
			order.setDirty(false);
		}
	}
	
	//update the quantity of the food
	public static void updateFoodQuantity(Context context, Order order, Order.Food food, int newquantity){
		ContentValues values = new ContentValues();
		values.put(OrderDetailTable.COLUMN_QUANTITY, newquantity);
		context.getContentResolver().update(OrderDetailContentProvider.CONTENT_URI, values, 
				OrderDetailTable.COLUMN_ORDER_KEY + "=? and " + OrderDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{ order.getOrderKey(), food.getKey()});
	}
	
	//update the free status of the food
	public static void updateFoodFreeStatus(Context context, Order order, Order.Food food){
		ContentValues values = new ContentValues();
		values.put(OrderDetailTable.COLUMN_FREE, String.valueOf(food.isFree()));
		context.getContentResolver().update(OrderDetailContentProvider.CONTENT_URI, values, 
				OrderDetailTable.COLUMN_ORDER_KEY + "=? and " + OrderDetailTable.COLUMN_FOOD_KEY + "=?", 
				new String[]{order.getOrderKey(), food.getKey()});
	}
	
	public static void updateFoodOfOrder(Context context, Order order, UpdateFood food){
		ContentValues values = new ContentValues();
		values.put(OrderDetailTable.COLUMN_QUANTITY, food.getQuantity());
		values.put(OrderDetailTable.COLUMN_FREE, String.valueOf(food.isFree()));
		context.getContentResolver().update(OrderDetailContentProvider.CONTENT_URI, values, 
				OrderDetailTable.COLUMN_ORDER_FOOD_REFID + "=?", 
				new String[]{String.valueOf(food.getRefId())});
	}
	
	public static int[] getColumnIndexs(Cursor cursor, String[] columns){
		int [] indexs = new int[columns.length];
		for(int i=0; i < columns.length; i++){
			indexs[i] = cursor.getColumnIndexOrThrow(columns[i]);
		}
		return indexs;
	}
	
}
