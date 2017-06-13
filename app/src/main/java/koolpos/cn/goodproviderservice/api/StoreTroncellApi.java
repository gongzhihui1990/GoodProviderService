package koolpos.cn.goodproviderservice.api;

import io.reactivex.Observable;
import koolpos.cn.goodproviderservice.model.response.AdBean;
import koolpos.cn.goodproviderservice.model.response.BaseResponse;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.model.response.ProductRootItem;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/5.
 */

public interface StoreTroncellApi {
    public static final String HostUrl="http://store.troncell.com";

    @GET("/api/v1/storesdk/ProductCategories")
    Observable<BaseResponse<PageDataResponse<ProductCategoryBean>>> getProductCategories(@Query("subkey") String subkey);
    @GET("/api/v1/storesdk/products")
    Observable<BaseResponse<PageDataResponse<ProductRootItem>>> getProducts(@Query("subkey") String subkey);
    @GET("/api/v1/storesdk/ads")
    Observable<BaseResponse<PageDataResponse<AdBean>>> getAds(@Query("subkey") String subkey);
    @GET("/api/v1/storesdk/RegisterDevice")
    Observable<BaseResponse<String>> register(@Query("subkey") String subkey, @Body String body);
}
