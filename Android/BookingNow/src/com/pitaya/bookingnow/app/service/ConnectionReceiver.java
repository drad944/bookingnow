package com.pitaya.bookingnow.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class ConnectionReceiver extends BroadcastReceiver{
	
	 private static final String TAG= "ConnectionReceiver";
	
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	     boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
	     //Intent newintent = new Intent(context, EnhancedMessageService.class);
	     Intent newintent = new Intent(context, MessageService.class);
	     Bundle bundle = new Bundle();
		 if(isConnected){
			 bundle.putBoolean("connected", true);
			 Log.d(TAG, "Connection established");
		 } else {
			 bundle.putBoolean("connected", false);
			 Log.d(TAG, "Connection down");
		 }
	     newintent.putExtras(bundle);
	     context.startService(newintent);
	 }
}
