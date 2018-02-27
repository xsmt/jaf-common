package cn.jcloud.jaf.common.base.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wei Han on 2016/2/4.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BizDomain<I extends Serializable> extends BaseDomain<I> {

    @LastModifiedDate
    private Date updateTime;

    @CreatedDate
    @Column(updatable = false)
    private Date createTime;

    private boolean deleted;

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
