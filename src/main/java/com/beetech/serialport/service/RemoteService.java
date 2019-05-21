package com.beetech.serialport.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.app.Service;
import android.util.Log;
import android.app.Notification;
import com.beetech.module.KeepAliveConnection;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.utils.ServiceAliveUtils;
import android.support.annotation.Nullable;

/**
 * 守护进程 双进程通讯
 *
 * @author zhangcs
 * @time Created by 2019-1-9 17:43:17
 */
public class RemoteService extends Service {
    private final static String TAG = GuardService.class.getSimpleName();
    private AppLogSDDao appLogSDDao;

    @Override
    public void onCreate() {
        super.onCreate();
        appLogSDDao = new AppLogSDDao(this);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "GuardService:建立链接");
            boolean isServiceRunning = ServiceAliveUtils.isServiceRunning(RemoteService.this, "com.beetech.serialport.service.ScreenCheckService");
            if (!isServiceRunning) {
                Intent i = new Intent(RemoteService.this, ScreenCheckService.class);
                startService(i);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (!ServiceAliveUtils.isServiceRunning(RemoteService.this, "com.beetech.serialport.service.ModuleService")) {
                // 断开链接
                startService(new Intent(RemoteService.this, ModuleService.class));
                appLogSDDao.save(TAG+" start ModuleService");
            }
            // 重新绑定
            bindService(new Intent(RemoteService.this, ModuleService.class), mServiceConnection, Context.BIND_IMPORTANT);
            appLogSDDao.save(TAG+" bind ModuleService");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new KeepAliveConnection.Stub() {
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification());
        // 绑定建立链接
        bindService(new Intent(this, ModuleService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

}