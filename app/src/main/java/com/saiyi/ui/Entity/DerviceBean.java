package com.saiyi.ui.Entity;

import java.util.List;

/**
 * Created by 陈姣姣 on 2018/6/29.
 */

public class DerviceBean {

    /**
     * result : 1
     * data : [{"deviceId":737,"deviceName":"智能防盗报警","deviceMac":"F0FE6B35701E","deviceIsOff":0,"userId":30,"devicePassword":"123456","deviceImg":null,"deviceShare":null,"deviceConnectionStatus":null,"deviceInternetType":null,"deviceOnceQuery":null,"devicePhone":"","deviceCid":"123456","mac":null,"id":48}]
     * count : 2
     */

    private int result;
    private int count;
    /**
     * deviceId : 737
     * deviceName : 智能防盗报警
     * deviceMac : F0FE6B35701E
     * deviceIsOff : 0
     * userId : 30
     * devicePassword : 123456
     * deviceImg : null
     * deviceShare : null
     * deviceConnectionStatus : null
     * deviceInternetType : null
     * deviceOnceQuery : null
     * devicePhone :
     * deviceCid : 123456
     * mac : null
     * id : 48
     */

    private List<DataBean> data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int deviceId;
        private String deviceName;
        private String deviceMac;
        private int deviceIsOff;
        private int userId;
        private String devicePassword;
        private Object deviceImg;
        private Object deviceShare;
        private Object deviceConnectionStatus;
        private Object deviceInternetType;
        private Object deviceOnceQuery;
        private String devicePhone;
        private String deviceCid;
        private Object mac;
        private int id;

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }

        public int getDeviceIsOff() {
            return deviceIsOff;
        }

        public void setDeviceIsOff(int deviceIsOff) {
            this.deviceIsOff = deviceIsOff;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getDevicePassword() {
            return devicePassword;
        }

        public void setDevicePassword(String devicePassword) {
            this.devicePassword = devicePassword;
        }

        public Object getDeviceImg() {
            return deviceImg;
        }

        public void setDeviceImg(Object deviceImg) {
            this.deviceImg = deviceImg;
        }

        public Object getDeviceShare() {
            return deviceShare;
        }

        public void setDeviceShare(Object deviceShare) {
            this.deviceShare = deviceShare;
        }

        public Object getDeviceConnectionStatus() {
            return deviceConnectionStatus;
        }

        public void setDeviceConnectionStatus(Object deviceConnectionStatus) {
            this.deviceConnectionStatus = deviceConnectionStatus;
        }

        public Object getDeviceInternetType() {
            return deviceInternetType;
        }

        public void setDeviceInternetType(Object deviceInternetType) {
            this.deviceInternetType = deviceInternetType;
        }

        public Object getDeviceOnceQuery() {
            return deviceOnceQuery;
        }

        public void setDeviceOnceQuery(Object deviceOnceQuery) {
            this.deviceOnceQuery = deviceOnceQuery;
        }

        public String getDevicePhone() {
            return devicePhone;
        }

        public void setDevicePhone(String devicePhone) {
            this.devicePhone = devicePhone;
        }

        public String getDeviceCid() {
            return deviceCid;
        }

        public void setDeviceCid(String deviceCid) {
            this.deviceCid = deviceCid;
        }

        public Object getMac() {
            return mac;
        }

        public void setMac(Object mac) {
            this.mac = mac;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
