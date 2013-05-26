package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.R;

import java.lang.reflect.Field;  
import java.util.ArrayList;  
  
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.os.Bundle;  
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GalleryView extends ContentView{
	
	public GalleryView(Context context) {
		super(context);
	}

	public GalleryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GalleryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setupView() {
		if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		View galleryview =View.inflate(context, R.layout.galleryview, null);
		GridView gridview = (GridView)(galleryview.findViewById(R.id.gridview));
		try {
			gridview.setAdapter(new ImageAdapter(context));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();  
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        }
		mContainer.addView(galleryview);
	}
	
	private class ImageAdapter extends BaseAdapter{  
        private Context mContext;  
        private ArrayList<Integer> imgList=new ArrayList<Integer>(); 
        public ImageAdapter(Context c) throws IllegalArgumentException, IllegalAccessException{  
            mContext = c;
             
            Field[] fields = R.drawable.class.getDeclaredFields();  
            for (Field field : fields){  
                if (field.getName().endsWith("s")){
                    int index=field.getInt(R.drawable.class);  
                    imgList.add(index);
//                    int size[]=new int[2];  
//                    Bitmap bmImg=BitmapFactory.decodeResource(context.getResources(), index);
//                    size[0]=bmImg.getWidth();size[1]=bmImg.getHeight();  
//                    imgSizes.add(size);  
                }
            }  
        }
        @Override  
        public int getCount() {  
            return imgList.size();  
        }  
  
        @Override  
        public Object getItem(int position) {
            return position;  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {
        	View view = View.inflate(mContext, R.layout.goodview, null);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.gooditem);
            ImageView image = (ImageView) rl.findViewById(R.id.image);
            image.setImageResource(imgList.get(position));
            
            TextView text = (TextView) rl.findViewById(R.id.introduction);
            text.setText("这是第"+position+"道菜的介绍");
            return view;
        }
          
    }
 
}
