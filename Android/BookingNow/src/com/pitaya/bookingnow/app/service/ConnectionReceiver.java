package com.pitaya.bookingnow.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectionReceiver extends BroadcastReceiver{
	
	 private static final String TAG= "ConnectionReceiver";
	
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	     boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
		 if(isConnected){
			 context.startService(new Intent(context, MessageService.class));
			 Log.d(TAG, "Connection established");
		 }
	 }
}
