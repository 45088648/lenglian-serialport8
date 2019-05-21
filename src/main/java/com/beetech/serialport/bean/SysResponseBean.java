package com.beetech.serialport.bean;

import com.beetech.serialport.utils.StringUtils;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 启动监控响应Bean
 * sys|true|19952200225|张长森|aaaaaa|300|0|10|30|10|gtw1.wendu114.com|8088|20190219111801|35|75|10|30|35|75|1|1|1|1|1|0|t.wendu114.com/CT110/201/vert.txt|*|*|*|*|1|30|
 */
@Entity
public class SysResponseBean {

	@Id(autoincrement = true)
	private Long _id;
	private String devNum; // 设备号
	private String userName; // 设备负责人登陆用户名
	private String devEncryption; // 设备密码
	private String devSendCycle; // 采集周期
	private String devAutosend; // 上报周期
	private String tempLower; // 低温阀值
	private String tempHight; // 高温阀值
	private String batteryLower; // 电量阀值
	private String devServerIp; // 服务器IP
	private String devServerPort; // 服务器端口
	private String sysDatetime; // 系统时间 yyyyMMddHHmmss
	private Date inputTime;
	private String rhLower; // 低湿度阀值
	private String rhHight; // 高湿度阀值
	private String tempAlarmFlag; // 温度是否报警
	private String rhAlarmFlag; // 湿度是否报警
	private String batteryAlarmFlag; // 电量是否报警
	private String extPowerAlarmFlag; // 外接电源是否报警
	private String unqualifyRecordFlag; // 温度超温后是否1分钟记录一次
	private String nextUpdateFlag; // 下次是否自动升级0:否,1:是,2:强制升级

	private String updateUrl; // 升级地址
	private String destination; // 送至目的地
	private String orderNo; // 单号
	private String receiver; // 收件人
	private String company; // 公司
	private String devTypeFlag; // 设备类型 0温度 |1温湿度
	private String alarmInterval; //报警时间间隔： -1 不限制
	private String tempLower1;
	private String tempHight1;
	private String rhLower1;
	private String rhHight1;
	private String equipType;//设备型号

	public SysResponseBean() {}

	public SysResponseBean(String[] reponseStrArr) {
		int i = 2;
		this.devNum = reponseStrArr[i++];
		this.userName = reponseStrArr[i++];
		this.devEncryption = reponseStrArr[i++];
		this.devSendCycle = reponseStrArr[i++];
		this.devAutosend = reponseStrArr[i++];
		this.tempLower = reponseStrArr[i++];
		this.tempHight = reponseStrArr[i++];
		this.batteryLower = reponseStrArr[i++];
		this.devServerIp = reponseStrArr[i++];
		this.devServerPort = reponseStrArr[i++];
		this.sysDatetime = reponseStrArr[i++];
		this.rhLower = reponseStrArr[i++];
		this.rhHight = reponseStrArr[i++];

		if(i+1 < reponseStrArr.length - 13){
			this.tempLower1 = reponseStrArr[i++];
			this.tempHight1 = reponseStrArr[i++];
			this.rhLower1 = reponseStrArr[i++];
			this.rhHight1 = reponseStrArr[i++];
			this.tempAlarmFlag = reponseStrArr[i++];
			this.rhAlarmFlag = reponseStrArr[i++];
			this.batteryAlarmFlag = reponseStrArr[i++];
			this.extPowerAlarmFlag = reponseStrArr[i++];
			this.unqualifyRecordFlag = reponseStrArr[i++];
			this.nextUpdateFlag = reponseStrArr[i++];
			this.updateUrl = reponseStrArr[i++];
			this.destination = reponseStrArr[i++];
			this.orderNo = reponseStrArr[i++];
			this.receiver = reponseStrArr[i++];
			this.company = reponseStrArr[i++];
			this.devTypeFlag = reponseStrArr[i++];
			this.alarmInterval = reponseStrArr[i++];
		}

		inputTime = new Date();

		this.tempLower1 = tempLower1 == null ? tempLower : tempLower1;
		this.tempHight1 = tempHight1 == null ? tempHight : tempHight1;
	}

	@Generated(hash = 482760234)
	public SysResponseBean(Long _id, String devNum, String userName, String devEncryption, String devSendCycle, String devAutosend, String tempLower, String tempHight, String batteryLower,
			String devServerIp, String devServerPort, String sysDatetime, Date inputTime, String rhLower, String rhHight, String tempAlarmFlag, String rhAlarmFlag, String batteryAlarmFlag,
			String extPowerAlarmFlag, String unqualifyRecordFlag, String nextUpdateFlag, String updateUrl, String destination, String orderNo, String receiver, String company, String devTypeFlag,
			String alarmInterval, String tempLower1, String tempHight1, String rhLower1, String rhHight1, String equipType) {
		this._id = _id;
		this.devNum = devNum;
		this.userName = userName;
		this.devEncryption = devEncryption;
		this.devSendCycle = devSendCycle;
		this.devAutosend = devAutosend;
		this.tempLower = tempLower;
		this.tempHight = tempHight;
		this.batteryLower = batteryLower;
		this.devServerIp = devServerIp;
		this.devServerPort = devServerPort;
		this.sysDatetime = sysDatetime;
		this.inputTime = inputTime;
		this.rhLower = rhLower;
		this.rhHight = rhHight;
		this.tempAlarmFlag = tempAlarmFlag;
		this.rhAlarmFlag = rhAlarmFlag;
		this.batteryAlarmFlag = batteryAlarmFlag;
		this.extPowerAlarmFlag = extPowerAlarmFlag;
		this.unqualifyRecordFlag = unqualifyRecordFlag;
		this.nextUpdateFlag = nextUpdateFlag;
		this.updateUrl = updateUrl;
		this.destination = destination;
		this.orderNo = orderNo;
		this.receiver = receiver;
		this.company = company;
		this.devTypeFlag = devTypeFlag;
		this.alarmInterval = alarmInterval;
		this.tempLower1 = tempLower1;
		this.tempHight1 = tempHight1;
		this.rhLower1 = rhLower1;
		this.rhHight1 = rhHight1;
		this.equipType = equipType;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getDevNum() {
		return devNum == null ? "" : devNum;
	}

	public String getUserName() {
		return userName == null ? "" : userName;
	}

	public String getDevEncryption() {
		return devEncryption == null ? "" : devEncryption;
	}

	public String getDevSendCycle() {
		return devSendCycle == null ? "" : devSendCycle;
	}

	public String getDevAutosend() {
		return devAutosend;
	}

	public String getTempLower() {
		return tempLower == null ? "" : tempLower;
	}

	public String getTempHight() {
		return tempHight == null ? "" : tempHight;
	}

	public String getBatteryLower() {
		return batteryLower == null ? "" : batteryLower;
	}

	public String getDevServerIp() {
		return devServerIp == null ? "" : devServerIp;
	}

	public String getDevServerPort() {
		return devServerPort == null ? "" : devServerPort;
	}

	public String getSysDatetime() {
		return sysDatetime == null ? "" : sysDatetime;
	}

	public String getRhLower() {
		return rhLower == null ? "" : rhLower;
	}

	public String getRhHight() {
		return rhHight == null ? "" : rhHight;
	}

	public String getTempAlarmFlag() {
		return tempAlarmFlag;
	}

	public String getRhAlarmFlag() {
		return rhAlarmFlag;
	}

	public String getBatteryAlarmFlag() {
		return batteryAlarmFlag;
	}

	public String getExtPowerAlarmFlag() {
		return extPowerAlarmFlag;
	}

	public String getUnqualifyRecordFlag() {
		return unqualifyRecordFlag;
	}

	public String getNextUpdateFlag() {
		return nextUpdateFlag;
	}

	public String getUpdateUrl() {
		return updateUrl == null ? "" : updateUrl;
	}

	public String getDestination() {
		return destination == null ? "" : destination;
	}

	public String getOrderNo() {
		return orderNo == null ? "" : orderNo;
	}

	public String getReceiver() {
		return receiver == null ? "" : receiver;
	}

	public String getCompany() {
		return  StringUtils.isBlank(this.company) ?  "*" : company;
	}

	public String getDevTypeFlag() {
		return devTypeFlag == null ? "" : devTypeFlag;
	}

	//--------------set------------------------

	public void setDevNum(String devNum) {
		this.devNum = devNum;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setDevEncryption(String devEncryption) {
		this.devEncryption = devEncryption;
	}

	public void setDevSendCycle(String devSendCycle) {
		this.devSendCycle = devSendCycle;
	}

	public void setDevAutosend(String devAutosend) {
		this.devAutosend = devAutosend;
	}

	public void setTempLower(String tempLower) {
		this.tempLower = tempLower;
	}

	public void setTempHeight(String tempHight) {
		this.tempHight = tempHight;
	}

	public void setBatteryLower(String batteryLower) {
		this.batteryLower = batteryLower;
	}

	public void setDevServerIp(String devServerIp) {
		this.devServerIp = devServerIp;
	}

	public void setDevServerPort(String devServerPort) {
		this.devServerPort = devServerPort;
	}

	public void setSysDatetime(String sysDatetime) {
		this.sysDatetime = sysDatetime;
	}

	public void setRhLower(String rhLower) {
		this.rhLower = rhLower;
	}

	public void setRhHight(String rhHight) {
		this.rhHight = rhHight;
	}

	public void setTempAlarmFlag(String tempAlarmFlag) {
		this.tempAlarmFlag = tempAlarmFlag;
	}

	public void setRhAlarmFlag(String rhAlarmFlag) {
		this.rhAlarmFlag = rhAlarmFlag;
	}

	public void setBatteryAlarmFlag(String batteryAlarmFlag) {
		this.batteryAlarmFlag = batteryAlarmFlag;
	}

	public void setExtPowerAlarmFlag(String extPowerAlarmFlag) {
		this.extPowerAlarmFlag = extPowerAlarmFlag;
	}

	public void setUnqualifyRecordFlag(String unqualifyRecordFlag) {
		this.unqualifyRecordFlag = unqualifyRecordFlag;
	}

	public void setNextUpdateFlag(String nextUpdateFlag) {
		this.nextUpdateFlag = nextUpdateFlag;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public void setDestination(String destination) {
		this.destination =  destination;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo =  orderNo;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setDevTypeFlag(String devTypeFlag) {
		this.devTypeFlag = devTypeFlag;
	}
	
	public String getAlarmInterval() {
		return alarmInterval;
	}

	public void setAlarmInterval(String alarmInterval) {
		this.alarmInterval = alarmInterval;
	}

	public String getTempLower1() {
		return tempLower1;
	}

	public void setTempLower1(String tempLower1) {
		this.tempLower1 = tempLower1;
	}

	public String getTempHight1() {
		return tempHight1;
	}

	public void setTempHight1(String tempHight1) {
		this.tempHight1 = tempHight1;
	}

	public String getRhLower1() {
		return rhLower1;
	}

	public void setRhLower1(String rhLower1) {
		this.rhLower1 = rhLower1;
	}

	public String getRhHight1() {
		return rhHight1;
	}

	public void setRhHight1(String rhHight1) {
		this.rhHight1 = rhHight1;
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public void setTempHight(String tempHight) {
		this.tempHight = tempHight;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	@Override
	public String toString() {
		StringBuffer responseSb = new StringBuffer();
		responseSb.append("sys|true|")
				.append( getDevNum() ).append( "|" )
				.append( getUserName() ).append( "|" )
				.append( getDevEncryption() ).append( "|" )
				.append( getDevSendCycle() ).append( "|" )
				.append( getDevAutosend()).append( "|" )
				.append( getTempLower() ).append( "|" )
				.append( getTempHight() ).append( "|" )
				.append( getBatteryLower() ).append( "|" )
				.append( getDevServerIp() ).append( "|" )
				.append( getDevServerPort() ).append( "|" )
				.append( getSysDatetime() ).append( "|" )

				.append( getRhLower() ).append( "|" )
				.append( getRhHight() ).append( "|")
		        .append( getTempLower1() ).append( "|")
				.append( getTempHight1() ).append( "|")
				.append( getRhLower1() ).append( "|")
				.append( getRhHight1() ).append( "|")

				.append( tempAlarmFlag).append( "|" )
				.append( rhAlarmFlag).append( "|" )
				.append( batteryAlarmFlag).append( "|" )
				.append( extPowerAlarmFlag).append( "|" )
				.append( unqualifyRecordFlag).append( "|" )
				.append( nextUpdateFlag ).append( "|" )
				.append( getUpdateUrl() ).append( "|" )
				.append( getDestination() ).append( "|" )
				.append( getOrderNo() ).append( "|" )
				.append( getReceiver() ).append( "|" )
				.append( getCompany() ).append( "|" )
				.append(getDevTypeFlag()).append("|")
				.append(getAlarmInterval()).append( "|");

		return responseSb.toString();
	}

	public static void main(String[] args){
		String requestStr = "sys|true|19952200225|张长森|aaaaaa|300|0|10|30|10|gtw1.wendu114.com|8088|20190219111801|35|75|10|30|35|75|1|1|1|1|1|0|t.wendu114.com/CT110/201/vert.txt|*|*|*|*|1|30|";
		String[] requestStrArr = requestStr.split("\\|");
		SysResponseBean bean = new SysResponseBean(requestStrArr);
		System.out.println(bean.devSendCycle);
		System.out.println(bean.toString());
	}
}
