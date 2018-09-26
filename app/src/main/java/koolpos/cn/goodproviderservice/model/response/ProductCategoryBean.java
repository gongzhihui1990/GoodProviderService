package koolpos.cn.goodproviderservice.model.response;

import koolpos.cn.goodproviderservice.model.BaseBean;
import koolpos.cn.goodproviderservice.mvcDao.GreenDaoInsert;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategoryDao;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProductCategoryBean extends BaseBean implements GreenDaoInsert<ProductCategoryDao> {
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


//    {
//        "totalCount": 0,
//            "items": [
//        {
//            "name": "string",
//                "organizationUnitId": 0,
//                "resourceType": "string",
//                "type": "Single",
//                "auditStatus": "Offline",
//                "adsTags": [
//            {
//                "id": 0,
//                    "name": "string"
//            }
//      ],
//            "productAttributes": "string",
//                "ageScope": "string",
//                "gender": "string",
//                "orderNumber": 0,
//                "fileUrl": "string",
//                "isMine": true,
//                "isDeleted": true,
//                "deleterUserId": 0,
//                "deletionTime": "2018-07-25T09:02:31.943Z",
//                "lastModificationTime": "2018-07-25T09:02:31.943Z",
//                "lastModifierUserId": 0,
//                "creationTime": "2018-07-25T09:02:31.943Z",
//                "creatorUserId": 0,
//                "id": 0
//        }
//  ]
//    }
    private int id;

    private String categoryCode;
    private String parentCategoryCode;
    private int parentCategoryId;
    private String name;
    private ProductCategoryBean parentCategory;
    private String groupId;
    private String imageUrl;
    private String iconUrl;
    private boolean isLocal;
    private boolean isSpecial;
    private String fromType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ProductCategoryBean getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ProductCategoryBean parentCategory) {
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

    public boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public boolean getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }



    @Override
    public void insert(ProductCategoryDao productCategoryDao) {

        if (parentCategory!=null){
            parentCategoryId=parentCategory.getId();
            parentCategoryCode=parentCategory.getCategoryCode();
        }
        koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategory entity
                = koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductCategory.create(id, categoryCode, name,groupId, imageUrl, iconUrl, isLocal, isSpecial, fromType,parentCategoryId,parentCategoryCode);
        productCategoryDao.insertOrReplace(entity);
    }
}
