package site.dqxfz.portal.pojo.po;

import site.dqxfz.portal.constant.IconClsEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件或者文件夹所对应的实体类
 * @Author wengyang
 * @Date 2020年02月12日
 **/

public class Portfolio {
    private String id;
    private String name;
    private String type;
    private IconClsEnum iconCls;
    private String fatherId;
    private List<String> childList = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(String name, String type, IconClsEnum iconCls, String fatherId) {
        this.name = name;
        this.type = type;
        this.iconCls = iconCls;
        this.fatherId = fatherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IconClsEnum getIconCls() {
        return iconCls;
    }

    public void setIconCls(IconClsEnum iconCls) {
        this.iconCls = iconCls;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public List<String> getChildList() {
        return childList;
    }

    public void setChildList(List<String> childList) {
        this.childList = childList;
    }
}
