package koolpos.cn.goodproviderservice.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
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
import koolpos.cn.goodproviderservice.rx.RetryWithDelay;
import koolpos.cn.goodproviderservice.util.Loger;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by caroline on 2017/6/11.
 */

public class ServerApi {


    private Setting setting;
    public ServerApi(Setting setting){
        this.setting=setting;
    }
    public void initServerData() {
        if (setting.getLoadCacheFirst()){
            initServerDataByCacheFile();
        }else {
            initServerDataByNet();
        }
    }

    private void initServerDataByCacheFile() {
        //TODO
    }

    private void initServerDataByNet(){
        Observable<BaseResponse<PageDataResponse<ProductRootItem>>> productsObs = getAllProducts();
        Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> categoryObs = getCategory();
        Observable.zip(productsObs, categoryObs, new BiFunction<BaseResponse<PageDataResponse<ProductRootItem>>, BaseResponse<PageDataResponse<ProductCategoryBean>>, Boolean>() {
            @Override
            public Boolean apply(@NonNull BaseResponse<PageDataResponse<ProductRootItem>> allProducts, @NonNull BaseResponse<PageDataResponse<ProductCategoryBean>> allCategory) throws Exception {
                Loger.d("数据全部下载，开始写本地缓存任务");
                SrcFileApi.save(allProducts,allCategory);
                Loger.d("数据全部下载，开始写数据库");
                List<ProductCategoryBean> categories = allCategory.getData().getData();
                if (categories.size() != 0) {
                    ProductCategoryDao productCategoryDao = MyApplication.getDaoSession().getProductCategoryDao();
                    productCategoryDao.deleteAll();//清除旧的数据
                    for (ProductCategoryBean category : categories) {
                        category.insert(productCategoryDao);//写入新的数据
                    }
                }
                List<ProductRootItem> products = allProducts.getData().getData();
                if (products.size() != 0) {
                    ProductDao productDao = MyApplication.getDaoSession().getProductDao();
                    productDao.deleteAll();
                    for (ProductRootItem product : products) {
                        product.insert(productDao);//写入新的数据
                    }
                }


                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Loger.d("数据库写成功？" + aBoolean);
                        //Loger.d("所有可显示类型:"+ LocalApi.getCategory().toString());
                        ProductDao productDao = MyApplication.getDaoSession().getProductDao();
                        Loger.d("所有商品数量:" + productDao.queryBuilder().count());
                        MyApplication.StateNow=new State(StateEnum.Ok,"");
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
    /**
     * getAllProducts from net
     *
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
                        return storeTroncellApi.getProducts(setting.getDeviceKey())
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
     *
     * @return
     */
    private Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> getCategory() {
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
                                })
                                .retryWhen(RetryWithDelay.crate())
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io());
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
