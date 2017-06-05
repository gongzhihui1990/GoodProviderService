package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/3.
 */
@Entity()
public class SPU implements Serializable{
    private static final long serialVersionUID = -1654006803930283594L;
    @Id
    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    @Unique
    private String spu_id;
    private String img_url;
    private String spu_url;
    @NotNull
    @Index
    private String spu_type;
    @Generated(hash = 407865759)
    public SPU(Long id, @NotNull String name, String description,
            @NotNull String spu_id, String img_url, String spu_url,
            @NotNull String spu_type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.spu_id = spu_id;
        this.img_url = img_url;
        this.spu_url = spu_url;
        this.spu_type = spu_type;
    }
    @Generated(hash = 2069596877)
    public SPU() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSpu_id() {
        return this.spu_id;
    }
    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
    }
    public String getImg_url() {
        return this.img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    public String getSpu_url() {
        return this.spu_url;
    }
    public void setSpu_url(String spu_url) {
        this.spu_url = spu_url;
    }
    public String getSpu_type() {
        return this.spu_type;
    }
    public void setSpu_type(String spu_type) {
        this.spu_type = spu_type;
    }
}
