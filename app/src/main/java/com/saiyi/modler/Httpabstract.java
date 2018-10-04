package com.saiyi.modler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.saiyi.interfaces.HttpRequestCallback;


/**
 * Created by Administrator on 2017/6/30.
 * 获取数据的抽象类
 */

public abstract class Httpabstract {

    //获取数据回调
    public HttpRequestCallback callback = new HttpRequestCallback() {
        @Override
        public void onResponse(String sequest, int type) {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putString("data", sequest);
            b.putInt("type", type);
            msg.setData(b);
            responseHander.sendMessage(msg);
        }

        @Override
        public void onFailure(String exp) {
            Message msg = new Message();
            Bundle b = new Bundle();// 存放数据
            b.putString("exp", exp);
            msg.setData(b);
            expHander.sendMessage(msg);
        }
    };
    Handler responseHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String sequest = b.getString("data");
            int type = b.getInt("type");
            onResponses(sequest, type);
        }
    };

    Handler expHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            String exp = b.getString("exp");
            onFailures(exp);
            super.handleMessage(msg);
        }
    };

    public abstract void onResponses(String sequest, int type);

    public abstract void onFailures(String exp);
}
