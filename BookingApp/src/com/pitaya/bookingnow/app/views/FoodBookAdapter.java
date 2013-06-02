package com.pitaya.bookingnow.app.views;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aphidmobile.utils.UI;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.domain.Food;

public class FoodBookAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int repeatCount = 1;
	private List<Food> foodList;
	
	public FoodBookAdapter(Context context, List<Food> foodlist){
		inflater = LayoutInflater.from(context);
		this.foodList = foodlist;
	}
	
	@Override
	public int getCount() {
		return this.foodList.size() * repeatCount;
	}
	
	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
	   this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
	    if (convertView == null) {
		      layout = inflater.inflate(R.layout.foodbooklayout, null);
	    }
	    final Food food = this.foodList.get(position % foodList.size());
	    UI
        .<ImageView>findViewById(layout, R.id.photo)
        .setImageBitmap(BitmapFactory.decodeByteArray(food.getLargeImage(), 0, food.getLargeImage().length));

	    UI
        .<TextView>findViewById(layout, R.id.description)
        .setText(Html.fromHtml(food.getDescription()));
	    return layout;
	}
	
	 public void removeData(int index) {
		 if (foodList.size() > 1) {
			 foodList.remove(index);
		 }
	 }

}
