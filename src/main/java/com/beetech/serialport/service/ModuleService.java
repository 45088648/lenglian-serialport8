package com.beetech.serialport.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import com.beetech.module.KeepAliveConnection;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.client.ClientConnectManager;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.listener.BatteryListener;
import com.beetech.serialport.listener.PhoneStatListener;
import com.beetech.serialport.receiver.ConnectReceiver;
import com.beetech.serialport.receiver.WakeReceiver;
import com.beetech.serialport.thread.ThreadLocation;
import com.beetech.serialport.thread.ThreadModuleInit;
import com.beetech.serialport.thread.ThreadModuleReceive;
import com.beetech.serialport.thread.ThreadReConnectGtw;
import com.beetech.serialport.thread.ThreadSendGpsData;
import com.beetech.serialport.thread.ThreadSendShtd;
import com.beetech.serialport.utils.ServiceAliveUtils;
import org.apache.mina.core.session.IoSession;

public class ModuleService extends Service {
    private final static String TAG = ModuleService.class.getSimpleName();
    public final static ConnectReceiver conncetReceiver = new ConnectReceiver();

    private AppLogSDDao appLogSDDao;
    private MyApplication myApp;
    private BatteryListener batteryListener;

    public TelephonyManager mTelephonyManager;
    public PhoneStatListener mListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new KeepAliveConnection.Stub() {};
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "---->onCreate");

        //  服务保活 start
        startGuardService();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(conncetReceiver , filter);

        Intent innerIntent = new Intent(this, GrayInnerService.class);
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        startService(innerIntent);
        startForeground(Constant.GRAY_SERVICE_ID, notification);


        //  服务保活 end

        if(appLogSDDao == null){
            appLogSDDao = new AppLogSDDao(this);
        }
        appLogSDDao.save(TAG + " onCreate");

        myApp = (MyApplication)getApplicationContext();

        /* 启动VT服务端通信服务 */
        IoSession session = myApp.session;
        if(session == null || !session.isConnected()){
            ClientConnectManager.getInstance(this).connect();
        }

        //串口模块上电初始化
        if(myApp.threadModuleInit == null){
            myApp.threadModuleInit = ThreadModuleInit.getInstance();
            myApp.threadModuleInit.init(this);
            myApp.threadModuleInit.start();
        }

        handler.postDelayed(runnable, 3000);

        handler.postDelayed(runnableWakerUpAlarm, 10000);


        //电量和插拔电源状态广播监听
        if(batteryListener == null){
            batteryListener = new BatteryListener(this);
            batteryListener.register();
        }

        //获取telephonyManager, 监听信号强度
        if(mListener == null){
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mListener = new PhoneStatListener(this);
            mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
        }
     }

    private Handler handler = new Handler(){};
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //接收串口数据
            if(myApp.threadModuleReceive == null) {
                myApp.threadModuleReceive = ThreadModuleReceive.getInstance();
                myApp.threadModuleReceive.init(ModuleService.this);
                myApp.threadModuleReceive.start();
            }

            //发送温度数据到网关
            if(myApp.threadSendShtd == null) {
                myApp.threadSendShtd = ThreadSendShtd.getInstance();
                myApp.threadSendShtd.init(ModuleService.this);
                myApp.threadSendShtd.start();
            }

            //发送Gps数据到网关
            if(myApp.threadSendGpsData == null) {
                myApp.threadSendGpsData = ThreadSendGpsData.getInstance();
                myApp.threadSendGpsData.init(ModuleService.this);
                myApp.threadSendGpsData.start();
            }

            //监测网关连接并重连
            if(myApp.threadReConnectGtw == null) {
                myApp.threadReConnectGtw = ThreadReConnectGtw.getInstance();
                myApp.threadReConnectGtw.init(ModuleService.this);
                myApp.threadReConnectGtw.start();
            }

            //定位线程
            if(myApp.threadLocation == null) {
                myApp.threadLocation = ThreadLocation.getInstance();
                myApp.threadLocation.init(ModuleService.this);
                myApp.threadLocation.start();
            }

        }
    };

    private Runnable runnableWakerUpAlarm = new Runnable() {
        @Override
        public void run() {
            //发送唤醒广播来促使挂掉的UI进程重新启动起来
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent();
            alarmIntent.setAction(WakeReceiver.ACTION);
            alarmIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent operation = PendingIntent.getBroadcast(ModuleService.this, Constant.WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constant.ALARM_INTERVAL, operation);
        }
    };

    public void startGuardService() {
        Intent intent = new Intent();
        intent.setClass(this, GuardService.class);
        startService(intent);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "StepService:建立链接");
            boolean isServiceRunning = ServiceAliveUtils.isServiceRunning(ModuleService.this, "com.beetech.serialport.service.ScreenCheckService");
            if (!isServiceRunning) {
                Intent i = new Intent(ModuleService.this, ScreenCheckService.class);
                startService(i);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断开链接
            startService(new Intent(ModuleService.this, RemoteService.class));
            // 重新绑定
            bindService(new Intent(ModuleService.this, RemoteService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        appLogSDDao.save(TAG+" onStartCommand");

        // 绑定建立链接
        bindService(new Intent(this, RemoteService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "---->onDestroy");
        appLogSDDao.save(TAG+" onDestroy");
        super.onDestroy();

        Intent intent = new Intent(ConnectReceiver.ACTION);
        sendBroadcast(intent);
        unregisterReceiver( conncetReceiver );
        Log.d(TAG, "sendBroadcast[" + ConnectReceiver.ACTION + "]");
    }



    private Handler handlerToast = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Object toastMsg = msg.obj;
            if(toastMsg != null){
                Toast.makeText(getApplicationContext(), toastMsg.toString(), Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };


    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "InnerService -> onStartCommand");
            Notification notification = new Notification();
            notification.flags = Notification.FLAG_ONGOING_EVENT;
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
            startForeground(Constant.GRAY_SERVICE_ID, notification);
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
