package koolpos.cn.goodproviderservice.api;

import io.reactivex.Observable;
import koolpos.cn.goodproviderservice.model.BaseResponse;
import koolpos.cn.goodproviderservice.model.PageDataResponse;
import koolpos.cn.goodproviderservice.model.ProductCategory;
import koolpos.cn.goodproviderservice.model.ProductRootItem;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/5.
 */

public interface StoreTroncellApi {


    public static final String HostUrl="http://store.troncell.com";

    @GET("/api/v1/storesdk/ProductCategories")
    Observable<BaseResponse<PageDataResponse<ProductCategory>>> getProductCategories(@Query("subkey") String subkey);
    @GET("/api/v1/storesdk/products")
    Observable<BaseResponse<PageDataResponse<ProductRootItem>>> getProducts(@Query("subkey") String subkey);
}
