package com.saiyi.ui.fragment.child;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.modler.BigModle;
import com.saiyi.ui.activity.NetWorkActivity;
import com.saiyi.ui.activity.WarnNumActivity;
import com.saiyi.ui.adapter.MyDevicesActivityAdapter;
import com.saiyi.ui.view.ForPlayDialog;

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

public class CfChildMsgSetFragement extends Fragment implements View.OnClickListener, HttpRequestCallback {
    View baseView;

    ForPlayDialog forPlayDialogs;
    private String strPassWord;
    private String strCid;
    private int flag;
    private String IntentDerviceId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (baseView == null) {
            baseView = inflater.inflate(R.layout.cf_chid_num_fragment, null);
        }
        baseView.findViewById(R.id.num).setOnClickListener(this);
        baseView.findViewById(R.id.pwd).setOnClickListener(this);
        baseView.findViewById(R.id.cid).setOnClickListener(this);
        baseView.findViewById(R.id.net).setOnClickListener(this);

        if (MyDevicesActivityAdapter.getIntentDerviceId() != null) {
            IntentDerviceId = MyDevicesActivityAdapter.getIntentDerviceId();
            Log.i("--->id_dervice", IntentDerviceId);
        }


        return baseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.net:
                startActivity(new Intent(getActivity(), NetWorkActivity.class));
                break;
            case R.id.num:
                startActivity(new Intent(getActivity(), WarnNumActivity.class));
                break;
            case R.id.pwd:

                forPlayDialogs = new ForPlayDialog(getActivity());
                forPlayDialogs.setTitle(getActivity().getResources().getString(R.string.ed_baojing_pass));
                forPlayDialogs.setYesOnclickListener(getActivity().getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        flag = 1;
                        strPassWord = forPlayDialogs.setMessage();
                        // bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "10" + "0" + (position + 1) + phone_one + ",1", QUREPHONE, WarnNumActivity.this);
                        okHttpSet();
                        forPlayDialogs.dismiss();
                    }
                });
                forPlayDialogs.setNoOnclickListener(getActivity().getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        forPlayDialogs.dismiss();
                    }
                });
                forPlayDialogs.show();


                break;
            case R.id.cid:

                forPlayDialogs = new ForPlayDialog(getActivity());
                forPlayDialogs.setTitle(getActivity().getResources().getString(R.string.set_CID_number));
                forPlayDialogs.setYesOnclickListener(getActivity().getResources().getString(R.string.sure), new ForPlayDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        flag = 2;
                        strCid = forPlayDialogs.setMessage();
                        Log.i("--->strCid", strCid);
                        okHttpSet();
                        forPlayDialogs.dismiss();
                    }
                });
                forPlayDialogs.setNoOnclickListener(getActivity().getResources().getString(R.string.link_cancle), new ForPlayDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        //   Toast.makeText(getActivity(), "点击了--取消--按钮", Toast.LENGTH_LONG).show();
                        forPlayDialogs.dismiss();
                    }
                });
                forPlayDialogs.show();


                break;
        }
    }

    BigModle bigModle = new BigModle();

    private void okHttpSet() {
        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_ReName_DERVICE;
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        JSONObject jsonObject = new JSONObject();
        try {
            if (flag == 1) {
                jsonObject.put("deviceId", IntentDerviceId);
                jsonObject.put("devicePassword", strPassWord);

            } else {
                jsonObject.put("deviceId", IntentDerviceId);
                jsonObject.put("deviceCid", strCid);
            }
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
                    //设置CID
                    bigModle.getPutMac(getActivity(), ConstantInterface.MAC, ConstantInterface.MAC + "10" + strCid + "4", 0, CfChildMsgSetFragement.this);
                } else {
                    Log.i("--->", "错误的修改信息" + response.body().string());
                }
            }
        });
    }

    @Override
    public void onResponse(String sequest, int type) {
        Toast.makeText(getActivity(), getResources().getString(R.string.ok), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String exp) {
        Toast.makeText(getActivity(), getResources().getString(R.string.no), Toast.LENGTH_LONG).show();
    }
}
