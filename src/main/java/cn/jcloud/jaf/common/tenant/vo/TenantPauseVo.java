package cn.jcloud.jaf.common.tenant.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Wei Han on 2016/2/18.
 */
public class TenantPauseVo {

    @NotNull(message = "请指定暂停状态")
    private Boolean pauseFlag;

    @Length(message = "暂停服务信息限定在256个字符内")
    @NotEmpty(message = "请指定暂停服务信息")
    private String msg;

    public Boolean getPauseFlag() {
        return this.pauseFlag;
    }

    public void setPauseFlag(Boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
