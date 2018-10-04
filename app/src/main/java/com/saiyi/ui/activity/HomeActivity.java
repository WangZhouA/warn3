package com.saiyi.ui.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.ui.fragment.FragmentLoginActivity;
import com.saiyi.ui.fragment.FragmentRegisterActivity;
import com.saiyi.utils.AppManager;

public class HomeActivity extends BasActivity implements View.OnClickListener {

    private FragmentLoginActivity fLogin;
    private FragmentRegisterActivity fRegister;

    private Button login;
    private Button register;

    private static  String userNumToRegister;
    private static  String userPasToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MyApplication.getInstance().addActivity(this);
        login = (Button) findViewById(R.id.login_btn);
        register = (Button) findViewById(R.id.register_btn);
        login.setSelected(true);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        setDefaultFragment();// 设置默认的Fragment

//        Toast.makeText(this,"进来没大哥",Toast.LENGTH_SHORT).show();
        Intent i = getIntent();
        if (i.getStringExtra("start") != null) {
            chongdeng = i.getStringExtra("start");
            FragmentManager fm = getFragmentManager();
            // 开启Fragment事务
            FragmentTransaction transaction = fm.beginTransaction();
            login.setSelected(true);
            register.setSelected(false);
            if (fLogin == null) {
                fLogin = new FragmentLoginActivity();
            }
            // 使用当前Fragment的布局替代fragment的控件
            transaction.replace(R.id.fragment, fLogin);
        }


        if (i.getStringExtra("userNum") != null && i.getStringExtra("pwd") != null) {
            userNumToRegister =i.getStringExtra("userNum");
            userPasToRegister =i.getStringExtra("pwd");

        }

    }
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fLogin = new FragmentLoginActivity();
        transaction.replace(R.id.fragment, fLogin);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {

        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.login_btn:
                login.setSelected(true);
                register.setSelected(false);
                if (fLogin == null) {
                    fLogin = new FragmentLoginActivity();
                }
                // 使用当前Fragment的布局替代fragment的控件
                transaction.replace(R.id.fragment, fLogin);
                break;
            case R.id.register_btn:
                login.setSelected(false);
                register.setSelected(true);
                if (fRegister == null) {
                    fRegister = new FragmentRegisterActivity();
                }
                transaction.replace(R.id.fragment, fRegister);
                break;
        }
        // 事务提交
        transaction.commit();
    }



    private  static  String   chongdeng;
    public   static  String getflag(){
        return   chongdeng;
    }
    public   static  String getUserNumToRegister(){
        return   userNumToRegister;
    }
    public   static  String getUserPasToRegister(){
        return   userPasToRegister;
    }



    /**
     * 按两次退出
     */
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getInstance().closeAllActivity();
                MyApplication.getInstance().exit();

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
