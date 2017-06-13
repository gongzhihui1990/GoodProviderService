package koolpos.cn.goodproviderservice.mvcDao.greenDao;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import koolpos.cn.goodproviderservice.model.response.AdBean;

/**
 * Created by Administrator on 2017/6/7.
 */
@Entity
public class Ad implements Serializable {
    private static final long serialVersionUID = -6143549198857532691L;
    @Id
    private Long id;
    private int beanId;
    private String deviceName;
    private String name;
    private String size;
    private String resourceName;
    private String content;
    private String deviceId;
    private String groupId;
    private String adsId;
    private String auditStatus;
    private String pixel;
    private String created;
    private String resourType;
    private boolean isFromBrand;
    private String fileurl;
    private String thumbnailUrl;
    private String startedTime;
    private String endTime;
    private String duration;
    private String transition;

    @Generated(hash = 598234031)
    public Ad(Long id, int beanId, String deviceName, String name, String size,
            String resourceName, String content, String deviceId, String groupId,
            String adsId, String auditStatus, String pixel, String created,
            String resourType, boolean isFromBrand, String fileurl,
            String thumbnailUrl, String startedTime, String endTime,
            String duration, String transition) {
        this.id = id;
        this.beanId = beanId;
        this.deviceName = deviceName;
        this.name = name;
        this.size = size;
        this.resourceName = resourceName;
        this.content = content;
        this.deviceId = deviceId;
        this.groupId = groupId;
        this.adsId = adsId;
        this.auditStatus = auditStatus;
        this.pixel = pixel;
        this.created = created;
        this.resourType = resourType;
        this.isFromBrand = isFromBrand;
        this.fileurl = fileurl;
        this.thumbnailUrl = thumbnailUrl;
        this.startedTime = startedTime;
        this.endTime = endTime;
        this.duration = duration;
        this.transition = transition;
    }
    @Generated(hash = 413799064)
    public Ad() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceName() {
        return this.deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getResourceName() {
        return this.resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDeviceId() {
        return this.deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getGroupId() {
        return this.groupId;
    }
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getAdsId() {
        return this.adsId;
    }
    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }
    public String getAuditStatus() {
        return this.auditStatus;
    }
    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getPixel() {
        return this.pixel;
    }
    public void setPixel(String pixel) {
        this.pixel = pixel;
    }
    public String getCreated() {
        return this.created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getResourType() {
        return this.resourType;
    }
    public void setResourType(String resourType) {
        this.resourType = resourType;
    }
    public boolean getIsFromBrand() {
        return this.isFromBrand;
    }
    public void setIsFromBrand(boolean isFromBrand) {
        this.isFromBrand = isFromBrand;
    }
    public String getFileurl() {
        return this.fileurl;
    }
    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public String getStartedTime() {
        return this.startedTime;
    }
    public void setStartedTime(String startedTime) {
        this.startedTime = startedTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getTransition() {
        return this.transition;
    }
    public void setTransition(String transition) {
        this.transition = transition;
    }
    public int getBeanId() {
        return this.beanId;
    }
    public void setBeanId(int beanId) {
        this.beanId = beanId;
    }

    public static Ad createByBean(AdBean adBean) {
        Ad ad =new Ad();
        ad.beanId = adBean.getId();
        ad.deviceName = adBean.getDeviceName();
        ad.name = adBean.getName();
        ad.size = adBean.getSize();
        ad.resourceName = adBean.getResourceName();
        ad.content = adBean.getContent();
        ad.deviceId = adBean.getDeviceId();
        ad.groupId = adBean.getGroupId();
        ad.adsId = adBean.getAdsId();
        ad.auditStatus = adBean.getAuditStatus();
        ad.pixel = adBean.getPixel();
        ad.created = adBean.getCreated();
        ad.resourType = adBean.getResourType();
        ad.isFromBrand = adBean.isFromBrand();
        ad.fileurl = adBean.getFileurl();
        ad.thumbnailUrl = adBean.getThumbnailUrl();
        ad.startedTime = adBean.getStartedTime();
        ad.endTime = adBean.getEndTime();
        ad.duration = adBean.getDuration();
        ad.transition = adBean.getTransition();
        return ad;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
