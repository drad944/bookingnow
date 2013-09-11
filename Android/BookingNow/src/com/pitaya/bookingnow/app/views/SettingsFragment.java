package com.pitaya.bookingnow.app.views;

import com.pitaya.bookingnow.app.HomeActivity;
import com.pitaya.bookingnow.app.R;
import com.pitaya.bookingnow.app.data.MessageHandler;
import com.pitaya.bookingnow.app.preference.*;
import com.pitaya.bookingnow.app.service.EnhancedMessageService;
import com.pitaya.bookingnow.app.service.HttpService;
import com.pitaya.bookingnow.app.util.ToastUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        
        EditTextPreference addrEditor = (EditTextPreference)this.findPreference(SettingsContentView.ADDRESS);
        addrEditor.setOnPreferenceChangeListener(new OnPreferenceChangeListener(){

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				if(newValue != null && newValue.toString().split(":").length > 1){
					HttpService.setIp(newValue.toString());
					return true;
				}
				ToastUtil.showToast(SettingsFragment.this.getActivity(), "Invalid input, use 'ip:port'", Toast.LENGTH_LONG);
				return false;
			}
        	
        });
        
        ButtonPreference upbtn = (ButtonPreference)this.findPreference(SettingsContentView.CHECK_MENU_UPDATE);
        upbtn.setValues(new String[]{"获取最新菜单", "更新"});
        upbtn.setClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getActivity() instanceof HomeActivity){
					((HomeActivity)getActivity()).checkMenuUpdate();
				}
			}
			
		});
        ButtonPreference cleanbtn = (ButtonPreference)this.findPreference(SettingsContentView.CLEAN_DATA);
        cleanbtn.setValues(new String[]{"清除菜单，订单数据", "清除"});
        ButtonPreference connectbtn = (ButtonPreference)this.findPreference(SettingsContentView.CONNECT_SERVER);
        connectbtn.setValues(new String[]{"连接服务器", "连接"});
        connectbtn.setClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().startService(new Intent(getActivity(), EnhancedMessageService.class));
			}
			
		});
    }

}