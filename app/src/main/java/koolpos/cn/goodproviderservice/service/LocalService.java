package koolpos.cn.goodproviderservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.api.StoreTroncellApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.constans.Constant;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.model.BaseResponse;
import koolpos.cn.goodproviderservice.model.PageDataResponse;
import koolpos.cn.goodproviderservice.model.ProductRootItem;
import koolpos.cn.goodproviderservice.util.Loger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/6/5.
 */

public class LocalService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocalService(String name) {
        super(name);
    }
    public LocalService() {
        super("LocalService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action ="";
        if (intent!=null){
            action = intent.getAction();
        }
        Loger.d("onHandleIntent "+ action);
        switch (action){
            case Action.InitData:
                initData(1);
                break;
        }
    }
    private void initData(final int timeDelay){
        Loger.d("initData");
        MyApplication.StateNow = new State(StateEnum.Progressing,"商品数据加载中");

        Observable.timer(timeDelay,TimeUnit.SECONDS)
                .map(new Function<Long, StoreTroncellApi>() {
                    @Override
                    public StoreTroncellApi apply(@NonNull Long aLong) throws Exception {
                        return getStoreApiService();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<StoreTroncellApi>() {
                    @Override
                    public void accept(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                        storeTroncellApi.getProducts(Constant.TestKey)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(new Observer<BaseResponse<PageDataResponse<ProductRootItem>>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(BaseResponse<PageDataResponse<ProductRootItem>> pageDataResponseBaseResponse) {
                                        Loger.d("pageDataResponseBaseResponse"+pageDataResponseBaseResponse.toString());
                                        if (pageDataResponseBaseResponse.isOK()){

                                        }else {
                                            if (timeDelay>=30){
                                                initData(timeDelay);
                                            }else {
                                                int delay = timeDelay+1;
                                                initData(delay);
                                            }
                                        }
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
                });
    }

    public StoreTroncellApi getStoreApiService() throws Exception {
        okhttp3.OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(5, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(StoreTroncellApi.HostUrl)
                .client(okBuilder.build())
                //baseUrl:只要符合格式即可
                .addConverterFactory(GsonConverterFactory.create())
                //addConverterFactory:设置把网络请求结果转成我们需要的bean时，使用的工厂类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //addCallAdapterFactory:设置处理请求结果的工厂类
                .build();
        StoreTroncellApi saasApiService = retrofit.create(StoreTroncellApi.class);
        return saasApiService;
    }
}
