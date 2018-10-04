package com.saiyi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.saiyi.R;
import com.saiyi.application.MyApplication;

public class MainActivity extends BasActivity  {
    Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        },3000);

    }


}
