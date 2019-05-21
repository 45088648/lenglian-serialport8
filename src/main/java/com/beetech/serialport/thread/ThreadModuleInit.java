package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.utils.ModuleUtils;

/**
 * 串口模组实例化上电初始化
 */
public class ThreadModuleInit extends Thread {
    private final static String TAG = ThreadModuleInit.class.getSimpleName();
    public final static int INTERVAL = 1000*60*5; //启动间隔
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private Context mContext;
    private AppLogSDDao appLogSDDao;
    private MyApplication myApp;
    private long readDataResponseTimeOut = 1000*60*5; // 接收传感器数据长时间超时，模块重新上电初始化依据
    private ModuleUtils moduleUtils;

    private static ThreadModuleInit instance;

    public synchronized static ThreadModuleInit getInstance() {
        if (null == instance) {
            synchronized(ThreadModuleInit.class) {
                instance = new ThreadModuleInit();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    public void init(Context mContext){
        this.mContext = mContext;
        appLogSDDao = new AppLogSDDao(mContext);
        moduleUtils = new ModuleUtils(mContext);

        myApp = (MyApplication) mContext.getApplicationContext();
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

            try {
                long currentTimeMillis = System.currentTimeMillis();
                long responseTimeInterval = currentTimeMillis - myApp.readDataResponseTime; // 接收传感器数据时间和当前时间间隔
                //接收传感器数据长时间超时
                if(responseTimeInterval > readDataResponseTimeOut){
                    boolean result = moduleUtils.init();
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
