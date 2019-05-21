package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.bean.SysResponseBean;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.utils.BeanPropertiesUtil;
import com.beetech.serialport.utils.ByteUtilities;
import com.beetech.serialport.utils.MobileInfoUtil;
import com.beetech.serialport.utils.StringUtils;
import com.rscja.deviceapi.Module;
import java.util.Calendar;
import java.util.Date;

/**
 * 读取串口模块数据
 */
public class ThreadModuleReceive extends Thread {
    private final static String TAG = ThreadModuleReceive.class.getSimpleName();
    public static int INTERVAL = 1000*10;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private Context mContext;
    private MyApplication myApp;

    private static ThreadModuleReceive instance;

    public synchronized static ThreadModuleReceive getInstance() {
        if (null == instance) {
            synchronized(ThreadModuleReceive.class) {
                instance = new ThreadModuleReceive();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
        myApp = (MyApplication) context.getApplicationContext();
        NUM = 0;

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e(TAG,"未捕获异常", e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        while(true){
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));

            Module module = myApp.module;
            if (module != null && myApp.initResult) {
                try {

                    byte[] buf = module.receive();
                    myApp.lastReadTime = System.currentTimeMillis();

//                          TODO test 模拟收到数据
//                            String hex = "550b001454ef0a7c2e97fe550b011455a77a7ab3b830550b001454ef0a7c1e5253550b011455a28f7ab3b820550b001454fabc7c31fadb550b0114559d647a8c53dc550b001454fabc7c19c528550b0114559d647aa2ca4f550b001454f4a37c1f63a3550b01145598917a9b8771550b001454ef0a7c2e97fe550b0114559d647ab1da30550b001454fabc7c2831ad550b011455a77a7a95986e550b001454f4a37c013fe5550b0114559d647aab42ce550b001454f4a37c30cb2a550b0114559d647a9c100f550b001454fabc7c31fadb550b0114559d647ab1da30550b001454f4a37c1f63a3550b0114559d647aab42ce550b001454f4a37c18f419550b01145598917aaa7376550b001454fabc7c21b92c550b011455a28f7a9c10df550b001454ef0a7c1e5253550b0114559d647a9c100f550b001454f4a37c30cb2a550b0114559d647a9c100f550b001454ff497c2a53f7550b0114559d647aab42ce550b001454ff497c1ba7b2550b0114559d647a933eea550b001454f4a37c1f63a3550b0114559d647a933eea550b001454f4a37c18f419550b0114559d647aa2ca4f550b001454f4a37c30cb2a550b011455a28f7a9c10df550b001454f4a37c366d82550b0114559d647a9c100f550b001454f4a37c18f419550b01145598917a9b8771550b001454fabc7c2831ad550b0114559d647a9c100f550b001454ef0a7c271f7d550b011455a28f7a8c53ac550b001454fabc7c122fc5550b011455a28f7a8c53ac550b001454fabc7c19c528550b011455a28f7a95985e550b001454fabc7c19c528550b011455a28f7a9c10df550b001454fabc7c2831ad550b011455ada17aaeb709550b001454ef0a7bfaf9d1550b01145598917aa19959550b001454f4a37c098696550b011455a77a7a95986e550b001454f4a37c098696550b011455a28f7a7de02e";
//                            byte[] buf = ByteUtilities.asByteArray(hex);

                    if (buf != null && buf.length > 0) {
                        unpackReceiveBuf(buf);
                        Log.d(TAG, " bufHex = " + ByteUtilities.asHex(buf).toUpperCase());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                SystemClock.sleep(INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //解析读取到的串口数据
    private void unpackReceiveBuf(byte[] buf) {
        ReadDataResponse readDataResponse = new ReadDataResponse(buf);
        readDataResponse.unpack();

        ReadDataRealtime readDataRealtime = myApp.readDataRealtimeSDDao.queryOne();
        if(StringUtils.isBlank(Constant.imei)){
            if(readDataRealtime != null && !StringUtils.isBlank(readDataRealtime.getSensorId())){
                Constant.imei = readDataRealtime.getSensorId();
            } else {
                Constant.imei = MobileInfoUtil.getIMEI(myApp);
            }
        }
        readDataResponse.setSensorId(Constant.imei);
        try {

            if(readDataRealtime == null){
                readDataRealtime = new ReadDataRealtime();
                BeanPropertiesUtil.copyPropertiesExclude(readDataResponse, readDataRealtime, new String[]{"_id", "begin", "packLen", "loadLen", "dataLen", "cmd", "crc", "end", "buf"});
                myApp.readDataRealtimeSDDao.save(readDataRealtime);
            } else {
                BeanPropertiesUtil.copyPropertiesExclude(readDataResponse, readDataRealtime, new String[]{"_id", "begin", "packLen", "loadLen", "dataLen", "cmd", "crc", "end", "buf"});
                myApp.readDataRealtimeSDDao.updateToDB(readDataRealtime);
            }
            myApp.readDataRealtime = readDataRealtime;

            Date sensorDataTime = readDataResponse.getSensorDataTime();
            Calendar cal = Calendar.getInstance();
            cal.setTime(sensorDataTime);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND,0);
            readDataResponse.setSensorDataTime(cal.getTime());

            ReadDataResponse readDataResponseDB = myApp.readDataSDDao.query(readDataResponse);
            if(readDataResponseDB != null){
                return;
            }
            readDataResponse.setPower(1);
            readDataResponse.setBattery(myApp.batteryPercent);
            readDataResponse.setCsq(myApp.signalStrength);
            //获取手机基站信息
            try {
                TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String operator = manager.getNetworkOperator();
                if(!StringUtils.isBlank(operator)){
                    /**通过operator获取 MCC 和MNC */
                    int mcc = Integer.parseInt(operator.substring(0, 3));
                    int mnc = Integer.parseInt(operator.substring(3));
                    readDataResponse.setMcc(mcc);
                    readDataResponse.setMnc(mnc);
                }

                if(manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA){
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) manager.getCellLocation();
                    if(gsmCellLocation != null){
                        int cid = gsmCellLocation.getCid(); //获取gsm基站识别标号
                        int lac = gsmCellLocation.getLac(); //获取gsm网络编号
                        readDataResponse.setCid(cid);
                        readDataResponse.setLac(lac);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SysResponseBean sysResponseBean = myApp.sysResponseBean;
            if(sysResponseBean != null) {
                String devSendCycle = sysResponseBean.getDevSendCycle();
                int devSendCycleMin = Integer.valueOf(devSendCycle)/60;
                if(devSendCycleMin > 0){
                    int minute = cal.get(Calendar.MINUTE);

                    Double tempLower = Double.valueOf(sysResponseBean.getTempLower());
                    Double tempHight = Double.valueOf(sysResponseBean.getTempHight());
                    Double tempLower1 = Double.valueOf(sysResponseBean.getTempLower1());
                    Double tempHight1 = Double.valueOf(sysResponseBean.getTempHight1());
                    Double temp = readDataResponse.getTemp();
                    boolean isTempOver = false;
                    if(tempLower != null && tempHight != null && tempLower > temp && temp > tempHight){
                        isTempOver = true;
                    }
                    Double temp1 = readDataResponse.getTemp1();
                    if(tempLower1 != null && tempHight1 != null && tempLower1 > temp1 && temp1 > tempHight1){
                        isTempOver = true;
                    }
                    String unqualifyRecordFlag = sysResponseBean.getUnqualifyRecordFlag();
                    if(minute % devSendCycleMin != 0 && ("1".equals(unqualifyRecordFlag) && !isTempOver)){
                        return;
                    }
                }
            }
            myApp.readDataSDDao.save(readDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            myApp.appLogSDDao.save(e.getMessage());
        }

        myApp.readDataResponseTime = System.currentTimeMillis();

//        moduleBufSDDao.save(buf, 1, readDataResponse.getCmd(), true); // 保存串口通信数据
    };

}
