package com.saiyi.http;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 陈姣姣 on 2017/4/28.
 */

public class ConstantInterface {

     //服务器
     public  static  String  HTTP_SERVICE="http://58.250.30.13:8086/StolenWarning/";
     //本地
//     public  static  String  HTTP_SERVICE="http://172.16.2.27:8088/StolenWarning/";
     //接图片
//     public  static  String  HTTP_SERVICE_HOST="http://58.250.30.13:8086/";
//     public  static  String  HTTP_SERVICE_HOST="http://172.16.2.27:8080/";
     public  static  String  HTTP_SERVICE_HOST="http://58.250.30.13:8086/";

     //websocket
//     public  static  String WEB_SOCKET_HOST ="ws://172.16.2.27:8088/StolenWarning/websocket/";
     public  static  String WEB_SOCKET_HOST ="ws://58.250.30.13:8086/StolenWarning/websocket/";

     //验证码
     public  static  String  HTTP_SERVICE_ADDRESS = HTTP_SERVICE + "authcode/sendcode/";
     //log标签
     public static  String TAG="--->";
     //注册
     public  static  String  HTTP_SERVICE_ADDRESS_REGISTER = HTTP_SERVICE  + "user/regist";
     //登录
     public  static  String HTTP_SERVICE_ADDRESS_LOGIN = HTTP_SERVICE + "user/login";
     //找回密码
     public  static  String  HTTP_SERVICE_ADDRESS_FORGOT_PASSWORD  = HTTP_SERVICE + "user/updatepwd";
     //查看用户信息
     public  static  String HTTP_SERVICE_ADDRESS_VIEWING_USER_INFORMATION = HTTP_SERVICE + "user/queryuserinfo/";
     //添加设备
//     public  static  String HTTP_SERVICE_ADDRESS_ADD_DERVICE = HTTP_SERVICE +"warnDevice/add";
     public  static  String HTTP_SERVICE_ADDRESS_ADD_DERVICE = HTTP_SERVICE +"userDevice/insert";

     public  static  String HTTP_SERVICE_ADDRESS_ReName_DERVICE = HTTP_SERVICE +"warnDevice/update";
     //查询设备列表
//     public  static  String HTTP_SERVICE_ADDRESS_QUERY_DERVICE = HTTP_SERVICE + "warnDevice/queryLists";
     public  static  String HTTP_SERVICE_ADDRESS_QUERY_DERVICE = HTTP_SERVICE + "userDevice/queryLists1";
     //删除
//     public  static  String HTTP_SERVICE_ADDRESS_DELETE_DERVICE=HTTP_SERVICE + "warnDevice/del/";
     public  static  String HTTP_SERVICE_ADDRESS_DELETE_DERVICE=HTTP_SERVICE + "userDevice/delete";
     //添加手机号码
     public  static  String HTTP_SERVICE_ADDRESS_ADD_NUMBER = HTTP_SERVICE + "warnDeviceNumber/add";
     //查询手机号码
     public  static  String HTTP_SERVICE_ADDRESS_QUERY_NUMBER = HTTP_SERVICE+"warnDeviceNumber/queryLists";
     //修改设备各种手机号码
     public  static  String HTTP_SERVICE_ADDRESS_RENAME_NUMBER =  HTTP_SERVICE  + "warnDeviceNumber/update";
     //设备布放/撤防/留守布放
     public  static  String HTTP_SERVICE_ADDRESS_DERVICE_STATE=  HTTP_SERVICE+"warnDevice/zoneSet";
     //查询防区列表
     public  static  String HTTP_SERVICE_ADDRESS_QUERY_DERVICE_LIST= HTTP_SERVICE+"warnDeviceZone/queryLists";
     //防区布放/撤防
     public  static  String HTTP_SERVICE_ADDRESS_OFF_OPEN = HTTP_SERVICE+"warnDeviceZone/zoneSet";
     //修改防区属性
     public  static  String HTTP_SERVICE_ADDRESS_MODIFY_THE_PROPERTIES = HTTP_SERVICE+"warnDeviceZone/update";
     //修改用户头像
     public  static  String HTTP_SERVICE_ADDRESS_REPICTURE = HTTP_SERVICE + "file/updateheadimg";
     //修改用户信息
     public  static  String HTTP_SERVICE_ADDRESS_DATA = HTTP_SERVICE+"user/update";
     //修改用户名字
     public  static  String HTTP_SERVICE_ADDRESS_USER_RENAME=HTTP_SERVICE+"user/updatename";
     //查看用户信息
     public  static  String HTTP_SERVICE_ADDRESS_QUERY_USER_MESSAGE = HTTP_SERVICE+"user/queryuserinfo/";
     //下载图片
     public static  String HTTP_SERVICE_ADDRESS_DOWNLOAD_PICTURE = HTTP_SERVICE + "upload/";
     //查询最新版本
     public static  String HTTP_SERVICE_ADDRESS_QUERY_VERSION= HTTP_SERVICE + "versions/queryVersion";

     public static  String DERVICEID_TOMAC = HTTP_SERVICE+"warnDevice/queryMac";

     public static  String MAC;
     public static  String  UPDATE_MESSAGE_NOW ="www.shuaxin.com";
     public static  String  UPDATE_CEFANG ="www.cefang.com";
     public static  String  UPDATE_BUFANG ="www.bufang.com";
     public static  String  UPDATE_LIUSHOUBUFANG ="www.liushoubufang.com";
     public static  String  FANGQU_ITEM_UPDATE = "www.fangqushuaxin.com";


     public  static  void showImage(Context context, String url, int erro , int loadpic, ImageView imageView){
          Glide.with(context).load(url).asBitmap().fitCenter().placeholder(loadpic).error(erro).dontAnimate().into(imageView);
     }


}

