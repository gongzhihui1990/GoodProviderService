package koolpos.cn.goodproviderservice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.greendao.database.Database;

import koolpos.cn.goodproviderservice.api.LocalTestApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoMaster;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoSession;
import koolpos.cn.goodproviderservice.service.LocalService;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/5/14.
 */

public class MyApplication extends Application {
    private static Context context;
    private static DaoSession daoSession;
    public static final String State_Loading = "State_Loading";
    public static final String State_Load_Image="State_Load_Image";
    public static final String State_OK = "State_OK";
    public static  String State = State_Loading;

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
        context=getBaseContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "gp-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Intent intent = new Intent(getContext(), LocalService.class);
        intent.setAction(Action.InitData);
        startService(intent);
    }
}
