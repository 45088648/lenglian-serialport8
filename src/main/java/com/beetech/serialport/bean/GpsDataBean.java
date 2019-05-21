package com.beetech.serialport.bean;

import com.baidu.location.BDLocation;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.utils.DateUtils;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GpsDataBean {

    @Id(autoincrement = true)
    private Long _id;
    private double lat;
    private double lng;
    private float radius;
    private float direction;
    private float speed;
    private double altitude;
    private String address;
    private Date dataTime;
    private int locType;
    private int sendFlag;

    public GpsDataBean(){
        this.dataTime = new Date();
        this.sendFlag = 0;
    }

    public GpsDataBean(BDLocation location){
        this.dataTime = new Date();
        this.sendFlag = 0;

        setLat(location.getLatitude());
        setLng(location.getLongitude());
        setRadius(location.getRadius());
        setSpeed(location.getSpeed()); // 单位：公里每小时
        setDirection(location.getDirection());// 单位度
        setAltitude(location.getAltitude()); // 单位：米
        setAddress(location.getAddrStr());
        setLocType(location.getLocType());
    }

    @Generated(hash = 1537768466)
    public GpsDataBean(Long _id, double lat, double lng, float radius, float direction, float speed, double altitude, String address,
            Date dataTime, int locType, int sendFlag) {
        this._id = _id;
        this.lat = lat;
        this.lng = lng;
        this.radius = radius;
        this.direction = direction;
        this.speed = speed;
        this.altitude = altitude;
        this.address = address;
        this.dataTime = dataTime;
        this.locType = locType;
        this.sendFlag = sendFlag;
    }


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public int getLocType() {
        return locType;
    }

    public void setLocType(int locType) {
        this.locType = locType;
    }

    public int getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(int sendFlag) {
        this.sendFlag = sendFlag;
    }

    @Override
    public String toString() {
        // * 平板android app：GPS数据上传: gps|设备号|密钥|数据时间|纬度|经度|速度|方向|IMEI|IMEI值|id
        // * gps_data|8888888888|aaa|20190221142526|31.4554932|119.5901331|0.1|230|IMEI|867578030282161|123|
        StringBuffer sb = new StringBuffer();
        sb.append("gps_data|8888888888|aaa|").append(DateUtils.parseDateToString(dataTime, DateUtils.C_YYYYMMDDHHMMSS)).append("|");
        sb.append(lat).append("|").append(lng).append("|0|0|IMEI|").append(Constant.imei).append("|").append(_id).append("|");
        return sb.toString();
    }
}
