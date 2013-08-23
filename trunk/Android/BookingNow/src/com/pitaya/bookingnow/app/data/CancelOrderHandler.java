package com.pitaya.bookingnow.app.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.util.ToastUtil;

public class CancelOrderHandler extends HttpHandler {

	private Context mContext;
	private Order mOrder;
	
	public CancelOrderHandler(Context ctx, Order order){
		this.mContext = ctx;
		this.mOrder = order;
	} 
	
	@Override
    public void onSuccess(String action, String response) {
		try {
			JSONObject jresp = new JSONObject(response);
			if(jresp.has("executeResult") && jresp.getBoolean("executeResult") == true){
				mOrder.remove(mContext);
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.operationerror), Toast.LENGTH_SHORT);
	}
	
	@Override
	public void onFail(String action, int statusCode){
		ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.operationfail), Toast.LENGTH_SHORT);
	}
	
}
