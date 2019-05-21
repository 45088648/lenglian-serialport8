package com.beetech.serialport.thread;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.client.ClientConnectManager;
import com.beetech.serialport.bean.vt.VtStateRequestBeanUtils;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

/**
 * 发送状态数据到VT网关
 */
public class ThreadSendVtState extends Thread {

    private final static String TAG = ThreadSendVtState.class.getSimpleName();
    public final static int INTERVAL = 1000*60*5;
    public static int NUM = 0;
    public static long instanceTime;
    public static long runTime;

    private static ThreadSendVtState instance;

    public synchronized static ThreadSendVtState getInstance() {
        if (null == instance) {
            synchronized(ThreadSendVtState.class){
                instance = new ThreadSendVtState();
                instanceTime = System.currentTimeMillis();
            }
        }
        return instance;
    }

    private Context mContext;
    private VtSocketLogSDDao vtSocketLogSDDao;
    private MyApplication myApp;
    private VtStateRequestBeanUtils vtStateRequestBeanUtils;

    public void init(Context mContext){
        this.mContext = mContext;
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);

        vtStateRequestBeanUtils = new VtStateRequestBeanUtils(mContext);
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
        while(!isInterrupted()){
            runTime = System.currentTimeMillis();
            Log.d(TAG, " run " + (NUM++));
            try {
                IoSession mSession = myApp.session;
                if(mSession != null && mSession.isConnected()){
                    String inText = vtStateRequestBeanUtils.getMessage();
                    if(inText != null ){

                        //保存日志
                        if(Constant.IS_SAVE_SOCKET_LOG) {
                            try {
                                vtSocketLogSDDao.save(inText, 0, 0L, Thread.currentThread().getName());
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

                } else {
                    ClientConnectManager.getInstance(myApp).rePeatConnect();
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
