package cn.jcloud.jaf.common.menu.domain;

public class MenuOperation implements Comparable<MenuOperation>{

    private String id;
    private String sequence;
    private String code;
    private String name;
    private String title;
    private String authCode;
    private String action;
    private String params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int compareTo(MenuOperation operation) {
        int result = this.sequence.compareTo(operation.getSequence());
        if (result == 0) {
            result = this.id.compareTo(operation.getId());
        } else if (this.sequence.length() == 0
                || operation.getSequence().length() == 0) {
            result = -result; // 序号为空字符串的菜单项排在最后
        }
        return result;
    }

}
