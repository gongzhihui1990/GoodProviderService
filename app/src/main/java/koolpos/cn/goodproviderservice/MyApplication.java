package koolpos.cn.goodproviderservice;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.api.StoreTroncellApiV1;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.constans.Constant;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.model.response.BaseResponseV1;
import koolpos.cn.goodproviderservice.model.response.StoreInfoBean;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoMaster;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.DaoSession;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.SettingDao;
import koolpos.cn.goodproviderservice.service.LocalIntentService;
import koolpos.cn.goodproviderservice.util.Loger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/14.
 */

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;

    private static DaoSession daoSession;
    //    public static final String State_Loading = "State_Loading";
//    public static final String State_Load_Image="State_Load_Image";
//    public static final String State_OK = "State_OK";
    public static State StateNow = new State(StateEnum.Progressing, "尚未启动");

    public static Setting getSetting() {
        Setting deviceSetting = MyApplication.getDaoSession()
                .getSettingDao().queryBuilder()
                .where(SettingDao.Properties.DeviceSn.eq(Constant.SERIAL)).unique();
        if (deviceSetting == null) {
            deviceSetting = new Setting();
            deviceSetting.setDeviceSn(Constant.SERIAL);
        }
        return deviceSetting;
    }

    private boolean ENCRYPTED = false;

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public WifiManager getWifiManager() {
        WifiManager wifiManager = (WifiManager) getBaseContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StateNow = new State(StateEnum.Progressing, "服务启动中");
        Loger.setLogClose(false);
        context = getBaseContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "gp-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Constant.SERIAL = MySPEdit.getInstance().getMacSN();
        appInit();
        querySoftWare();
    }

    private void querySoftWare() {
        Observable.interval(3, 10, TimeUnit.SECONDS)
                .map(new Function<Long, StoreTroncellApiV1>() {
                    @Override
                    public StoreTroncellApiV1 apply(@NonNull Long aLong) throws Exception {
                        return getStoreApiService();
                    }
                }).filter(new Predicate<StoreTroncellApiV1>() {
            @Override
            public boolean test(@NonNull StoreTroncellApiV1 storeTroncellApi) throws Exception {
                if (daoSession == null) {
                    return false;
                }
                Setting setting = daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.DeviceSn.eq(Constant.SERIAL)).unique();
                if (setting == null) {
                    return false;
                }
                if (setting.getDeviceKey() == null) {
                    return false;
                }
                return true;
            }
        }).flatMap(new Function<StoreTroncellApiV1, ObservableSource<BaseResponseV1<StoreInfoBean>>>() {
            @Override
            public ObservableSource<BaseResponseV1<StoreInfoBean>> apply(@NonNull StoreTroncellApiV1 storeTroncellApi) throws Exception {
                Setting setting = daoSession.getSettingDao().queryBuilder().where(SettingDao.Properties.DeviceSn.eq(Constant.SERIAL)).unique();
                String subKey = setting.getDeviceKey();
                Observable<BaseResponseV1<StoreInfoBean>> response = storeTroncellApi.getSoftWare(subKey);
                return response;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseV1<StoreInfoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseV1<StoreInfoBean> storeInfoBeanBaseResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private StoreTroncellApiV1 getStoreApiService() throws Exception {
        okhttp3.OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(StoreTroncellApiV1.HostUrl)
                .client(okBuilder.build())
                //baseUrl:只要符合格式即可
                .addConverterFactory(GsonConverterFactory.create())
                //addConverterFactory:设置把网络请求结果转成我们需要的bean时，使用的工厂类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //addCallAdapterFactory:设置处理请求结果的工厂类
                .build();
        StoreTroncellApiV1 saasApiService = retrofit.create(StoreTroncellApiV1.class);
        return saasApiService;
    }

    public static void appInit() {
        Intent intent = new Intent(getContext(), LocalIntentService.class);
        intent.setAction(Action.InitData);
        getContext().startService(intent);
    }
}
