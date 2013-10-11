package com.pitaya.bookingnow.app.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pitaya.bookingnow.app.R;
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
				view = View.inflate(mContext, R.layout.preferenceview, null);
				TextView text = (TextView)view.findViewById(R.id.preference);
				ImageView selected = (ImageView)view.findViewById(R.id.selected);
				final ViewHolder holder = new ViewHolder();
				holder.text = text;
				holder.selected = selected;
				view.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View v) {
						int i;
						for(i = 0; i < selectedPrefs.size(); i++){
							if(selectedPrefs.get(i).equals(holder.text.getText().toString())){
								break;
							}
						}
						if(i == selectedPrefs.size()){
							selectedPrefs.add(holder.text.getText().toString());
							holder.selected.setVisibility(View.VISIBLE);
						} else {
							selectedPrefs.remove(i);
							holder.selected.setVisibility(View.GONE);
						}
					}

				});
				view.setTag(holder);
			}
			
			ViewHolder viewHolder = (ViewHolder)view.getTag();
			viewHolder.text.setText(pref);
			if(this.selectedPrefs.indexOf(pref) != -1){
				viewHolder.selected.setVisibility(View.VISIBLE);
			} else {
				viewHolder.selected.setVisibility(View.GONE);
			}
			return view;
		}
		
		private static class ViewHolder{
			TextView text;
			ImageView selected;
		}
		
}
