package com.saiyi.ui.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.utils.UserInfo;

public  class ReConntionActivity extends BasActivity implements View.OnClickListener {

    TextView link_wifi_text_name;
    EditText link_wifi__pas;
    ImageButton link_wifi_eyes;
    Button link_btn;
    private int flag;

    private ImageButton header_left;
    private TextView header_text;

 UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_conntion);
        MyApplication.getInstance().addActivity(this);

        userInfo=new UserInfo(this);
        link_wifi_text_name = (TextView) findViewById(R.id.link_deviced_text_name);
        link_wifi__pas = (EditText) findViewById(R.id.link_deviced_edit_pas);
        link_wifi_eyes = (ImageButton) findViewById(R.id.link_deviced_eyes);
        link_btn = (Button) findViewById(R.id.link_btn);
        link_wifi_eyes.setOnClickListener(this);
        link_btn.setOnClickListener(this);

        header_left= (ImageButton) findViewById(R.id.header_left);
        header_text= (TextView) findViewById(R.id.header_text);

        header_left.setOnClickListener(this);
        header_text.setText(R.string.Connected_devices);

        //密文显示
        if (flag == 0) {
            link_wifi__pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
            flag = 1;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.link_deviced_eyes:
                if (flag == 1) {
                    link_wifi__pas.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = 0;
                } else {
                    link_wifi__pas.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = 1;
                }
                break;

            case R.id.link_btn:

                userInfo.setUserInfo("wifiPass", link_wifi__pas.getText().toString());
                userInfo.setUserInfo("ssid", link_wifi_text_name.getText().toString());

                if (!TextUtils.isEmpty(link_wifi_text_name.getText().toString()) && !TextUtils.isEmpty(link_wifi__pas.getText().toString())) {

                    startActivity(new Intent(ReConntionActivity.this, ConntionIngActivity.class).putExtra("WifiName", link_wifi_text_name.getText().toString()).
                            putExtra("WifiPass", link_wifi__pas.getText().toString()));
                }

                break;

            case R.id.header_left:
                startActivity(new Intent(ReConntionActivity.this, MyDevicesEmptyActivity.class));
                finish();

                break;


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        link_wifi_text_name.setText(getSSid());
//
//        if (!TextUtils.isEmpty(userInfo.getStringInfo("wifiPass"))){
//
//            link_wifi__pas.setText(userInfo.getStringInfo("wifiPass"));
//        }

        if (!TextUtils.isEmpty(userInfo.getStringInfo("ssid"))){

            if (userInfo.getStringInfo("ssid").contains(getSSid())){
                link_wifi_text_name.setText(getSSid());
                link_wifi__pas.setText(userInfo.getStringInfo("wifiPass"));
            }else {
                link_wifi_text_name.setText(getSSid());
                link_wifi__pas.setText("");
            }
        }else {

            link_wifi_text_name.setText(getSSid());
            link_wifi__pas.setText(userInfo.getStringInfo("wifiPass"));
        }
    }

    private String getSSid() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                } else {
                    return ssid;
                }
            }
        }

        return "";
    }
}