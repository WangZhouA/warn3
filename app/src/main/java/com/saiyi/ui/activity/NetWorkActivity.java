package com.saiyi.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.interfaces.WarnNmAdapterCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.QueryLists;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.adapter.NetWorkAdapter;
import com.saiyi.ui.view.ForPlayDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class NetWorkActivity extends BasActivity  implements View.OnClickListener, HttpRequestCallback, WarnNmAdapterCallback {


    //    ListView warn_num_list;
//    WarnNumActivityAdapter adapter;
    private ImageButton header_left;
    private ImageButton header_right;
    private TextView header_text;
    private RecyclerView recyclerView;
    private String intentDerviceId;
    private ForPlayDialog forPlayDialog;
    private QueryLists querylists;
    private ForPlayDialog forPlayDialogToReName;
    NetWorkAdapter warnNumAdapter;
    LinearLayoutManager layoutManager;
    private final int GETDATA = 100;
    private final int ADDPHONE_MAC = 200;
    private final int ADDPHONE_HTTP = 300;
    private final int EDITPHONE_MAC = 400;
    private final int EDITPHONE_HTTP = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_num);
        MyApplication.getInstance().addActivity(this);
        header_left = (ImageButton) findViewById(R.id.header_left);
        header_right = (ImageButton) findViewById(R.id.header_right);
        header_text = (TextView) findViewById(R.id.header_text);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        header_left.setOnClickListener(this);
        header_right.setOnClickListener(this);
        header_left.setImageResource(R.drawable.ic_back);
        header_right.setVisibility(View.GONE);
        header_text.setText(R.string.baojing_number);
        forPlayDialog = new ForPlayDialog(NetWorkActivity.this);
        forPlayDialog.setTitle(getResources().getString(R.string.Enter_the_contact_number));
        forPlayDialogToReName = new ForPlayDialog(NetWorkActivity.this);
        forPlayDialogToReName.setTitle(getResources().getString(R.string.rethe_contact_number));
        header_text.setText(R.string.Networking_center_number);
        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
            intentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
        }
        okHttpQureNumber();

        IntentFilter intentFilter =new IntentFilter(ConstantInterface.UPDATE_MESSAGE_NOW);
        registerReceiver(myBroadcastReceiver,intentFilter);

    }

    //添加号码{"numberPhone":"15707906505","numberType":"1","deviceId":"1"}
    private void addPhone(String phone, int pos) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberPhone", phone);
            jsonObject.put("numberType", 3);
            jsonObject.put("deviceId", intentDerviceId);
            jsonObject.put("numberOrder", pos);
            bigModle.getData(this, jsonObject, ADDPHONE_HTTP, ConstantInterface.HTTP_SERVICE_ADDRESS_ADD_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}
    }

    //x修改号码{"numberPhone":"15707906505","numberId":"1","deviceId":"1"}
    private void editPhone(String numberId, String phone, int pos) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberPhone", phone);
            jsonObject.put("numberId", numberId);
            jsonObject.put("deviceId", intentDerviceId);
            jsonObject.put("numberType", 3 + "");
            jsonObject.put("numberOrder", pos + "");
            bigModle.getData(this, jsonObject, EDITPHONE_HTTP, ConstantInterface.HTTP_SERVICE_ADDRESS_RENAME_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}
    }

    //查询电话号码
    private void okHttpQureNumber() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberType", 3);
            jsonObject.put("pageNo", 1);
            jsonObject.put("pageSize", 2);
            jsonObject.put("deviceId", intentDerviceId);
            bigModle.getData(this, jsonObject, GETDATA, ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}

    }

    String phone_one;
    String phone_id;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:
                finish();
                break;

        }
    }

    /**
     * final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_ADD_NUMBER;
     * jsonObject.put("numberPhone", phone_one);
     * jsonObject.put("numberType", 1);
     * jsonObject.put("deviceId", intentDerviceId);
     * String flagStr;
     * flagStr = "0" + flag;
     * bigModle.getPutMac(WarnNumActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "10" + flagStr + phone + "1", 0, WarnNumActivity.this);
     */

    BigModle bigModle = new BigModle();

    /**
     * JSONObject jsonObject = new JSONObject();
     * jsonObject.put("numberType", 1);
     * jsonObject.put("pageNo", 1);
     * jsonObject.put("pageSize", 10);
     * jsonObject.put("deviceId", intentDerviceId);
     * bigModle.getData(this, jsonObject, GETDATA, ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_NUMBER, this);
     *
     * @param sequest
     * @param type
     */
    @Override
    public void onResponse(String sequest, int type) {

        switch (type) {
            case ADDPHONE_MAC://设置设备成功之后,再添加给数据库____添加
//                Toast.makeText(NetWorkActivity.this, sequest, Toast.LENGTH_LONG).show();
                //添加到数据库
                break;
            case EDITPHONE_MAC://设置设备成功之后,再添加给数据库___修改
//                Toast.makeText(NetWorkActivity.this, sequest, Toast.LENGTH_LONG).show();
                //添加到数据库
                break;

            case ADDPHONE_HTTP://添加给数据库_____添加
//                Toast.makeText(NetWorkActivity.this, sequest, Toast.LENGTH_LONG).show();
                okHttpQureNumber();
                break;
            case EDITPHONE_HTTP://添加给数据库_____修改
//                Toast.makeText(NetWorkActivity.this, sequest, Toast.LENGTH_LONG).show();
                okHttpQureNumber();
                break;
            case GETDATA:
                JSONObject object = null;
                try {
                    Log.e("saiyi", sequest);
                    object = new JSONObject(sequest);
                    String flag = object.getString("result");
                    if (flag.equals(1) || flag.equals("1")) {
                        querylists = new Gson().fromJson(sequest, QueryLists.class);
                        warnNumAdapter = new NetWorkAdapter(this, querylists, this);
                        recyclerView.setAdapter(warnNumAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String exp) {
        Toast.makeText(NetWorkActivity.this, getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
    }


    @Override
    public void setPhone(final int position, final String phone, QueryLists.Query item ) {
       phone_id = item==null?null:item.numberId;;
        if (TextUtils.isEmpty(phone)) {
            //添加
            forPlayDialog.setYesOnclickListener(getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {

                @Override
                public void onYesClick() {
                    phone_one = forPlayDialog.setMessage();
                    forPlayDialog.setMessageToNull();
                    forPlayDialog.dismiss();
                    //提交数据 F0FE6B2F6D20 10 01 13976249310,3
//                    addPhone(phone_one, position);
                    bigModle.getPutMac(NetWorkActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "10" + "0" + (position ) + phone_one + ",3", ADDPHONE_MAC, NetWorkActivity.this);
                }
            });
            forPlayDialog.setNoOnclickListener(getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    forPlayDialog.dismiss();
                }
            });
            forPlayDialog.show();
        } else {
            //修改
            forPlayDialogToReName.setYesOnclickListener(getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {
                @Override
                public void onYesClick() {
                    phone_one = forPlayDialogToReName.setMessage();
                    forPlayDialogToReName.setMessageToNull();
                    forPlayDialogToReName.dismiss();
                    //提交数据
//                    editPhone(phone_ids, phone_one, position);
                    bigModle.getPutMac(NetWorkActivity.this, ConstantInterface.MAC, ConstantInterface.MAC + "10" + "0" + (position ) + phone_one + ",3", EDITPHONE_MAC, NetWorkActivity.this);
                }
            });
            forPlayDialogToReName.setNoOnclickListener(getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
                @Override
                public void onNoClick() {
                    forPlayDialogToReName.dismiss();
                }
            });
            forPlayDialogToReName.show();
        }
    }


    private BroadcastReceiver myBroadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
//                Toast.makeText(context, "action"+action, Toast.LENGTH_SHORT).show();
            if (action.contains(ConstantInterface.UPDATE_MESSAGE_NOW)){
                okHttpQureNumber();

            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }
}