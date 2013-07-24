package com.pitaya.bookingnow.app.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	
	public static void showToast (Context context, CharSequence text, int duration){
		 Toast.makeText(context, text, duration).show();
	}

}
