package com.pitaya.bookingnow.app.views;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GoodMenuFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleList;

    public GoodMenuFragmentAdapter (FragmentManager fm, List<Fragment> views, List<String> titleList){
        super(fm);
        this.fragmentList = views;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int index) {
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(index);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (titleList.size() > position) ? titleList.get(position) : "";
    }

    @Override
    public int getCount() {
        return 5;
    }
}
