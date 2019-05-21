package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.client.ClientConnectManager;
import org.apache.mina.core.session.IoSession;

/**
 * 监测网关连接并重连
 */
public class ThreadReConnectGtw extends Thread {

    private final static String TAG = ThreadReConnectGtw.class.getSimpleName();
    public final static int INTERVAL = 1000*60;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadReConnectGtw instance;

    public synchronized static ThreadReConnectGtw getInstance() {
        if (null == instance) {
            synchronized(ThreadReConnectGtw.class){
                instance = new ThreadReConnectGtw();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private MyApplication myApp;

    public void init(Context mContext){
        this.mContext = mContext;
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
        while(!isInterrupted()){
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {
                /* 启动VT服务端通信服务 */
                IoSession session = myApp.session;
                if(session == null || !session.isConnected()){
                    ClientConnectManager.getInstance(mContext).connect();
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
