package com.beetech.serialport.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 传感器实时数据
 */
@Entity
public class ReadDataRealtime {
    // ID @Id主键,int类型,数据库建表时此字段会设为自增长
    @Id(autoincrement = true)
    private Long _id;
    private String sensorId; //传感器ID, 传感器编号
    private Date sensorDataTime; // 本条数据的采集时间，BCD码，格式：“年 月 日 时 分 秒”。
    private double temp; // 温度；
    private double temp1;
    private double temp2;
    private double temp3;
    private double temp4;
    private double temp5;
    private double temp6;
    private double temp7;

    private double rh; // 湿度, T = (MSB*256+LSB)/100，单位：%；
    private double rh1;
    private double rh2;
    private double rh3;
    private double rh4;
    private double rh5;
    private double rh6;
    private double rh7;

    public ReadDataRealtime(){}

    @Generated(hash = 1774369701)
    public ReadDataRealtime(Long _id, String sensorId, Date sensorDataTime,
            double temp, double temp1, double temp2, double temp3, double temp4,
            double temp5, double temp6, double temp7, double rh, double rh1,
            double rh2, double rh3, double rh4, double rh5, double rh6,
            double rh7) {
        this._id = _id;
        this.sensorId = sensorId;
        this.sensorDataTime = sensorDataTime;
        this.temp = temp;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
        this.temp6 = temp6;
        this.temp7 = temp7;
        this.rh = rh;
        this.rh1 = rh1;
        this.rh2 = rh2;
        this.rh3 = rh3;
        this.rh4 = rh4;
        this.rh5 = rh5;
        this.rh6 = rh6;
        this.rh7 = rh7;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Date getSensorDataTime() {
        return sensorDataTime;
    }

    public void setSensorDataTime(Date sensorDataTime) {
        this.sensorDataTime = sensorDataTime;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public double getTemp1() {
        return temp1;
    }

    public void setTemp1(double temp1) {
        this.temp1 = temp1;
    }

    public double getTemp2() {
        return temp2;
    }

    public void setTemp2(double temp2) {
        this.temp2 = temp2;
    }

    public double getTemp3() {
        return temp3;
    }

    public void setTemp3(double temp3) {
        this.temp3 = temp3;
    }

    public double getTemp4() {
        return temp4;
    }

    public void setTemp4(double temp4) {
        this.temp4 = temp4;
    }

    public double getTemp5() {
        return temp5;
    }

    public void setTemp5(double temp5) {
        this.temp5 = temp5;
    }

    public double getTemp6() {
        return temp6;
    }

    public void setTemp6(double temp6) {
        this.temp6 = temp6;
    }

    public double getTemp7() {
        return temp7;
    }

    public void setTemp7(double temp7) {
        this.temp7 = temp7;
    }

    public double getRh1() {
        return rh1;
    }

    public void setRh1(double rh1) {
        this.rh1 = rh1;
    }

    public double getRh2() {
        return rh2;
    }

    public void setRh2(double rh2) {
        this.rh2 = rh2;
    }

    public double getRh3() {
        return rh3;
    }

    public void setRh3(double rh3) {
        this.rh3 = rh3;
    }

    public double getRh4() {
        return rh4;
    }

    public void setRh4(double rh4) {
        this.rh4 = rh4;
    }

    public double getRh5() {
        return rh5;
    }

    public void setRh5(double rh5) {
        this.rh5 = rh5;
    }

    public double getRh6() {
        return rh6;
    }

    public void setRh6(double rh6) {
        this.rh6 = rh6;
    }

    public double getRh7() {
        return rh7;
    }

    public void setRh7(double rh7) {
        this.rh7 = rh7;
    }
}
