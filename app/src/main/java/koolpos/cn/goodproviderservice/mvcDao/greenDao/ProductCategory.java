package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by caroline on 2017/6/6.
 */
@Entity
public class ProductCategory implements Serializable {
    private static final long serialVersionUID = 86883663409873853L;
    @Id
    private Long id;

    private String categoryCode;
    private String name;
//    private koolpos.cn.goodproviderservice.model.ProductCategory parentCategory;
    private String groupId;
    private String imageUrl;
    private String iconUrl;
    private boolean isLocal;
    private boolean isSpecial;
    private String fromType;
    
    public static ProductCategory create(Long id, String categoryCode, String name,
                           String groupId, String imageUrl, String iconUrl, boolean isLocal,
                           boolean isSpecial, String fromType) {
        ProductCategory category =new ProductCategory();
        category.id = id;
        category.categoryCode = categoryCode;
        category.name = name;
        category.groupId = groupId;
        category.imageUrl = imageUrl;
        category.iconUrl = iconUrl;
        category.isLocal = isLocal;
        category.isSpecial = isSpecial;
        category.fromType = fromType;
        return category;
    }
    @Generated(hash = 587827635)
    public ProductCategory() {
    }
    @Generated(hash = 1291516557)
    public ProductCategory(Long id, String categoryCode, String name, String groupId,
            String imageUrl, String iconUrl, boolean isLocal, boolean isSpecial,
            String fromType) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.name = name;
        this.groupId = groupId;
        this.imageUrl = imageUrl;
        this.iconUrl = iconUrl;
        this.isLocal = isLocal;
        this.isSpecial = isSpecial;
        this.fromType = fromType;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCategoryCode() {
        return this.categoryCode;
    }
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getIconUrl() {
        return this.iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public boolean getIsLocal() {
        return this.isLocal;
    }
    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }
    public boolean getIsSpecial() {
        return this.isSpecial;
    }
    public void setIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
    public String getFromType() {
        return this.fromType;
    }
    public void setFromType(String fromType) {
        this.fromType = fromType;
    }


}
