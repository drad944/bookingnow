package com.pitaya.bookingnow.app.util;

import android.content.Context;
import android.app.Activity;
import android.util.DisplayMetrics;

public class ContentUtil {
	
	public static float SCALE;
	public static double WIDTHDP;
	public static double HEIGHTDP;
	private static DisplayMetrics METRIC;
	
	public static void init(Context context){
		METRIC = context.getResources().getDisplayMetrics();
		SCALE = METRIC.density;
	    WIDTHDP = Math.floor(METRIC.widthPixels/SCALE);
	    HEIGHTDP = Math.floor(METRIC.heightPixels/SCALE);
	}
	
	public static int getPixelsByDP(int dp){
		return  (int) (dp * SCALE + 0.5f);
	}
	
}
