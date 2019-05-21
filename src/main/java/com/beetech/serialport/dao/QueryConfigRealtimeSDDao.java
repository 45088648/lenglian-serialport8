package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.QueryConfigRealtime;
import java.util.List;

public class QueryConfigRealtimeSDDao {
    private final static String TAG = QueryConfigRealtimeSDDao.class.getSimpleName();

    private MyApplication myApp;
    public QueryConfigRealtimeSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(QueryConfigRealtime queryConfigRealtime){
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getQueryConfigRealtimeDao().insertInTx(queryConfigRealtime);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public QueryConfigRealtime queryOne() {
        long startTimeInMills = System.currentTimeMillis();
        QueryConfigRealtime queryConfigRealtime = null;
        try{
            List<QueryConfigRealtime> list = myApp.getDaoSession().getQueryConfigRealtimeDao().queryBuilder().limit(1).list();
            if(list != null && !list.isEmpty()){
                queryConfigRealtime = list.get(0);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryOne异常", e);
            throw e;
        } finally {
            Log.d(TAG, "queryOne耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return queryConfigRealtime;
    }

    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getQueryConfigRealtimeDao().deleteAll();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "truncate异常", e);
            throw e;
        } finally {
            Log.d(TAG, "truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
