package com.saiyi.http;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 陈姣姣 on 2017/4/28.
 */

public class HttpUtils {

    private static OkHttpClient mOkHttpClient;//okHttpClient 实例

    public static void okhttpGet(String strurl) throws IOException {
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
                Log.e(ConstantInterface.TAG, "get--->" + Thread.currentThread().getName() + "结果  " + response.body().string());

            }
        });
    }

    /**
     * okHttp post同步请求表单提交
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    public static void requestPostBySynWithForm(String actionUrl, HashMap<String, String> paramsMap) {
    }

}
