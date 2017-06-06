package koolpos.cn.goodproviderservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
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
import koolpos.cn.goodproviderservice.model.ProductCategory;
import koolpos.cn.goodproviderservice.model.ProductRootItem;
import koolpos.cn.goodproviderservice.rx.RetryWithDelay;
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
        String action = "";
        if (intent != null) {
            action = intent.getAction();
        }
        Loger.d("onHandleIntent " + action);
        switch (action) {
            case Action.InitData:
                Observable<BaseResponse<PageDataResponse<ProductRootItem>>> productsObs = getAllProducts();
                Observable<BaseResponse<PageDataResponse<ProductCategory>>> categoryObs = getCategory();
                Observable.zip(productsObs, categoryObs, new BiFunction<BaseResponse<PageDataResponse<ProductRootItem>>, BaseResponse<PageDataResponse<ProductCategory>>, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull BaseResponse<PageDataResponse<ProductRootItem>> allProducts, @NonNull BaseResponse<PageDataResponse<ProductCategory>> allCategory) throws Exception {
                        Loger.d("数据全部下载，开始写数据库");
                        Loger.d("allProducts:"+allProducts.getData().toString());
                        Loger.d("allCategory:"+allCategory.getData().toString());

                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                Loger.d("数据库写成功？"+aBoolean);
                            }
                        });
                break;
        }
    }

    /**
     * getAllProducts from net
     * @return
     */
    private Observable<BaseResponse<PageDataResponse<ProductRootItem>>> getAllProducts() {
        Loger.d("所有商品数据加载中");
        MyApplication.StateNow = new State(StateEnum.Progressing, "所有商品数据加载中");
        return Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Long, StoreTroncellApi>() {
                    @Override
                    public StoreTroncellApi apply(@NonNull Long aLong) throws Exception {
                        return getStoreApiService();
                    }
                }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<PageDataResponse<ProductRootItem>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<PageDataResponse<ProductRootItem>>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                        return storeTroncellApi.getProducts(Constant.TestKey)
                                .map(new Function<BaseResponse<PageDataResponse<ProductRootItem>>, BaseResponse<PageDataResponse<ProductRootItem>>>() {
                                    @Override
                                    public BaseResponse<PageDataResponse<ProductRootItem>> apply(@NonNull BaseResponse<PageDataResponse<ProductRootItem>> response) throws Exception {
                                        if (response.isOK()) {
                                            return response;
                                        } else {
                                            throw new Exception(response.getMessage());
                                        }
                                    }
                                })
                                .retryWhen(RetryWithDelay.crate())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io());
                    }
                });
    }

    /**
     * getCategory from net
     * @return
     */
    private Observable<BaseResponse<PageDataResponse<ProductCategory>>> getCategory() {
        Loger.d("商品类型数据加载中");
        MyApplication.StateNow = new State(StateEnum.Progressing, "商品类型数据加载中");
       return Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Long, StoreTroncellApi>() {
                    @Override
                    public StoreTroncellApi apply(@NonNull Long aLong) throws Exception {
                        return getStoreApiService();
                    }
                }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<PageDataResponse<ProductCategory>>>>() {
            @Override
            public ObservableSource<BaseResponse<PageDataResponse<ProductCategory>>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                return  storeTroncellApi.getProductCategories(Constant.TestKey)
                        .map(new Function<BaseResponse<PageDataResponse<ProductCategory>>, BaseResponse<PageDataResponse<ProductCategory>>>() {
                            @Override
                            public BaseResponse<PageDataResponse<ProductCategory>> apply(@NonNull BaseResponse<PageDataResponse<ProductCategory>> response) throws Exception {
                                if (response.isOK()) {
                                    return response;
                                } else {
                                    throw new Exception(response.getMessage());
                                }
                            }
                        })
                        .retryWhen(RetryWithDelay.crate())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
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
