/******************************************************
 * Copyrights @ 2017，YunduanNetWorkTechnology Co., Ltd.
 *               东莞市云端网络科技有限公司
 * All rights reserved.
 *
 * Filename：
 *              AppUtils.java
 * Description：
 *              app工具类
 * Author:
 *              youngHu
 * Finished：
 *              2017年11月12日
 ********************************************************/
package com.saiyi.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.saiyi.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtils {

    /**
     * 使用浏览器打开url
     *
     * @param context 上下文
     * @param url     链接
     */
    public static void browseWeb(Context context, String url) {
        try {
            if (url != null && url.length() > 6) {
                if (!url.startsWith("http://")) {
                    url = "http://" + url;
                }
                Intent intentf = new Intent(Intent.ACTION_VIEW);
                intentf.setData(Uri.parse(url));
                context.startActivity(intentf);
            }
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.no_browse), Toast.LENGTH_SHORT).show();
        }
    }

    public static String bytesToHexString(byte[] src) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.delete(0, stringBuffer.length());
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b :
                src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hv);
        }
        return stringBuffer.toString();
    }


    /**
     * 设置手机号码中间隐藏
     *
     * @param mobile
     * @return
     */
    public static String setMobileNum(String mobile) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < mobile.length(); i++) {
            if (i >= 3 && i < 7) {
                stringBuffer.append("*");
            } else {
                stringBuffer.append(mobile.charAt(i));
            }
        }

        return stringBuffer.toString();
    }

    /**
     * 设置系统的基本信息
     *
     * @param
     */
    public static String getSystemInfo(Context context) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

            java.util.Random random = new java.util.Random();// 定义随机类
            String result = String.valueOf(random.nextInt(100000));// 返回[0,1000000)集合中的整数，注意不包括1000000
            String string = "";
            if (result.length() != 6) {
                for (int i = 0; i < (6 - result.length()); i++) {
                    string = string + "0";
                }
                result = string + result;
            }

            Date date = new Date();
            String str = "timestamp=" + sdf.format(date)
                    + "&version=1.0&"
                    + "App_version=" + packageInfo.versionName + "&"
                    + "mobile_os=Android&"
                    + "mobile_os_version=" + Build.VERSION.RELEASE + "&"
                    + "message_id=" + new SimpleDateFormat("yyyyMMddHHmmss").format(date) + result + "&"
                    + "mobile_type=" + Build.MODEL + "&"
                    + "resolution=" + dm.widthPixels + "*" + dm.heightPixels;

            return str;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 设置语言
     *
     * @param mContext     上下文
     * @param languageCode 语言code
     */
    public static void setLanguage(Context mContext, String languageCode) {

        Resources resources = mContext.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //如果是简体中文
        if (Constants.LANGUAGE_CN.equals(languageCode)) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
        }
        //如果是日语
        else if (Constants.LANGUAGE_JP.equals(languageCode)) {
            config.locale = Locale.forLanguageTag("ru");
            resources.updateConfiguration(config, dm);
        }
        //如果是英文
        else if (Constants.LANGUAGE_EN.equals(languageCode)) {
            config.locale = Locale.UK;
            resources.updateConfiguration(config, dm);
        }
    }
}
