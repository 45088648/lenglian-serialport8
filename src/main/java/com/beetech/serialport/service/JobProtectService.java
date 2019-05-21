package com.beetech.serialport.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.beetech.serialport.activity.RealtimeMonitorActivity;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.utils.ServiceAliveUtils;

public class JobProtectService extends Service {
    private final static String TAG = JobProtectService.class.getSimpleName();
    private MyApplication myApp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = (MyApplication)getApplicationContext();
        myApp.appLogSDDao.save(TAG + " schedule ");

//        Toast.makeText(getApplicationContext(), TAG + "schedule ", Toast.LENGTH_SHORT).show();

        /*如果服务正在运行，直接return*/
        if (!ServiceAliveUtils.isServiceRunning(JobProtectService.this, "com.beetech.serialport.service.ModuleService")){
            /* 启动串口通信服务 */
            startService(new Intent(this, ModuleService.class));
        }

        boolean isRealtimeMonitorActivityRunning = ServiceAliveUtils.isActivityRunning(JobProtectService.this, "com.beetech.serialport.activity.RealtimeMonitorActivity");
        myApp.appLogSDDao.save(TAG + " isRealtimeMonitorActivityRunning = "+isRealtimeMonitorActivityRunning);
        if(!isRealtimeMonitorActivityRunning){
            //
            RealtimeMonitorActivity realtimeMonitorActivity = myApp.realtimeMonitorActivity;
            if(realtimeMonitorActivity != null){
                boolean isDestroyed = realtimeMonitorActivity.isDestroyed();
                boolean isFinishing = realtimeMonitorActivity.isFinishing();
                boolean isRestricted = realtimeMonitorActivity.isRestricted();

                myApp.appLogSDDao.save(TAG + " isDestroyed = "+isDestroyed+", isFinishing = "+isFinishing+", isRestricted = "+isRestricted);

            }else {
                myApp.appLogSDDao.save(TAG + " realtimeMonitorActivity is null ");
            }
            Intent intent = new Intent(JobProtectService.this, RealtimeMonitorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
