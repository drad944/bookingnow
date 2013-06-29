package com.pitaya.bookingnow.app.data;

import java.lang.ref.WeakReference;

import com.aphidmobile.flip.FlipViewController;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class FlipAsyncImageTask extends AsyncImageTask{
	
	private final WeakReference<FlipViewController> controllerRef;
	
	public FlipAsyncImageTask(FlipViewController controller, Context context, ImageView imageView,
			int pageIndex, String imageName) {
		 super(context, imageView, pageIndex, imageName);
		 controllerRef = new WeakReference<FlipViewController>(controller);
	}
	
    @Override
    protected void onPostExecute(Bitmap bitmap) {
	     if (isCancelled()) {
	         return;
	     }
	
	     ImageView imageView = imageViewRef.get();
	     if (imageView != null && AsyncDrawable.getTask(imageView)
	                               == this) { //the imageView can be reused for another page, so it's necessary to check its consistence
	    	 imageView.setImageBitmap(bitmap);
	    	 FlipViewController controller = controllerRef.get();
	    	 if (controller != null) {
	    		 controller.refreshPage(pageIndex);
	    	 }
	     }
    }
	
}
