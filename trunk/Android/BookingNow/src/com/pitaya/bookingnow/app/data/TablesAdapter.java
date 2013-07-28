package com.pitaya.bookingnow.app.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.pitaya.bookingnow.app.model.Table;

public class TablesAdapter extends BaseAdapter{

		private ArrayList<Table> tables;
		private ArrayList<Table> selectedTables;
		private Context mContext;
		
		public TablesAdapter(Context context, ArrayList<Table> tableids) throws IllegalArgumentException, IllegalAccessException{  
			tables = tableids;
			mContext = context;
			selectedTables = new ArrayList<Table>();
        }
		
		public ArrayList<Table> getSelectedTables(){
			return this.selectedTables;
		}
		
		@Override
		public int getCount() {
			return tables.size();
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
			final int index = position;
			if(view == null){
				view = new CheckBox(mContext);
				((CheckBox)view).setText(this.tables.get(position).getLabel());
				((CheckBox)view).setOnCheckedChangeListener(new OnCheckedChangeListener(){

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if(isChecked){
							selectedTables.add(tables.get(index));
						} else {
							int i = 0;
							while(!selectedTables.get(i).equals(tables.get(index).getId())){
								i++;
							}
							if(i < selectedTables.size()){
								selectedTables.remove(i);
							}
						}
					}
					
				});
			}
			return view;
		}
	
}
