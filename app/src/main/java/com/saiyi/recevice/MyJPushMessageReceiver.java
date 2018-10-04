package com.saiyi.recevice;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by 陈姣姣 on 2018/8/22.
 */

public class MyJPushMessageReceiver extends BroadcastReceiver {

        private NotificationManager nm;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == nm) {
                nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            Bundle bundle = intent.getExtras();
            Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + bundle);

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                Log.d(TAG, "JPush用户注册成功");

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的自定义消息");

                // Push Talk messages are push down by custom message format

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "接受到推送下来的通知");


            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "用户点击打开了通知");

            } else {
                Log.d(TAG, "Unhandled intent - " + intent.getAction());
            }
        }

}
