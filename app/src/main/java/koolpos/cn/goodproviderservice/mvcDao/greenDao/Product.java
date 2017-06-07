package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/6/7.
 */
@Entity
public class Product implements Serializable {
    private static final long serialVersionUID = -475667528300648817L;
    @Id
    private Long id;

    private String title;
    private String price;
    private String picUrl;
    private boolean hasSelfImage;
    private String qrCodeUrl;
    private String groupId;
    @Convert(converter =CategoryIDs.class, columnType = String.class)
    private List<Integer> productCategoryIDs;
    @Generated(hash = 880660975)
    public Product(Long id, String title, String price, String picUrl, boolean hasSelfImage,
            String qrCodeUrl, String groupId, List<Integer> productCategoryIDs) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.picUrl = picUrl;
        this.hasSelfImage = hasSelfImage;
        this.qrCodeUrl = qrCodeUrl;
        this.groupId = groupId;
        this.productCategoryIDs = productCategoryIDs;
    }
    @Generated(hash = 1890278724)
    public Product() {
    }
    public static Product create(String title,String price,String picUrl,boolean hasSelfImage,String qrCodeUrl,String groupId,List<Integer> productCategoryIDs){
        Product product=new Product();
        product.title= title;
        product.price= price;
        product.picUrl = picUrl;
        product.hasSelfImage = hasSelfImage;
        product.qrCodeUrl = qrCodeUrl;
        product.groupId = groupId;
        product.productCategoryIDs = productCategoryIDs;
        return product;
    }
    public static class CategoryIDs implements PropertyConverter<List<Integer>, String> {

        @Override
        public List<Integer> convertToEntityProperty(String databaseValue) {
            if (databaseValue!=null){
                String[] array= databaseValue.split(",");
                List<Integer> ids=new ArrayList<>();
                for (String id: array) {
                    ids.add(Integer.valueOf(id));
                }
                return ids;
            }
            return null;
        }

        @Override
        public String convertToDatabaseValue(List<Integer> entityProperty) {
            if (entityProperty!=null&&entityProperty.size()!=0){
                String data="";
                for (Integer id:entityProperty) {
                    data+=","+id;
                }
                return data;
            }
            return null;
        }
    }
//"Id" integer primary key autoincrement not null ,
//            "Url" varchar not null ,
//            "FileName" varchar ,
//            "CheckStatus" integer ,
//            "DownloadStatus" integer ,
//            "LocalFile" varchar ,
//            "Extension" varchar ,
//            "OriginFileName" varchar ,
//            "Type" varchar ,
//            "RetryCount" integer ,
//            "ErrorMessage" varchar ,
//            "AddedTime" datetime )
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getQrCodeUrl() {
        return this.qrCodeUrl;
    }
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public List<Integer> getProductCategoryIDs() {
        return this.productCategoryIDs;
    }
    public void setProductCategoryIDs(List<Integer> productCategoryIDs) {
        this.productCategoryIDs = productCategoryIDs;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public boolean getHasSelfImage() {
        return this.hasSelfImage;
    }
    public void setHasSelfImage(boolean hasSelfImage) {
        this.hasSelfImage = hasSelfImage;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
