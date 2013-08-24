package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.TablesAdapter;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ContentUtil;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.PopupWindow;

public class TablesView{
	
	private static final String TAG = "TablesView";
	private Context mContext;
	private OnConfirmListener mListener;
	private View mTablesView;
	private PopupWindow mPopupView;
	private View mParentView;
	
	public TablesView(Context context, View parentView) {
		this.mContext = context;
		this.mParentView = parentView;
		this.setupView();
	}
	
	private void setupView(){
		mTablesView = View.inflate(mContext, R.layout.tablesview, null);
		final GridView tablegridview = (GridView)mTablesView.findViewById(R.id.tablegridview);
		Button confirmBtn =  (Button)mTablesView.findViewById(R.id.confirm);
		confirmBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final ArrayList<Table> tables = ((TablesAdapter)tablegridview.getAdapter()).getSelectedTables();
				if(mListener != null){
					mListener.onConfirm(tables);
				}
			}
			
		});
		
		mPopupView =  new PopupWindow(mTablesView, ContentUtil.getPixelsByDP(400), ContentUtil.getPixelsByDP(400), true);
		mPopupView.setFocusable(true);
		mPopupView.setOutsideTouchable(false);
		mPopupView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.common_background));
		mPopupView.setAnimationStyle(R.style.AnimBottom);
	}
	
	public void setOnConfirmListener(OnConfirmListener l){
		this.mListener = l;
	}
	
	public void popup(final View anchor){
		
		OrderService.getAvailableTables(Constants.TABLE_EMPTY, new HttpHandler(){
			
			public void onSuccess(String action, String response){
				try {
					JSONObject jresp = new JSONObject(response);
					if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == false){
						//TODO handle fail
					} else {
						JSONArray jtables = jresp.getJSONArray("result");
						ArrayList<Table> tables = new ArrayList<Table>();
						for(int i=0; i < jtables.length(); i++){
							tables.add(new Table(jtables.getJSONObject(i).getLong("id"), jtables.getJSONObject(i).getString("address")));
						}
						try {
							 ((GridView)mTablesView.findViewById(R.id.tablegridview)).setAdapter(new TablesAdapter(mContext, tables));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
						if(anchor != null){
							mPopupView.showAsDropDown(anchor);
						} else {
							mPopupView.showAtLocation(mParentView, Gravity.CENTER, 0, 0);
						}
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			public void onFail(String action, int statuscode){
				Log.e(TAG, "[OrderService.getAvailableTables] Network error:" + statuscode);
			}
			
		});
	}
	
	public void close(){
		mPopupView.dismiss();
	}
	
	public interface OnConfirmListener{
		
		public void onConfirm(ArrayList<Table> selectedTables);
		
	}
}
