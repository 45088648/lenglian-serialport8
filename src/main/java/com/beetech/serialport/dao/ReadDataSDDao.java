package com.beetech.serialport.dao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.greendao.dao.ReadDataResponseDao;
import java.util.List;
import java.util.Date;

public class ReadDataSDDao {
    private final static String TAG = ReadDataSDDao.class.getSimpleName();

    private MyApplication myApp;
    public ReadDataSDDao(Context context){
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(ReadDataResponse readDataResponse){
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getReadDataResponseDao().insertInTx(readDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "save异常", e);
            throw e;

        } finally {
            Log.d(TAG, "save耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public void updateToDB(ReadDataResponse readDataResponse) {
        long startTimeInMills = System.currentTimeMillis();
        try {

            myApp.getDaoSession().getReadDataResponseDao().updateInTx(readDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateToDB异常", e);
            throw e;

        } finally {
            Log.d(TAG, "updateToDB耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }

    public ReadDataResponse queryById(Long _id) {
        long startTimeInMills = System.currentTimeMillis();
        ReadDataResponse readDataResponse = null;
        try{
//            myApp.myReadDataSendEntityDaoSession.clear();
            readDataResponse = myApp.getDaoSession().getReadDataResponseDao().load(_id);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryById异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryById耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }

        return readDataResponse;
    }

    public List<ReadDataResponse> query(String sensorId) {
        long startTimeInMills = System.currentTimeMillis();
        List<ReadDataResponse> list = null;
        try{
            if(!TextUtils.isEmpty(sensorId)){
                list = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                        .where(ReadDataResponseDao.Properties.SensorId.eq(sensorId))
                        .orderDesc(ReadDataResponseDao.Properties.SensorDataTime)
                        .list();
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "query异常", e);
            throw e;

        } finally {
            Log.d(TAG, "query耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }

    public List<ReadDataResponse> queryAll(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<ReadDataResponse> list = null;
        try{
            list = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                    .orderDesc(ReadDataResponseDao.Properties._id)
                    .limit(count)
                    .offset(startPosition)
                    .list();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "query异常", e);
            throw e;

        } finally {
            Log.d(TAG, "query耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }

    public ReadDataResponse query(ReadDataResponse readDataResponse) {
        long startTimeInMills = System.currentTimeMillis();
        try{
            readDataResponse = myApp.getDaoSession().getReadDataResponseDao().load(readDataResponse.get_id());
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "queryById异常", e);
            throw e;

        } finally {
            Log.d(TAG, "queryById耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }

        return readDataResponse;
    }

    public List<ReadDataResponse> query(String sensorId, int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<ReadDataResponse> list = null;
        try{
            if(!TextUtils.isEmpty(sensorId)){
                list = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                        .where(ReadDataResponseDao.Properties.SensorId.eq(sensorId))
                        .orderDesc(ReadDataResponseDao.Properties.SensorDataTime)
                        .limit(count)
                        .offset(startPosition)
                        .list();
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "query异常", e);
            throw e;

        } finally {
            Log.d(TAG, "query耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }

    public List<ReadDataResponse> query(String sensorId, Date sensorDataTimeEnd, int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<ReadDataResponse> list = null;
        try{
            if(!TextUtils.isEmpty(sensorId)){
                list = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                        .where(ReadDataResponseDao.Properties.SensorId.eq(sensorId))
                        .where(ReadDataResponseDao.Properties.SensorDataTime.lt(sensorDataTimeEnd))
                        .orderDesc(ReadDataResponseDao.Properties.SensorDataTime)
                        .limit(count)
                        .offset(startPosition)
                        .list();
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "query异常", e);
            throw e;

        } finally {
            Log.d(TAG, "query耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
        return list;
    }

    public List<ReadDataResponse> queryForSend(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<ReadDataResponse> list = null;
        try{
            list = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                    .where(ReadDataResponseDao.Properties.SendFlag.eq(0))
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

    public Long queryMaxId() {
        long maxId = 0;
        long startTimeInMills = System.currentTimeMillis();
        ReadDataResponse readDataResponse = null;
        try{
            readDataResponse = myApp.getDaoSession().getReadDataResponseDao().queryBuilder()
                    .orderDesc(ReadDataResponseDao.Properties._id)
                    .unique();
            if(readDataResponse != null){
                maxId = readDataResponse.get_id();
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "查询queryForSend异常", e);
            throw e;

        } finally {
            Log.d(TAG, "查询queryForSend耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }

        return maxId;
    }


    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getReadDataResponseDao().deleteAll();

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "删除truncate异常", e);
            throw e;

        } finally {
            Log.d(TAG, "删除truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }


    /**
     * 删除inputTimeEndInMills前数据
     * @param inputTimeEndInMills 录入时间前
     */
    public void deleteReadDataOld(long inputTimeEndInMills) {

        long startTimeInMills = System.currentTimeMillis();
        try{

            myApp.getDatabase().execSQL("DELETE FROM READ_DATA_RESPONSE where INPUT_TIME < "+ inputTimeEndInMills);

        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "删除deleteReadDataOld异常", e);
            throw e;

        } finally {
            Log.d(TAG, "删除deleteReadDataOld耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
