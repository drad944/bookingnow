package com.pitaya.bookingnow.app.data;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.pitaya.bookingnow.app.util.FileUtil;

public class AsyncImageTask extends AsyncTask<Void, Void, Bitmap> {
	  
	  protected final WeakReference<ImageView> imageViewRef;
	  protected final int pageIndex;
	  protected final String imageName;
	  protected final WeakReference<Context> contextRef;

	  public AsyncImageTask(Context context, ImageView imageView, int pageIndex, String imageName) {
		  imageViewRef = new WeakReference<ImageView>(imageView);
		  this.pageIndex = pageIndex;
		  this.imageName = imageName;
		  this.contextRef = new WeakReference<Context>(context);
	  }
	
	  public int getPageIndex() {
		  return pageIndex;
	  }
	
	  public String getImageName() {
		  return imageName;
	  }
	
	  @Override
	  protected Bitmap doInBackground(Void... params) {
		  Context ctx = contextRef.get();
		  if(ctx != null){
			  return FileUtil.readBitmap(ctx, imageName);
		  } else {
			  return null;
		  }
	  }
	
	  @Override
	  protected void onPostExecute(Bitmap bitmap) {
		  if (isCancelled()) {
			  return;
		  }
		  ImageView imageView = imageViewRef.get();
		  if (imageView != null && AsyncDrawable.getTask(imageView) == this) { 
			  imageView.setImageBitmap(bitmap);
		  }
	  }
}
