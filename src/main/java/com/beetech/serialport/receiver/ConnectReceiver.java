package com.beetech.serialport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.service.GuardService;
import com.beetech.serialport.service.ModuleService;
import com.beetech.serialport.utils.ServiceAliveUtils;

public class ConnectReceiver extends BroadcastReceiver {
 
    private final static String TAG = ConnectReceiver.class.getSimpleName();
    public final static String ACTION = "com.beetech.module.receiver.CONNECT_SERVICE";
    private MyApplication myApp;
    public ConnectReceiver() {
    }
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        myApp = (MyApplication)context.getApplicationContext();
        if (!ServiceAliveUtils.isServiceRunning(context, "com.beetech.serialport.service.ModuleService")) {
            Intent mIntent = new Intent();
            mIntent.setClass(context, ModuleService.class);
            context.startService(mIntent);
            myApp.appLogSDDao.save(TAG + " start ModuleService");
            Log.d(TAG, "start ModuleService");
        }

        if (!ServiceAliveUtils.isServiceRunning(context, "com.beetech.serialport.service.GuardService")) {
            Intent intent1 = new Intent();
            intent1.setClass(context, GuardService.class);
            context.startService(intent1);
            myApp.appLogSDDao.save(TAG + " start GuardService");
            Log.d(TAG, "start GuardService");
        }
    }
}
