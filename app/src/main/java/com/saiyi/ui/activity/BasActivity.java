package com.saiyi.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.utils.AppManager;
import com.saiyi.utils.AppUtils;
import com.saiyi.utils.Constants;
import com.saiyi.utils.SPUtils;

/**
 * Created by 陈姣姣 on 2017/5/3.
 */
public  abstract class BasActivity extends Activity {
String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        MyApplication.getInstance().addActivity(this);

        //配置语言
        AppUtils.setLanguage(this, SPUtils.get(this, SPUtils.LANGUAGE_CODE, Constants.LANGUAGE_CN).toString());

        //获取activity名称
        TAG = this.getClass().getSimpleName();
        //将activity名称添加到管理类
        if (getIsPutActivity()) {
            AppManager.getInstance().putActivity(TAG, this);
        }

    }


    // ---------------下面两个类,是从写点击事件分发,只要不点击到EditText上就不现实软键盘--------------------

    /**
     * 对于所有的Activity都适用,所以定义在BaseActivity里面,被子类继承
     * 点击外部隐藏输入软键盘,获取到EditText的位置,做出点击判断
     */
    public boolean isClickEditText(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的 location 位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            // 此处根据输入框左上位置和宽高获得右下位置
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 分发点击事件.点击外部键盘消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前获得当前焦点所在View
            View view = getCurrentFocus();
            if (isClickEditText(view, event)) {
                // 如果不是edittext，则隐藏键盘
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    // 隐藏键盘
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(event);
        }
        /**
         * 看源码可知superDispatchTouchEvent 是个抽象方法，用于自定义的Window
         * 此处目的是为了继续将事件由dispatchTouchEvent (MotionEvent event) 传递到onTouchEvent
         * (MotionEvent event) 必不可少，否则所有组件都不能触发 onTouchEvent (MotionEvent event)
         */
        if (getWindow().superDispatchTouchEvent(event)) {
            return true;
        }
        return onTouchEvent(event);
    }
    // ---------------上面两个类,是从写点击事件分发,只要不点击到EditText上就不现实软键盘--------------------

    private BroadcastReceiver imReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WebSocketService.WEBSOCKET_ACTION.equals(action)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String msg = bundle.getString("message");
                    if (!TextUtils.isEmpty(msg))
                        getMessage(msg);
                }

            }
        }
    };

    protected void getMessage(String msg) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(WebSocketService.WEBSOCKET_ACTION);
        registerReceiver(imReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(imReceiver);
        AppManager.getInstance().removeActivity(TAG);
    }


    @Override
    protected void onResume() {

        if (TureOrFalseNetwork() == true) {

        } else {

            Toast.makeText(this,   getResources().getString(R.string.No_Internet_connection), Toast.LENGTH_SHORT).show();

        }
        super.onResume();

    }
    //判断是否有网络连接
    private boolean TureOrFalseNetwork() {

        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        return true;
    }



    //判断网络类别
    private   boolean NetworkType( ){
        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {   //MOBILE
            return info.isConnected();
        } else {
            return false;

        }
    }


    protected boolean getIsPutActivity() {
        return true;
    }

}
