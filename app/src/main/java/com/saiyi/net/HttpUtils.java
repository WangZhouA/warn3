package com.saiyi.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.saiyi.R;
import com.saiyi.interfaces.HttpRequestCallback;

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

/**
 * Created by Administrator on 2017/6/23.
 */

public class HttpUtils {

    static OkHttpClient mOkHttpClient;

    /**
     * 获取数据接口
     *
     * @param context         上下文
     * @param action          服务器方法
     * @param HttpType        接口类型
     * @param requestCallback 数据回调接口
     *                        <p>
     *                        HttpParams params = new HttpParams();
     *                        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
     *                        params.put("commonParamsKey2", "这里支持中文参数");
     */
    public static void postAsynHttp(final Context context, String action, JSONObject jsonObject, final int HttpType, final HttpRequestCallback requestCallback) {
        CheckConnect(context);
        ///http://172.16.10.103:8086/StolenWarning/device/sendToDevice
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        mOkHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(20000, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(20000, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(20000, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        final Request request = new Request.Builder()
                .url(action)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("--->", "" + e.toString());
                String exceptionCode = e.getLocalizedMessage();
                requestCallback.onFailure(exceptionCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String strResult = response.body().string().toString();
                        Log.i("--->", "指令返回数据" + strResult);
                        JSONObject jsonObjs = new JSONObject(strResult);
                        String s = jsonObjs.getString("result");
                        if (s.equals(1) || s.equals("1")) {
                            requestCallback.onResponse(strResult, HttpType);
                        } else {
                            requestCallback.onFailure(strResult);
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

    private static void CheckConnect(Context cont) {
        ConnectivityManager cm = (ConnectivityManager) cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            Toast.makeText(cont, cont.getResources().getString(R.string.No_Internet_connection), Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
