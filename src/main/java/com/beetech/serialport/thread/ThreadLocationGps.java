package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.GpsDataBean;
import com.beetech.serialport.dao.GpsDataSDDao;
import com.beetech.serialport.service.LocationService;

public class ThreadLocationGps extends Thread{
    private final static String TAG = ThreadLocationGps.class.getSimpleName();
    public final static int INTERVAL = 1000*30;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadLocationGps instance;

    private Context mContext;
    private BaiduMap mBaiduMap;
    private LocationService locService;
    private GpsDataSDDao gpsDataSDDao;
    private MyApplication myApp;

    /***
     * 定位结果回调，在此方法中处理定位结果
     */
    BDAbstractLocationListener listener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
                myApp.location = location;
                GpsDataBean gpsDataBean = new GpsDataBean(location);
                gpsDataSDDao.save(gpsDataBean);
            }
        }
    };

    public synchronized static ThreadLocationGps getInstance() {
        if (null == instance) {
            synchronized(ThreadLocationGps.class){
                instance = new ThreadLocationGps();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    public void init(Context context){
        this.mContext = context;
        gpsDataSDDao = new GpsDataSDDao(context);
        myApp = (MyApplication) context.getApplicationContext();
        NUM = 0;

        locService = myApp.locationService;

        LocationClientOption mOption = locService.getDefaultLocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setCoorType("bd09ll");

        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setScanSpan(1000*30);
        //可选，默认false，设置是否开启Gps定位
        mOption.setOpenGps(true);

        locService.setLocationOption(mOption);
        locService.registerListener(listener);
        locService.start();

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
        while (true) {
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                SystemClock.sleep(INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
