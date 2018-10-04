package com.saiyi.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.pgyersdk.crash.PgyCrashManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 陈姣姣 on 2017/5/3.
 */
public class MyApplication extends Application {

    //获取本机MIEI号码（仅手机存在）
    private static String DEVICE_ID;
    //获取设备序列号
    private static String DEVICE_SERIAL_NUMBER;
    //获取本机的型号
    private static String MAC_NUMBER;

    private static String YUYAN;

    // 获取设备的MAC码

    private static String DEVICE_MODEL;


    private List<Activity> activityList = new LinkedList();
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
           		// 初始化 JPush

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Log.i("--->初始化", "初始化完成");


        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        DEVICE_ID = tm.getDeviceId();
        Log.i("--->DEVICE_ID", "获取设备的唯一ID=" + DEVICE_ID);

        DEVICE_SERIAL_NUMBER = tm.getSimSerialNumber();
        Log.i("--->DEVICE_SERIA_NUMBER", "获取设备的序列号=" + DEVICE_SERIAL_NUMBER);

        DEVICE_MODEL = android.os.Build.MODEL;
        Log.i("--->DEVICE_MODEL", "获取设备的型号=" + DEVICE_MODEL);

        getLocalMac();
        Log.i("--->Mac", "获取设备的MAC=" + getLocalMac());

        YUYAN=Locale.getDefault().getLanguage();
        Log.i("--->YUYAN", YUYAN);


           Log.i("---->getPinYin",getPinYin("王")) ;
        PgyCrashManager.register(getApplicationContext());
    }

    public static String getDeviceId() {

        return DEVICE_ID;
    }
    public static String getYUYAN() {

        return YUYAN;
    }

    public static String getDeviceSerialNumber() {

        return DEVICE_SERIAL_NUMBER;
    }

    public static String getDeviceModel() {

        return DEVICE_MODEL;
    }

    public static String mac = null;

    //获取设备的MAC号码
    public static String getLocalMac() {
        if (TextUtils.isEmpty(mac)) {
            return "";
        }
        return mac;
    }


    public MyApplication() {
    }

    //单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }


    public static String getPinYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";
        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {  //判断字符是否是中文
                    //toHanyuPinyinStringArray 如果传入的不是汉字，就不能转换成拼音，那么直接返回null
                    //由于中文有很多是多音字，所以这些字会有多个String，在这里我们默认的选择第一个作为pinyin
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else {
                    output += Character.toString(input[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
//          Log.v(TAG, "BadHanyuPinyinOutputFormatCombination");
        }
        return output;
    }



}

