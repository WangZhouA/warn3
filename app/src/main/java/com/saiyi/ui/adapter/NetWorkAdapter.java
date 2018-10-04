package com.saiyi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.interfaces.WarnNmAdapterCallback;
import com.saiyi.ui.Entity.QueryLists;

/**
 * Created by 陈姣姣 on 2018/4/2.
 */

public class NetWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    private QueryLists lists;
    WarnNmAdapterCallback callback;

    public NetWorkAdapter(Context ctx, QueryLists lists, WarnNmAdapterCallback callback) {
        this.mContext = ctx;
        this.lists = lists;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.warn_itme, null, false));
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //设置数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.index_tv_1.setText((position + 1) + "");
        viewHolder.index_tv_1.setHint(mContext.getResources().getString(R.string.Networking_center_number));

        if (lists.data.size() > position) {
            Log.e("saiyi", lists.data.get(position).numberId);
            viewHolder.areaTv.setText(lists.data.get(position).numberPhone);
            viewHolder.im_add_phone_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    callback.setPhone(position+1, viewHolder.areaTv.getText().toString().trim(), lists.data.get(position).);
                }
            });
        } else {
            viewHolder.im_add_phone_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    callback.setPhone(position+1, viewHolder.areaTv.getText().toString().trim(), "");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView areaTv;
        TextView index_tv_1;
        ImageView im_add_phone_1;

        public MyViewHolder(View view) {
            super(view);
            areaTv = (TextView) view.findViewById(R.id.et_msg_1);
            index_tv_1 = (TextView) view.findViewById(R.id.index_tv_1);
            im_add_phone_1 = (ImageView) view.findViewById(R.id.im_add_phone_1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
