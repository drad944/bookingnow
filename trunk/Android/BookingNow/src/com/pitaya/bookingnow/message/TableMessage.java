package com.pitaya.bookingnow.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.util.Constants;

public class TableMessage extends Message{
	
	private static final long serialVersionUID = -1986562133322559438L;
	private Table table;
	private Order order;
	
	public TableMessage(){
		super(Constants.TABLE_MESSAGE);
	}
	
	public TableMessage(Table table, Order order){
		super(Constants.TABLE_MESSAGE);
	}
	
	public void setTable(Table t){
		this.table = t;
	}
	
	public Table getTable(){
		return this.table;
	}
	
	public void setOrder(Order o){
		this.order = o;
	}
	
	public Order getOrder(){
		return this.order;
	}
	
	@Override
	public void fromJSONObject(JSONObject jsonObj) {
		super.fromJSONObject(jsonObj);
		try {
			JSONObject jtable = jsonObj.getJSONObject("table");
			Table table = new Table();
			table.setId(jtable.getLong("id"));
			table.setMaxCount(jtable.getInt("maxCustomerCount"));
			table.setMinCount(jtable.getInt("minCustomerCount"));
			table.setLabel(jtable.getString("address"));
			this.setTable(table);
			JSONObject jorder = jsonObj.getJSONObject("order");
			Long orderid = jorder.getLong("id");
			Long user_id = jorder.getLong("user_id");
			int count = jorder.getInt("customer_count");
			JSONObject jcustomer = jorder.getJSONObject("customer");
			String phone = jcustomer.getString("phone");
			String name = jcustomer.getString("name");
			Order order = new Order(orderid, user_id, null, phone, name, count, null);
			order.setSubmitTime(jorder.getLong("submit_time"));
			this.setOrder(order);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
}
