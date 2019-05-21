package com.beetech.serialport.bean;

import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.utils.DateUtils;
import java.util.Date;

/**
 * 启动监控请求Bean
 *   sys|8888888888|aaaaaa|867578030282161|1.1|20190219111053|0|
 *   请求类型|设备号|设备密码|IMEI|Mre版本号|设备启动监控时间|获取参数类型（0 短信获取参数、1 按键进入监控、2 自动进入监控）|
 */
public class SysRequestBean{
	private Date beginMonitorTime; // 设备启动监控时间

	public SysRequestBean() {
		beginMonitorTime = new Date();
	}


	public Date getBeginMonitorTime() {
		return beginMonitorTime;
	}

	public void setBeginMonitorTime( Date beginMonitorTime ) {
		this.beginMonitorTime = beginMonitorTime;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("sys|8888888888|aaaaaa|");
		sb.append(Constant.imei).append("|").append(Constant.verName).append("|").append(DateUtils.parseDateToString( beginMonitorTime, DateUtils.C_YYYYMMDDHHMMSS )).append("|").append("0").append("|");
		return sb.toString();
	}
}
