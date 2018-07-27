package cn.jcloud.jaf.common.whitelist.vo;

import javax.validation.constraints.NotNull;

/**
 * Created by Wei Han on 2016/3/4.
 */
public class GuestMode {

    private Long orgId;

    @NotNull(message = "是否启用标志不能为空")
    private Boolean flag;

    public GuestMode() {
    }

    public GuestMode(Long orgId, Boolean flag) {
        this.orgId = orgId;
        this.flag = flag;
    }

    public Long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Boolean getFlag() {
        return this.flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
