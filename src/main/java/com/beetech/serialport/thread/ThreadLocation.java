package com.beetech.serialport.thread;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.baidu.location.BDAbstractLocationListener;
import com.beetech.serialport.R;
import com.beetech.serialport.activity.MainActivity;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.listener.MyBDLocationListener;
import com.beetech.serialport.service.LocationService;

public class ThreadLocation extends Thread {

    private final static String TAG = ThreadLocation.class.getSimpleName();
    public final static int INTERVAL = 1000*30;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadLocation instance;

    public synchronized static ThreadLocation getInstance() {
        if (null == instance) {
            synchronized(ThreadLocation.class){
                instance = new ThreadLocation();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private MyApplication myApp;
    private LocationService locService;
    private BDAbstractLocationListener locationListener;

    public void init(Context mContext){
        this.mContext = mContext;
        myApp = (MyApplication) mContext.getApplicationContext();
        NUM = 0;

        locService = myApp.locationService;
        //开启前台定位服务：
        Notification.Builder builder = new Notification.Builder (mContext.getApplicationContext());
        Intent nfIntent = new Intent(mContext.getApplicationContext(), MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(mContext, 0, nfIntent, 0)) // 设置PendingIntent
                .setContentTitle("正在进行后台定位")
                .setSmallIcon(R.mipmap.temp)
                .setContentText("后台定位通知")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        Notification notification = null;
        notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;
        locService.getClient().enableLocInForeground(1001, notification);

        locationListener = new MyBDLocationListener(mContext);
        locService.registerListener(locationListener);
    }

    @Override
    public void run() {
        while (true) {
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {
                locService = myApp.locationService;

                if(!locService.isStart()){
                    locService.start();

                    Log.i(TAG, "locationClient requestLocation");
                    locService.getClient().requestLocation();
                }
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
