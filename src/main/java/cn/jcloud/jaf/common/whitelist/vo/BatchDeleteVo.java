package cn.jcloud.jaf.common.whitelist.vo;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * 白名单批量删除vo
 * Created by Wei Han on 2016/3/7.
 */
public class BatchDeleteVo {
    @NotEmpty(message = "缺少参数ids或为空")
    private List<Long> ids;

    public List<Long> getIds() {
        return this.ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
