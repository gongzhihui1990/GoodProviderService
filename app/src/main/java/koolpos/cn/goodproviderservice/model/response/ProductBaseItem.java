package koolpos.cn.goodproviderservice.model.response;

import android.text.TextUtils;

import java.util.List;

import koolpos.cn.goodproviderservice.model.BaseBean;
import koolpos.cn.goodproviderservice.mvcDao.GreenDaoInsert;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/6/5.
 */

abstract class ProductBaseItem extends BaseBean implements GreenDaoInsert<ProductDao> {
    private static final long serialVersionUID = -5773957836869932341L;
    // -   id	1
// -   itemId	550044298161
// -   num	444
// -   propsName	""
//  -  title	"自然醒老虎民族风粗布荞麦枕颈椎枕修复护颈枕成人门店同款午睡枕"
//  -  price	178
//   - qrCodeUrl	"http://h5.m.taobao.com/al…&displayType=zhinengping"
//  -  keywords	""
//   - auditStatus	"上线审核中"
//   - groupId	5
//   - groupName	"自然醒品牌"
//  -  picUrl	"https://img.alicdn.com/ba…XcIipXa_!!2752545374.jpg"
//    likeCount	0
//  -  description	null
//   - created	"2017-06-03T23:37:00.7349884"
//   - lastUpdated	"2017-06-04T11:12:15.5091985"
//  -  orderNumber	0

    //New
//    "onlineStoreInfos": [
//    {
//        "id": 74283,
//            "skuId": 486,
//            "itemId": 0,
//            "storeId": null,
//            "type": 1,
//            "onlineStoreType": "Taobao",
//            "pid": null,
//            "price": 179,
//            "inventory": 20,
//            "qrcode": "http://h5.m.taobao.com/alizhanggui/buyer-goto.html?linkType=item&itemId=549892776476&skuId=3348114081350&sellerId=2752545374&displayType=zhinengping&storeid=89078"
//    }
//            ]
//}
    private String id;
    private String itemId;
    private String num;
    private String propsName;
    private String title;
    private String price;
    private String qrCodeUrl;
    private String keywords;
    private String auditStatus;
    private String groupId;
    private String groupName;
    private String picUrl;
    private String likeCount;
    private String description;
    private String created;
    private String lastUpdated;
    private String orderNumber;
    private String outerId;

    private List<OnlineStoreInfo> onlineStoreInfos;

    public List<OnlineStoreInfo> getOnlineStoreInfos() {
        return onlineStoreInfos;
    }

    public void setOnlineStoreInfos(List<OnlineStoreInfo> onlineStoreInfos) {
        this.onlineStoreInfos = onlineStoreInfos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQrCodeUrl() {
        if (TextUtils.isEmpty(qrCodeUrl)) {
            if (onlineStoreInfos != null && onlineStoreInfos.size() != 0) {
                qrCodeUrl = onlineStoreInfos.get(0).getQrcode();
            }else {
                Loger.e("onlineStoreInfos----null");
            }
        }
        Loger.d("qrCodeUrl----" + qrCodeUrl);
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }
}
