package com.saiyi.ui.activity;

/**
 * Created by 陈姣姣 on 2017/6/27.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.application.MyApplication;
import com.saiyi.http.ConstantInterface;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by wxs on 16/8/17.
 */
public class WebSocketService extends Service {
    private static final String TAG = WebSocketService.class.getSimpleName();

    public static final String WEBSOCKET_ACTION = "WEBSOCKET_ACTION";

    private BroadcastReceiver connectionReceiver;
    private static boolean isClosed = true;
    private static WebSocketConnection webSocketConnection;
    private static WebSocketOptions options = new WebSocketOptions();
    private static boolean isExitApp = false;


    private UserInfo userInfo = new UserInfo(this);

    private String websocketHost; //websocket服务端的url,,,ws是协议,和http一样,我写的时候是用的我们公司的服务器所以这里不能贴出来


    private Handler handler = new Handler();

    private static double dateflag;
    //
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (userInfo.getStringInfo("user_name") != null) {
                sendMsg(userInfo.getStringInfo("user_name"));
                handler.postDelayed(this, 20000);
            }
        }
    };


    private static Forced_Offline_Dialog forced_offline_dialog;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (connectionReceiver == null) {
            connectionReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                    if (networkInfo == null || !networkInfo.isAvailable()) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.duanwang), Toast.LENGTH_SHORT).show();
                    } else {
                        if (webSocketConnection != null) {
                            webSocketConnection.disconnect();
                        }
                        if (isClosed) {
                            webSocketConnect();
                        }
                    }
                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectionReceiver, intentFilter);
        }


        handler.postDelayed(runnable, 4000);
        if (userInfo.getStringInfo("user_name") != null) {
            websocketHost = ConstantInterface.WEB_SOCKET_HOST + userInfo.getStringInfo("user_name");//内网
            Log.e("------->",websocketHost);
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void closeWebsocket(boolean exitApp) {
        isExitApp = exitApp;
        if (webSocketConnection != null && webSocketConnection.isConnected()) {
            webSocketConnection.disconnect();
            webSocketConnection = null;
        }
    }

    public void webSocketConnect() {

        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        closeWebsocket(true);

        webSocketConnection = new WebSocketConnection();
        try {
            webSocketConnection.connect(websocketHost, new WebSocketHandler() {


                //websocket启动时候的回调
                @Override
                public void onOpen() {
                    Log.d(TAG, "open");
                    Log.d("------wang", "open");
                    isClosed = false;
                }

                //websocket接收到消息后的回调
                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "payload = " + payload);
//                    long  endTime =System.currentTimeMillis();
//                    Log.d("--->运行时间是",""+String.valueOf(dateflag-endTime));
                    try {
                        JSONObject jsonObject = new JSONObject(payload);
                        String message = jsonObject.getString("message");


//                        0001 撤防
//                        0002 布防
//                        0003 留守
//                        0004 设置号码组
//                        0005 设置防区属性
//                        0006 延迟设置
//                        0007 设置CID


                        if (message.equals("isLoginOut")) {
                            showDialog();

                        }else   if (message.equals("0004")) {
                            Log.e("------>message","0004");
                            Intent intent =new Intent(ConstantInterface.UPDATE_MESSAGE_NOW);
                            sendBroadcast(intent);
                        }else if (message.equals("0001")) {
                            Intent intent = new Intent(ConstantInterface.UPDATE_CEFANG);
                            sendBroadcast(intent);
                        }else if (message.equals("0002")) {
                            Intent intent = new Intent(ConstantInterface.UPDATE_BUFANG);
                            sendBroadcast(intent);
                        }else if (message.equals("0003")) {
                            Intent intent = new Intent(ConstantInterface.UPDATE_LIUSHOUBUFANG);
                            sendBroadcast(intent);
                        }else if (message.equals("0005")   ) {
                            Intent intent = new Intent(ConstantInterface.FANGQU_ITEM_UPDATE);
                            sendBroadcast(intent);
                        }else if (message.equals("0006")) {
                            Intent intent = new Intent(ConstantInterface.FANGQU_ITEM_UPDATE);
                            sendBroadcast(intent);
                        }else if (message.substring(12,14).contains("07")){
                            show1(message);
                            startActivity(new Intent(WebSocketService.this,BaojingActvivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("msg",message));
                            Log.e(TAG, "payload->message"+message);
                        }
                        else {
                            Log.e("------>message",message);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //websocket关闭时候的回调
                @Override
                public void onClose(int code, String reason) {
                    isClosed = true;
                    Log.d(TAG, "code = " + code + " reason = " + reason);
                    switch (code) {
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3://手动断开连接
//                            if (!isExitApp) {
//                                webSocketConnect();
//                            }
                            break;
                        case 4:
                            break;
                        /**
                         * 由于我在这里已经对网络进行了判断,所以相关操作就不在这里做了
                         */
                        case 5://网络断开连接
                            closeWebsocket(false);
                            webSocketConnect();
                            break;
                    }
                }
            }, options);
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }


    private  void showDialog() {

        forced_offline_dialog = new Forced_Offline_Dialog(getApplicationContext());
        forced_offline_dialog.setYesOnclickListener(getResources().getString(R.string.sure), new Forced_Offline_Dialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {

                Intent intent = new Intent(WebSocketService.this, HomeActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("start", "1");
                startActivity(intent);
                forced_offline_dialog.dismiss();


            }
        });

        forced_offline_dialog.setNoOnclickListener(getResources().getString(R.string.link_cancle), new Forced_Offline_Dialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                MyApplication.getInstance().exit();
                forced_offline_dialog.dismiss();
            }
        });

        forced_offline_dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        forced_offline_dialog.show();


    }

    public void sendMsg(final String s) {
        Log.d(TAG, "sendMsg = " + s);
        if (!TextUtils.isEmpty(s))
            if (webSocketConnection != null) {
                Log.i("---s", "麻瓜" + s);
//                webSocketConnection.sendTextMessage(s);
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"结束");
        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        closeWebsocket(true);
    }

    //按钮点击事件（通知栏）
    public void show1(String msg){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher_android);
        builder.setContentTitle(getResources().getString(R.string.my_devices_text));
        String [] str =msg.split(",");
        builder.setContentText("("+msg.substring(0,12)+")"+" "+msg.substring(14,16)+" "+str[1]+" "+getResources().getString(R.string.Someone_broke_into));
        msgIntent ="("+msg.substring(0,12)+")"+" "+msg.substring(14,16)+" "+str[1]+" "+getResources().getString(R.string.Someone_broke_into);

        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        Intent intent = new Intent(this,BaojingActvivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);
    }




    private  static  String msgIntent;
    public  static  String getMsg(){

        return msgIntent;
    }



}
