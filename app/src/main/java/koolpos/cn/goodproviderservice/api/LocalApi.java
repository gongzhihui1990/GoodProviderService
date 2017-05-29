package koolpos.cn.goodproviderservice.api;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Goods;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.GoodsDao;
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
        String action = reqJson.optString("action");
        AIDLResponse response=new AIDLResponse();
        switch (action){
            case "local/get/appState":
                response.setData(getAppState());
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

    private static String getAppState() {
        return MyApplication.State;
    }

    private static String getListByType(String type) {
        List<Goods> goodsList= MyApplication.getDaoSession().getGoodsDao().queryBuilder()
                .where(GoodsDao.Properties.Goods_type.eq(type)).list();
        return new Gson().toJson(goodsList);
    }

    private static String getTypeList() {
//        ery query = userDao.queryRawCreate(
//                ", GROUP G WHERE G.NAME=? AND T.GROUP_ID=G._ID", "admin");

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
}
