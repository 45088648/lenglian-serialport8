package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.GpsDataBean;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.GpsDataSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import java.util.List;

/**
 * 发数据到服务器网关
 */
public class ThreadSendGpsData extends Thread {

    private final static String TAG = ThreadSendGpsData.class.getSimpleName();
    public final static int INTERVAL = 1000*13;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadSendGpsData instance;

    public synchronized static ThreadSendGpsData getInstance() {
        if (null == instance) {
            synchronized(ThreadSendGpsData.class){
                instance = new ThreadSendGpsData();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private GpsDataSDDao gpsDataSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;
    private MyApplication myApp;
    private int queryCount = 30;

    public void init(Context mContext){
        this.mContext = mContext;
        gpsDataSDDao = new GpsDataSDDao(mContext);
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);

        myApp = (MyApplication) mContext.getApplicationContext();
        NUM = 0;
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e(TAG,"未捕获异常", e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        while(true){
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {
                IoSession mSession = myApp.session;
                if(mSession != null && mSession.isConnected()){
                    List<GpsDataBean> dataList = gpsDataSDDao.queryForSend(queryCount, 0);
                    if(dataList != null && !dataList.isEmpty()){
                        for (GpsDataBean gpsDataBean : dataList){
                            String inText = gpsDataBean.toString();
                            //保存日志
                            if(Constant.IS_SAVE_SOCKET_LOG) {
                                try {
                                    vtSocketLogSDDao.save(inText, 0, gpsDataBean.get_id(), Thread.currentThread().getName());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            WriteFuture writeResult= mSession.write(inText);

                            writeResult.addListener(new IoFutureListener() {
                                public void operationComplete(IoFuture future) {
                                    WriteFuture wfuture = (WriteFuture) future;
                                    // 写入成功  
                                    if (wfuture.isWritten()) {
                                        return;
                                    }
                                    // 写入失败，自行进行处理  
                                }
                            });

                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                SystemClock.sleep(INTERVAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
