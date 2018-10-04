package com.saiyi.ui.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.saiyi.R;
import com.saiyi.http.ConstantInterface;
import com.saiyi.ui.view.AppUpdateProgressDialog;
import com.saiyi.ui.view.CircleImageView;
import com.saiyi.utils.InfoHintDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class About extends BasActivity implements View.OnClickListener {

    private TextView header_text;
    private ImageButton header_left;
    private ImageButton header_right;
    private CircleImageView circleImageView;
    private RelativeLayout update;
    private RelativeLayout header_all;


    private String versionName;
    private String versionCode;
    private String url = "http://textbooks.ipmph.com/books/Appdownload.zaction?type=ANDROID&SiteID=33";//apk下载地址


    private Dialog dialog;
    private View inflate;
    private TextView choosePhoto;
    private TextView takePhoto;

    protected InfoHintDialog mHintDialog;

    private boolean version;

    private AppUpdateProgressDialog appDialog;


    private long reference;
    private Forced_Offline_Dialog  forced_Offline_Dialog;


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    //获取系统的版本号 和code
                    Log.i("--->getVersion()", getVersion());
                    Log.i("--->getVersioncode()", getVersionCode());

                    float serviceName = Float.valueOf(versionName);
                    float mySersionName = Float.valueOf(getVersion());

                    Log.i("---> S  == M", "" + serviceName + "==" + mySersionName);

                    if (serviceName > mySersionName && Integer.parseInt(getVersionCode()) < Integer.parseInt(versionCode)) {

                        //=====================================去下载
                        version = true;
//                        forced_Offline_Dialog=new Forced_Offline_Dialog(About.this);
//                        forced_Offline_Dialog.setYesOnclickListener("确定",new Forced_Offline_Dialog.onYesOnclickListener() {
//                            @Override
//                            public void onYesClick() {
//                                GoToUpate();
//                            }
//                        });
//
//                        forced_Offline_Dialog.setNoOnclickListener("取消",new Forced_Offline_Dialog.onNoOnclickListener() {
//                            @Override
//                            public void onNoClick() {
//                                forced_Offline_Dialog.dismiss();
//                            }
//                        });
                        GoToUpate();

                    } else {
                        version = false;
                        Toast.makeText(About.this, getResources().getString(R.string.now_new), Toast.LENGTH_SHORT).show();
                    }

                    break;


                case 100:

                    Toast.makeText(About.this, getResources().getString(R.string.to_anzhuang), Toast.LENGTH_SHORT).show();

                    break;


                case 5:


                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        PgyCrashManager.register(this);
        PgyFeedback.getInstance().setMoreParam("tao", "PgyFeedbackShakeManager");
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        mHintDialog = new InfoHintDialog(About.this);

        header_text = (TextView) findViewById(R.id.header_text);
        header_left = (ImageButton) findViewById(R.id.header_left);
        header_right = (ImageButton) findViewById(R.id.header_right);
        update = (RelativeLayout) findViewById(R.id.update);
        circleImageView = (CircleImageView) findViewById(R.id.logo);
        header_text.setText(R.string.about);
        header_left.setOnClickListener(this);
        update.setOnClickListener(this);
        header_right.setVisibility(View.GONE);
        header_all = (RelativeLayout) findViewById(R.id.header_all);
        header_all.setBackgroundColor(Color.parseColor("#ffffff"));


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_left:
                finish();
                break;
            case R.id.update:

                okhttpQueryVisonNumber();

                break;

            case R.id.choosePhoto:

                //取消
                dialog.dismiss();

                break;

            case R.id.takePhoto:
                //确定
                dialog.dismiss();
                downloadFile(url, getResources().getString(R.string.home_text)+".apk");
                break;


        }
    }

    /**
     * 下载文件 （版本更新下载）
     *
     * @param httpPath 更新地址
     * @param name     更新文件名
     */
    private void downloadFile(String httpPath, String name) {
        if (httpPath == null || !httpPath.startsWith("http")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.xia_yichang), Toast.LENGTH_LONG).show();
            return;
        }
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (reference != 0) {
            if (!isDownSuccess(downloadManager)) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.xia), Toast.LENGTH_LONG).show();
                return;
            }
            DownloadManager.Query query = new DownloadManager.Query();
            //在广播中取出下载任务的id
            query.setFilterById(reference);
            Cursor c = downloadManager.query(query);
            if (c.moveToFirst()) {
                //获取文件下载路径
                String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                Log.i("--->", "onClick: fileName   " + fileName);
                //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                if (fileName != null && fileName.endsWith(".apk")) {
                    unregisterReceiver();
                    //安装 apk
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                    startActivity(install);
                }
            }
            return;
        }
        Uri uri = Uri.parse(httpPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //设置下载中通知栏提示的标题
//        request.setTitle("停简单");
        //表示下载进行中和下载完成的通知栏是否显示。默认只显示下载中通知。
        // VISIBILITY_VISIBLE_NOTIFY_COMPLETED表示下载完成后显示通知栏提示。
        // VISIBILITY_HIDDEN表示不显示任何通知栏提示，这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //表示设置下载地址为sd卡的Download文件夹，文件名为 停简单.apk。
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //在这里返回的reference变量是系统为当前的下载请求分配的一个唯一的ID，我们可以通过这个ID重新获得这个下载任务，进行一些自己想要进行的操作或者查询
        reference = downloadManager.enqueue(request);
        registerReceiver();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.to_start), Toast.LENGTH_LONG).show();


    }


    private void okhttpQueryVisonNumber() {

        final String strurl = ConstantInterface.HTTP_SERVICE_ADDRESS_QUERY_VERSION;
        //创建网络处理的对象
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(strurl).build();
        //call就是我们可以执行的请求类
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
                Log.e(ConstantInterface.TAG, "get--->" + Thread.currentThread().getName() + "结果  " + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                Log.e(ConstantInterface.TAG,"get--->"+Thread.currentThread().getName() + "结果  " + response.body().string());

                String result = response.body().string();
                Log.e(ConstantInterface.TAG, "get--->" + result);

                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject objs = obj.getJSONObject("data");
                    versionName = objs.getString("versionName");
                    versionCode = objs.getString("versionCode");
                    Log.i("--->versionName", versionName);
                    Log.i("--->versionCode", versionCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);

            }
        });

    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取版本code
     *
     * @return 当前应用的版本号
     */
    public String getVersionCode() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = String.valueOf(info.versionCode);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void GoToUpate() {

        if (version == true) {
            dialog = new Dialog(About.this, R.style.ActionSheetDialogStyle);
            //填充对话框的布局
            inflate = LayoutInflater.from(About.this).inflate(R.layout.bottom_dialog_s, null);
            //初始化控件
            choosePhoto = (TextView) inflate.findViewById(R.id.choosePhoto);
            takePhoto = (TextView) inflate.findViewById(R.id.takePhoto);
            choosePhoto.setOnClickListener(this);
            takePhoto.setOnClickListener(this);

            takePhoto.setText(R.string.sure);
            choosePhoto.setText(R.string.link_cancle);

            //将布局设置给Dialog
            dialog.setContentView(inflate);
            //获取当前Activity所在的窗体
            Window dialogWindow = dialog.getWindow();
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.BOTTOM);
            //获得窗体的属性
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = 20;//设置Dialog距离底部的距离
            //       将属性设置给窗体
            dialogWindow.setAttributes(lp);
            dialog.show();//显示对话框
        }
    }


    /**
     * 是否下载成功
     *
     * @param downManager DownloadManager
     * @return boolean
     */
    private boolean isDownSuccess(DownloadManager downManager) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = downManager.query(query);
        try {
            while (cursor.moveToNext()) {
                int downId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                if (downId == reference) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return false;
    }

    /**
     * 取消注册广播接收器
     */
    protected void unregisterReceiver() {
        try {
            unregisterReceiver(broadcast);
        } catch (Exception e) {

        }
    }


    /**
     * 注册广播接收器
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        this.registerReceiver(broadcast, filter);


    }



    //下载广播监听
    BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("--->", "onReceive: ");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                DownloadManager.Query query = new DownloadManager.Query();
                //在广播中取出下载任务的id
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (reference != id) return;
                query.setFilterById(id);
                Cursor c = manager.query(query);
                if (c.moveToFirst()) {
                    //获取文件下载路径
                    String fileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
                    if (fileName != null && fileName.endsWith(".apk")) {
                        //安装 apk
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                        startActivity(install);
                    }
                }
                unregisterReceiver();
            }
        }
    };



}