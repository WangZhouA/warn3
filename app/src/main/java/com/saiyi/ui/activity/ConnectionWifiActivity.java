package com.saiyi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.application.MyApplication;

/**
 * Created by 陈姣姣 on 2017/4/19.
 */
public class ConnectionWifiActivity extends BasActivity implements View.OnClickListener {

    private Button next_button;
    private TextView my_wifi_text;
    private ImageButton my_wifi_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_item);
        MyApplication.getInstance().addActivity(this);
        initview();
        setLiteners();
        //设置不能点击
        next_button.setEnabled(false);
        my_wifi_text.setText(R.string.connection_WiFi);
    }

    private void setLiteners() {
        next_button.setOnClickListener(this);
        my_wifi_text.setOnClickListener(this);
    }

    private void initview() {
        next_button = (Button) findViewById(R.id.next_button);
        my_wifi_text = (TextView) findViewById(R.id.my_wifi_text);
        my_wifi_left = (ImageButton) findViewById(R.id.my_wifi_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
//                next_button.setBackgroundResource(R.drawable.def_login_btn1);
//                next_button.setEnabled(false);
//                next_button.setBackgroundResource(R.drawable.def_login_btn);
                break;

            case R.id.my_wifi_text:

                finish();

                break;


        }
    }
}
