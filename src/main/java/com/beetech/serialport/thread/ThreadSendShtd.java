package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.SHTDBean;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import com.google.gson.Gson;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import java.util.List;

public class ThreadSendShtd extends Thread {

    private final static String TAG = ThreadSendShtd.class.getSimpleName();
    public final static int INTERVAL = 1000*13;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadSendShtd instance;

    public synchronized static ThreadSendShtd getInstance() {
        if (null == instance) {
            synchronized(ThreadSendShtd.class){
                instance = new ThreadSendShtd();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private ReadDataSDDao readDataSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;
    private MyApplication myApp;
    private int queryCount = 30;
    private Gson gson = new Gson();

    public void init(Context mContext){
        this.mContext = mContext;
        readDataSDDao = new ReadDataSDDao(mContext);
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
                    List<ReadDataResponse> readDataResponseList = readDataSDDao.queryForSend(queryCount, 0);
                    if(readDataResponseList != null && !readDataResponseList.isEmpty()){
                        for (ReadDataResponse readDataResponse : readDataResponseList){
                            SHTDBean shtd = new SHTDBean(readDataResponse);
                            String inText = shtd.toString();
                            //保存日志
                            if(Constant.IS_SAVE_SOCKET_LOG){
                                try {
                                    vtSocketLogSDDao.save(inText, 0, readDataResponse.get_id(), Thread.currentThread().getName());
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
