package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.service.UserManager;
import com.pitaya.bookingnow.app.util.Constants;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class SettingsContentView extends BaseContentView{
	
	public static final String CHECK_MENU_UPDATE = "check_update";
	public static final String CONNECT_SERVER = "connect_server";
	public static final String CLEAN_DATA ="clean_data";
	public static final String ADDRESS = "server_address";
	
	private static final String TAG = "SettingsContentView";
	private Fragment mSettingsFragment;
	
	public SettingsContentView(String key, Context context, SlideContent home) {
		super(key, context, home);
	}
	
	@Override
	public void setupView(ViewGroup container){
		if(mView == null){
			mView = View.inflate(this.mContext, R.layout.settingscontentview, null);
		}
		container.addView(mView);
		if(mSettingsFragment == null){
			mSettingsFragment = new SettingsFragment();
		}
		FragmentManager fragmentManager = ((Activity)this.mContext).getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.settings, mSettingsFragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	@Override
	public boolean destroyView(ViewGroup container){
		FragmentManager fragmentManager = ((Activity)this.mContext).getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if(mSettingsFragment != null){
			fragmentTransaction.remove(mSettingsFragment);
		}
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
		if(this.mView != null){
			container.removeView(this.mView);
		}
		return true;
	}

}
