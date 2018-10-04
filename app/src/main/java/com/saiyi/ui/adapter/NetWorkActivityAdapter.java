package com.saiyi.ui.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.saiyi.R;

public class NetWorkActivityAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;


    public NetWorkActivityAdapter(Context context) {
        mInflater = LayoutInflater.from(context);// 获得 LayoutInflater 实例
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.network_list_item, null);
        }

        ImageButton network_list_item_right = (ImageButton) convertView.findViewById(R.id.network_list_item_right);
        network_list_item_right.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }
}


