package com.pitaya.bookingnow.app.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.model.Table;
import com.pitaya.bookingnow.app.model.Order.OnOrderStatusChangedListener;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.service.MessageService;
import com.pitaya.bookingnow.app.service.OrderService;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ToastUtil;
import com.pitaya.bookingnow.app.data.HttpHandler;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.data.WorkerOrderDetailAdapter;
import com.pitaya.bookingnow.message.Message;
import com.pitaya.bookingnow.message.OrderDetailMessage;
import com.pitaya.bookingnow.message.OrderMessage;
import com.pitaya.bookingnow.message.TableMessage;

public class WelcomerOrderLeftView extends OrderLeftView{
	
	private final static String TAG = "WelcomerOrderLeftView";
	
	private OrderListView mOrderListView;
	
	public WelcomerOrderLeftView(OrderContentView v){
		super(v);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(this.mContentContainer.getStatus() != null){
			this.lastSelectItem = (String)this.mContentContainer.getStatus();
		}
		mMessageHandler.setOnMessageListener(new MessageHandler.OnMessageListener(){

			@Override
			public void onMessage(Message message) {
				if(message instanceof OrderMessage){
					OrderMessage msg = (OrderMessage)message;
					if(msg.getAction().equals(Constants.ACTION_REMOVE)){
						Order removedorder =  new Order(msg.getOrderId(), null, null, 
								null, null, 0, null);
						int i = 0;
						boolean isRemoved = false;
						for(Order order : mOrderListView.getOrderList()){
							if(order.getOrderKey().equals(removedorder.getOrderKey())){
								mOrderListView.getOrderList().remove(i);
								isRemoved = true;
								break;
							}
							i++;
						}
						if(isRemoved){
							mOrderListView.refresh();
						}
					}
				}
			}
			
		});
		this.mOrderListView = new WelcomerOrderListView(this.getActivity(), this);
		mOrderListView.setupViews();
		this.doBindService();
		return mOrderListView;
	}
	
	public void showOrderDetail(Order order, boolean isForce){
		order.enrichFoods(this.getActivity());
    	if(order.getStatus() == Constants.ORDER_WAITING && order.isDirty()){
    		order.enrichUpdateFoods(this.getActivity());
    	}
		super.showOrderDetail(order, isForce, WorkerOrderDetailAdapter.class, 650);
	}

}
