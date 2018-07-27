package cn.jcloud.jaf.common.virtualorg.domain;

import java.util.Map;

public class VOrgNode {

    private String orgId;
    private String orgName;
    private String orgFullName;
    private String ucOrgId;
    private Map<String, Object> orgExinfo;

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgFullName() {
        return this.orgFullName;
    }

    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    public String getUcOrgId() {
        return this.ucOrgId;
    }

    public void setUcOrgId(String ucOrgId) {
        this.ucOrgId = ucOrgId;
    }

    public Map<String, Object> getOrgExinfo() {
        return this.orgExinfo;
    }

    public void setOrgExinfo(Map<String, Object> orgExinfo) {
        this.orgExinfo = orgExinfo;
    }
}
