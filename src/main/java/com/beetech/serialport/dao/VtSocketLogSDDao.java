package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.vt.VtSocketLog;
import com.beetech.serialport.greendao.dao.VtSocketLogDao;
import java.util.List;

public class VtSocketLogSDDao {
    private final static String TAG = VtSocketLogSDDao.class.getSimpleName();

    private MyApplication myApp;
    public VtSocketLogSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(VtSocketLog vtSocketLog) {
        long startTimeInMills = System.currentTimeMillis();
        try {
            if (vtSocketLog == null) {
                return;
            }
            myApp.getDaoSession().getVtSocketLogDao().insertInTx(vtSocketLog);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "保存save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "保存save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void save(String text, int type, Long dataId, String threadName){
        long startTimeInMills = System.currentTimeMillis();
        try {
            VtSocketLog vtSocketLog = new VtSocketLog(text, type, dataId, threadName);
            myApp.getDaoSession().getVtSocketLogDao().insertInTx(vtSocketLog);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "保存save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "保存save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public List<VtSocketLog> queryAll(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<VtSocketLog> list = null;
        try{
            list = myApp.getDaoSession().getVtSocketLogDao().queryBuilder()
                    .orderDesc(VtSocketLogDao.Properties._id)
                    .limit(count)
                    .offset(startPosition)
                    .list();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "查询queryAll异常", e);
            throw e;

        } finally {
            Log.d(TAG, "查询queryAll耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }

    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getVtSocketLogDao().deleteAll();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "删除truncate异常", e);
            throw e;
        } finally {
            Log.d(TAG, "删除truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
