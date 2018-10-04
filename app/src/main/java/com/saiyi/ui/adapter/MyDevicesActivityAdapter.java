package com.saiyi.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.ui.Entity.DerviceBean;
import com.saiyi.ui.activity.AlarmActivity;

import java.util.List;

public class MyDevicesActivityAdapter extends BaseAdapter {


    private Context mContext;
    private List<DerviceBean.DataBean> lists;


    private static String IntentDerviceId;


    public MyDevicesActivityAdapter(Context context, List<DerviceBean.DataBean> lists) {

        this.mContext = context;
        this.lists = lists;

    }

    public void setData(List<DerviceBean.DataBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {

        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        final DerviceBean.DataBean itemEntity = lists.get(position);

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.my_devices_list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.my_devices_list_tx3.setText(itemEntity.getDeviceName());


        viewHolder.rl_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentDerviceId = lists.get(position).getDeviceId()+"";
                if (!TextUtils.isEmpty(lists.get(position).getDeviceMac()))
                    mContext.startActivity(new Intent(mContext, AlarmActivity.class).putExtra("mac", lists.get(position).getDeviceMac()).putExtra("name", lists.get(position).getDeviceName()));
            }
        });
        return convertView;
    }



    class ViewHolder {
        TextView my_devices_list_tx3;
        Button my_devices_unbound_btn;
        RelativeLayout rl_onclick;
        ImageView my_devices_input;

        public ViewHolder(View view) {
            my_devices_list_tx3 = (TextView) view.findViewById(R.id.my_devices_list_tx3);
            my_devices_unbound_btn = (Button) view.findViewById(R.id.my_devices_unbound_btn);
            rl_onclick = (RelativeLayout) view.findViewById(R.id.rl_onclick);
            my_devices_input = (ImageView) view.findViewById(R.id.my_devices_input);
        }
    }

    public static String getIntentDerviceId() {

        return IntentDerviceId;
    }


}
