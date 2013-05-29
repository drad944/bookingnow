package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;

import org.json.JSONArray;

import com.pitaya.bookingnow.app.R;

import android.view.View;
import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;

public class GoodMenuContentView extends BaseContentView{

	private ViewPager mGoodMenuViewPager;
	
	public GoodMenuContentView(Context context) {
		super(context);
	}

	@Override
	public boolean canIntercept(){
		return mGoodMenuViewPager.getCurrentItem() == 0;
	}
	
	public View getView(){
		this.mView = View.inflate(this.mContext, R.layout.goodmenucontentview, null);
		mGoodMenuViewPager = (ViewPager)this.mView.findViewById(R.id.goodmenuviewpager);
		ArrayList<String> titleList = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			titleList.add("title " + i);
		}
		GoodMenuAdapter adapter= new GoodMenuAdapter(this.mContext,  new JSONArray(), titleList);
		mGoodMenuViewPager.setAdapter(adapter);
//		ArrayList<String> titleList = new ArrayList<String>();
//		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
//		for (int i = 0; i < 5; i++) {
//            titleList.add("title " + i);
//            fragments.add(new GoodMenuFragment());
//        }
//		mGoodMenuViewPager.setAdapter(new GoodMenuFragmentAdapter(((FragmentActivity)this.mContext).getSupportFragmentManager(), fragments, titleList));

		return this.mView;
	}
	
}
