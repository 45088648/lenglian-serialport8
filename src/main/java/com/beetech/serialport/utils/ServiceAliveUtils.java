package com.beetech.serialport.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import java.util.List;

public class ServiceAliveUtils {

    /**
     * 判断服务是否运行
     */
    public static boolean isServiceRunning(Context context, final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    public static boolean isActivityRunning(Context mContext, String activityClassName){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if(info != null && info.size() > 0){
            ComponentName component = info.get(0).topActivity;
            if(activityClassName.equals(component.getClassName())){
                return true;
            }
        }
        return false;
    }

}
