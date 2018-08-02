package cn.jcloud.jaf.common.whitelist.vo;

import java.util.List;

/**
 * Created by Wei Han on 2016/6/20.
 */
public class BatchAddVo {

    private List<String> accesses;

    public List<String> getAccesses() {
        return this.accesses;
    }

    public BatchAddVo setAccesses(List<String> accesses) {
        this.accesses = accesses;
        return this;
    }
}
