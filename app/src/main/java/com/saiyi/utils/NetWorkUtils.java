package com.saiyi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by 陈姣姣 on 2017/5/24.
 */

public class NetWorkUtils  {

    public  static String getnetworkstate(Context context) {
        String returnint;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo net = connectivityManager.getActiveNetworkInfo();
            if (net == null) {
                returnint = "not";
            } else {
                returnint = net.getTypeName();
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.v("程序出错:", e.getMessage());
            returnint="not";
        }
        return returnint;
    }
}
