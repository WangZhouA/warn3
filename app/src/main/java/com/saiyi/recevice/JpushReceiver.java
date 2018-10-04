package com.saiyi.recevice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.activity.BaojingActvivity;
import com.saiyi.ui.activity.Forced_Offline_Dialog;
import com.saiyi.ui.activity.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 陈姣姣 on 2018/7/26.
 */
public class JpushReceiver extends BroadcastReceiver {

    int count=1;
    private static final String TAG = "JIGUANG-Example";
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle bundle = intent.getExtras();
//                Toast.makeText(context, ""+printBundle(bundle), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
                try {
                JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_MESSAGE));
                String message = jsonObject.getString("message");


//                        0001 撤防
//                        0002 布防
//                        0003 留守
//                        0004 设置号码组
//                        0005 设置防区属性
//                        0006 延迟设置
//                        0007 设置CID


                if (message.equals("isLoginOut")) {
                    showDialog(context);
                }else   if (message.equals("0004")) {
                    Log.e("------>message","0004");
                    Intent intentA =new Intent(ConstantInterface.UPDATE_MESSAGE_NOW);
                    context.sendBroadcast(intentA);
                }else if (message.equals("0001")) {
                    Intent intentA = new Intent(ConstantInterface.UPDATE_CEFANG);
                    context.sendBroadcast(intentA);
                }else if (message.equals("0002")) {
                    Intent intentA = new Intent(ConstantInterface.UPDATE_BUFANG);
                    context.sendBroadcast(intentA);
                }else if (message.equals("0003")) {
                    Intent intentA = new Intent(ConstantInterface.UPDATE_LIUSHOUBUFANG);
                    context.sendBroadcast(intentA);
                }else if (message.equals("0005")   ) {
                    Intent intentA = new Intent(ConstantInterface.FANGQU_ITEM_UPDATE);
                    context.sendBroadcast(intentA);
                }else if (message.equals("0006")) {
                    Intent intentA = new Intent(ConstantInterface.FANGQU_ITEM_UPDATE);
                    context.sendBroadcast(intentA);
                }else if (message.substring(12,14).contains("07")){
                    show1(context,message);
                    context.startActivity(new Intent(context,BaojingActvivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("msg",message));
                    Log.e(TAG, "payload->message"+message);
                }
                else {
                    Log.e("------>message",message);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 用户点击打开了通知");

//                //打开自定义的Activity
//                Intent i = new Intent(context, TestActivity.class);
//                i.putExtras(bundle);
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.e(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
    Forced_Offline_Dialog forced_offline_dialog;
    private  void showDialog(final Context context) {

        forced_offline_dialog = new Forced_Offline_Dialog(context);
        forced_offline_dialog.setYesOnclickListener(context.getResources().getString(R.string.sure), new Forced_Offline_Dialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {

                Intent intent = new Intent(context, HomeActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("start", "1");
                context.startActivity(intent);
                forced_offline_dialog.dismiss();


            }
        });

        forced_offline_dialog.setNoOnclickListener(context.getResources().getString(R.string.link_cancle), new Forced_Offline_Dialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                MyApplication.getInstance().exit();
                forced_offline_dialog.dismiss();
            }
        });

        forced_offline_dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        forced_offline_dialog.show();


    }

    //按钮点击事件（通知栏）
    public void show1(Context Context,String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Context);
        builder.setSmallIcon(R.drawable.ic_launcher_android);
        builder.setContentTitle(Context.getResources().getString(R.string.app_name));
        String [] str =msg.split(",");
        builder.setContentText("("+msg.substring(0,12)+")"+" "+msg.substring(14,16)+" "+str[1]+" "+Context.getResources().getString(R.string.Someone_broke_into));
        msgIntent ="("+msg.substring(0,12)+")"+" "+msg.substring(14,16)+" "+str[1]+" "+Context.getResources().getString(R.string.Someone_broke_into);

        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        Intent intent = new Intent(Context,BaojingActvivity.class);
        PendingIntent pi = PendingIntent.getActivity(Context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) Context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }

    private  static  String msgIntent;
    public  static  String getMsg(){

        return msgIntent;
    }

}
