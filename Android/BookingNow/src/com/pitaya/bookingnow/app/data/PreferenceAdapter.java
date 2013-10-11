package com.pitaya.bookingnow.app.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.util.Constants;

public class PreferenceAdapter extends BaseAdapter{

		private ArrayList<String> selectedPrefs;
		private Context mContext;
		
		public PreferenceAdapter(Context context) throws IllegalArgumentException, IllegalAccessException{  
			mContext = context;
			this.selectedPrefs = new ArrayList<String>();
        }
		
		public ArrayList<String> getSelectedPrefs(){
			return this.selectedPrefs;
		}
		
		public void setSelectedPrefs(String[] prefs){
			if(prefs != null){
				this.selectedPrefs = new ArrayList<String>(Arrays.asList(prefs));
			} else {
				this.selectedPrefs = new ArrayList<String>();
			}
		}
		
		@Override
		public int getCount() {
			return Constants.preferences.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			String pref = Constants.preferences[position];
			if(view == null){
				view = new RelativeLayout(mContext);
				view.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				TextView text = new TextView(mContext);
				ImageView selected = new ImageView(mContext);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				((RelativeLayout)view).addView(text, params);
				params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, -1);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
				((RelativeLayout)view).addView(selected, params);
				ViewHolder holder = new ViewHolder();
				holder.text = text;
				holder.selected = selected;
				view.setTag(holder);
			}
			final ViewHolder viewHolder = (ViewHolder)view.getTag();
			viewHolder.text.setText(pref);
			if(this.selectedPrefs.indexOf(pref) != -1){
				viewHolder.selected.setVisibility(View.VISIBLE);
			} else {
				viewHolder.selected.setVisibility(View.GONE);
			}
			viewHolder.text.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					int i;
					for(i = 0; i < selectedPrefs.size(); i++){
						if(selectedPrefs.get(i).equals(viewHolder.text.getText().toString())){
							break;
						}
					}
					if(i == selectedPrefs.size()){
						selectedPrefs.add(viewHolder.text.getText().toString());
						viewHolder.selected.setVisibility(View.VISIBLE);
					} else {
						selectedPrefs.remove(i);
						viewHolder.selected.setVisibility(View.GONE);
					}
				}

			});
			return view;
		}
		
		private static class ViewHolder{
			TextView text;
			ImageView selected;
		}
		
}
