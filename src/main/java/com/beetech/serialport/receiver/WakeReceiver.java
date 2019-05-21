package com.beetech.serialport.receiver;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.thread.ThreadLocation;
import com.beetech.serialport.thread.ThreadModuleInit;
import com.beetech.serialport.thread.ThreadModuleReceive;
import com.beetech.serialport.thread.ThreadReConnectGtw;
import com.beetech.serialport.thread.ThreadSendGpsData;
import com.beetech.serialport.thread.ThreadSendShtd;

/**
 * 灰色保活手段唤醒广播
 */
public class WakeReceiver extends BroadcastReceiver {

    private final static String TAG = WakeReceiver.class.getSimpleName();
    private final static int WAKE_SERVICE_ID = -1111;
    public final static String ACTION = "com.beetech.module.receiver.Wake";

    private MyApplication myApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            Log.i(TAG, "onReceive");

            Intent wakeIntent = new Intent(context, WakeNotifyService.class);
            context.startService(wakeIntent);
            myApp = (MyApplication)context.getApplicationContext();

            // 监测进程状态

            //串口实例化、上电初始化
            ThreadModuleInit threadModuleInit = myApp.threadModuleInit;
            if(threadModuleInit == null){
                myApp.appLogSDDao.save(TAG + " threadModuleInit is null");

                myApp.threadModuleInit = ThreadModuleInit.getInstance();
                myApp.threadModuleInit.init(context);
                myApp.threadModuleInit.start();
            }
            Log.d(TAG, "threadModuleInit = "+threadModuleInit);

            //读串口
            ThreadModuleReceive threadModuleReceive = myApp.threadModuleReceive;
            if(threadModuleReceive == null){
                myApp.appLogSDDao.save(TAG +  " threadModuleReceive is null");

                myApp.threadModuleReceive = ThreadModuleReceive.getInstance();
                myApp.threadModuleReceive.init(context);
                myApp.threadModuleReceive.start();
            }
            Log.d(TAG, "threadModuleReceive = "+threadModuleReceive);

            // 发温控数据到VT网关
            ThreadSendShtd threadSendShtd = myApp.threadSendShtd;
            if(threadSendShtd == null){
                myApp.appLogSDDao.save(TAG + " threadSendShtd is null");

                myApp.threadSendShtd = ThreadSendShtd.getInstance();
                myApp.threadSendShtd.init(context);
                myApp.threadSendShtd.start();
            }
            Log.d(TAG, "threadSendShtd = "+threadSendShtd);

            // 发gps数据到VT网关
            ThreadSendGpsData threadSendGpsData = myApp.threadSendGpsData;
            if(threadSendGpsData == null){
                myApp.appLogSDDao.save(TAG + " threadSendGpsData is null");

                myApp.threadSendGpsData = ThreadSendGpsData.getInstance();
                myApp.threadSendGpsData.init(context);
                myApp.threadSendGpsData.start();
            }
            Log.d(TAG, "threadSendGpsData = "+threadSendGpsData);

            //
            ThreadReConnectGtw threadReConnectGtw = myApp.threadReConnectGtw;
            if(threadReConnectGtw == null){
                myApp.appLogSDDao.save(TAG + " threadReConnectGtw is null");

                myApp.threadReConnectGtw = ThreadReConnectGtw.getInstance();
                myApp.threadReConnectGtw.init(context);
                myApp.threadReConnectGtw.start();
            }
            Log.d(TAG, "threadReConnectGtw = "+threadSendGpsData);

            //
            ThreadLocation threadLocation = myApp.threadLocation;
            if(threadLocation == null){
                myApp.appLogSDDao.save(TAG + " threadLocation is null");

                myApp.threadLocation = ThreadLocation.getInstance();
                myApp.threadLocation.init(context);
                myApp.threadLocation.start();
            }
            Log.d(TAG, "threadLocation = "+threadLocation);

        }
    }

    /**
     * 用于其他进程来唤醒UI进程用的Service
     */
    public static class WakeNotifyService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "WakeNotifyService->onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "WakeNotifyService->onStartCommand");
            if (Build.VERSION.SDK_INT < 18) {
                startForeground(WAKE_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
            } else {
                Intent innerIntent = new Intent(this, WakeGrayInnerService.class);
                startService(innerIntent);
                startForeground(WAKE_SERVICE_ID, new Notification());
            }
            return START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "WakeNotifyService->onDestroy");
            super.onDestroy();
        }
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class WakeGrayInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "InnerService -> onStartCommand");
            startForeground(WAKE_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "InnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
