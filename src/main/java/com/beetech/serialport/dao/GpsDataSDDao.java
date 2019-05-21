package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.GpsDataBean;
import com.beetech.serialport.greendao.dao.GpsDataBeanDao;
import java.util.List;

public class GpsDataSDDao {
    private final static String TAG = GpsDataSDDao.class.getSimpleName();

    private MyApplication myApp;
    public GpsDataSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void saveToDB(GpsDataBean gpsDataBean) {
        long startTimeInMills = System.currentTimeMillis();
        try {
            myApp.getDaoSession().getGpsDataBeanDao().insertInTx(gpsDataBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void save(GpsDataBean gpsDataBean){
        long startTimeInMills = System.currentTimeMillis();
        try {
            myApp.getDaoSession().getGpsDataBeanDao().insertInTx(gpsDataBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void updateToDB(GpsDataBean gpsDataBean) {
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getGpsDataBeanDao().updateInTx(gpsDataBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateToDB异常", e);
            throw e;

        } finally {
            Log.d(TAG, "updateToDB耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public List<GpsDataBean> queryAll(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<GpsDataBean> list = null;
        try{
            list = myApp.getDaoSession().getGpsDataBeanDao().queryBuilder()
                    .orderDesc(GpsDataBeanDao.Properties._id)
                    .limit(count)
                    .offset(startPosition)
                    .list();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryAll异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryAll耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }


    public List<GpsDataBean> queryForSend(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<GpsDataBean> list = null;
        try{
            list = myApp.getDaoSession().getGpsDataBeanDao().queryBuilder()
                    .where(GpsDataBeanDao.Properties.SendFlag.eq(0))
                    .limit(count)
                    .list();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryForSend异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryForSend耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }

        return list;
    }

    public GpsDataBean queryById(Long _id) {
        long startTimeInMills = System.currentTimeMillis();
        GpsDataBean gpsDataBean = null;
        try{
//            myApp.myReadDataSendEntityDaoSession.clear();
            gpsDataBean = myApp.getDaoSession().getGpsDataBeanDao().load(_id);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryById异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryById耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }

        return gpsDataBean;
    }

    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getGpsDataBeanDao().deleteAll();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "truncate异常", e);
            throw e;
        } finally {
            Log.d(TAG, "truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
