package com.saiyi.ui.Entity;

/**
 * Created by 陈姣姣 on 2017/5/17.
 */

public class EDerviceQueryList {

    private String zoneId;
    private String zoneName;
    private String zoneCode;
    private String zoneType;
    private String zoneDelayOpen;
    private String zoneDelayWarn;
    private String zoneOnOff;
    private String zoneStatus ;
    private String deviceId;


    public EDerviceQueryList(String zoneId, String zoneName, String zoneCode, String zoneType, String zoneDelayOpen, String zoneDelayWarn, String zoneOnOff, String zoneStatus, String deviceId) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.zoneCode = zoneCode;
        this.zoneType = zoneType;
        this.zoneDelayOpen = zoneDelayOpen;
        this.zoneDelayWarn = zoneDelayWarn;
        this.zoneOnOff = zoneOnOff;
        this.zoneStatus = zoneStatus;
        this.deviceId = deviceId;
    }

    public EDerviceQueryList(String zoneName,String zoneOnOff) {
        this.zoneName = zoneName;
        this.zoneOnOff = zoneOnOff;

    }
    public EDerviceQueryList() {


    }


    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneType() {
        return zoneType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public String getZoneDelayOpen() {
        return zoneDelayOpen;
    }

    public void setZoneDelayOpen(String zoneDelayOpen) {
        this.zoneDelayOpen = zoneDelayOpen;
    }

    public String getZoneDelayWarn() {
        return zoneDelayWarn;
    }

    public void setZoneDelayWarn(String zoneDelayWarn) {
        this.zoneDelayWarn = zoneDelayWarn;
    }

    public String getZoneOnOff() {
        return zoneOnOff;
    }

    public void setZoneOnOff(String zoneOnOff) {
        this.zoneOnOff = zoneOnOff;
    }

    public String getZoneStatus() {
        return zoneStatus;
    }

    public void setZoneStatus(String zoneStatus) {
        this.zoneStatus = zoneStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
