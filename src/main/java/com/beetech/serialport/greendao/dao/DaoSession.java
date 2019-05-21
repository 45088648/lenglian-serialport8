package com.beetech.serialport.greendao.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.bean.ReadDataRealtime;
import com.beetech.serialport.bean.AppLog;
import com.beetech.serialport.bean.GpsDataBean;
import com.beetech.serialport.bean.ModuleBuf;
import com.beetech.serialport.bean.QueryConfigRealtime;
import com.beetech.serialport.bean.vt.VtSocketLog;
import com.beetech.serialport.bean.SysResponseBean;

import com.beetech.serialport.greendao.dao.ReadDataResponseDao;
import com.beetech.serialport.greendao.dao.ReadDataRealtimeDao;
import com.beetech.serialport.greendao.dao.AppLogDao;
import com.beetech.serialport.greendao.dao.GpsDataBeanDao;
import com.beetech.serialport.greendao.dao.ModuleBufDao;
import com.beetech.serialport.greendao.dao.QueryConfigRealtimeDao;
import com.beetech.serialport.greendao.dao.VtSocketLogDao;
import com.beetech.serialport.greendao.dao.SysResponseBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig readDataResponseDaoConfig;
    private final DaoConfig readDataRealtimeDaoConfig;
    private final DaoConfig appLogDaoConfig;
    private final DaoConfig gpsDataBeanDaoConfig;
    private final DaoConfig moduleBufDaoConfig;
    private final DaoConfig queryConfigRealtimeDaoConfig;
    private final DaoConfig vtSocketLogDaoConfig;
    private final DaoConfig sysResponseBeanDaoConfig;

    private final ReadDataResponseDao readDataResponseDao;
    private final ReadDataRealtimeDao readDataRealtimeDao;
    private final AppLogDao appLogDao;
    private final GpsDataBeanDao gpsDataBeanDao;
    private final ModuleBufDao moduleBufDao;
    private final QueryConfigRealtimeDao queryConfigRealtimeDao;
    private final VtSocketLogDao vtSocketLogDao;
    private final SysResponseBeanDao sysResponseBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        readDataResponseDaoConfig = daoConfigMap.get(ReadDataResponseDao.class).clone();
        readDataResponseDaoConfig.initIdentityScope(type);

        readDataRealtimeDaoConfig = daoConfigMap.get(ReadDataRealtimeDao.class).clone();
        readDataRealtimeDaoConfig.initIdentityScope(type);

        appLogDaoConfig = daoConfigMap.get(AppLogDao.class).clone();
        appLogDaoConfig.initIdentityScope(type);

        gpsDataBeanDaoConfig = daoConfigMap.get(GpsDataBeanDao.class).clone();
        gpsDataBeanDaoConfig.initIdentityScope(type);

        moduleBufDaoConfig = daoConfigMap.get(ModuleBufDao.class).clone();
        moduleBufDaoConfig.initIdentityScope(type);

        queryConfigRealtimeDaoConfig = daoConfigMap.get(QueryConfigRealtimeDao.class).clone();
        queryConfigRealtimeDaoConfig.initIdentityScope(type);

        vtSocketLogDaoConfig = daoConfigMap.get(VtSocketLogDao.class).clone();
        vtSocketLogDaoConfig.initIdentityScope(type);

        sysResponseBeanDaoConfig = daoConfigMap.get(SysResponseBeanDao.class).clone();
        sysResponseBeanDaoConfig.initIdentityScope(type);

        readDataResponseDao = new ReadDataResponseDao(readDataResponseDaoConfig, this);
        readDataRealtimeDao = new ReadDataRealtimeDao(readDataRealtimeDaoConfig, this);
        appLogDao = new AppLogDao(appLogDaoConfig, this);
        gpsDataBeanDao = new GpsDataBeanDao(gpsDataBeanDaoConfig, this);
        moduleBufDao = new ModuleBufDao(moduleBufDaoConfig, this);
        queryConfigRealtimeDao = new QueryConfigRealtimeDao(queryConfigRealtimeDaoConfig, this);
        vtSocketLogDao = new VtSocketLogDao(vtSocketLogDaoConfig, this);
        sysResponseBeanDao = new SysResponseBeanDao(sysResponseBeanDaoConfig, this);

        registerDao(ReadDataResponse.class, readDataResponseDao);
        registerDao(ReadDataRealtime.class, readDataRealtimeDao);
        registerDao(AppLog.class, appLogDao);
        registerDao(GpsDataBean.class, gpsDataBeanDao);
        registerDao(ModuleBuf.class, moduleBufDao);
        registerDao(QueryConfigRealtime.class, queryConfigRealtimeDao);
        registerDao(VtSocketLog.class, vtSocketLogDao);
        registerDao(SysResponseBean.class, sysResponseBeanDao);
    }
    
    public void clear() {
        readDataResponseDaoConfig.clearIdentityScope();
        readDataRealtimeDaoConfig.clearIdentityScope();
        appLogDaoConfig.clearIdentityScope();
        gpsDataBeanDaoConfig.clearIdentityScope();
        moduleBufDaoConfig.clearIdentityScope();
        queryConfigRealtimeDaoConfig.clearIdentityScope();
        vtSocketLogDaoConfig.clearIdentityScope();
        sysResponseBeanDaoConfig.clearIdentityScope();
    }

    public ReadDataResponseDao getReadDataResponseDao() {
        return readDataResponseDao;
    }

    public ReadDataRealtimeDao getReadDataRealtimeDao() {
        return readDataRealtimeDao;
    }

    public AppLogDao getAppLogDao() {
        return appLogDao;
    }

    public GpsDataBeanDao getGpsDataBeanDao() {
        return gpsDataBeanDao;
    }

    public ModuleBufDao getModuleBufDao() {
        return moduleBufDao;
    }

    public QueryConfigRealtimeDao getQueryConfigRealtimeDao() {
        return queryConfigRealtimeDao;
    }

    public VtSocketLogDao getVtSocketLogDao() {
        return vtSocketLogDao;
    }

    public SysResponseBeanDao getSysResponseBeanDao() {
        return sysResponseBeanDao;
    }

}