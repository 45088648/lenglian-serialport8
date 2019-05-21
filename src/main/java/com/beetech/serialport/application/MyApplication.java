package com.beetech.serialport.application;

import android.app.Application;
import android.app.Service;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.beetech.serialport.activity.RealtimeMonitorActivity;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.bean.SysResponseBean;
import com.beetech.serialport.cockroach.Cockroach;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.dao.AppLogSDDao;
import com.beetech.serialport.dao.GpsDataSDDao;
import com.beetech.serialport.dao.ModuleBufSDDao;
import com.beetech.serialport.dao.QueryConfigRealtimeSDDao;
import com.beetech.serialport.dao.ReadDataRealtimeSDDao;
import com.beetech.serialport.dao.ReadDataSDDao;
import com.beetech.serialport.dao.VtSocketLogSDDao;
import com.beetech.serialport.greendao.dao.DaoMaster;
import com.beetech.serialport.greendao.dao.DaoSession;
import com.beetech.serialport.handler.CrashHandler;
import com.beetech.serialport.service.LocationService;
import com.beetech.serialport.thread.ThreadLocation;
import com.beetech.serialport.thread.ThreadModuleInit;
import com.beetech.serialport.thread.ThreadModuleReceive;
import com.beetech.serialport.thread.ThreadReConnectGtw;
import com.beetech.serialport.thread.ThreadSendGpsData;
import com.beetech.serialport.thread.ThreadSendShtd;
import com.beetech.serialport.utils.APKVersionCodeUtils;
import com.beetech.serialport.utils.MobileInfoUtil;
import com.beetech.serialport.utils.PhoneInfoUtils;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.rscja.deviceapi.Module;
import org.apache.mina.core.session.IoSession;
import org.greenrobot.greendao.query.QueryBuilder;
import com.beetech.serialport.cockroach.ExceptionHandler;

public class MyApplication extends Application {
    private static MyApplication instance;

    public Module module;
    public boolean initResult; //模块上电初始化结果
    public long createTime;
    public long initTime;
    public long lastReadTime;
    public long lastWriteTime;

    public int batteryPercent = 0; // 电量百分比
    public int power = 1;// 0断开  1接通

    public int netWorkType = 0;// 网络类型
    public int signalStrength = 0;// 信号强度

    public String gwId = "00000000";
    public int serialNo = 0;
    public long readDataResponseTime;

    public ThreadModuleReceive threadModuleReceive;
    public ThreadModuleInit threadModuleInit;

    public ThreadSendShtd threadSendShtd;
    public ThreadSendGpsData threadSendGpsData;
    public ThreadReConnectGtw threadReConnectGtw;
    public ThreadLocation threadLocation;
    public RealtimeMonitorActivity realtimeMonitorActivity;

    public IoSession session;
    public SysResponseBean sysResponseBean;
    private PhoneInfoUtils phoneInfoUtils;
    public LocationService locationService;
    public BDLocation location;
    public Vibrator mVibrator;
    public ReadDataRealtime readDataRealtime;

    private CrashHandler appException;

    //db操作对象
    public DaoMaster.DevOpenHelper devOpenHelper;
    public SQLiteDatabase database;
    public DaoMaster daoMaster;
    public DaoSession daoSession;

    public AppLogSDDao appLogSDDao;
    public QueryConfigRealtimeSDDao queryConfigRealtimeSDDao;
    public ReadDataSDDao readDataSDDao;
    public ReadDataRealtimeSDDao readDataRealtimeSDDao;
    public ModuleBufSDDao moduleBufSDDao;
    public GpsDataSDDao gpsDataSDDao;
    public VtSocketLogSDDao vtSocketLogSDDao;

    @Override
    public void onCreate() {
        super.onCreate();

        appException = CrashHandler.getInstance();
        appException.init(getApplicationContext());

        new ANRWatchDog().setANRListener(new ANRWatchDog.ANRListener() {
            @Override
            public void onAppNotResponding(ANRError error) {
                // Handle the error. For example, log it to HockeyApp:
                appException.saveCrashInfo2File(error);
            }
        }).start();
        phoneInfoUtils = new PhoneInfoUtils(this);

        Constant.verName = "APadD"+APKVersionCodeUtils.getVerName(this);
        Constant.imei = MobileInfoUtil.getIMEI(this);
        Constant.phoneNumber = phoneInfoUtils.getNativePhoneNumber();
        Constant.iccid = phoneInfoUtils.getIccid();


        //初始化db操做类
        installDb();
        appLogSDDao = new AppLogSDDao(this);
        queryConfigRealtimeSDDao = new QueryConfigRealtimeSDDao(this);
        readDataSDDao = new ReadDataSDDao(this);
        readDataRealtimeSDDao = new ReadDataRealtimeSDDao(this);
        moduleBufSDDao = new ModuleBufSDDao(this);
        gpsDataSDDao = new GpsDataSDDao(this);
        vtSocketLogSDDao = new VtSocketLogSDDao(this);

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        install();

    }
    private void installDb() {
        devOpenHelper = new DaoMaster.DevOpenHelper(this, Constant.DATABASE_NAME, null);
        //获取可写数据库
        database = devOpenHelper.getWritableDatabase();
        //获取数据库对象
        daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();


        //控制台打印SQL语句日志
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }


    private void install() {
        final Thread.UncaughtExceptionHandler sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler();
        Cockroach.install(this.getApplicationContext(), new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", throwable);
                appException.saveCrashInfo2File(throwable);
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                throwable.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
            }

            @Override
            protected void onEnterSafeMode() {

            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                Thread thread = Looper.getMainLooper().getThread();
                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:" + thread + "<---", e);
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
            }

        });

    }

    public DaoMaster.DevOpenHelper getDevOpenHelper() {
        return devOpenHelper;
    }

    public void setDevOpenHelper(DaoMaster.DevOpenHelper devOpenHelper) {
        this.devOpenHelper = devOpenHelper;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }
}
