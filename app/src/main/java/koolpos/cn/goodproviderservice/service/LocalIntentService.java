package koolpos.cn.goodproviderservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.api.ServerApi;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.api.StoreTroncellApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.constans.Constant;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.model.response.BaseResponse;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.model.response.ProductRootItem;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Product;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategory;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategoryDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.SettingDao;
import koolpos.cn.goodproviderservice.rx.RetryWithDelay;
import koolpos.cn.goodproviderservice.util.Loger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/5.
 */

public class LocalIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocalIntentService(String name) {
        super(name);
    }

    public LocalIntentService() {
        super("LocalService");
    }
    private Setting setting;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = "";
        if (intent != null) {
            action = intent.getAction();
        }
        setting = MyApplication.getSetting();
        switch (action) {
            case Action.InitData:
                SrcFileApi.initSrcProperties();
                new ServerApi(setting).initServerData();
                break;
        }
    }


}
