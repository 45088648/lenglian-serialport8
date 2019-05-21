package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.SysResponseBean;
import java.util.List;

public class SysResponseBeanSDDao{
    private final static String TAG = AppLogSDDao.class.getSimpleName();

    private MyApplication myApp;
    public SysResponseBeanSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(SysResponseBean sysResponseBean){
        long startTimeInMills = System.currentTimeMillis();
        try {
            myApp.getDaoSession().getSysResponseBeanDao().insertInTx(sysResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void updateToDB(SysResponseBean sysResponseBean) {
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getSysResponseBeanDao().updateInTx(sysResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateToDB异常", e);
            throw e;

        } finally {
            Log.d(TAG, "updateToDB耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public SysResponseBean queryLast() {
        long startTimeInMills = System.currentTimeMillis();
        SysResponseBean sysResponseBean = null;
        try{
            List<SysResponseBean> list = myApp.getDaoSession().getSysResponseBeanDao().queryBuilder()
                    .limit(1)
                    .list();

            if(list != null && !list.isEmpty()){
                sysResponseBean = list.get(0);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryLast异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryLast耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return sysResponseBean;
    }

    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getSysResponseBeanDao().deleteAll();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "删除truncate异常", e);
            throw e;

        } finally {
            Log.d(TAG, "删除truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
