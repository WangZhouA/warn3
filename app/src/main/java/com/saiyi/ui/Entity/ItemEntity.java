package com.saiyi.ui.Entity;

/**
 * Created by 陈姣姣 on 2017/5/4.
 */
public class ItemEntity {


    private String   deviceId; //设备ID
    private String        deviceName; //设备名称
    private  String      deviceMac; //设备MAC码 （必填）
    private String deviceIsOff; // 设备开关状态
    private String    userId;  //设备用户  （必填）
    private  String   devicePassword; // 设备密码/报警密码
    private String deviceImg;// 设备图片
    private String  deviceShare;//  设备分享状态
    private String  deviceConnectionStatus; //  设备联网状态
    private String  deviceInternetType;  // 设备联网类型
    private String   deviceOnceQuery; // 设备一次性查询密码
    private String  devicePhone; // 设备电话
    private String   deviceCid;  //CID码;

    public ItemEntity() {

    }

    public ItemEntity(String deviceCid, String deviceConnectionStatus, String deviceId, String deviceImg, String deviceInternetType, String deviceIsOff, String deviceMac, String deviceName, String deviceOnceQuery, String devicePassword, String devicePhone, String deviceShare, String userId) {
        this.deviceCid = deviceCid;
        this.deviceConnectionStatus = deviceConnectionStatus;
        this.deviceId = deviceId;
        this.deviceImg = deviceImg;
        this.deviceInternetType = deviceInternetType;
        this.deviceIsOff = deviceIsOff;
        this.deviceMac = deviceMac;
        this.deviceName = deviceName;
        this.deviceOnceQuery = deviceOnceQuery;
        this.devicePassword = devicePassword;
        this.devicePhone = devicePhone;
        this.deviceShare = deviceShare;
        this.userId = userId;
    }

    public ItemEntity(String deviceIsOff, String deviceName, String deviceImg) {
        this.deviceIsOff = deviceIsOff;
        this.deviceName = deviceName;
        this.deviceImg = deviceImg;
    }

    public String getDeviceCid() {
        return deviceCid;
    }

    public void setDeviceCid(String deviceCid) {
        this.deviceCid = deviceCid;
    }

    public String isDeviceConnectionStatus() {
        return deviceConnectionStatus;
    }

    public void setDeviceConnectionStatus(String deviceConnectionStatus) {
        this.deviceConnectionStatus = deviceConnectionStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceImg() {
        return deviceImg;
    }

    public void setDeviceImg(String deviceImg) {
        this.deviceImg = deviceImg;
    }

    public String getDeviceInternetType() {
        return deviceInternetType;
    }

    public void setDeviceInternetType(String deviceInternetType) {
        this.deviceInternetType = deviceInternetType;
    }

    public String isDeviceIsOff() {
        return deviceIsOff;
    }

    public void setDeviceIsOff(String deviceIsOff) {
        this.deviceIsOff = deviceIsOff;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOnceQuery() {
        return deviceOnceQuery;
    }

    public void setDeviceOnceQuery(String deviceOnceQuery) {
        this.deviceOnceQuery = deviceOnceQuery;
    }

    public String getDevicePassword() {
        return devicePassword;
    }

    public void setDevicePassword(String devicePassword) {
        this.devicePassword = devicePassword;
    }

    public String getDevicePhone() {
        return devicePhone;
    }

    public void setDevicePhone(String devicePhone) {
        this.devicePhone = devicePhone;
    }

    public String isDeviceShare() {
        return deviceShare;
    }

    public void setDeviceShare(String deviceShare) {
        this.deviceShare = deviceShare;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}