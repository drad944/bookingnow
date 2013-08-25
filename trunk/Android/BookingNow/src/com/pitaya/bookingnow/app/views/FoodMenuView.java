package com.pitaya.bookingnow.app.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aphidmobile.utils.IO;
import com.pitaya.bookingnow.app.FoodBookActivity;
import com.pitaya.bookingnow.app.FoodGalleryActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.AsyncDrawable;
import com.pitaya.bookingnow.app.data.AsyncImageTask;
import com.pitaya.bookingnow.app.data.OrderDetailAdapter;
import com.pitaya.bookingnow.app.model.Food;
import com.pitaya.bookingnow.app.model.Order;
import com.pitaya.bookingnow.app.service.DataService;
import com.pitaya.bookingnow.app.util.Constants;
import com.pitaya.bookingnow.app.util.ContentUtil;
import com.pitaya.bookingnow.app.util.FileUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

/*
 * This is each Food menu view
 */
public class FoodMenuView extends FrameLayout{
	
	private GridView mGridView;
	private FoodMenuContentView mContentContainer;
	private ArrayList<Food> foodList = new ArrayList<Food>();
	private ImageAdapter mFoodMenuAdapter;
	private boolean needUpdateImage = true;
	
    public FoodMenuView(Context context){
        super(context);
    }
      
    public FoodMenuView(Context context, AttributeSet attrs) {  
        super(context, attrs);
    }
    
    public void needUpdateImage(boolean flag){
    	this.needUpdateImage = flag;
    }
    
    public void setContentContainer(FoodMenuContentView v){
    	mContentContainer = v;
    }
    
    public void setupViews(ArrayList<Food> foods){
    	this.foodList = foods;
        View view = View.inflate(getContext(), R.layout.foodmenuview, null);
        mGridView = (GridView)view.findViewById(R.id.gridview);
		try {
			mFoodMenuAdapter = new ImageAdapter(getContext(), mGridView);
			mGridView.setAdapter(mFoodMenuAdapter);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        }
        addView(view);
    }
    
    public void updateFoodList(ArrayList<Food> foodList){
    	this.foodList = foodList;
    }
    
    public void refresh(){
    	mFoodMenuAdapter.notifyDataSetChanged();
    }
    
	private void unbindDrawables(View view) {
        if (view instanceof ImageView) {
        	ImageView v = (ImageView)view;
        	if(v.getBackground() != null){
        		v.getBackground().setCallback(null);
        	}
        	if (v.getDrawable() != null) {
        		v.getDrawable().setCallback(null);
        	}
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            	unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            if(!(view instanceof AdapterView)){
            	((ViewGroup) view).removeAllViews();
            }
        }
    }
    
    public void recycle(){
    	unbindDrawables(this.mGridView);
    }
      
	private class ImageAdapter extends BaseAdapter{  
        
		private Context mContext;
		private View mView;
		private EditText mEditText;
		private Bitmap placeholderBitmap;
       
        public ImageAdapter(Context c, View view) throws IllegalArgumentException, IllegalAccessException{  
            mContext = c;
            this.mView = view;
            //Use a system resource as the placeholder
            placeholderBitmap = BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.dark_header);
            
            mView.setOnTouchListener(new View.OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent arg1) {
            	    if(v instanceof EditText){
            	    	mEditText.clearFocus();
            	    	return false;
            	    } else if(mEditText != null){
            	    	mEditText.clearFocus();
            	    }
				    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
            	    if(imm.isActive()){
            	    	imm.hideSoftInputFromWindow(mView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            	    }
					return false;
				}
            });
        }
        
        @Override
        public int getCount() {  
            return foodList.size();
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
	        View view = convertView;
	        if(view == null){
	        	 view = View.inflate(mContext, R.layout.foodview, null);
	        	 RelativeLayout fooditemRL = (RelativeLayout) view.findViewById(R.id.fooditem);
	        	 final ViewHolder holder = new ViewHolder();
	        	 holder.image = (ImageView) fooditemRL.findViewById(R.id.image);
	        	 holder.introText = (TextView) fooditemRL.findViewById(R.id.introduction);
	        	 
	        	 if(mContentContainer.getOrder() == null){
	        		 holder.priceText = (TextView) fooditemRL.findViewById(R.id.price);
	        	 } else {
		        	 fooditemRL.findViewById(R.id.price).setVisibility(View.GONE);
		        	 
		        	 View foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
			         RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
			            		 ViewGroup.LayoutParams.WRAP_CONTENT,
			                     ViewGroup.LayoutParams.WRAP_CONTENT);
			         fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
			         fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
			         fooditemRL.addView(foodstepper,fsRL_LP);
		             RelativeLayout fsRL = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
		             holder.stepper = fsRL;
		             
		        	 holder.priceText = (TextView) fsRL.findViewById(R.id.price);
		        	 holder.quantityText = (EditText)fsRL.findViewById(R.id.quantity);
		        	 holder.quantityText.addTextChangedListener(new TextWatcher(){
			            	
							@Override
							public void afterTextChanged(Editable text) {
								Order order = mContentContainer.getOrder();
								int quantity = 0;
								try{
									quantity = Integer.parseInt(text.toString());
								} catch(Exception e){
									Log.e("FoodMenuView", "Fail to parse food quantity");
									quantity = 0;
									return;
								}
								Order.Food bookingfood = order.new Food(holder.food.getKey(), holder.food.getName(), holder.food.getPrice());
								bookingfood.setVersion(holder.food.getVersion());
								
								if(order.getStatus() != Constants.ORDER_PAYING || order.getStatus() == Constants.ORDER_FINISHED){
									int result = DataService.updateOrderDetails(mContext, order, bookingfood, quantity);
									if(result != Order.IGNORED && (order.getStatus() == Constants.ORDER_COMMITED
											|| order.getStatus() == Constants.ORDER_WAITING)){
										order.addUpdateFoods(mContext, result, bookingfood, quantity);
										order.markDirty(mContext, true);
									}
								}
							}
		
							@Override
							public void beforeTextChanged(CharSequence text, int arg1,
									int arg2, int arg3) {
								holder.quantityText.setSelection(text.length());
							}
		
							@Override
							public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
							}
			            	
			            });
			         	
		        	 	holder.quantityText.setOnFocusChangeListener(new OnFocusChangeListener(){
		
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if(hasFocus){
									mEditText = holder.quantityText;
								} else {
									mEditText = null;
								}
							}
			            	
			            });
		        	 	
			            ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){
			        		
							@Override
							public void onClick(View v) {
								String current = holder.quantityText.getText().toString();
								int quantity = 0;
								try{
									quantity = Integer.parseInt(current) - 1;
								} catch(Exception e){
									Log.e("FoodMenuView", "Fail to parse food quantity");
								}
								if(quantity < 0){
									return;
								}
								holder.quantityText.setText(String.valueOf(quantity));
							}
			            	
			            });
			            
			            ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){
			
							@Override
							public void onClick(View v) {
								String current = holder.quantityText.getText().toString();
								int quantity = 0;
								try{
									quantity = Integer.parseInt(current) + 1;
								} catch(Exception e){
									Log.e("FoodMenuView", "Fail to parse food quantity");
									quantity = 1;
								}
								holder.quantityText.setText(String.valueOf(quantity));
							}
			            	
			            });
	        	 }
	             view.setTag(holder);
	        }
	        
			final Food food = foodList.get(position);
	        final int index = position;
	        
	        final ViewHolder viewHolder = (ViewHolder)view.getTag();
	        viewHolder.food = food;
	        
	        String name = food.getName();
	        String desc = food.getDescription();
	        viewHolder.introText.setText(name + "\n\n" + desc);
	        viewHolder.priceText.setText(String.valueOf(food.getPrice())+"元/份");
	        
	        boolean needReload = true;
	         AsyncImageTask previousTask = AsyncDrawable.getTask(viewHolder.image);
	         if (previousTask != null) {
		           if (previousTask.getPageIndex() == position && previousTask.getImageName()
		               .equals(food.getSmallImageName()))  {
		        	   needReload = false;
		           } else {
		        	   previousTask.cancel(true);
		           }
	         } else if(viewHolder.image.getDrawable() != null && !needUpdateImage){
	        	 needReload = false;
	         }
	         if (needReload) {
	        	 AsyncImageTask task = new AsyncImageTask(mContext, viewHolder.image, position, food.getSmallImageName());
	        	 viewHolder.image.setImageDrawable(new AsyncDrawable(mContext.getResources(), placeholderBitmap, task));
	           	 task.execute();
	         }
	         if(food.getCategory() != null && !food.getCategory().equals("")){
	        	 viewHolder.image.setOnClickListener(new OnClickListener(){
		             	
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("category", viewHolder.food.getCategory());
							bundle.putString("id", viewHolder.food.getKey());
							bundle.putSerializable("order", mContentContainer.getOrder());
							Intent intent = new Intent(FoodMenuView.this.getContext(), FoodGalleryActivity.class);
							intent.putExtras(bundle);
							FoodMenuView.this.getContext().startActivity(intent);
						}
		         	
		         	});
	         }else {
	        	 //Display category item view
        		 viewHolder.image.setOnClickListener(new OnClickListener(){
             	
					@Override
					public void onClick(View v) {
						mContentContainer.selectPage(index + 1);
					}
	         	
	         	 });
        		 if(viewHolder.stepper != null){
        			 viewHolder.stepper.setVisibility(View.GONE);
        		 }
        		 viewHolder.priceText.setVisibility(View.GONE);
		    }
	        
	        if(viewHolder.quantityText != null){
	             boolean hasFound = false;
	             for(Entry<Order.Food, Integer> entry : mContentContainer.getOrder().getFoods().entrySet()){
	             	if(entry.getKey().getKey().equals(viewHolder.food.getKey())){
	             		 viewHolder.quantityText.setText(String.valueOf(entry.getValue()));
	             		 hasFound = true;
	             		 break;
	             	}
	             }
	             if(!hasFound){
	             	viewHolder.quantityText.setText("0");
	             }
	         }
	         
			 return view;
		}
  
        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
	         final Food food = foodList.get(position);
	         final int index = position;
	         View view = convertView;
	         if(view == null){
	        	 view = View.inflate(mContext, R.layout.foodview, null);
	        	 ViewHolder holder = new ViewHolder();
	         }
			 RelativeLayout fooditemRL = (RelativeLayout) view.findViewById(R.id.fooditem);
	         ImageView image = (ImageView) fooditemRL.findViewById(R.id.image);
	         boolean needReload = true;
	         AsyncImageTask previousTask = AsyncDrawable.getTask(image);
	         if (previousTask != null) {
		           if (previousTask.getPageIndex() == position && previousTask.getImageName()
		               .equals(food.getSmallImageName()))  {
		        	   needReload = false;
		           } else {
		        	   previousTask.cancel(true);
		           }
	         } else if(image.getDrawable() != null && !needUpdateImage){
	        	 needReload = false;
	         }
	         
	         if (needReload) {
	        	 AsyncImageTask task = new AsyncImageTask(mContext, image, position, food.getSmallImageName());
           		 image.setImageDrawable(new AsyncDrawable(mContext.getResources(), placeholderBitmap, task));
	           	 task.execute();
	         }

	         TextView text = (TextView) fooditemRL.findViewById(R.id.introduction);
	         text.setTextSize(20);
	         String name = food.getName();
	         String desc = food.getDescription();
	         text.setText(name + "\n\n" + desc);
	         
	         if(food.getCategory() != null && !food.getCategory().equals("")){
	         	 image.setOnClickListener(new OnClickListener(){
	             	
					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("category", food.getCategory());
						bundle.putString("id", food.getKey());
						bundle.putSerializable("order", mContentContainer.getOrder());
						Intent intent = new Intent(FoodMenuView.this.getContext(), FoodBookActivity.class);
						intent.putExtras(bundle);
						FoodMenuView.this.getContext().startActivity(intent);
					}
	         	
	         	});
	         	View foodstepper = fooditemRL.findViewById(R.id.food_stepper);
	         	RelativeLayout fsRL = null;
	         	if(foodstepper == null){
	         		//First time add foodstepper on the view
	         		foodstepper = View.inflate(mContext, R.layout.foodstepper, null);
		            RelativeLayout.LayoutParams fsRL_LP = new RelativeLayout.LayoutParams(
		            		 ViewGroup.LayoutParams.WRAP_CONTENT,
		                     ViewGroup.LayoutParams.WRAP_CONTENT);
		            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
		            fsRL_LP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
		            fooditemRL.addView(foodstepper,fsRL_LP);
		         	fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
	         	} else {
	         		fsRL  = (RelativeLayout) foodstepper.findViewById(R.id.food_stepper);
	         		if(watchers.get(view.hashCode()) != null){
	         			((EditText)fsRL.findViewById(R.id.quantity)).removeTextChangedListener(watchers.get(view.hashCode()));
	         		}
	         	}
         	
	         	TextView priceText = (TextView) fsRL.findViewById(R.id.price);
	         	priceText.setText(String.valueOf(food.getPrice())+"元/份");
	         	
	         	final EditText quantityText = (EditText)fsRL.findViewById(R.id.quantity);
	         	
	         	if(mContentContainer.getOrder() == null){
	         		quantityText.setVisibility(View.GONE);
	         		((Button)fsRL.findViewById(R.id.addbtn)).setVisibility(View.GONE);
	         		((Button)fsRL.findViewById(R.id.minusbtn)).setVisibility(View.GONE);
	         	} else {
		            watchers.put(view.hashCode(), new TextWatcher(){
		            	
						@Override
						public void afterTextChanged(Editable text) {
							Order order = mContentContainer.getOrder();
							int quantity = 0;
							try{
								quantity = Integer.parseInt(text.toString());
							} catch(Exception e){
								Log.e("FoodMenuView", "Fail to parse food quantity");
								quantity = 0;
								return;
							}
							Order.Food bookingfood = order.new Food(food.getKey(), food.getName(), food.getPrice());
							bookingfood.setVersion(food.getVersion());
							
							if(order.getStatus() != Constants.ORDER_PAYING || order.getStatus() == Constants.ORDER_FINISHED){
								int result = DataService.updateOrderDetails(mContext, order, bookingfood, quantity);
								if(result != Order.IGNORED && (order.getStatus() == Constants.ORDER_COMMITED
										|| order.getStatus() == Constants.ORDER_WAITING)){
									order.addUpdateFoods(mContext, result, bookingfood, quantity);
									order.markDirty(mContext, true);
								}
							}
						}
	
						@Override
						public void beforeTextChanged(CharSequence text, int arg1,
								int arg2, int arg3) {
							quantityText.setSelection(text.length());
						}
	
						@Override
						public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
						}
		            	
		            });
		         	
		            quantityText.addTextChangedListener(watchers.get(view.hashCode()));
		            quantityText.setOnFocusChangeListener(new OnFocusChangeListener(){
	
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if(hasFocus){
								mEditText = quantityText;
							} else {
								mEditText = null;
							}
						}
		            	
		            });
		            
		            mView.setOnTouchListener(new View.OnTouchListener(){
	
						@Override
						public boolean onTouch(View v, MotionEvent arg1) {
		            	    if(v instanceof EditText){
		            	    	mEditText.clearFocus();
		            	    	return false;
		            	    } else if(mEditText != null){
		            	    	mEditText.clearFocus();
		            	    }
						    InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
		            	    if(imm.isActive()){
		            	    	imm.hideSoftInputFromWindow(mView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		            	    }
							return false;
						}
		            });
	            
		            ((Button)fsRL.findViewById(R.id.minusbtn)).setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							String current = quantityText.getText().toString();
							int quantity = 0;
							try{
								quantity = Integer.parseInt(current) - 1;
							} catch(Exception e){
								Log.e("FoodMenuView", "Fail to parse food quantity");
							}
							if(quantity < 0){
								return;
							}
							quantityText.setText(String.valueOf(quantity));
						}
		            	
		            });
		            
		            ((Button)fsRL.findViewById(R.id.addbtn)).setOnClickListener(new OnClickListener(){
		
						@Override
						public void onClick(View v) {
							String current = quantityText.getText().toString();
							int quantity = 0;
							try{
								quantity = Integer.parseInt(current) + 1;
							} catch(Exception e){
								Log.e("FoodMenuView", "Fail to parse food quantity");
								quantity = 1;
							}
							quantityText.setText(String.valueOf(quantity));
						}
		            	
		            });
	
		            boolean hasFound = false;
		            for(Entry<com.pitaya.bookingnow.app.model.Order.Food, Integer> entry : mContentContainer.getOrder().getFoods().entrySet()){
		            	if(entry.getKey().getKey().equals(food.getKey())){
		            		 quantityText.setText(String.valueOf(entry.getValue()));
		            		 hasFound = true;
		            		 break;
		            	}
		            }
		            if(!hasFound){
		            	quantityText.setText("0");
		            }
	         	}
	        } else {
	        	//Display category item view
        		image.setOnClickListener(new OnClickListener(){
             	
					@Override
					public void onClick(View v) {
						mContentContainer.selectPage(index + 1);
					}
	         	
	         	});
	        }
            return view;
        }*/
          
    }
    
	private static class ViewHolder{
		ImageView image;
		TextView introText;
		TextView priceText;
		EditText quantityText;
		RelativeLayout stepper;
		Food food;
    }

}
