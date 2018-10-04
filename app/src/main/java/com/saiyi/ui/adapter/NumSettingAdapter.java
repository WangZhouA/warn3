package com.saiyi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saiyi.R;

public class NumSettingAdapter extends BaseAdapter{

	Context mContext;
	
	public  NumSettingAdapter(Context context) {
		mContext = context;
	}
	@Override
	public int getCount() {
		return 8;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 final ViewHolder viewHolder;
	        if(convertView ==null){
	            convertView = LayoutInflater.from(mContext).inflate(R.layout.cf_chid_msg_fragment_item,null);
	            viewHolder = new ViewHolder(convertView);
	            convertView.setTag(viewHolder);
	        }else{
	            viewHolder = (ViewHolder)convertView.getTag();
	        }
	        viewHolder.areaTv.setText(position+1+"");

	        return convertView;
	}

	
	class ViewHolder {
		TextView areaTv;
		public  ViewHolder(View view) {
			areaTv = (TextView) view.findViewById(R.id.index_tv);
	
		}
	}


}
