package com.beetech.serialport.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 本地配置实时数据
 */
@Entity
public class QueryConfigRealtime {

    @Id(autoincrement = true)
    private Long _id;
    private String hardVer; // 硬件版本
    private String softVer; // 软件版本
    private int customer; //客户码
    private int debug; // debug固定值
    private int category; // 分类码
    private int interval; // 时间间隔
    private Date calendar; // 采集时间,BCD码，格式：“16年05月20日17时12分46秒
    private int pattern; //工作模式
    private int bps; // 传输速率
    private int channel; // 频段
    private int ramData; // RAM数据
    private int front; // pflash 循环队列的读指针，最大值是1023
    private int rear; // pflash 循环队列的写指针，最大值是1023
    private int pflashLength; // pflash 循环队列中已存数据的数目，最大值是1023。
    private int sendOk; // 数据包发送成功标识位： 0 = 失败； 1 = 成功； other = 未定义
    private double gwVoltage; //计算公式：U = x*4/1023, 单位：V，其中，x = byte1*256+byte2
    public String gwId; // 网关序列号(小模块网关编号 )

    public QueryConfigRealtime(){}

    @Generated(hash = 966896135)
    public QueryConfigRealtime(Long _id, String hardVer, String softVer,
            int customer, int debug, int category, int interval, Date calendar,
            int pattern, int bps, int channel, int ramData, int front, int rear,
            int pflashLength, int sendOk, double gwVoltage, String gwId) {
        this._id = _id;
        this.hardVer = hardVer;
        this.softVer = softVer;
        this.customer = customer;
        this.debug = debug;
        this.category = category;
        this.interval = interval;
        this.calendar = calendar;
        this.pattern = pattern;
        this.bps = bps;
        this.channel = channel;
        this.ramData = ramData;
        this.front = front;
        this.rear = rear;
        this.pflashLength = pflashLength;
        this.sendOk = sendOk;
        this.gwVoltage = gwVoltage;
        this.gwId = gwId;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getHardVer() {
        return hardVer;
    }

    public void setHardVer(String hardVer) {
        this.hardVer = hardVer;
    }

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getRamData() {
        return ramData;
    }

    public void setRamData(int ramData) {
        this.ramData = ramData;
    }

    public int getFront() {
        return front;
    }

    public void setFront(int front) {
        this.front = front;
    }

    public int getRear() {
        return rear;
    }

    public void setRear(int rear) {
        this.rear = rear;
    }

    public int getPflashLength() {
        return pflashLength;
    }

    public void setPflashLength(int pflashLength) {
        this.pflashLength = pflashLength;
    }

    public int getSendOk() {
        return sendOk;
    }

    public void setSendOk(int sendOk) {
        this.sendOk = sendOk;
    }

    public double getGwVoltage() {
        return gwVoltage;
    }

    public void setGwVoltage(double gwVoltage) {
        this.gwVoltage = gwVoltage;
    }

    public String getGwId() {
        return gwId;
    }

    public void setGwId(String gwId) {
        this.gwId = gwId;
    }
}
