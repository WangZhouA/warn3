package com.saiyi.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.fragment.AlarmBfFragement;
import com.saiyi.ui.fragment.AlarmCfFragement;
import com.saiyi.ui.fragment.AlarmLfFragement;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlarmActivity extends FragmentActivity implements OnClickListener, HttpRequestCallback {
    AlarmBfFragement bfFragement;
    AlarmCfFragement cfFragement;
    AlarmLfFragement lfFragement;

    String mac;
    String derviceName;
    Fragment mLastFragment;
    View bf;
    View cf;
    View lf;
    private int flagToBtton = 1;
    private String IntentDerviceId;
    Intent myintent;

    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        MyApplication.getInstance().addActivity(this);
        mac = getIntent().getStringExtra("mac");
        derviceName = getIntent().getStringExtra("name");
        userInfo= new UserInfo(this);
        String newMacString = mac.replaceAll(":", "");
        ConstantInterface.MAC = newMacString;
        bf = findViewById(R.id.llt_buf);
        cf = findViewById(R.id.llt_cf);
        lf = findViewById(R.id.llt_lsf);
        bf.setOnClickListener(this);
        cf.setOnClickListener(this);
        lf.setOnClickListener(this);
        ImageButton header_left = (ImageButton) findViewById(R.id.header_left);
        ImageButton header_right = (ImageButton) findViewById(R.id.header_right);
        TextView header_text = (TextView) findViewById(R.id.header_text);
        header_text.setText(derviceName);
        header_text.setTextColor(Color.WHITE);
        header_right.setVisibility(View.GONE);
        header_left.setVisibility(View.VISIBLE);
        header_left.setImageResource(R.drawable.ic_back_w);
        header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.header_all).setBackgroundResource(R.drawable.alarm_bg);

        if (TextUtils.isEmpty(userInfo.getStringInfo("flag"))) {
            changeTab(cf.getId());
        }else {
            if (userInfo.getStringInfo("flag").contains("0")){
                flagToBtton = 0;
                changeTab(cf.getId());
                bf.setSelected(true);
                cf.setSelected(false);
                lf.setSelected(false);
            }else if (userInfo.getStringInfo("flag").contains("1")){
                flagToBtton = 1;
                changeTab(cf.getId());
                bf.setSelected(false);
                cf.setSelected(true);
                lf.setSelected(false);
                if (cfFragement == null) {
                    cfFragement = new AlarmCfFragement();
                }


            }else {
                changeTab(cf.getId());
                flagToBtton = 2;
                bf.setSelected(false);
                cf.setSelected(false);
                lf.setSelected(true);
            }
        }


        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
            IntentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            Log.i("--->IntentDerviceId", "" + IntentDerviceId);
        }


        IntentFilter intentFilter =new IntentFilter();
        intentFilter.addAction(ConstantInterface.UPDATE_BUFANG);
        intentFilter.addAction(ConstantInterface.UPDATE_CEFANG);
        intentFilter.addAction(ConstantInterface.UPDATE_LIUSHOUBUFANG);
        registerReceiver(myBroadcastReceiver,intentFilter);



        okHttpGetMac();





    }

    private void okHttpGetMac() {

        final String strurl = ConstantInterface.DERVICEID_TOMAC;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", IntentDerviceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(strurl)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String  result = response.body().string();
                    try {
                        JSONObject json =new JSONObject(result);
                        if (json.getString("result").contains("1")){

                            JSONObject jsonItem  =json.getJSONObject("data");
                            ConstantInterface.MAC = jsonItem.getString("deviceMac");
                            Log.e("----->MAC", ConstantInterface.MAC);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.i("--->", "错误的信息" + response.body().string());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
//		changeTab(v.getId());

        switch (v.getId()) {
            case R.id.llt_buf:
                flagToBtton = 0;
                userInfo.setUserInfo("flag","0");
                bf.setSelected(true);
                cf.setSelected(false);
                lf.setSelected(false);
                okHttpPostState();
                break;
            case R.id.llt_cf:
                flagToBtton = 1;
                userInfo.setUserInfo("flag","1");
                bf.setSelected(false);
                cf.setSelected(true);
                lf.setSelected(false);
                if (cfFragement == null) {
                    cfFragement = new AlarmCfFragement();
                }
                okHttpPostState();
                break;
            case R.id.llt_lsf:
                flagToBtton = 2;
                userInfo.setUserInfo("flag","2");
                bf.setSelected(false);
                cf.setSelected(false);
                lf.setSelected(true);
                okHttpPostState();
                break;
            default:
                break;
        }


    }



    private BroadcastReceiver myBroadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.contains(ConstantInterface.UPDATE_BUFANG)){
                flagToBtton = 0;
                bf.setSelected(true);
                cf.setSelected(false);
                lf.setSelected(false);
                userInfo.setUserInfo("flag","0");



            }else if (action.contains(ConstantInterface.UPDATE_CEFANG)){
                flagToBtton = 1;
                bf.setSelected(false);
                cf.setSelected(true);
                lf.setSelected(false);
                if (cfFragement == null) {
                    cfFragement = new AlarmCfFragement();
                }
                userInfo.setUserInfo("flag","1");


            }else if (action.contains(ConstantInterface.UPDATE_LIUSHOUBUFANG )){
                flagToBtton = 2;
                bf.setSelected(false);
                cf.setSelected(false);
                lf.setSelected(true);
                userInfo.setUserInfo("flag","2");
            }

        }
    };









    private void changeTab(int tabId) {
        Fragment newFragment = getSupportFragmentManager().findFragmentByTag(tabId + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (tabId) {
            case R.id.llt_buf:
                flagToBtton = 0;
                bf.setSelected(true);
                cf.setSelected(false);
                lf.setSelected(false);
                if (bfFragement == null) {
                    bfFragement = new AlarmBfFragement();
                }
//			newFragment = bfFragement;

                break;
            case R.id.llt_cf:
                flagToBtton = 1;
                bf.setSelected(false);
                cf.setSelected(true);
                lf.setSelected(false);
                if (cfFragement == null) {
                    cfFragement = new AlarmCfFragement();
                }
                newFragment = cfFragement;
//                okHttpPostState();

                break;
            case R.id.llt_lsf:
                flagToBtton = 2;
                bf.setSelected(false);
                cf.setSelected(false);
                lf.setSelected(true);
                if (lfFragement == null) {
                    lfFragement = new AlarmLfFragement();
                }
//			newFragment = lfFragement;
                break;

            default:
                break;
        }

//		if (!newFragment.isAdded()) { // 先判断是否被add过
        if (mLastFragment == null) {
            ft.add(R.id.container, newFragment, tabId + "").commit();

//			} else {
//				ft.hide(mLastFragment).add(R.id.container, newFragment, tabId + "").commit(); // 隐藏当前的fragment，add下一个到Activity中
//			}
//		} else {
//			if (mLastFragment == null) {
//				ft.show(newFragment).commit();
//			} else {
//				ft.hide(mLastFragment).show(newFragment).commit(); // 隐藏当前的fragment，显示下一个
//			}
        }
//		mLastFragment = newFragment;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    BigModle bigModle = new BigModle();

    //设备布放/撤防/留守布放
    private void okHttpPostState() {
//        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_DERVICE_STATE;
//        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
//        JSONObject jsonObject = new JSONObject();
//        try {
//            if (flagToBtton == 1) {
//                jsonObject.put("deviceId", IntentDerviceId);
//                jsonObject.put("zoneOnOff", 0);
//            } else if (flagToBtton == 0) {
//                jsonObject.put("deviceId", IntentDerviceId);
//                jsonObject.put("zoneOnOff", 1);
//            } else if (flagToBtton == 2) {
//                jsonObject.put("deviceId", IntentDerviceId);
//                jsonObject.put("zoneOnOff", 2);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//
//        final Request request = new Request.Builder()
//                .url(strurl)
//                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("--->", "" + e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    Log.i("--->", "正确的信息" + response.body().string());
//                    //布放
                    if (flagToBtton==0) {
                        bigModle.getPutMac(AlarmActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "0100", 0, AlarmActivity.this);
                        myintent=new Intent("UPDATE");
                        sendBroadcast(myintent);
                    }else if (flagToBtton==1){
                        bigModle.getPutMac(AlarmActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "0000", 0, AlarmActivity.this);
                        myintent=new Intent("UPDATE");
                        sendBroadcast(myintent);
                    }else {
                        bigModle.getPutMac(AlarmActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "0200", 0, AlarmActivity.this);
                        myintent=new Intent("UPDATE");
                        sendBroadcast(myintent);
                    }
//                } else {
//                    Log.i("--->", "错误的信息" + response.body().string());
//                }
//            }
//        });
    }

    //成功
    @Override
    public void onResponse(String sequest, int type) {
        String data = sequest;
        Toast.makeText(this, getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
    }

    //失败
    @Override
    public void onFailure(String exp) {
        Toast.makeText(this, getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

     unregisterReceiver(myBroadcastReceiver);
    }
}

