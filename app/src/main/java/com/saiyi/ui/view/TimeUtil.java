package com.saiyi.ui.view;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述：时间处理器
 * 创建作者：黎丝军
 * 创建时间：2016/10/15 15:39
 */

public class TimeUtil {

    /**
     * 根据时间错获取日期
     */
    public static String getLongTime(String format, long time) {
        return DateFormat.format(format, time).toString();
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getCurrentTime(String format) {
        return DateFormat.format(format, System.currentTimeMillis()).toString();
    }


    /**
     * 获取当前时间,返回时间格式为yyyy-MM-dd
     *
     * @return 时间字符串
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd");
    }

    public static String getCurrentMouth() {
        return getCurrentTime("yyyy-MM");
    }

    public static String getCurrentYear() {
        return getCurrentTime("yyyy");
    }

    /**
     * 获取当前全时间，返回格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 全时间
     */
    public static String getCurrentAllTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }
    public static String getCurrentAllTimeNowMiao() {
        return getCurrentTime("yyyy-MM-dd HH:mm");
    }

    /**
     * 获取时间，根据时间制定时间格式
     *
     * @param dateStr          时间字符串
     * @param originTimeFormat 原始时间格式
     * @param timeFormat       时间格式
     * @return 现在的时间格式
     */
    public static String getTime(String dateStr, String originTimeFormat, String timeFormat) {
        SimpleDateFormat dateFormatSimple = new SimpleDateFormat(originTimeFormat);
        try {
            final Date date = dateFormatSimple.parse(dateStr);
            dateFormatSimple = new SimpleDateFormat(timeFormat);
            return dateFormatSimple.format(date);
        } catch (Exception e) {
        }
        return getCurrentTime(timeFormat);
    }

    /**
     * 获取时间，根据时间制定时间格式
     *
     * @param dateStr    时间字符串
     * @param timeFormat 时间格式
     * @return 现在的时间格式 "date" -> "2016-12-14 09:58:57.0"
     */
    public static String getTime(String dateStr, String timeFormat) {
        return getTime(dateStr, "yyyy-MM-dd HH:mm:ss", timeFormat);
    }


    /**
     * 获取当前时间的时分
     *
     * @return
     */
    public static String getCurrHourMin() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }

    /**
     * 根据年龄返回生日
     */
    public static String getBirthday(int age) {
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);
        int yearbir = yearNow - age;
        String time = yearbir + "-" + monthNow + "-" + dayNow;
        return time;
    }


    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(String birthTimeString) {
        // 先截取到字符串中的年、月、日
        String strs[] = birthTimeString.trim().split("-");
        int selectYear = Integer.parseInt(strs[0]);
        int selectMonth = Integer.parseInt(strs[1]);
        int selectDay = Integer.parseInt(strs[2]);
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        int yearMinus = yearNow - selectYear;
        int monthMinus = monthNow - selectMonth;
        int dayMinus = dayNow - selectDay;

        int age = yearMinus;// 先大致赋值
        if (yearMinus < 0) {// 选了未来的年份
            age = 0;
        } else if (yearMinus == 0) {// 同年的，要么为1，要么为0
            if (monthMinus < 0) {// 选了未来的月份
                age = 0;
            } else if (monthMinus == 0) {// 同月份的
                if (dayMinus < 0) {// 选了未来的日期
                    age = 0;
                } else if (dayMinus >= 0) {
                    age = 1;
                }
            } else if (monthMinus > 0) {
                age = 1;
            }
        } else if (yearMinus > 0) {
            if (monthMinus < 0) {// 当前月>生日月
            } else if (monthMinus == 0) {// 同月份的，再根据日期计算年龄
                if (dayMinus < 0) {
                } else if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    // 根据时间戳计算年龄
    public static int getAgeFromBirthTime(long birthTimeLong) {
        Date date = new Date(birthTimeLong);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String birthTimeString = format.format(date);

        return getAgeFromBirthTime(birthTimeString);
    }

    public  static  String getString( String  str ){

        String  [] time =str.split(" ");
        System.out.println(time[0]);
        String [] YYR  = time[0].split("-");
        String [] HMM =   time[1].split(":");
        str = YYR[0]+YYR[1]+YYR[2]+HMM[0]+HMM[1]+"00";
        return  str;
    }

}
