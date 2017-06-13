package koolpos.cn.goodproviderservice.model.response;

import koolpos.cn.goodproviderservice.model.BaseBean;
import koolpos.cn.goodproviderservice.mvcDao.GreenDaoInsert;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.ProductDao;

/**
 * Created by Administrator on 2017/6/5.
 */

public class StoreInfoBean extends BaseBean  {
    private static final long serialVersionUID = -6932045770210873520L;
    int id;
    String name;
    String status;
    String mac;
    String groupId;
    StoreGroupBean group;
    boolean isLocked;
    String deviceTypeId;
    String deviceTypeName;
    String address;
    String intranetIP;
    String internetIP;
    boolean isRegistered;
    String auditStatus;
    String licenseInfo;
    String hardwareCode;
    String shutdownTime;
    String resolution_Width;
    String resolution_Height;
    String heartBeatTime;
    String subKey;
    String os;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public StoreGroupBean getGroup() {
        return group;
    }

    public void setGroup(StoreGroupBean group) {
        this.group = group;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntranetIP() {
        return intranetIP;
    }

    public void setIntranetIP(String intranetIP) {
        this.intranetIP = intranetIP;
    }

    public String getInternetIP() {
        return internetIP;
    }

    public void setInternetIP(String internetIP) {
        this.internetIP = internetIP;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getLicenseInfo() {
        return licenseInfo;
    }

    public void setLicenseInfo(String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }

    public String getHardwareCode() {
        return hardwareCode;
    }

    public void setHardwareCode(String hardwareCode) {
        this.hardwareCode = hardwareCode;
    }

    public String getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(String shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public String getResolution_Width() {
        return resolution_Width;
    }

    public void setResolution_Width(String resolution_Width) {
        this.resolution_Width = resolution_Width;
    }

    public String getResolution_Height() {
        return resolution_Height;
    }

    public void setResolution_Height(String resolution_Height) {
        this.resolution_Height = resolution_Height;
    }

    public String getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(String heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
