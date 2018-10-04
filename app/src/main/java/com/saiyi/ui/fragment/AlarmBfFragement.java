package com.saiyi.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saiyi.R;

public class AlarmBfFragement extends Fragment {

	public AlarmBfFragement() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView tx = new TextView(getActivity());
		tx.setText(R.string.protection);
		return tx;
	}

}
