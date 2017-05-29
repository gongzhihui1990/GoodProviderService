package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/14.
 */
@Entity()
public class Goods implements Serializable{
    private static final long serialVersionUID = 672767594116405769L;
    @Id
    private Long id;
    @NotNull
    private String goods_name;
    @NotNull
    @Unique
    private String goods_id;
    private String image_url;
    @NotNull
    @Index
    private String goods_type;

    @Generated(hash = 1362257094)
    public Goods(Long id, @NotNull String goods_name, @NotNull String goods_id,
            String image_url, @NotNull String goods_type) {
        this.id = id;
        this.goods_name = goods_name;
        this.goods_id = goods_id;
        this.image_url = image_url;
        this.goods_type = goods_type;
    }

//    public static Goods createFromTest(@NotNull String goods_name, @NotNull String goods_id,
//                                         String image_url, @NotNull String goods_type){
//        Goods goods=new Goods();
//        goods.goods_name = goods_name;
//        goods.goods_id = goods_id;
//        goods.image_url = image_url;
//        goods.goods_type = goods_type;
//        return goods;
//    }
    public static Goods createFromTest(@NotNull String goods_name, @NotNull String goods_type,@NotNull String  image_url){
        Goods goods=new Goods();
        goods.goods_id = UUID.randomUUID().toString();
        goods.goods_name = goods_name+"-"+goods.goods_id;
        goods.image_url = image_url;
        goods.goods_type = goods_type;
        return goods;
    }
    @Generated(hash = 1770709345)
    public Goods() {
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
