package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.CookingItemsAdapter;
import com.pitaya.bookingnow.app.model.CookingItem;
import com.pitaya.bookingnow.app.model.Order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class CookingItemsListView extends RelativeLayout{
	
	protected static String TAG = "FoodListView";
	
	protected View mHeaderView;
	protected ListView mListView;
	protected View mControlbar;
	protected ArrayList<CookingItem> mCookingItemsList;
	protected CookingItemsAdapter mAdapter;
	
	public CookingItemsListView(Context context){
        super(context);
        mCookingItemsList = new ArrayList<CookingItem>();
        this.setupViews();
    }
      
    public CookingItemsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCookingItemsList = new ArrayList<CookingItem>();
        this.setupViews();
    }
    
    public void refresh(){
    	this.mAdapter.notifyDataSetChanged();
    }
    
    public void setupViews(){
    	this.mHeaderView = View.inflate(this.getContext(), R.layout.cookingitemlistheader, null);
    	this.mHeaderView.setId(1);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(this.mHeaderView, lp);
        this.mListView = new ListView(this.getContext());
        lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, this.mHeaderView.getId());
        addView(mListView, lp);
        
        ((TextView)this.mHeaderView.findViewById(R.id.nametitle)).setText(R.string.cookingitem_title_name);
        ((TextView)this.mHeaderView.findViewById(R.id.quantitytitle)).setText(R.string.cookingitem_title_quantity);
        ((TextView)this.mHeaderView.findViewById(R.id.timertitle)).setText(R.string.cookingitem_title_timer);
        ((TextView)this.mHeaderView.findViewById(R.id.statustitle)).setText(R.string.cookingitem_title_status);
        ((TextView)this.mHeaderView.findViewById(R.id.operationstitle)).setText(R.string.cookingitem_title_operations);
        //TODO get cooking items from server and renderer the list view
    }
}
