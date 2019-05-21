package com.beetech.serialport.dao;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.ModuleBuf;
import com.beetech.serialport.greendao.dao.ModuleBufDao;

import java.util.List;

public class ModuleBufSDDao {
    private final static String TAG = AppLogSDDao.class.getSimpleName();

    private MyApplication myApp;
    public ModuleBufSDDao(Context context) {
        myApp = (MyApplication)context.getApplicationContext();
    }

    public void save(byte[] buf, int type, int cmd, boolean result){
        ModuleBuf moduleBuf = new ModuleBuf(buf, type, cmd, result);
        myApp.getDaoSession().getModuleBufDao().insertInTx(moduleBuf);
    }

    public List<ModuleBuf> queryAll(int count, int startPosition) {
        long startTimeInMills = System.currentTimeMillis();
        List<ModuleBuf> list = null;
        try{
            list = myApp.getDaoSession().getModuleBufDao().queryBuilder()
                    .orderDesc(ModuleBufDao.Properties._id)
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

    public void truncate(){
        long startTimeInMills = System.currentTimeMillis();
        try{
            myApp.getDaoSession().getModuleBufDao().deleteAll();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "truncate异常", e);
            throw e;
        } finally {
            Log.d(TAG, "truncate耗时：" + (System.currentTimeMillis() - startTimeInMills));
        }
    }
}
