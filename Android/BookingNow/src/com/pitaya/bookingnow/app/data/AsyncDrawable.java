package com.pitaya.bookingnow.app.data;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Borrowed from the official BitmapFun tutorial: http://developer.android.com/training/displaying-bitmaps/index.html
 */
public final class AsyncDrawable extends BitmapDrawable {

	  private final WeakReference<AsyncImageTask> taskRef;
	
	  public AsyncDrawable(Resources res, Bitmap bitmap, AsyncImageTask task) {
		   super(res, bitmap);
		   this.taskRef = new WeakReference<AsyncImageTask>(task);
	  }
	
	  public static AsyncImageTask getTask(ImageView imageView) {
		   Drawable drawable = imageView.getDrawable();
		   if (drawable instanceof AsyncDrawable) {
		        return ((AsyncDrawable) drawable).taskRef.get();
		   }
		   return null;
	  }
}
