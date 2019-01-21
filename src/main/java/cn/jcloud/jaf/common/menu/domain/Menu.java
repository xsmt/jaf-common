package cn.jcloud.jaf.common.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private long version;
    private String actionExtension;
    private List<MenuItem> menuItems = new ArrayList<>();
    private boolean needUpdate;

    public static Menu notNeedUpdate() {
        Menu menu = new Menu();
        menu.setNeedUpdate(false);
        return menu;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getActionExtension() {
        return this.actionExtension;
    }

    public void setActionExtension(String actionExtension) {
        this.actionExtension = actionExtension;
    }

    public List<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void addItem(MenuItem item) {
        this.menuItems.add(item);
    }

    public void sort() {
        Collections.sort(this.menuItems);
        for (MenuItem item : this.menuItems) {
            item.sort();
        }
    }
}
