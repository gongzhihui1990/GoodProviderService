package koolpos.cn.goodproviderservice.model;

import java.util.List;

import koolpos.cn.goodproviderservice.mvcDao.GreenDaoInsert;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Product;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProductRootItem extends ProductBaseItem implements GreenDaoInsert<ProductDao>{

    private static final long serialVersionUID = -6594185873104815905L;
    private List<ProductCategoryOnlyID> p_ProductCategories;
    private List<Object> productTags;
    private List<ProductSkuItem> skus;
    private boolean isFromBrand;
    private String sumPropsName;
    private String sellerId;
//    p_ProductCategories	[2]
//    productTags
//     skus[]
//    itemImagesOrVedios	[]
//    isFromBrand	true
//    sumPropsName	"颜色分类:争奇斗艳-小号,争奇斗艳-中号,妙笔生花…,黄旗紫盖-中号,百鸟朝凤-小号,百鸟朝凤-中号"
//    sellerId	"2752545374"


// -   id	495
// -   itemId	550044298161
//    skuId	"3348823357015"
//    num	50
//    propsName	"颜色分类:异域风情-小号"
//    title	"自然醒老虎民族风粗布荞麦枕颈椎枕修复护颈枕成人门店同款午睡枕"
//    price	178
//    qrCodeUrl	"http://h5.m.taobao.com/al…&displayType=zhinengping"
//    keywords	null
//    auditStatus	"上线审核中"
//    groupId	5
//    groupName	"自然醒品牌"
//    picUrl	"https://img.alicdn.com/ba…XX8mXXa_!!2752545374.jpg"
//    hasSelfImage	true
//    description	null
//    created	"2017-05-04T18:25:05"
//    lastUpdated	"2017-06-03T23:37:00.7349884"
//    orderNumber	0

    public List<ProductCategoryOnlyID> getP_ProductCategories() {
        return p_ProductCategories;
    }

    public void setP_ProductCategories(List<ProductCategoryOnlyID> p_ProductCategories) {
        this.p_ProductCategories = p_ProductCategories;
    }

    public List<Object> getProductTags() {
        return productTags;
    }

    public void setProductTags(List<Object> productTags) {
        this.productTags = productTags;
    }

    public List<ProductSkuItem> getSkus() {
        return skus;
    }

    public void setSkus(List<ProductSkuItem> skus) {
        this.skus = skus;
    }

    public boolean isFromBrand() {
        return isFromBrand;
    }

    public void setFromBrand(boolean fromBrand) {
        isFromBrand = fromBrand;
    }

    public String getSumPropsName() {
        return sumPropsName;
    }

    public void setSumPropsName(String sumPropsName) {
        this.sumPropsName = sumPropsName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public void insert(ProductDao productDao) {
        if (skus!=null&&skus.size()!=0){
            for(ProductSkuItem item :skus){
                item.setProductCategoryIDs(p_ProductCategories);
                item.insert(productDao);
            }
        }else {

        }

    }
}
