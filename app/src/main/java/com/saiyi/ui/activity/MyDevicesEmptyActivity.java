package com.saiyi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.DerviceBean;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.fragment.FragmentLoginActivity;
import com.saiyi.ui.view.ForPlayDialog;
import com.saiyi.utils.AppManager;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyDevicesEmptyActivity extends BasActivity implements View.OnClickListener , HttpRequestCallback {

    private ImageButton my_devices_empty_header_left;
    private ImageButton my_devices_empty_header_right;
    private TextView my_devices_empty_header_text;

    //第一布局
    private LinearLayout one_linearLayout;
    //数据
    List<DerviceBean.DataBean> lists = new ArrayList<>();
    //适配器
    private MyDevicesActivityAdapter activityAdapter;
    //listview列表
    private SwipeMenuListView swipeMenuListView;
    private String userId;
    //修改名字的弹窗
    private ForPlayDialog forPlayDialog;
    //自己去修改的名字的变量
    private String ed_dervice_name;
    private UserInfo userInfo;
    BigModle bigModle = new BigModle();
    private  List<String>listsMac =new ArrayList<>();


    Intent websocketServiceIntent;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 2:
                    //没有
                    one_linearLayout.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    // 有
                    one_linearLayout.setVisibility(View.GONE);
                    activityAdapter.setData(lists);
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices_empty);

        userInfo = new UserInfo(this);
        userId=userInfo.getStringInfo("userId");
        Log.e("---->",userId+"");

        if (FragmentLoginActivity.getUser_number() != null) {
             websocketServiceIntent = new Intent(this, WebSocketService.class);
            websocketServiceIntent.putExtra("useNumber", FragmentLoginActivity.getUser_number());
            startService(websocketServiceIntent);
        }
        MyApplication.getInstance().addActivity(this);

        my_devices_empty_header_left = (ImageButton) findViewById(R.id.header_left);
        my_devices_empty_header_right = (ImageButton) findViewById(R.id.header_right);
        my_devices_empty_header_text = (TextView) findViewById(R.id.header_text);
        my_devices_empty_header_left.setOnClickListener(this);
        my_devices_empty_header_text.setText(R.string.my_dervice);
        my_devices_empty_header_left.setImageResource(R.drawable.ic_back);

        my_devices_empty_header_left.setOnClickListener(this);
        my_devices_empty_header_right.setOnClickListener(this);


        one_linearLayout = (LinearLayout) findViewById(R.id.one_linearLayout);
        one_linearLayout.setVisibility(View.VISIBLE);
        my_devices_empty_header_left.setImageResource(R.drawable.ic_menu);
        my_devices_empty_header_right.setImageResource(R.drawable.ic_add);

        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.swipeListview);
        activityAdapter = new MyDevicesActivityAdapter(MyDevicesEmptyActivity.this, lists);
        swipeMenuListView.setAdapter(activityAdapter);
        initView();

        for (int i =0 ;i<listsMac.size();i++) {
            Log.e("----->listMac", listsMac.get(i));
            bigModle.getPutMac(MyDevicesEmptyActivity.this, listsMac.get(i), "hello", 0, MyDevicesEmptyActivity.this);

        }
    }

    // 刚刚进入界面的时候就去服务端拿数据并且显示到listview上面
    private void okHttpQueryDervice() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pageNo", "1");
            jsonObject.put("pageSize", "99");
            jsonObject.put("userId", userId);
            bigModle.getData(this, jsonObject, 0x01, ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_DERVICE, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                startActivity(new Intent(MyDevicesEmptyActivity.this, PersonalActivity.class));

                break;
            case R.id.header_right:
                if (TureOrFalseNetwork()==true) {
                    startActivity(new Intent(MyDevicesEmptyActivity.this, LinkActivity.class));
                }else {
                    Toast.makeText(this, getResources().getString(R.string.No_Internet_connection), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void initView() {

        //加入侧滑显示的菜单
        //1.首先实例化SwipeMenuCreator对象
        SwipeMenuCreator creater = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem reName = new SwipeMenuItem(MyDevicesEmptyActivity.this);
                reName.setBackground(new ColorDrawable(Color.rgb(0xFA, 0xA8, 0x1E)));
                reName.setWidth(dp2px(72));
                reName.setTitle(getResources().getString(R.string.re_name));
                reName.setTitleSize(17);
                reName.setTitleColor(Color.WHITE);
                menu.addMenuItem(reName);


                SwipeMenuItem deleteItem = new SwipeMenuItem(MyDevicesEmptyActivity.this);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFE, 0x38, 0x24)));
                deleteItem.setWidth(dp2px(72));
                deleteItem.setTitle(getResources().getString(R.string.delete));
                deleteItem.setTitleSize(17);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);

            }
        };
        // set creator
        swipeMenuListView.setMenuCreator(creater);


        //2.菜单点击事件
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {

                    case 0:
                        //修改设备名字
                        odifymNameDialog(position);
                        break;
                    case 1:

                        try {
                            //删除设备
                            okhttpDelete(position);
                            lists.remove(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        activityAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

    }


    //删除设备的请求
    private void okhttpDelete(final int position) throws IOException {
        DerviceBean.DataBean itemEntity = lists.get(position);
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_DELETE_DERVICE ;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", itemEntity.getDeviceId());
            jsonObject.put("userId", userId);

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
                    Log.i("--->", "正确的修改信息" + response.body().string());
                    okHttpQueryDervice();
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });

        Log.i("--->getCount()", "" + swipeMenuListView.getCount());
        if (swipeMenuListView.getCount() == 1) {
            Message msq = handler.obtainMessage();
            msq.what = 2;
            handler.sendMessage(msq);
        }
    }


    //修改设备的请求
    private void odifymNameDialog(final int position) {

        forPlayDialog = new ForPlayDialog(MyDevicesEmptyActivity.this);
        forPlayDialog.setTitle(getResources().getString(R.string.redervice_name));
        forPlayDialog.setYesOnclickListener(getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ed_dervice_name = forPlayDialog.setMessage();

                if (ed_dervice_name != null) {
                    //已经拿到用户舒服的名字，现在需要联网请求去改变名字了
                    //===============================
                    lists.get(position).setDeviceName(ed_dervice_name);
                    //==================================
                    activityAdapter.notifyDataSetChanged();
                    okHttpReName(position);
                }

                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.setNoOnclickListener(getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                forPlayDialog.dismiss();
            }
        });
        forPlayDialog.show();
    }

    private void okHttpReName(final int position) {

        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_ReName_DERVICE;

        DerviceBean.DataBean itemEntity = lists.get(position);

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceId", itemEntity.getDeviceId());
            jsonObject.put("deviceName", ed_dervice_name);

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
                    Log.i("--->", "正确的修改信息" + response.body().string());

                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    //判断是否有网络连接
    private boolean TureOrFalseNetwork() {

        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        return true;
    }

    /**
     * 下拉刷新
     */

    @Override
    protected void onResume() {
        okHttpQueryDervice();
        super.onResume();
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
                stopService( websocketServiceIntent );
                AppManager.getInstance().closeAllActivity();
                AppManager.getInstance().removeActivity("MyDevicesEmptyActivity");
                MyApplication.getInstance().exit();
                finish();


            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    DerviceBean derviceBean;
    @Override
    public void onResponse(String sequest, int type) {

        Log.e("----进来没",sequest);
        Gson gson = new Gson();
        derviceBean= gson.fromJson(sequest,DerviceBean.class);
        lists.clear();
        lists.addAll(derviceBean.getData());
        if(derviceBean.getData().size()>0){
            handler.sendEmptyMessage(3);
            Log.e("----有数据",sequest);
        }else {
            handler.sendEmptyMessage(2);
            Log.e("----没有有数据",sequest);
        }
    }

    @Override
    public void onFailure(String exp) {

    }
}

