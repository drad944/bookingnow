package com.pitaya.bookingnow.app;

import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.domain.Food;
import com.pitaya.bookingnow.app.domain.Ticket;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PopUpViewer extends Activity{

	@Override
    protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Bundle bundle = this.getIntent().getExtras();
		Ticket ticket = (Ticket)bundle.getSerializable("ticket");
		View popupview = this.getLayoutInflater().inflate(R.layout.popupviewer, null);
		LinearLayout tableContent = (LinearLayout)popupview.findViewById(R.id.popupcontent);
		TableRow tableRow = new TableRow(this);
        TextView textView = new TextView(this);
        textView.setText("您的订单");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        tableRow.setGravity(Gravity.CENTER);
        tableRow.addView(textView);
        tableContent.addView(tableRow);
		for(Entry<Ticket.Food, Integer> entry : ticket.getFoods().entrySet()){
			tableRow = new TableRow(this);
			tableRow.setGravity(Gravity.CENTER);
			Ticket.Food food = entry.getKey();
			textView = new TextView(this);
			textView.setText(food.getName());
			textView.setTextSize(25);
	        textView.setGravity(Gravity.CENTER);
	        tableRow.addView(textView);
			textView = new TextView(this);
			textView.setText(entry.getValue()+"份");
			textView.setTextSize(25);
			textView.setGravity(Gravity.CENTER);
	        tableRow.addView(textView);
			textView = new TextView(this);
			textView.setText(food.getPrice()*entry.getValue()*1.0+"元");
			textView.setTextSize(25);
			textView.setGravity(Gravity.CENTER);
	        tableRow.addView(textView);
	        tableContent.addView(tableRow);
		}
		setContentView(popupview);
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }
}
