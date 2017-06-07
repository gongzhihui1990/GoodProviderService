package koolpos.cn.goodproviderservice.model;

import java.util.ArrayList;
import java.util.List;

import koolpos.cn.goodproviderservice.mvcDao.greenDao.Product;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProductSkuItem extends ProductBaseItem {
    private static final long serialVersionUID = -3766362549885369986L;
//    picUrl	"https://img.alicdn.com/ba…XX8mXXa_!!2752545374.jpg"
//    hasSelfImage	true
//    description	null
//    outerId	"6928521610055-异域风情-小号"
//    orderNumber	0
//    tags	null
    private  boolean hasSelfImage;
    private  String skuId;

    public boolean isHasSelfImage() {
        return hasSelfImage;
    }

    public void setHasSelfImage(boolean hasSelfImage) {
        this.hasSelfImage = hasSelfImage;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    private List<Integer> productCategoryIDs;

    public void setProductCategoryIDs(List<ProductCategoryOnlyID> productCategoryOnlyIDs) {
        this.productCategoryIDs=new ArrayList<>();
        if ( productCategoryOnlyIDs!=null&&productCategoryOnlyIDs.size()!=0){
            for (ProductCategoryOnlyID productCategoryOnlyID:productCategoryOnlyIDs) {
                this. productCategoryIDs.add(productCategoryOnlyID.getId());
            }
        }
    }

    private List<Integer> getProductCategoryIDs(){
        return productCategoryIDs;
    }
    @Override
    public void insert(ProductDao productDao) {
        Product entity=Product.create(getTitle(),getPrice(),getPicUrl(),hasSelfImage,getQrCodeUrl(),getGroupId(),getProductCategoryIDs());
        productDao.insertOrReplace(entity);
    }
}
