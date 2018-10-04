package com.saiyi.ui.fragment.child;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.interfaces.WarnNmAdapterCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.Entity.QueryLists;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.adapter.WarnNumAdapter;
import com.saiyi.ui.view.ForPlayDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class CfChildNumSetFragement extends Fragment implements HttpRequestCallback, WarnNmAdapterCallback {

    private String intentDerviceId;
    private ForPlayDialog forPlayDialog;
    private ForPlayDialog forPlayDialogToReName;
    private int count;
    private RecyclerView recyclerView;
    private QueryLists querylists;
    WarnNumAdapter warnNumAdapter;
    LinearLayoutManager layoutManager;
    private final int GETDATA = 100;
    private final int ADDPHONE_MAC = 200;
    private final int ADDPHONE_HTTP = 300;
    private final int EDITPHONE_MAC = 400;
    private final int EDITPHONE_HTTP = 500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sms_fragment, null);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        forPlayDialog = new ForPlayDialog(getActivity());
        forPlayDialog.setTitle(getActivity().getResources().getString(R.string.ed_jiejing_duanxin));
        forPlayDialogToReName = new ForPlayDialog(getActivity());
        forPlayDialogToReName.setTitle(getActivity().getResources().getString(R.string.re_jiejing_duanxin));
        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
//			Toast.makeText(getActivity(), MyDevicesActivityAdapter.getIntentDerviceId(), Toast.LENGTH_SHORT).show();
            intentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            okHttpQureNumber();
        }


        warnNumAdapter = new WarnNumAdapter(getActivity(), this);
        recyclerView.setAdapter(warnNumAdapter);

        IntentFilter intentFilter =new IntentFilter(ConstantInterface.UPDATE_MESSAGE_NOW);
        getActivity().registerReceiver(myBroadcastReceiver,intentFilter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        getActivity().unregisterReceiver(myBroadcastReceiver);
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


    //查询电话号码
    private void okHttpQureNumber() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberType", 2);
            jsonObject.put("pageNo", 1);
            jsonObject.put("pageSize", 10);
            jsonObject.put("deviceId", intentDerviceId);
            bigModle.getData(getActivity(), jsonObject, GETDATA, ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}

    }   //添加号码{"numberPhone":"15707906505","numberType":"1","deviceId":"1"}

    private void addPhone(String phone) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberPhone", phone);
            jsonObject.put("numberType", 2);
            jsonObject.put("deviceId", intentDerviceId);
            bigModle.getData(getActivity(), jsonObject, ADDPHONE_HTTP, ConstantInterface.HTTP_SERVICE_ADDRESS_ADD_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}
    }

    //添加号码{"numberPhone":"15707906505","numberId":"1","deviceId":"1"}
    private void editPhone(String numberId, String phone) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("numberPhone", phone);
            jsonObject.put("numberId", numberId);
            jsonObject.put("deviceId", intentDerviceId);
            bigModle.getData(getActivity(), jsonObject, EDITPHONE_HTTP, ConstantInterface.HTTP_SERVICE_ADDRESS_RENAME_NUMBER, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //{"pageNo":"1","pageSize":"2","deviceId":"1","numberType":"1"}
    }

    String phone_one;
    String phone_id;


    /**
     * final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_ADD_NUMBER;
     * jsonObject.put("numberPhone", phone_one);
     * jsonObject.put("numberType", 1);
     * jsonObject.put("deviceId", intentDerviceId);
     * String flagStr;
     * flagStr = "0" + flag;
     * bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "10" + flagStr + phone + "1", 0, getActivity());
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
                if (sequest.contains("1")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), sequest, Toast.LENGTH_LONG).show();
                }
                //添加到数据库
                break;
            case EDITPHONE_MAC://设置设备成功之后,再添加给数据库___修改
                if (sequest.contains("1")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), sequest, Toast.LENGTH_LONG).show();
                }
                //添加到数据库
                break;

            case ADDPHONE_HTTP://添加给数据库_____添加
                if (sequest.contains("1")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), sequest, Toast.LENGTH_LONG).show();
                }
                okHttpQureNumber();
                break;
            case EDITPHONE_HTTP://添加给数据库_____修改
                if (sequest.contains("1")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), sequest, Toast.LENGTH_LONG).show();
                }
                okHttpQureNumber();
                break;
            case GETDATA:
                JSONObject object = null;
                try {
                    object = new JSONObject(sequest);
                    String flag = object.getString("result");
                    if (flag.equals(1) || flag.equals("1")) {
                        querylists = new Gson().fromJson(sequest, QueryLists.class);
                        warnNumAdapter.setDataAndNotify(querylists);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onFailure(String exp) {
        Toast.makeText(getActivity(), getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
    }


    @Override
    public void setPhone(final int position, final String phone,  QueryLists.Query item) {


        phone_id = item==null?null:item.numberId;
        if (TextUtils.isEmpty(phone)) {
            //添加
            forPlayDialog.setYesOnclickListener(getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {

                @Override
                public void onYesClick() {
                    phone_one = forPlayDialog.setMessage();
                    forPlayDialog.setMessageToNull();
                    forPlayDialog.dismiss();
                    //提交数据 F0FE6B2F6D20 10 01 13976249310,1
//                    addPhone(phone_one);
                    bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "10" + "0" + (position ) + phone_one + ",2", ADDPHONE_MAC, CfChildNumSetFragement.this);
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

//                    editPhone(phone_ids, phone_one);
                    bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "10" + "0" + (position ) + phone_one + ",2", EDITPHONE_MAC, CfChildNumSetFragement.this);
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
}





