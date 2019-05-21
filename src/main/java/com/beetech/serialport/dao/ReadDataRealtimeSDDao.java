package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.ReadDataRealtime;
import java.util.List;

public class ReadDataRealtimeSDDao {
    private final static String TAG = ReadDataRealtimeSDDao.class.getSimpleName();

    private MyApplication myApp;
    public ReadDataRealtimeSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(ReadDataRealtime readDataRealtime){
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getReadDataRealtimeDao().insertInTx(readDataRealtime);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void updateToDB(ReadDataRealtime readDataRealtime) {
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getReadDataRealtimeDao().updateInTx(readDataRealtime);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateToDB异常", e);
            throw e;

        } finally {
            Log.d(TAG, "updateToDB耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public ReadDataRealtime queryOne() {
        long startTimeInMills = System.currentTimeMillis();
        ReadDataRealtime readDataRealtime = null;
        try{
            List<ReadDataRealtime> list = myApp.getDaoSession().getReadDataRealtimeDao().queryBuilder()
                    .limit(1)
                    .list();

            if(list != null && !list.isEmpty()){
                readDataRealtime = list.get(0);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "query异常", e);
            throw e;

        } finally {
            Log.d(TAG, "query耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return readDataRealtime;
    }


    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getReadDataRealtimeDao().deleteAll();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "truncate异常", e);
            throw e;

        } finally {
            Log.d(TAG, "truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
