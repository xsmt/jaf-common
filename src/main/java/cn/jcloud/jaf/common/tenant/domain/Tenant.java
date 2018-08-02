package cn.jcloud.jaf.common.tenant.domain;


import cn.jcloud.jaf.common.base.domain.BizDomain;
import cn.jcloud.jaf.common.constant.IDG;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by Wei Han on 2016/1/27.
 */
@Entity
@Table(name = "t_tenant")
@GenericGenerator(name = "id", strategy = IDG.ASSIGNED)
@Document(collection = "t_tenant")
@TypeAlias("Tenant")
public class Tenant extends BizDomain<Long> {

    /**
     * 租户id,组织号
     */
    @Column(length = 12)
    @NotBlank(message = "租户id不能为空")
    private String orgId;

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    @Column(length = 64)
    private String name;

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    @Column(length = 64)
    private String appName;

    /**
     * 领域
     */
    @NotBlank(message = "领域不能为空")
    @Pattern(regexp = "^.+\\.sdp\\.nd$", message = "正确格式:[org_id].sdp.nd")
    @Column(length = 64)
    private String realm;

    /**
     * 暂停服务返回信息
     */
    private String msg;

    /**
     * 开发者uid，即应用的创建者99U工号
     */
    @Column(length = 10)
    private Long developerUid;
    /**
     * 备注信息
     */
    @Column(length = 256)
    private String info;

    /**
     * 扩展字段
     */
    @Column(length = 512)
    private String addition;

    /**
     * 暂停状态
     */
    private boolean pauseFlag;

    /**
     * 数据源
     */
    @Column(length = 20)
    private String dbConn;

    /**
     * 表索引号
     */
    private Long tenancy;

    /**
     * 最后访问日期
     */
    private Date lastAccessTime;

    private boolean guestMode;

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRealm() {
        return this.realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getDeveloperUid() {
        return this.developerUid;
    }

    public void setDeveloperUid(Long developerUid) {
        this.developerUid = developerUid;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddition() {
        return this.addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public boolean isPauseFlag() {
        return this.pauseFlag;
    }

    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public String getDbConn() {
        return this.dbConn;
    }

    public void setDbConn(String dbConn) {
        this.dbConn = dbConn;
    }

    public Long getTenancy() {
        return this.tenancy;
    }

    public void setTenancy(Long tenancy) {
        this.tenancy = tenancy;
    }

    public Date getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean isGuestMode() {
        return guestMode;
    }

    public void setGuestMode(boolean guestMode) {
        this.guestMode = guestMode;
    }
}
