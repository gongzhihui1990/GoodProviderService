package koolpos.cn.goodproviderservice.api;

import java.util.Map;

import io.reactivex.Observable;
import koolpos.cn.goodproviderservice.model.response.AdBean;
import koolpos.cn.goodproviderservice.model.response.BaseResponseV1;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.model.response.ProductRootItem;
import koolpos.cn.goodproviderservice.model.response.StoreInfoBean;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/5.
 */

public interface StoreTroncellApiV2 {
    String HostUrl = "http://store.troncell.com";

    /**
     * 设备注册
     */
    @POST("/api/services/app/SensingDevice/RegisterDevice")
    Observable<BaseResponseV1<StoreInfoBean>> register(@Query("subkey") String subkey, @Body Map<String, Object> requestMap);

    /**
     * 获取设备信息
     */
    @GET("/api/services/app/SensingDevice/GetDeviceInfo")
    Observable<BaseResponseV1<StoreInfoBean>> getDeviceInfo(@Query("subkey") String subkey);

    /**
     * 获取广告
     */
    @GET("/api/services/app/SensingDevice/GetAds")
    Observable<BaseResponseV1<PageDataResponse<AdBean>>> getAds(@Query("subkey") String subkey);




    /**
     * 获取产品类型
     */
    @GET("/api/v1/storesdk/ProductCategories")
    Observable<BaseResponseV1<PageDataResponse<ProductCategoryBean>>> getProductCategories(@Query("subkey") String subkey);

    /**
     * 获取产品
     */
    @GET("/api/services/app/SensingDevice/GetProducts")
    Observable<BaseResponseV1<PageDataResponse<ProductRootItem>>> getProducts(@Query("subkey") String subkey);

    /**
     * 获取软件
     */
    @GET("/api/services/app/SensingDevice/GetApps")
    Observable<BaseResponseV1<StoreInfoBean>> getSoftWare(@Query("subkey") String subkey);

}
