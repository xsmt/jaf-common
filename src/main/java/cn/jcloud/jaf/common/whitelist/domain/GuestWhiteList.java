package cn.jcloud.jaf.common.whitelist.domain;

import cn.jcloud.jaf.common.base.domain.BaseTenantDomain;
import cn.jcloud.jaf.common.constant.IDG;
import cn.jcloud.jaf.common.handler.TenantHandler;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/**
 * 白名单
 * Created by Wei Han on 2016/3/4.
 */
@Entity
@Table(name = TenantHandler.PREFIX + "guest_white_list")
@GenericGenerator(name = "id", strategy = IDG.ASSIGNED)
@EntityListeners(AuditingEntityListener.class)
@Document(collection = "#{T(cn.jcloud.jaf.common.handler.TenantHandler).getTablePrefix().concat('guest_white_list')}")
@TypeAlias("GuestWhiteList")
public class GuestWhiteList extends BaseTenantDomain<Long> {

    public interface Optional {
    }

    @JsonView(Optional.class)
    @Length(max = 128, message = "[access]长度不能超过128个字符")
    @NotBlank(message = "[access]不能为空")
    @Column(length = 128)
    private String access;

    @JsonView(Optional.class)
    @Length(max = 200, message = "描述长度不能超过200个字符")
    @Column(length = 200)
    private String description;

    @CreatedDate
    @Column(updatable = false)
    private Long createTime;

    public GuestWhiteList() {
    }

    public GuestWhiteList(String access, String description) {
        this.access = access;
        this.description = description;
    }

    public String getAccess() {
        return this.access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
