package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
@Entity
public class Setting implements Serializable {
    private static final long serialVersionUID = -475667528300648817L;
    @Id
    private Long id;
    private int intervalAd;
    @NotNull
    @Unique
    private String deviceSn;
    private String deviceKey;
    private Date lastUpdateTime;
    private boolean loadCacheFirst;

    @Generated(hash = 1534237997)
    public Setting(Long id, int intervalAd, @NotNull String deviceSn,
            String deviceKey, Date lastUpdateTime, boolean loadCacheFirst) {
        this.id = id;
        this.intervalAd = intervalAd;
        this.deviceSn = deviceSn;
        this.deviceKey = deviceKey;
        this.lastUpdateTime = lastUpdateTime;
        this.loadCacheFirst = loadCacheFirst;
    }
    @Generated(hash = 909716735)
    public Setting() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getIntervalAd() {
        return this.intervalAd;
    }
    public void setIntervalAd(int intervalAd) {
        this.intervalAd = intervalAd;
    }
    public String getDeviceSn() {
        return this.deviceSn;
    }
    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
    public String getDeviceKey() {
        return this.deviceKey;
    }
    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public boolean getLoadCacheFirst() {
        return this.loadCacheFirst;
    }
    public void setLoadCacheFirst(boolean loadCacheFirst) {
        this.loadCacheFirst = loadCacheFirst;
    }
}
