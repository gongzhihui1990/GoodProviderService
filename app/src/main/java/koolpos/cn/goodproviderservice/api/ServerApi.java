package koolpos.cn.goodproviderservice.api;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.model.response.AdBean;
import koolpos.cn.goodproviderservice.model.response.BaseResponse;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.model.response.ProductRootItem;
import koolpos.cn.goodproviderservice.model.response.StoreInfoBean;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.AdDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Product;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategory;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategoryDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.util.Loger;
import koolpos.cn.goodproviderservice.util.SimpleToast;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static koolpos.cn.goodproviderservice.constans.Action.State_Ok;
import static koolpos.cn.goodproviderservice.constans.Action.State_Update;

/**
 * Created by caroline on 2017/6/11.
 */

public class ServerApi {


    private Setting setting;

    public ServerApi(Setting setting){
        this.setting=setting;
    }
    private boolean loadFromCache=false;
    public void initServerData() {
        Loger.i("数据开始加载");
        if (setting.getLoadCacheFirst()){
            Loger.i("数据开始加载-文件缓存");
            initServerDataByCacheFile();
        }else {
            Loger.i("数据开始加载-网络请求");
            initServerDataByNet();
        }
    }

    private void initServerDataByCacheFile() {
        loadFromCache=true;
        Observable<BaseResponse<PageDataResponse<ProductRootItem>>> productsObs = getAllProductsByFile();
        Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> categoryObs = getCategoryByFile();
        Observable<BaseResponse<PageDataResponse<AdBean>>> adObs = getAdsByFile();
        Observable<Boolean> loadFromCacheFile = Observable.just(true);
        Observable.zip(productsObs, categoryObs, adObs,loadFromCacheFile, getPersistentDataFunction()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(getPersistentDataObserver());
    }
    private Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> getCategoryByFile() {
        return Observable.just(SrcFileApi.productCategoryFileName)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String fileName) throws Exception {
                        return  SrcFileApi.getJsonFileToString(fileName);
                    }
                }).map(new Function<String, BaseResponse<PageDataResponse<ProductCategoryBean>>>() {
            @Override
            public BaseResponse<PageDataResponse<ProductCategoryBean>> apply(@NonNull String jsonStr) throws Exception {
                java.lang.reflect.Type token = new TypeToken<BaseResponse<PageDataResponse<ProductCategoryBean>>>() {}.getType();
                return new Gson().fromJson(jsonStr,token);
            }
        });
    }

    private Observable<BaseResponse<PageDataResponse<AdBean>>> getAdsByFile() {
        return Observable.just(SrcFileApi.adFileName)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String fileName) throws Exception {
                        return  SrcFileApi.getJsonFileToString(fileName);
                    }
                }).map(new Function<String, BaseResponse<PageDataResponse<AdBean>>>() {
                    @Override
                    public BaseResponse<PageDataResponse<AdBean>> apply(@NonNull String jsonStr) throws Exception {
                        java.lang.reflect.Type token = new TypeToken<BaseResponse<PageDataResponse<AdBean>> >() {}.getType();
                        return new Gson().fromJson(jsonStr,token);
                    }
                });
    }
    private Observable<BaseResponse<PageDataResponse<ProductRootItem>>> getAllProductsByFile() {
        return Observable.just(SrcFileApi.productFileName)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String fileName) throws Exception {
                        return  SrcFileApi.getJsonFileToString(fileName);
                    }
                }).map(new Function<String, BaseResponse<PageDataResponse<ProductRootItem>>>() {
                    @Override
                    public BaseResponse<PageDataResponse<ProductRootItem>> apply(@NonNull String jsonStr) throws Exception {
                        java.lang.reflect.Type token = new TypeToken<BaseResponse<PageDataResponse<ProductRootItem>>>() {}.getType();
                        return new Gson().fromJson(jsonStr,token);
                    }
                });
    }

    private  Function4 <BaseResponse<PageDataResponse<ProductRootItem>>,
            BaseResponse<PageDataResponse<ProductCategoryBean>>,
            BaseResponse<PageDataResponse<AdBean>>,Boolean, Boolean> getPersistentDataFunction(){
        return new Function4<BaseResponse<PageDataResponse<ProductRootItem>>,
                BaseResponse<PageDataResponse<ProductCategoryBean>>,
                BaseResponse<PageDataResponse<AdBean>>,Boolean, Boolean>() {
            @Override
            public Boolean apply(@NonNull BaseResponse<PageDataResponse<ProductRootItem>> allProducts,
                                 @NonNull BaseResponse<PageDataResponse<ProductCategoryBean>> allCategory,
                                 @NonNull BaseResponse<PageDataResponse<AdBean>> addAds,
                                 @NonNull Boolean loadFromCacheFile) throws Exception {
                Loger.d("数据全部加载,是否loadFromCacheFile："+loadFromCacheFile);

                if (!loadFromCacheFile){
                    Loger.d("开始写本地缓存任务");
                    try{
                        SrcFileApi.save(allProducts,allCategory,addAds);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                Loger.d("刷新数据库");

                //类型表
                List<ProductCategoryBean> categories = allCategory.getData().getData();
                if (categories.size() != 0) {
                    ProductCategoryDao productCategoryDao = MyApplication.getDaoSession().getProductCategoryDao();
                    productCategoryDao.deleteAll();//清除旧的数据
                    for (ProductCategoryBean category : categories) {
                        category.insert(productCategoryDao);//写入新的数据
                    }
                }
                //展示品表
                List<ProductRootItem> products = allProducts.getData().getData();
                if (products.size() != 0) {
                    ProductDao productDao = MyApplication.getDaoSession().getProductDao();
                    productDao.deleteAll();
                    for (ProductRootItem product : products) {
                        if (product.getP_ProductCategories().size()!=0){
                            product.insert(productDao);//写入新的数据
                        }else {
                            Loger.e("error src---"+product.toString()+"-"+product.getP_ProductCategories().size());
                        }
                    }
                }
                //广告表
                List<AdBean> ads = addAds.getData().getData();
                if (products.size() != 0) {
                    AdDao adDao = MyApplication.getDaoSession().getAdDao();
                    adDao.deleteAll();
                    for (AdBean adBean : ads) {
                        adBean.insert(adDao);//写入新的数据
                    }
                }
                return true;
            }
        };
    }
    private Observer<Boolean> getPersistentDataObserver(){
        return new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }
            @Override
            public void onNext(Boolean aBoolean) {
                Loger.d("数据库写成功？" + aBoolean);
                ProductDao productDao = MyApplication.getDaoSession().getProductDao();
                Loger.d("所有商品数量:" + productDao.queryBuilder().count());
                MyApplication.StateNow = new State(StateEnum.Progressing,"本地加载中");
                for (Product product : productDao.queryBuilder().list()) {
                    List<Integer> cats = product.getProductCategoryIDs();
                    if (cats != null && cats.size() != 0) {
                        String catStr = "";
                        for (Integer cat : cats) {
                            catStr += "," + cat;
                        }
                    } else {
                        Loger.e("product:" + product.getId() + "-no cate");
                    }
                }
                ProductCategoryDao productCategoryDao = MyApplication.getDaoSession().getProductCategoryDao();
                Loger.d("所有类型数量:" + productCategoryDao.queryBuilder().count());
                List<ProductCategory> categories = productCategoryDao.queryBuilder().list();
                for (ProductCategory category : categories) {

                    if (category.getParentCategoryCode() == null) {
                        List<Integer> categoryIds=new ArrayList<Integer>();
                        int categoryId = category.getCategoryId();
                        //LocalApi.getProducts(categoryId);
                        categoryIds.add(categoryId);
                        Loger.d("categoryId:" + categoryId);
                        List<ProductCategory> categoryChildList = productCategoryDao.queryBuilder().where(ProductCategoryDao.Properties.ParentCategoryId.eq(categoryId)).list();
                        if (categoryChildList != null && categoryChildList.size() != 0) {
                            String cd = "";
                            for (ProductCategory childCategory : categoryChildList) {
                                cd += "," + childCategory.getCategoryId();
                                categoryIds.add(childCategory.getCategoryId());
                            }
                        }
                        List<Product> allProduct= productDao.queryBuilder().list();
                        List<Product> inCategoryProducts = new ArrayList<Product>();

                        for (Product product:allProduct){
                            for (int id: categoryIds) {
                                if (product.getProductCategoryIDs()!=null&&
                                        product.getProductCategoryIDs().contains(id)){
                                    inCategoryProducts.add(product);
                                    break;
                                }
                            }
                        }
                    }
                }
                //应用初始化完成
                MyApplication.StateNow=new State(StateEnum.Ok,"");
                MyApplication.getContext().sendBroadcast(new Intent(State_Ok));
                MyApplication.getContext().sendBroadcast(new Intent(State_Update));
                Observable.just(true)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean o) throws Exception {
                                SimpleToast.toast("服务初始化完成");
                            }
                        });
                Loger.d("服务初始化完成");
            }

            @Override
            public void onError(final Throwable e) {
                e.printStackTrace();
                if (!loadFromCache){
                    initServerDataByCacheFile();
                    Observable.just(true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(@NonNull Boolean o) throws Exception {
                                    SimpleToast.toast("网络加载失败"+e.getMessage()+"，将进行本地数据加载");
                                }
                            });
                }else{
                    Observable.just(e)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable t) throws Exception {
                                    SimpleToast.toast("加载失败"+t.getMessage().toString());
                                }
                            });
                }

                //Loger.e("error---"+e.getClass().getSimpleName());
                //MyApplication.StateNow=new State(StateEnum.Error,e.getClass().getSimpleName()+":"+e.getMessage());
                //e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }

        };
    }
    private void initServerDataByNet(){
        loadFromCache=false;
        Observable<BaseResponse<PageDataResponse<ProductRootItem>>> productsObs = getAllProductsFromNet();
        Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> categoryObs = getCategoryFromNet();
        Observable<BaseResponse<PageDataResponse<AdBean>>> adObs = getAdsFromNet();
        Observable<Boolean> loadFromCacheFile = Observable.just(false);
        Observable.zip(productsObs, categoryObs, adObs,loadFromCacheFile, getPersistentDataFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(getPersistentDataObserver());
    }
    /**
     * getAllProductsFromNet from net
     *
     * @return
     */
    private Observable<BaseResponse<PageDataResponse<ProductRootItem>>> getAllProductsFromNet() {
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
                        return storeTroncellApi.getProducts(setting.getDeviceKey())
                                .map(new Function<BaseResponse<PageDataResponse<ProductRootItem>>, BaseResponse<PageDataResponse<ProductRootItem>>>() {
                                    @Override
                                    public BaseResponse<PageDataResponse<ProductRootItem>> apply(@NonNull BaseResponse<PageDataResponse<ProductRootItem>> response) throws Exception {
                                        if (response.isOK()) {
                                            Loger.d("getAllProductsFromNet:"+response.toString());
                                            return response;
                                        } else {
                                            throw new Exception(response.getMessage());
                                        }
                                    }
                                });
//                                .retryWhen(RetryWithDelay.crate());
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.io());
                    }
                });
    }

    public Observable<BaseResponse<StoreInfoBean>> getDeviceInfoObservable() {
        Loger.d("硬件信息查询中");
        MyApplication.StateNow = new State(StateEnum.Progressing, "硬件信息查询中");
        return Observable.just("查询")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, StoreTroncellApi>() {
                    @Override
                    public StoreTroncellApi apply(@NonNull String key) throws Exception {
                        return getStoreApiService();
                    }
                }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<StoreInfoBean>>>() {
                    @Override
                    public ObservableSource<BaseResponse<StoreInfoBean>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                        return storeTroncellApi.getDeviceInfo(setting.getDeviceKey())
                                .map(new Function<BaseResponse<StoreInfoBean>, BaseResponse<StoreInfoBean>>() {
                                    @Override
                                    public BaseResponse<StoreInfoBean> apply(@NonNull BaseResponse<StoreInfoBean> response) throws Exception {
                                        if (response.isOK()) {
                                            return response;
                                        } else {
                                            throw new Exception(response.getMessage());
                                        }
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                });
    }

    public Observable<BaseResponse<StoreInfoBean>> registerDeviceObservable(final Map<String,Object> requestMap){
        {
            Loger.d("注册机具中");
            MyApplication.StateNow = new State(StateEnum.Progressing, "注册机具中");
            return Observable.just("注册")
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Function<String, StoreTroncellApi>() {
                        @Override
                        public StoreTroncellApi apply(@NonNull String body) throws Exception {
                            return getStoreApiService();
                        }
                    }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<StoreInfoBean>>>() {
                        @Override
                        public ObservableSource<BaseResponse<StoreInfoBean>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                            Loger.d("key:"+setting.getDeviceKey());
                            Loger.d("requestMap size:"+requestMap.size());
                            return storeTroncellApi.register(setting.getDeviceKey(),requestMap)
                                    .map(new Function<BaseResponse<StoreInfoBean>, BaseResponse<StoreInfoBean>>() {
                                        @Override
                                        public BaseResponse<StoreInfoBean> apply(@NonNull BaseResponse<StoreInfoBean> response) throws Exception {
                                            if (response.isOK()) {
                                                return response;
                                            } else {
                                                Loger.e("response:"+response);
                                                throw new Exception(response.getMessage());
                                            }
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread());
                        }
                    });
        }
    }
    /**
     * getCategoryFromNet from net
     *
     * @return
     */
    private Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> getCategoryFromNet() {
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
                }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<PageDataResponse<ProductCategoryBean>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<PageDataResponse<ProductCategoryBean>>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                        return storeTroncellApi.getProductCategories(setting.getDeviceKey())
                                .map(new Function<BaseResponse<PageDataResponse<ProductCategoryBean>>, BaseResponse<PageDataResponse<ProductCategoryBean>>>() {
                                    @Override
                                    public BaseResponse<PageDataResponse<ProductCategoryBean>> apply(@NonNull BaseResponse<PageDataResponse<ProductCategoryBean>> response) throws Exception {
                                        if (response.isOK()) {
                                            return response;
                                        } else {
                                            throw new Exception(response.getMessage());
                                        }
                                    }
                                });
//                                .retryWhen(RetryWithDelay.crate());
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.io());
                    }
                });
    }
    private Observable<BaseResponse<PageDataResponse<AdBean>>> getAdsFromNet() {
        Loger.d("广告数据加载中");
        MyApplication.StateNow = new State(StateEnum.Progressing, "广告数据加载中");
        return Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Long, StoreTroncellApi>() {
                    @Override
                    public StoreTroncellApi apply(@NonNull Long aLong) throws Exception {
                        return getStoreApiService();
                    }
                }).flatMap(new Function<StoreTroncellApi, ObservableSource<BaseResponse<PageDataResponse<AdBean>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<PageDataResponse<AdBean>>> apply(@NonNull StoreTroncellApi storeTroncellApi) throws Exception {
                        return storeTroncellApi.getAds(setting.getDeviceKey())
                                .map(new Function<BaseResponse<PageDataResponse<AdBean>>, BaseResponse<PageDataResponse<AdBean>>>() {
                                    @Override
                                    public BaseResponse<PageDataResponse<AdBean>> apply(@NonNull BaseResponse<PageDataResponse<AdBean>> response) throws Exception {
                                        if (response.isOK()) {
                                            Loger.d("re:"+response.toString());
                                            return response;
                                        } else {
                                            throw new Exception(response.getMessage());
                                        }
                                    }
                                });
//                                .retryWhen(RetryWithDelay.crate());
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.io());
                    }
                });
    }

    private StoreTroncellApi getStoreApiService() throws Exception {
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
