package com.saiyi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.adapter.AlarmCfTabAdapter;
import com.saiyi.ui.fragment.child.CfChildAreaSetFragement;
import com.saiyi.ui.fragment.child.CfChildMsgSetFragement;
import com.saiyi.ui.fragment.child.CfChildNumSetFragement;
import com.saiyi.ui.fragment.child.CfChildSetTimFragment;
import com.saiyi.ui.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class AlarmCfFragement extends Fragment implements OnPageChangeListener {
    View baseView;
    AlarmCfTabAdapter adapter;
    String mac;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (baseView == null) {
            baseView = inflater.inflate(R.layout.cf_fragement, null);
            ViewPager pager = (ViewPager) baseView.findViewById(R.id.pager);
            List<Fragment> list = new ArrayList<>();
            list.add(new CfChildAreaSetFragement());
            list.add(new CfChildSetTimFragment());
            list.add(new CfChildMsgSetFragement());
            list.add(new CfChildNumSetFragement());
            adapter = new AlarmCfTabAdapter(getActivity(),list, getChildFragmentManager());
            pager.setAdapter(adapter);
            TabPageIndicator indicator = (TabPageIndicator) baseView.findViewById(R.id.indicator);
            indicator.setViewPager(pager);
            indicator.setOnPageChangeListener(this);
            mac = ConstantInterface.MAC;
        }
        return baseView;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {

    }
}
