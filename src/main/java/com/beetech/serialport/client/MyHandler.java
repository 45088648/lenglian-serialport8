package com.beetech.serialport.client;

import android.content.Context;
import android.util.Log;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.GpsDataBean;
import com.beetech.serialport.bean.SysResponseBean;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.GpsDataSDDao;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.dao.SysResponseBeanSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import com.beetech.serialport.utils.BeanPropertiesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class MyHandler extends IoHandlerAdapter {

    private final String TAG = MyHandler.class.getSimpleName();

    private Context mContext;
    private AppLogSDDao appLogSDDao;
    private ReadDataSDDao readDataSDDao;
    private GpsDataSDDao gpsDataSDDao;
    private VtSocketLogSDDao vtSocketLogSDDao;
    private SysResponseBeanSDDao sysResponseBeanSDDao;
    Gson gson = new Gson();

    public MyHandler(Context context) {
        this.mContext = context;

        readDataSDDao = new ReadDataSDDao(mContext);
        gpsDataSDDao = new GpsDataSDDao(mContext);
        vtSocketLogSDDao = new VtSocketLogSDDao(mContext);
        sysResponseBeanSDDao = new SysResponseBeanSDDao(mContext);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用异常："+cause.getMessage());
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Log.d(TAG,  "接收到服务器端消息：" + message.toString());

        String msg = message.toString();
        if(msg != null && !msg.isEmpty()){
            //更新传感器数据
            try {
                String[] msgStrArr = msg.split("\\|");
                if(msgStrArr != null){
                    String cmd = msgStrArr[0];
                    boolean success = Boolean.valueOf(msgStrArr[1]);


                    if("SHTD".equals(cmd)){
                        Long id = Long.valueOf(msgStrArr[2]);
                        //保存日志
                        if(Constant.IS_SAVE_SOCKET_LOG){
                            try {
                                vtSocketLogSDDao.save(msg, 1, id, Thread.currentThread().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if(success){
                            ReadDataResponse readDataResponse = readDataSDDao.queryById(id);
                            if(readDataResponse != null){
                                readDataResponse.setSendFlag(1);
                                readDataSDDao.updateToDB(readDataResponse);
                            }

                        }

                    } else if("gps_data".equals(cmd)){
                        Long id = Long.valueOf(msgStrArr[2]);
                        //保存日志
                        try {
                            vtSocketLogSDDao.save(msg, 1, id, Thread.currentThread().getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(success){
                            GpsDataBean gpsDataBean = gpsDataSDDao.queryById(id);
                            if(gpsDataBean != null){
                                gpsDataBean.setSendFlag(1);
                                gpsDataSDDao.updateToDB(gpsDataBean);
                            }

                        }

                    } else if("sys".equals(cmd)){
                        SysResponseBean responseBean = new SysResponseBean(msgStrArr);
                        SysResponseBean responseBeanFromDb = sysResponseBeanSDDao.queryLast();
                        if(responseBeanFromDb == null){
                            sysResponseBeanSDDao.save(responseBean);
                        } else {
                            BeanPropertiesUtil.copyPropertiesExclude(responseBean, responseBeanFromDb, new String[]{"_id"});
                            sysResponseBeanSDDao.updateToDB(responseBeanFromDb);
                        }
                        ((MyApplication)mContext.getApplicationContext()).sysResponseBean = responseBean;
                    }
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用messageSent：" + message.toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionClosed");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionCreated");
        session.getConfig().setBothIdleTime(ConnectUtils.IDLE_TIME);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionIdle");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        Log.d(TAG, ConnectUtils.stringNowTime() + " : 客户端调用sessionOpened");
    }
}
