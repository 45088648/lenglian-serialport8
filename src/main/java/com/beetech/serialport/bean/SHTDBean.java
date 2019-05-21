package com.beetech.serialport.bean;

import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.utils.DateUtils;
import java.util.Date;

public class SHTDBean {
    private double temp;
    private double temp1;

    private double rh;
    private double rh1;
    private Date dataTime;
    private Long id;
    private String imei;

    private int cid;
    private int csq;
    private int lac;
    private int mcc;
    private int mnc;

    private int power;
    private int battery;
    private String dateTimeStr;

    public SHTDBean(){}
    public SHTDBean(ReadDataResponse readDataResponse){
        this.temp = readDataResponse.getTemp();
        this.temp1 = readDataResponse.getTemp1();
        this.rh = readDataResponse.getRh();
        this.rh1 = readDataResponse.getRh1();
        this.dataTime = readDataResponse.getSensorDataTime();
        this.dateTimeStr = DateUtils.parseDateToString(dataTime, DateUtils.C_YYYYMMDDHHMMSS);
        this.id = readDataResponse.get_id();
        this.imei = readDataResponse.getSensorId();

        this.cid = readDataResponse.getCid();
        this.csq = readDataResponse.getCsq();
        this.lac = readDataResponse.getLac();
        this.mcc = readDataResponse.getMcc();
        this.mnc = readDataResponse.getMnc();

        this.power = readDataResponse.getPower();
        this.battery = readDataResponse.getBattery();
    }

    @Override
    public String toString() {
        // SHTD|8888888888|aaaaaa|2|8|35|75|68|769|67|774|100|10000000|38173|27|37000|460|0|20190215015000|IMEI|867578030282161|SH3d1.4.1BTBDZ|
        StringBuffer sb = new StringBuffer();
        sb.append("SHTD|8888888888|aaaaaa|2|8|35|75|");
        sb.append(Double.valueOf(temp*10).intValue()).append("|").append(Double.valueOf(rh*10).intValue()).append("|");
        sb.append(Double.valueOf(temp1*10).intValue()).append("|").append(Double.valueOf(rh1*10).intValue()).append("|");
        sb.append(battery).append("|").append(power).append("0000000").append("|").append(cid).append("|").append(csq).append("|").append(lac).append("|").append(mcc).append("|").append(mnc).append("|");
        sb.append(dateTimeStr).append("|");
        sb.append("IMEI|").append(this.imei).append("|");
        sb.append(Constant.verName).append("|");
        sb.append(this.id).append("|");
        return sb.toString();
    }
}
