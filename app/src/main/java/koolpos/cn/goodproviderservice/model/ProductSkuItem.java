package koolpos.cn.goodproviderservice.model;

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
}
