package com.saiyi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.interfaces.WarnNmAdapterCallback;
import com.saiyi.ui.Entity.QueryLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WarnNumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    private QueryLists lists;
    WarnNmAdapterCallback callback;
    List<String>listsAA;

    public WarnNumAdapter(Context ctx, WarnNmAdapterCallback callback) {
        this.mContext = ctx;
        this.callback = callback;
        listsAA =new ArrayList<>();

    }

    public  void setDataAndNotify(QueryLists data){
        if(data != null && lists != data){
            lists = data;
            HashMap<String, QueryLists.Query> dataMaps = new HashMap<>();
            for(QueryLists.Query query : data.data){
                dataMaps.put(query.numberOrder, query);
            }
            List<QueryLists.Query> queryData = new ArrayList<>();
            for(int i = 1; i<= 8 ; i++){
                if(dataMaps.containsKey(String.valueOf(i))){
                    queryData.add(dataMaps.get(String.valueOf(i)));
                }else{
                    queryData.add(null);
                }
            }
            lists.data = queryData;
            notifyDataSetChanged();
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.warn_itme, null, false));
        return holder;
    }



    //设置数据
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.index_tv_1.setText((position + 1) + "");

        QueryLists.Query item = lists.data.get(position);
        if (lists.data.size() > position) {

            if (item!=null){
                viewHolder.areaTv.setText(item.numberPhone);
            }else {
                viewHolder.areaTv.setText("");
            }

            viewHolder.im_add_phone_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        callback.setPhone(position + 1, viewHolder.areaTv.getText().toString().trim(), lists.data.get(position));

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
        return lists == null ? 0 : (lists.data == null ? 0 : lists.data.size());
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
