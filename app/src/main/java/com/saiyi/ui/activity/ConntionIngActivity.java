package com.saiyi.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;
import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 描述：设备连接界面
 * 创建作者：黎丝军
 * 创建时间：2016/10/8 8:52
 */

public class ConntionIngActivity extends BasActivity implements OnSmartLinkListener {

    //保存设备mac
    private String mDeviceMac;
    //设备名
    private String mDeviceName;
    //用于进度
    private int sumProgress;
    //进度定时器
    private Timer mProgressTimer;
    //wifi名
    private String wifiName;
    //wifi密码
    private String wifiPass;
    //取消按钮
    private Button mCancelBtn;
    //用于更新界面
    private Handler mViewHandler = new Handler();
    //wifi配置
    protected ISmartLinker mSnifferSmartLinker;

    public static final String EXTRA_SMARTLINK_VERSION = "EXTRA_SMARTLINK_VERSION";

    private UserInfo Userinfo;

    public  static  String PeiWangMac ;


    //进度按钮
    private TextView mProgressCp;
    //进度任务
    private TimerTask mProgressTask = new TimerTask() {

        public void run() {
            if (sumProgress < 40) {
                sumProgress += new Random().nextInt(10);
            } else if (sumProgress >= 40 && sumProgress < 80) {
                sumProgress += new Random().nextInt(5);
            } else if (sumProgress >= 80 && sumProgress < 99) {
                sumProgress += new Random().nextInt(2);
            } else {
            }
            mViewHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressCp.setText("" + sumProgress);
                }
            });
        }
    };

    private ImageButton header_left;
    private TextView header_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conntion_ing);

        Userinfo = new UserInfo(this);
        mDeviceName = getResources().getString(R.string.home_text);
        mCancelBtn = (Button) findViewById(R.id.link_btn);
        mProgressCp = (TextView) findViewById(R.id.link_devicing_text);
        wifiName = getIntent().getStringExtra("WifiName");
        Log.i("---->wifiName", wifiName);
        wifiPass = getIntent().getStringExtra("WifiPass");
        Log.i("---->WifiPass", wifiPass);


        header_left = (ImageButton) findViewById(R.id.header_left);
        header_text = (TextView) findViewById(R.id.header_text);

        header_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        header_text.setText(R.string.connection_WiFi);


        /**
         * 初始化 用于配置wifi
         * */
        int smartLinkVersion = getIntent().getIntExtra(EXTRA_SMARTLINK_VERSION, 3);
        Log.i("--->smartLinkVersion", "" + smartLinkVersion);
//        mSnifferSmartLinker = MulticastSmartLinker.getInstance();


        if (smartLinkVersion == 7) {
            mSnifferSmartLinker = MulticastSmartLinker.getInstance();
        } else {
            mSnifferSmartLinker = SnifferSmartLinker.getInstance();
        }
        mProgressTimer = new Timer("ProgressTimer");
        mSnifferSmartLinker.setTimeoutPeriod(30 * 1000);
        mSnifferSmartLinker.setOnSmartLinkListener(this);

        mViewHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mSnifferSmartLinker.start(ConntionIngActivity.this, wifiPass, wifiName);
                    mProgressTimer.schedule(mProgressTask, 0, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },2000);



        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelConfigHandle();
                startActivity(new Intent(ConntionIngActivity.this, ReConntionActivity.class));
                finish();
            }
        });

    }


    private void initDate(){

    }

    //停值
    private void stopTimer() {
        if (mProgressTimer != null) {
            mProgressTask.cancel();
            mProgressTimer.cancel();
            mProgressTimer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelConfigHandle();
        }
        return true;
    }

    /**
     * 取消配网处理方法
     */
    private void cancelConfigHandle() {
        stopTimer();
        mSnifferSmartLinker.setOnSmartLinkListener(null);
        mSnifferSmartLinker.stop();
        startActivity(new Intent(ConntionIngActivity.this, MyDevicesEmptyActivity.class));
        finish();
    }

    @Override
    public void onLinked(final SmartLinkedModule smartLinkedModule) {
        mViewHandler.post(new Runnable() {
            @Override
            public void run() {
                mDeviceMac = smartLinkedModule.getMac();
                MyApplication.mac=mDeviceMac;
                Toast.makeText(ConntionIngActivity.this, getResources().getString(R.string.lock_dervice), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCompleted() {
        mViewHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ConntionIngActivity.this,getResources().getString(R.string.connect_true), Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(mDeviceName)) {
                    addDeviceToServer(mDeviceMac);
                } else {
                    cancelConfigHandle();
                }
            }
        });
    }

    private void addDeviceToServer(final String mDeviceMac) {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_ADD_DERVICE;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceMac", mDeviceMac);
            jsonObject.put("userId", Userinfo.getStringInfo("userId"));
            jsonObject.put("deviceName", mDeviceName);
            jsonObject.put("deviceIsOff", "0");
            jsonObject.put("devicePhone", "");
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
                    String result = response.body().string();
                    try {
                        JSONObject object = new JSONObject(result);
                        String flag = object.getString("result");
                        if (flag.equals(1) || flag.equals("1")) {
                            MyApplication.mac = mDeviceMac;
                            setResult(RESULT_OK);
                            startActivity(new Intent(ConntionIngActivity.this, MyDevicesEmptyActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("--->", "错误的登录信息" + response.body().string());
                }
            }
        });

    }

    @Override
    public void onTimeOut() {
        mViewHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelConfigHandle();
                Toast.makeText(ConntionIngActivity.this, getResources().getString(R.string.time_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
