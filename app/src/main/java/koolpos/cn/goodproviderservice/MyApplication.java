package koolpos.cn.goodproviderservice;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.greenrobot.greendao.database.Database;

import koolpos.cn.goodproviderservice.api.LocalApi;
import koolpos.cn.goodproviderservice.api.LocalTestApi;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoMaster;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoSession;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.SettingDao;
import koolpos.cn.goodproviderservice.service.LocalIntentService;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/5/14.
 */

public class MyApplication extends Application {
    private static Context context;
    private static DaoSession daoSession;
//    public static final String State_Loading = "State_Loading";
//    public static final String State_Load_Image="State_Load_Image";
//    public static final String State_OK = "State_OK";
    public static State StateNow = new State(StateEnum.Progressing,"尚未启动");

    public static Setting getSetting(){
        Setting deviceSetting = MyApplication.getDaoSession()
                .getSettingDao().queryBuilder()
                .where(SettingDao.Properties.DeviceSn.eq(Build.SERIAL)).unique();
        if (deviceSetting==null){
            deviceSetting=new Setting();
            deviceSetting.setDeviceSn(Build.SERIAL);
        }
        return deviceSetting;
    }
    private boolean ENCRYPTED=false;
    public static Context getContext() {
        return context;
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        StateNow = new State(StateEnum.Progressing,"服务启动中");
        Loger.setLogClose(false);
        context=getBaseContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "gp-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        appInit();
    }

    private void appInit(){
        Intent intent = new Intent(getContext(), LocalIntentService.class);
        intent.setAction(Action.InitData);
        startService(intent);
    }
}
