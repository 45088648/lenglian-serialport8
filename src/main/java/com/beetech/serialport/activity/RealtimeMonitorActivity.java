package com.beetech.serialport.activity;

import android.app.KeyguardManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.beetech.serialport.R;
import com.beetech.serialport.application.MyApplication;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.constant.Constant;
import com.beetech.serialport.service.JobProtectService;
import com.beetech.serialport.service.ModuleService;
import com.beetech.serialport.service.PlayerMusicService;
import com.beetech.serialport.service.RemoteService;
import com.beetech.serialport.utils.ServiceAliveUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class RealtimeMonitorActivity extends AppCompatActivity {
    private final static String TAG = RealtimeMonitorActivity.class.getSimpleName();



    @ViewInject(R.id.readDataRealtimeLl)
    LinearLayout readDataRealtimeLl;
    @ViewInject(R.id.tvSensorId)
    TextView tvSensorId;

    @ViewInject(R.id.tvTemp)
    TextView tvTemp;
    @ViewInject(R.id.tvRh)
    TextView tvRh;
    @ViewInject(R.id.tvTemp1)
    TextView tvTemp1;
    @ViewInject(R.id.tvTemp2)
    TextView tvTemp2;
    @ViewInject(R.id.tvTemp3)
    TextView tvTemp3;
    @ViewInject(R.id.tvTemp4)
    TextView tvTemp4;
    @ViewInject(R.id.tvTemp5)
    TextView tvTemp5;
    @ViewInject(R.id.tvTemp6)
    TextView tvTemp6;
    @ViewInject(R.id.tvTemp7)
    TextView tvTemp7;

    @ViewInject(R.id.tvRh1)
    TextView tvRh1;
    @ViewInject(R.id.tvRh2)
    TextView tvRh2;
    @ViewInject(R.id.tvRh3)
    TextView tvRh3;
    @ViewInject(R.id.tvRh4)
    TextView tvRh4;
    @ViewInject(R.id.tvRh5)
    TextView tvRh5;
    @ViewInject(R.id.tvRh6)
    TextView tvRh6;
    @ViewInject(R.id.tvRh7)
    TextView tvRh7;

    @ViewInject(R.id.tvSensorDataTime)
    TextView tvSensorDataTime;

    //定位
    @ViewInject(R.id.bmapView)
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MyApplication myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_realtime_monitor);
        ViewUtils.inject(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD, WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);//开机不锁屏 设置
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //屏幕唤醒
        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "StartupReceiver:");//最后的参数是LogCat里用的Tag
        wl.acquire();

        //屏幕解锁
        KeyguardManager km= (KeyguardManager) getBaseContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("StartupReceiver");//参数是LogCat里用的Tag
        kl.disableKeyguard();

        startModuleService();

        //定位
        myApp = (MyApplication) getApplication();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));

        handlerRefresh.postDelayed(runnableRefresh, 0);

        myApp.realtimeMonitorActivity = this;

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Log.e(TAG,"未捕获异常", e);
                e.printStackTrace();
            }
        });

        myApp.appLogSDDao.save(TAG + " onCreate");
    }

    public void startModuleService(){
        /*如果服务正在运行，直接return*/
        if (!ServiceAliveUtils.isServiceRunning(this,"com.beetech.serialport.service.ModuleService")){
            /* 启动串口通信服务 */
            startService(new Intent(this, ModuleService.class));

            //开启守护线程 aidl
            startService(new Intent(this, RemoteService.class));

            //循环播放一段无声音频
            Intent intent = new Intent(this,PlayerMusicService.class);
            startService(intent);

            //创建唤醒定时任务
            try {
                //获取JobScheduler 他是一种系统服务
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
                JobInfo.Builder builder = new JobInfo.Builder(1024, new ComponentName(getPackageName(), JobProtectService.class.getName()));

                if(Build.VERSION.SDK_INT >= 24) {
                    //android N之后时间必须在15分钟以上
                    //            builder.setMinimumLatency(10 * 1000);
                    builder.setPeriodic(1000 * 60 * 15);
                }else{
                    builder.setPeriodic(1000 * 60 * 15);
                }
                builder.setPersisted(true);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
                int schedule = jobScheduler.schedule(builder.build());
                if (schedule <= 0) {
                    Log.w(TAG, "schedule error！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        appLogSDDao.save(TAG+" onResume");

        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        new ModuleUtils(this).free(); //我们的应用要一直运行
        myApp.appLogSDDao.save(TAG+" onDestroy");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.setClass(RealtimeMonitorActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(RealtimeMonitorActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //定时刷新
    private int intervalRefresh = 1000*5;
    private Handler handlerRefresh = new Handler(){};
    private Runnable runnableRefresh = new Runnable() {
        @Override
        public void run() {
            try {
                locRefresh();
                readDataRealtimeRefresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
            handlerRefresh.postDelayed(this, intervalRefresh);
        }
    };

    private void locRefresh(){
        Log.d(TAG, "locRefresh");

        BDLocation location = myApp.location;
        if (location == null) {
           return;
        }
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.loc);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        // 在地图上添加Marker，并显示
        mBaiduMap.clear();
        mBaiduMap.addOverlay(option);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));

        //画圆
//                    OverlayOptions ooCircle = new CircleOptions().fillColor(0x384d73b3)
//                            .center(point).stroke(new Stroke(3, 0x784d73b3))
//                            .radius(Float.valueOf(location.getRadius()).intValue());
//                    mBaiduMap.addOverlay(ooCircle);
    }

    private void readDataRealtimeRefresh(){
        Log.d(TAG, "readDataRealtimeRefresh");

        ReadDataRealtime readDataRealtime = myApp.readDataRealtime;
        if(readDataRealtime == null){
            return;
        }

        tvSensorId.setText(readDataRealtime.getSensorId());
        tvTemp.setText(readDataRealtime.getTemp()+"℃");
        tvTemp1.setText(readDataRealtime.getTemp1()+"℃");
        tvTemp2.setText(readDataRealtime.getTemp2()+"℃");
        tvTemp3.setText(readDataRealtime.getTemp3()+"℃");
        tvTemp4.setText(readDataRealtime.getTemp4()+"℃");
        tvTemp5.setText(readDataRealtime.getTemp5()+"℃");
        tvTemp6.setText(readDataRealtime.getTemp6()+"℃");
        tvTemp7.setText(readDataRealtime.getTemp7()+"℃");

        tvRh.setText(readDataRealtime.getRh()+"%");
        tvRh1.setText(readDataRealtime.getRh1()+"%");
        tvRh2.setText(readDataRealtime.getRh2()+"%");
        tvRh3.setText(readDataRealtime.getRh3()+"%");
        tvRh4.setText(readDataRealtime.getRh4()+"%");
        tvRh5.setText(readDataRealtime.getRh5()+"%");
        tvRh6.setText(readDataRealtime.getRh6()+"%");
        tvRh7.setText(readDataRealtime.getRh7()+"%");

        tvSensorDataTime.setText(Constant.sdf.format(readDataRealtime.getSensorDataTime()));
    }

    @OnClick(R.id.readDataRealtimeLl)
    public void readDataRealtimeLl_onClick(View v) {
        ReadDataRealtime readDataRealtime = myApp.readDataRealtime;
        Intent intent=new  Intent(RealtimeMonitorActivity.this, TempLineActivity.class);
        intent.putExtra("sensorId",readDataRealtime.getSensorId());
        startActivity(intent);
    }
}