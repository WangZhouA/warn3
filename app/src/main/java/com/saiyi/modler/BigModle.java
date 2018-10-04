package com.saiyi.modler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.saiyi.http.ConstantInterface;
import com.saiyi.interfaces.HttpRequestCallback;
import com.saiyi.net.HttpUtils;
import com.saiyi.utils.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author Nicholas.Huang
 * @Declaration:
 * @Email: kurode@sina.cn
 * <p>
 * 17-7-13 上午9:44
 **/
public class BigModle extends Httpabstract {
    HttpRequestCallback call;
    private UserInfo userInfo;

    /**
     * 提交指令的方法
     */
    public void getPutMac(Context ext, String mac, String number, int type, HttpRequestCallback call) {
        userInfo = new UserInfo(ext);
        JSONObject jsonObject_instructions = new JSONObject();
        try {
            jsonObject_instructions.put("mac", mac);
            jsonObject_instructions.put("message", number);
            if (!TextUtils.isEmpty(userInfo.getStringInfo("user_name"))) {
                jsonObject_instructions.put("phone", userInfo.getStringInfo("user_name"));
            }else {

            }
            Log.e("mac_____", number);
            this.call = call;
            //http://172.16.10.103:8086/StolenWarning/
            HttpUtils.postAsynHttp(ext, ConstantInterface.HTTP_SERVICE+"device/sendToDevice/", jsonObject_instructions, type, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取数据
     */
    public void getData(Context ext, JSONObject jsonObject, int type, String url, HttpRequestCallback call) throws JSONException {
        this.call = call;
        HttpUtils.postAsynHttp(ext, url, jsonObject, type, callback);
    }

    @Override
    public void onResponses(String sequest, int type) {
        call.onResponse(sequest, type);
    }

    @Override
    public void onFailures(String exp) {
        call.onFailure(exp);
    }
}
