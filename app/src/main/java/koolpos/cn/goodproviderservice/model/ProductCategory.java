package koolpos.cn.goodproviderservice.model;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProductCategory extends BaseBean{
    private static final long serialVersionUID = -8965713797831327632L;
//    id	4
//    categoryCode	"1289417787"
//    name	"新品上市"
//    parentCategory
//    groupId	5
//    imageUrl	""
//    iconUrl
//    isLocal	false
//    isSpecial	false
//    fromType	"Taobao"
    private String id;

    private String categoryCode;
    private String name;
    private ProductCategory parentCategory;
    private String groupId;
    private String imageUrl;
    private String iconUrl;
    private String isLocal;
    private String isSpecial;
    private String fromType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ProductCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(String isLocal) {
        this.isLocal = isLocal;
    }

    public String getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(String isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }
}
