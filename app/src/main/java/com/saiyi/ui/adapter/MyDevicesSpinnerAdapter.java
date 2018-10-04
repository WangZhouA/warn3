package com.saiyi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.domain.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MyDevicesSpinnerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    List<Mode> mList;

    public MyDevicesSpinnerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);// 获得 LayoutInflater 实例
        mContext = context;
        mList = new ArrayList<>();
        mList.add(new Mode("",R.drawable.ic_add));
        mList.add(new Mode("SIM模式", R.drawable.sim));
        mList.add(new Mode("WIFI模式", R.drawable.wifis));
        mList.add(new Mode("SIM+WIFI模式", R.drawable.sim_wifi));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.my_devices_empty_spinner_item, null);
        }
        convertView.setTag(mList.get(position));

        TextView my_devices_empty_spinner_item = (TextView) convertView.findViewById(R.id.my_devices_empty_spinner_item);
        my_devices_empty_spinner_item.setText(mList.get(position).name);
        ImageView img = (ImageView) convertView.findViewById(R.id.my_devices_empty_spinner_img);
        img.setImageResource(mList.get(position).resId);
//         my_devices_empty_spinner_item.setCompoundDrawables(mContext.getResources().getDrawable(mList.get(position).resId),null,null,null);
        //my_devices_empty_spinner_item.setBackgroundResource(mList.get(position).resId);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 3;
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}