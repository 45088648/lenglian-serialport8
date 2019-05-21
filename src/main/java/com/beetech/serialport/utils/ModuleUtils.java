package com.beetech.serialport.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.ModuleBufSDDao;
import com.rscja.deviceapi.DeviceInfo;
import com.rscja.deviceapi.Module;
import com.rscja.deviceapi.exception.ConfigurationException;

public class ModuleUtils {
    private final static String TAG = ModuleUtils.class.getSimpleName();

    private Context mContext;
    private MyApplication myApp;
    private AppLogSDDao appLogSDDao;
    private ModuleBufSDDao moduleBufSDDao;

    public ModuleUtils(Context mContext){
        this.mContext = mContext;
        this.appLogSDDao = new AppLogSDDao(mContext);
        this.moduleBufSDDao = new ModuleBufSDDao(mContext);
        myApp = (MyApplication) mContext.getApplicationContext();
    }

    public Module getInstance(){
        Module module = myApp.module;
        try {
            if(module == null){
                module = Module.getInstance();
                myApp.module = module;
                myApp.createTime = System.currentTimeMillis();
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return module;
    }

    public boolean init() {
        boolean result = false;
        String resultMsg = "";
        try {
            Module module = getInstance();
            int deviceType = DeviceInfo.getDeviceType();
            Log.d(TAG, "deviceType = "+deviceType);
            boolean freeResult = module.free();
            Thread.sleep(1000);
            Log.d(TAG, "init前free， freeResult = "+freeResult);
            result = module.init(Constant.module, Constant.baudrate); // 设备上电

            myApp.initTime = System.currentTimeMillis();
            myApp.initResult = result;
            resultMsg = result ? "成功" : "失败";

        } catch (Exception e) {
            e.printStackTrace();
            resultMsg = "异常："+e.getMessage();
        }

        String msgContent = "模块上电初始化 " + resultMsg;
        Log.d(TAG, msgContent);
        appLogSDDao.save(mContext.getClass().getSimpleName()+msgContent);

        Message msg = new Message();
        msg.obj = msgContent;
        handlerToast.sendMessage(msg);

        return result;
    }

    public boolean free(){
        boolean result = false;

        Module module = getInstance();
        String resultMsg = "";
        try {
            result = module.free();
            resultMsg = result ? "成功" : "失败";
            myApp.initResult = false;
        } catch (Exception e) {
            e.printStackTrace();
            resultMsg = "异常："+ e.getMessage();
        }

        String msgContent = "释放模块 " + resultMsg;
        Log.d(TAG, msgContent);
        appLogSDDao.save(mContext.getClass().getSimpleName()+msgContent);

        Message msg = new Message();
        msg.obj = msgContent;
        handlerToast.sendMessage(msg);
        return result;
    }

    private Handler handlerToast = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Object toastMsg = msg.obj;
            if(toastMsg != null){
                Toast.makeText(mContext.getApplicationContext(), toastMsg.toString(), Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };
}
