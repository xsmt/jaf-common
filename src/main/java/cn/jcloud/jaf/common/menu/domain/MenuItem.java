package cn.jcloud.jaf.common.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuItem implements Comparable<MenuItem>{
    private String id;
    private String sequence;
    private String code;
    private String name;
    private String title;
    private String icon;
    private String authCode;
    private String action;
    private List<MenuOperation> operations = new ArrayList<>();
    private List<MenuItem> children = new ArrayList<>();

    public List<MenuItem> addChild(MenuItem child) {
        this.children.add(child);
        return this.children;
    }

    public List<MenuOperation> addOperation(MenuOperation operation) {
        this.operations.add(operation);
        return this.operations;
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public List<MenuOperation> getOperations() {
        return operations;
    }

    public void setOperations(List<MenuOperation> operations) {
        this.operations = operations;
    }

    public List<MenuItem> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItem> children) {
        this.children = children;
    }

    public int compareTo(MenuItem item) {
        int result = this.sequence.compareTo(item.getSequence());
        if (result == 0) {
            result = this.id.compareTo(item.getId());
        } else if (this.sequence.length() == 0
                || item.getSequence().length() == 0) {
            result = -result; // 序号为空字符串的菜单项排在最后
        }
        return result;
    }

    public void sort() {
        Collections.sort(this.operations);
        Collections.sort(this.children);
        for (MenuItem child : this.children) {
            child.sort();
        }
    }

}
