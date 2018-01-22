package com.wanbox.bxweb.model;

/**
 * @author laihuan.wan
 * Created by laihuan.wan on 2018/01/13 0007.
 */

public class LocationInfo {
    /**
     * longitude : 1.988  TODO: 经度
     * latitude : 4.908  TODO: 纬度
     * serviceFlag : true TODO: 定位服务标记
     */

    private double longitude;
    private double latitude;

    private boolean serviceFlag;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isServiceFlag() {
        return serviceFlag;
    }

    public void setServiceFlag(boolean serviceFlag) {
        this.serviceFlag = serviceFlag;
    }
}
