package koolpos.cn.goodproviderservice.model.response;

import java.io.Serializable;

/**
 * Created by caroline on 2017/8/22.
 */

class OnlineStoreInfo implements Serializable {
    private static final long serialVersionUID = -3263411499098289700L;
    private String id;
    private String skuId;
    private String itemId;
    private String storeId;
    private String type;
    private String onlineStoreType;
    private String price;
    private String inventory;
    private String qrcode;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
