package koolpos.cn.goodproviderservice.api;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.constans.State;
import koolpos.cn.goodproviderservice.constans.StateEnum;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Goods;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.GoodsDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Product;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategory;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategoryDao;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/5/14.
 */

public class LocalApi {
    public static String getAll(){
        List<Goods> goodsList= MyApplication.getDaoSession().getGoodsDao().queryBuilder().list();
        return new Gson().toJson(goodsList);
    }

    public static AIDLResponse proxyPost(JSONObject reqJson) {
        AIDLResponse response=new AIDLResponse();
        if (getAppState().getEnum() != StateEnum.Ok){
            response.setCode(-1);
            response.setStatus(AIDLResponse.FAIL);
            response.setMessage(getAppState().getMessage());
            return response;
        }
        String action = reqJson.optString("action");
        switch (action){
            case "local/get/category":
                response.setData(getCategory());
                break;
            case "local/get/appState":
                response.setData(getAppState().getMessage());
            break;
            case "local/get/all":
            response.setData(getAll());
                break;
            case "local/get/getTypeList":
                response.setData(getTypeList());
                break;
            case "local/get/getListByType":
                String type = reqJson.optString("type");
                response.setData(getListByType(type));
                break;
            default:
                response.setCode(-1);
                response.setMessage("unSupport "+action);
                break;
        }
        return response;
    }

    private static State getAppState() {
        return MyApplication.StateNow;
    }

    private static String getListByType(String type) {
        List<Goods> goodsList= MyApplication.getDaoSession().getGoodsDao().queryBuilder()
                .where(GoodsDao.Properties.Goods_type.eq(type)).list();
        return new Gson().toJson(goodsList);
    }

    private static String getTypeList() {
        ArrayList<String> typeList=new ArrayList<>();
        List<Goods> list=MyApplication.getDaoSession().getGoodsDao()
        .queryBuilder().where(new WhereCondition.StringCondition("1 GROUP BY "+GoodsDao.Properties.Goods_type.name))
                .list();
        Loger.d("List size"+list.size());
        for (Goods item:list){
            typeList.add(item.getGoods_type());
        }
        return new Gson().toJson(typeList);
    }

    public static String getCategory() {
        ArrayList<ProductCategory> categoryList=new ArrayList<>();
        List<ProductCategory> list =MyApplication.getDaoSession().getProductCategoryDao()
                .queryBuilder().list();
        Loger.e("all size:"+list.size());
        for (ProductCategory item:list) {
            if (item.getParentCategoryCode()==null){
                categoryList.add(item);
                Loger.d("add :"+item.getCategoryCode());
            }
        }
        Loger.e("all root size:"+categoryList.size());

        return new Gson().toJson(categoryList);
    }
    public static String getProducts(int categoryIdParam){
        ProductCategoryDao productCategoryDao = MyApplication.getDaoSession().getProductCategoryDao();
        ProductDao productDao = MyApplication.getDaoSession().getProductDao();
        List<Product> inCategoryProducts = new ArrayList<Product>();
        List<Integer> allCategoryIds=new ArrayList<Integer>();
        int categoryId = categoryIdParam;
        addCategoryId(allCategoryIds,categoryId,productCategoryDao);
//        allCategoryIds.add(categoryId);
//        List<ProductCategory> categoryChildList = productCategoryDao.queryBuilder()
//                .where(ProductCategoryDao.Properties.ParentCategoryId.eq(categoryId)).list();
//        if (categoryChildList != null && categoryChildList.size() != 0) {
//            for (ProductCategory childCategory : categoryChildList) {
//                allCategoryIds.add(childCategory.getCategoryId());
//            }
//        }
        List<Product> allProduct= productDao.queryBuilder().list();
        for (Product product:allProduct){
            for (int id: allCategoryIds) {
                if (product.getProductCategoryIDs()!=null&&
                        product.getProductCategoryIDs().contains(id)){
                    inCategoryProducts.add(product);
                    break;
                }
            }
        }
        Loger.d("CategoryId-" + categoryId + "有" + inCategoryProducts.size()+"件商品");
        return new Gson().toJson(inCategoryProducts);
    }
    //TODO 递归
    private static void addCategoryId(List<Integer> allCategoryIds, int categoryId, ProductCategoryDao productCategoryDao ){
        allCategoryIds.add(categoryId);
        List<ProductCategory> categoryChildList = productCategoryDao.queryBuilder()
                .where(ProductCategoryDao.Properties.ParentCategoryId.eq(categoryId)).list();
        if (categoryChildList != null && categoryChildList.size() != 0) {
            for (ProductCategory childCategory : categoryChildList) {
                int childCategoryId= childCategory.getCategoryId();
                addCategoryId(allCategoryIds,childCategoryId,productCategoryDao);
            }
        }
    }
}
