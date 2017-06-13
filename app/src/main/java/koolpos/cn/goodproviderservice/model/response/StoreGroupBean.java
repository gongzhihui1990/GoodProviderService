package koolpos.cn.goodproviderservice.model.response;

import koolpos.cn.goodproviderservice.model.BaseBean;

/**
 * Created by Administrator on 2017/6/13.
 */

public class StoreGroupBean extends BaseBean{
    private static final long serialVersionUID = 9163228068512137798L;
    int id;
    String name;
    String logoUrl;
    String address;
    String groupType;
    String groupTypeValue;
    String created;
    String description;
    String omniStoreId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupTypeValue() {
        return groupTypeValue;
    }

    public void setGroupTypeValue(String groupTypeValue) {
        this.groupTypeValue = groupTypeValue;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOmniStoreId() {
        return omniStoreId;
    }

    public void setOmniStoreId(String omniStoreId) {
        this.omniStoreId = omniStoreId;
    }
}
