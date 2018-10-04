package com.saiyi.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.saiyi.R;

import java.util.List;

public class AlarmCfTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    FragmentManager fm;
    Context context;
    String[] title ;

    public AlarmCfTabAdapter( Context context, List<Fragment> list, android.support.v4.app.FragmentManager fm) {
        super(fm);
        this.fm = fm;
        this.list = list;
        this.context=context;

        title = new String [] {context.getResources().getString(R.string.Play_set), context.getResources().getString(R.string.setTime),context.getResources().getString(R.string.setnumber),context.getResources().getString(R.string.setmsg)};

    }

    public void setData(List<Fragment> list) {
        this.list = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = list.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }

}
